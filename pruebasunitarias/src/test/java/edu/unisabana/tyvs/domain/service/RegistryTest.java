package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para el servicio Registry.
 *
 * Metodología aplicada:
 *  - TDD  (Red → Green → Refactor)
 *  - AAA  (Arrange – Act – Assert)
 *  - BDD  (Given – When – Then expresado en comentarios)
 *
 * Matriz de clases de equivalencia cubierta:
 *  ┌──────────────────────────────────────┬──────────────────┐
 *  │ Clase / Condición                    │ Resultado        │
 *  ├──────────────────────────────────────┼──────────────────┤
 *  │ Persona null                         │ INVALID          │
 *  │ ID <= 0                              │ INVALID          │
 *  │ Persona muerta                       │ DEAD             │
 *  │ Edad < 0                             │ INVALID_AGE      │
 *  │ Edad > 120                           │ INVALID_AGE      │
 *  │ Edad 17 (menor de edad)              │ UNDERAGE         │
 *  │ Edad 18 (límite inferior válido)     │ VALID            │
 *  │ Edad 120 (límite superior válido)    │ VALID            │
 *  │ ID duplicado                         │ DUPLICATED       │
 *  │ Persona válida (camino feliz)        │ VALID            │
 *  └──────────────────────────────────────┴──────────────────┘
 */
public class RegistryTest {

    // Se crea una instancia fresca de Registry antes de CADA prueba
    // para garantizar que los tests sean INDEPENDIENTES entre sí.
    private Registry registry;

    @Before
    public void setUp() {
        registry = new Registry();
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #1: Camino feliz — persona válida
    // RED   → escribimos esta prueba; Registry devuelve VALID hardcodeado.
    // GREEN → la prueba ya pasa con la implementación mínima inicial.
    // REFACTOR → extraemos constantes MIN_VOTING_AGE / MAX_VALID_AGE.
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona viva, de 30 años, con ID único y positivo
     *   When   intento registrarla
     *   Then   el resultado debe ser VALID
     */
    @Test
    public void shouldRegisterValidPerson() {
        // Arrange: preparar los datos y el objeto a probar
        Person person = new Person("Ana Gómez", 1001, 30, Gender.FEMALE, true);

        // Act: ejecutar la acción que queremos probar
        RegisterResult result = registry.registerVoter(person);

        // Assert: verificar el resultado esperado
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #2: Persona muerta
    // RED   → esta prueba falla porque Registry solo devuelve VALID.
    // GREEN → agregamos: if (!p.isAlive()) return DEAD;
    // REFACTOR → extraemos la comprobación null antes de isAlive().
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona que no está viva (alive = false)
     *   When   intento registrarla
     *   Then   el resultado debe ser DEAD
     */
    @Test
    public void shouldRejectDeadPerson() {
        // Arrange
        Person dead = new Person("Carlos Ruiz", 1002, 40, Gender.MALE, false);

        // Act
        RegisterResult result = registry.registerVoter(dead);

        // Assert
        Assert.assertEquals(RegisterResult.DEAD, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #3: Persona null
    // RED   → esta prueba falla → NullPointerException sin validación null.
    // GREEN → agregamos: if (p == null) return INVALID;
    // REFACTOR → movemos la validación null al inicio del método.
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  la referencia a la persona es null
     *   When   intento registrarla
     *   Then   el resultado debe ser INVALID
     */
    @Test
    public void shouldReturnInvalidWhenPersonIsNull() {
        // Arrange: no hay persona
        Person person = null;

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #4: ID inválido (cero o negativo)
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona con ID = 0 (ID inválido)
     *   When   intento registrarla
     *   Then   el resultado debe ser INVALID
     *
     * Valor límite: 0 es el borde inferior inválido (el 1 sería válido).
     */
    @Test
    public void shouldRejectWhenIdIsZero() {
        // Arrange
        Person person = new Person("Luis Torres", 0, 25, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID, result);
    }

    /**
     * BDD:
     *   Given  una persona con ID negativo (-5)
     *   When   intento registrarla
     *   Then   el resultado debe ser INVALID
     *
     * Valor límite: -5 es un representante de la clase inválida (< 0).
     */
    @Test
    public void shouldRejectWhenIdIsNegative() {
        // Arrange
        Person person = new Person("Sofía Pérez", -5, 25, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #5: Edad inválida — fuera de rango biológico
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona con edad = -1 (imposible)
     *   When   intento registrarla
     *   Then   el resultado debe ser INVALID_AGE
     *
     * Valor límite: -1 es el borde inferior inválido de la edad.
     */
    @Test
    public void shouldRejectInvalidAgeNegative() {
        // Arrange
        Person person = new Person("Mario León", 2001, -1, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID_AGE, result);
    }

    /**
     * BDD:
     *   Given  una persona con edad = 121 (superior al máximo permitido)
     *   When   intento registrarla
     *   Then   el resultado debe ser INVALID_AGE
     *
     * Valor límite: 121 es el borde superior inválido (120 sería válido).
     */
    @Test
    public void shouldRejectInvalidAgeOver120() {
        // Arrange
        Person person = new Person("Abuela Rosa", 3001, 121, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.INVALID_AGE, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #6: Menor de edad
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona viva de 17 años (un año por debajo del límite)
     *   When   intento registrarla
     *   Then   el resultado debe ser UNDERAGE
     *
     * Valor límite: 17 es el borde superior de la clase "menor de edad".
     */
    @Test
    public void shouldRejectUnderageAt17() {
        // Arrange
        Person person = new Person("Joven Juan", 4001, 17, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.UNDERAGE, result);
    }

    /**
     * BDD:
     *   Given  una persona viva de exactamente 18 años (el límite mínimo)
     *   When   intento registrarla
     *   Then   el resultado debe ser VALID
     *
     * Valor límite: 18 es el borde inferior de la clase "mayor de edad".
     */
    @Test
    public void shouldAcceptAdultAt18() {
        // Arrange
        Person person = new Person("Adulto Joven", 5001, 18, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    /**
     * BDD:
     *   Given  una persona viva de exactamente 120 años (el límite máximo)
     *   When   intento registrarla
     *   Then   el resultado debe ser VALID
     *
     * Valor límite: 120 es el borde superior de la clase "edad válida".
     */
    @Test
    public void shouldAcceptMaxAge120() {
        // Arrange
        Person person = new Person("Centenaria María", 6001, 120, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    // ─────────────────────────────────────────────────────────────────────
    // CICLO TDD #7: Documento duplicado
    // ─────────────────────────────────────────────────────────────────────

    /**
     * BDD:
     *   Given  una persona ya registrada con ID = 777
     *   When   intento registrar otra persona con el mismo ID = 777
     *   Then   el resultado del segundo registro debe ser DUPLICATED
     */
    @Test
    public void shouldRejectDuplicateId() {
        // Arrange: primera persona se registra exitosamente
        Person first  = new Person("Pedro Ramírez", 777, 30, Gender.MALE, true);
        Person second = new Person("Pedro Copia",   777, 25, Gender.MALE, true);

        // Act: registrar ambas
        registry.registerVoter(first);           // primer registro → VALID
        RegisterResult result = registry.registerVoter(second); // duplicado

        // Assert
        Assert.assertEquals(RegisterResult.DUPLICATED, result);
    }
}
