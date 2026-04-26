# Gestión de Defectos — Taller de Pruebas Unitarias

Este archivo documenta los defectos encontrados (reales o simulados) durante el ciclo TDD
del ejercicio "Registraduría".

---

## Defecto 01 — Edad negativa no era validada

- **Caso:** Persona con `edad = -1`, viva, con ID válido.
- **Esperado:** `INVALID_AGE`
- **Obtenido (antes del fix):** `VALID` *(la implementación mínima inicial no validaba la edad)*
- **Causa probable:** Falta de validación del límite inferior de edad en `Registry.registerVoter()`.
- **Cómo se detectó:** Ciclo TDD #5 — prueba `shouldRejectInvalidAgeNegative()` falló en fase RED.
- **Corrección:** Se agregó la condición `if (p.getAge() < 0 || p.getAge() > MAX_VALID_AGE)` en el servicio.
- **Estado:** ✅ Cerrado

---

## Defecto 02 — ID cero era aceptado

- **Caso:** Persona con `id = 0`, viva, con edad válida.
- **Esperado:** `INVALID`
- **Obtenido (antes del fix):** `VALID` *(no se validaba que el ID fuera positivo)*
- **Causa probable:** Falta de regla de negocio para IDs no positivos.
- **Cómo se detectó:** Ciclo TDD #4 — prueba `shouldRejectWhenIdIsZero()` falló en fase RED.
- **Corrección:** Se agregó la condición `if (p.getId() <= 0)` antes de las otras validaciones.
- **Estado:** ✅ Cerrado

---

## Defecto 03 — Persona muerta podía registrarse (simulado)

- **Caso:** Persona con `alive = false`, edad 40, ID válido.
- **Esperado:** `DEAD`
- **Obtenido (simulado):** `VALID` *(si no existiera la validación de estado de vida)*
- **Causa probable:** Implementación inicial sin regla `alive`.
- **Cómo se detectó:** Ciclo TDD #2 — prueba `shouldRejectDeadPerson()` en fase RED.
- **Corrección:** Se agregó `if (!p.isAlive()) return RegisterResult.DEAD;`
- **Estado:** ✅ Cerrado
