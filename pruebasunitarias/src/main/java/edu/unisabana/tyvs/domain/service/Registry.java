package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;

import java.util.HashSet;
import java.util.Set;

/**
 * Servicio de dominio que gestiona el registro de votantes.
 *
 * Reglas de negocio implementadas:
 *  1. La persona no puede ser null.
 *  2. El ID debe ser positivo (> 0).
 *  3. La persona debe estar viva.
 *  4. La edad debe estar entre 0 y 120 (inclusive).
 *  5. La persona debe ser mayor de edad (>= 18 años).
 *  6. No se permite el mismo ID dos veces (unicidad).
 */
public class Registry {

    // Límites de edad definidos como constantes para evitar "números mágicos"
    private static final int MIN_VOTING_AGE = 18;
    private static final int MAX_VALID_AGE  = 120;

    // Almacena los IDs ya registrados para detectar duplicados
    private final Set<Integer> registeredIds = new HashSet<>();

    /**
     * Intenta registrar una persona como votante.
     *
     * @param p La persona a registrar.
     * @return  El resultado del intento de registro.
     */
    public RegisterResult registerVoter(Person p) {

        // Regla 1: validación defensiva — null no es una persona válida
        if (p == null) {
            return RegisterResult.INVALID;
        }

        // Regla 2: el ID debe ser un número positivo
        if (p.getId() <= 0) {
            return RegisterResult.INVALID;
        }

        // Regla 3: solo los vivos pueden votar
        if (!p.isAlive()) {
            return RegisterResult.DEAD;
        }

        // Regla 4: edad fuera del rango biológicamente posible
        if (p.getAge() < 0 || p.getAge() > MAX_VALID_AGE) {
            return RegisterResult.INVALID_AGE;
        }

        // Regla 5: debe ser mayor de edad
        if (p.getAge() < MIN_VOTING_AGE) {
            return RegisterResult.UNDERAGE;
        }

        // Regla 6: no se permite duplicar el documento
        if (registeredIds.contains(p.getId())) {
            return RegisterResult.DUPLICATED;
        }

        // Si pasó todas las reglas → registro exitoso
        registeredIds.add(p.getId());
        return RegisterResult.VALID;
    }
}
