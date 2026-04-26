package edu.unisabana.tyvs.domain.model;

/**
 * Enum que representa el resultado de intentar registrar un votante.
 *
 * VALID       → La persona fue registrada exitosamente.
 * DUPLICATED  → Ya existe un registro con el mismo ID.
 * INVALID     → La persona o sus datos son inválidos (null, id negativo, etc.).
 * INVALID_AGE → La edad está fuera del rango permitido (< 0 o > 120).
 * UNDERAGE    → La persona es menor de 18 años.
 * DEAD        → La persona no está viva.
 */
public enum RegisterResult {
    VALID,
    DUPLICATED,
    INVALID,
    INVALID_AGE,
    UNDERAGE,
    DEAD
}
