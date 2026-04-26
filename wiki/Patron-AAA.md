# 🧩 Patrón AAA — Arrange, Act, Assert

## ¿Qué es el patrón AAA?

El patrón **AAA (Arrange – Act – Assert)** es la forma estándar de estructurar pruebas unitarias para que sean claras, legibles y fáciles de mantener. Divide cada prueba en tres fases bien diferenciadas:

| Fase | ¿Qué hace? | En código |
|---|---|---|
| **Arrange** (Preparar) | Configura datos, objetos y estado inicial | Crear `Person`, instanciar `Registry` |
| **Act** (Actuar) | Ejecuta la acción a probar | Llamar `registry.registerVoter(person)` |
| **Assert** (Afirmar) | Verifica que el resultado sea el esperado | `Assert.assertEquals(RegisterResult.VALID, result)` |

---

## ¿Por qué usar AAA?

- ✅ **Legibilidad:** Cualquiera que lea la prueba entiende inmediatamente qué se prepara, qué se ejecuta y qué se verifica.
- ✅ **Mantenibilidad:** Si el código cambia, es fácil saber qué parte de la prueba hay que actualizar.
- ✅ **Depuración:** Cuando falla una prueba, AAA te dice exactamente en qué fase ocurrió el problema.
- ✅ **Evita cajas negras:** Las pruebas no son un misterio; cada sección tiene un propósito explícito.

---

## Ejemplo base: `shouldRegisterValidPerson`

```java
@Test
public void shouldRegisterValidPerson() {

    // ─── ARRANGE: preparar los datos y el objeto a probar ───────────────
    Registry registry = new Registry();
    Person person = new Person("Ana Gómez", 1001, 30, Gender.FEMALE, true);
    //                          nombre      id    edad  género          viva

    // ─── ACT: ejecutar la acción que queremos probar ────────────────────
    RegisterResult result = registry.registerVoter(person);

    // ─── ASSERT: verificar el resultado esperado ────────────────────────
    Assert.assertEquals(RegisterResult.VALID, result);
}
```

### Desglose de cada fase

**Arrange:**
- Creamos un `Registry` nuevo (estado limpio, sin IDs previos).
- Creamos una persona con todos los datos válidos: nombre, ID positivo (1001), edad adulta (30), género definido y `alive = true`.

**Act:**
- Llamamos `registerVoter(person)` y guardamos el resultado en `result`.
- Solo hay **una línea** en el Act — esto es una señal de que estamos probando una sola responsabilidad.

**Assert:**
- Verificamos que el resultado sea exactamente `RegisterResult.VALID`.
- Usamos `Assert.assertEquals(esperado, real)` — el primer argumento es siempre el **valor esperado**.

---

## Ejemplo avanzado: prueba de duplicado (más complejo en Arrange)

```java
@Test
public void shouldRejectDuplicateId() {

    // ─── ARRANGE ─────────────────────────────────────────────────────────
    // Nota: aquí el Arrange incluye una acción previa (registrar la primera
    // persona) para establecer el estado necesario de "ya existe ese ID".
    Person first  = new Person("Pedro Ramírez", 777, 30, Gender.MALE, true);
    Person second = new Person("Pedro Copia",   777, 25, Gender.MALE, true);
    registry.registerVoter(first);  // ← establece el estado: ID 777 ya existe

    // ─── ACT ─────────────────────────────────────────────────────────────
    RegisterResult result = registry.registerVoter(second);

    // ─── ASSERT ──────────────────────────────────────────────────────────
    Assert.assertEquals(RegisterResult.DUPLICATED, result);
}
```

> **Pauta importante:** cuando el Arrange requiere acciones previas (como registrar una persona para luego probar el duplicado), esas acciones pertenecen al Arrange, no al Act. El **Act** es exclusivamente la acción que estamos probando.

---

## Pautas aplicadas en este taller

1. **Una sola verificación por prueba** — cada `@Test` valida exactamente una cosa.
2. **Nombres descriptivos** — `shouldRejectDeadPerson()` dice exactamente qué comportamiento se espera.
3. **`@Before` para el Arrange común** — el `Registry` se crea en `setUp()`, evitando repetición en cada prueba.
4. **Comentarios explícitos** — `// Arrange`, `// Act`, `// Assert` en cada método para máxima claridad.
5. **Patrón de nomenclatura** — `should[Resultado]When[Condicion]()` o `should[Accion][Condicion]()`.

---

## Tabla de todas las pruebas con su estructura AAA

| Prueba | Arrange | Act | Assert |
|---|---|---|---|
| `shouldRegisterValidPerson` | Person(edad=30, viva, id=1001) | `registerVoter(person)` | `VALID` |
| `shouldRejectDeadPerson` | Person(alive=false) | `registerVoter(dead)` | `DEAD` |
| `shouldReturnInvalidWhenPersonIsNull` | `person = null` | `registerVoter(null)` | `INVALID` |
| `shouldRejectWhenIdIsZero` | Person(id=0) | `registerVoter(person)` | `INVALID` |
| `shouldRejectWhenIdIsNegative` | Person(id=-5) | `registerVoter(person)` | `INVALID` |
| `shouldRejectInvalidAgeNegative` | Person(edad=-1) | `registerVoter(person)` | `INVALID_AGE` |
| `shouldRejectInvalidAgeOver120` | Person(edad=121) | `registerVoter(person)` | `INVALID_AGE` |
| `shouldRejectUnderageAt17` | Person(edad=17) | `registerVoter(person)` | `UNDERAGE` |
| `shouldAcceptAdultAt18` | Person(edad=18) | `registerVoter(person)` | `VALID` |
| `shouldAcceptMaxAge120` | Person(edad=120) | `registerVoter(person)` | `VALID` |
| `shouldRejectDuplicateId` | 2× Person(id=777) | `registerVoter(second)` | `DUPLICATED` |
