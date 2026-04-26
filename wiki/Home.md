# 🗳️ Wiki — Taller de Pruebas Unitarias: Registraduría

## Descripción del dominio

El sistema modela el proceso de **inscripción de votantes** para las próximas elecciones colombianas. La entidad central es una **Persona** que desea registrarse como votante. La lógica de negocio reside en el servicio `Registry`, que valida cada solicitud de inscripción según reglas estrictas del dominio.

```
Persona solicita inscripción ──► Registry.registerVoter(person) ──► RegisterResult
                                        │
                              Evalúa 6 reglas de negocio:
                              1. ¿Es null?         → INVALID
                              2. ¿ID inválido?      → INVALID
                              3. ¿Está viva?        → DEAD
                              4. ¿Edad fuera rango? → INVALID_AGE
                              5. ¿Es mayor de edad? → UNDERAGE
                              6. ¿ID duplicado?     → DUPLICATED
                              7. Todo OK            → VALID
```

---

## Alcance del taller

| Aspecto | Detalle |
|---|---|
| **Metodología** | TDD (Test-Driven Development) — Red → Green → Refactor |
| **Patrón de pruebas** | AAA (Arrange – Act – Assert) |
| **Técnica de diseño** | Clases de equivalencia y valores límite |
| **Lenguaje de negocio** | BDD (Given – When – Then) |
| **Arquitectura** | Clean Architecture — dominio puro, sin dependencias externas |
| **Cobertura mínima** | ≥ 80% (obtenida: **91% global**, **100% en dominio**) |
| **Framework de pruebas** | JUnit 4.13.2 |
| **Herramienta de cobertura** | JaCoCo 0.8.12 |

---

## Estructura del proyecto

```
pruebasunitarias/
 ├─ src/
 │   ├─ main/java/edu/unisabana/tyvs/
 │   │   ├─ App.java                          ← clase principal (Hello World)
 │   │   └─ domain/
 │   │       ├─ model/
 │   │       │   ├─ Gender.java               ← enum: MALE, FEMALE, UNIDENTIFIED
 │   │       │   ├─ RegisterResult.java       ← enum: VALID, DEAD, INVALID...
 │   │       │   └─ Person.java               ← modelo inmutable de persona
 │   │       └─ service/
 │   │           └─ Registry.java             ← servicio con 6 reglas de negocio
 │   └─ test/java/edu/unisabana/tyvs/
 │       ├─ AppTest.java                      ← prueba básica de Maven
 │       └─ domain/
 │           └─ service/
 │               └─ RegistryTest.java         ← 11 pruebas unitarias
 ├─ pom.xml                                   ← Maven + JUnit + JaCoCo
 └─ target/site/jacoco/index.html             ← reporte de cobertura
```

---

## Equipo

| Nombre | Rol |
|---|---|
| Juan David Cruz Ángel | Desarrollador / Estudiante |

**Curso:** Testing y Validación de Software  
**Programa:** Maestría en Ingeniería de Software – Universidad de La Sabana  
**Año:** 2025

---

## Índice del Wiki

- [🔴🟢🔵 TDD (Red → Green → Refactor)](TDD-Historia.md)
- [🧩 Patrón AAA (Arrange – Act – Assert)](Patron-AAA.md)
- [🧮 Clases de Equivalencia y Valores Límite](Clases-Equivalencia.md)
- [🤝 Escenarios BDD (Given – When – Then)](Escenarios-BDD.md)
- [📊 Resultados de Cobertura JaCoCo](Resultados-Cobertura.md)
- [🪲 Gestión de Defectos](../defectos.md)
