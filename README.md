# 🗳️ Registraduría — Taller de Pruebas Unitarias con TDD

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![JUnit](https://img.shields.io/badge/JUnit-4.13.2-blue?logo=junit5)](https://junit.org/junit4/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.12-green)](https://www.jacoco.org/jacoco/)
[![Coverage](https://img.shields.io/badge/Coverage-91%25-brightgreen)](#-cobertura-jacoco)
[![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apachemaven)](https://maven.apache.org/)

> Implementación del ejercicio "Registraduría" aplicando **TDD** (Red → Green → Refactor), patrón **AAA**, **Clases de Equivalencia** y **BDD (Given–When–Then)** sobre una **Arquitectura Limpia**.

---

## 👤 Integrantes

| Nombre | Programa |
|---|---|
| Juan David Cruz Ángel | Ingeniería de Software |

**Universidad:** Universidad de La Sabana  
**Año:** 2026

---

## 🎯 ¿Qué hace este proyecto?

El sistema valida si una persona puede registrarse como votante en la Registraduría. El servicio central (`Registry`) aplica **6 reglas de negocio** de forma ordenada:

```
Person ──► Registry.registerVoter(p) ──► RegisterResult
                     │
              1. ¿Es null?          → INVALID
              2. ¿ID inválido?       → INVALID
              3. ¿Está viva?         → DEAD
              4. ¿Edad fuera rango?  → INVALID_AGE
              5. ¿Es mayor de edad?  → UNDERAGE
              6. ¿ID duplicado?      → DUPLICATED
              7. Todo correcto       → VALID
```

---

## 🗂️ Estructura del proyecto

```
pruebasunitarias/
 ├─ src/
 │   ├─ main/java/edu/unisabana/tyvs/
 │   │   ├─ App.java
 │   │   └─ domain/
 │   │       ├─ model/
 │   │       │   ├─ Gender.java            # MALE | FEMALE | UNIDENTIFIED
 │   │       │   ├─ RegisterResult.java    # VALID | DEAD | INVALID | INVALID_AGE | UNDERAGE | DUPLICATED
 │   │       │   └─ Person.java            # Modelo inmutable del votante
 │   │       └─ service/
 │   │           └─ Registry.java          # Servicio con las 6 reglas de negocio
 │   └─ test/java/edu/unisabana/tyvs/
 │       ├─ AppTest.java
 │       └─ domain/service/
 │           └─ RegistryTest.java          # 11 pruebas unitarias (TDD + AAA + BDD)
 ├─ pom.xml                                # Maven + JUnit 4 + JaCoCo
 └─ target/site/jacoco/index.html          # Reporte de cobertura (generado por mvn verify)

wiki/
 ├─ Home.md                   # Resumen del dominio y equipo
 ├─ TDD-Historia.md           # 7 ciclos Red → Green → Refactor
 ├─ Patron-AAA.md             # Patrón Arrange – Act – Assert
 ├─ Clases-Equivalencia.md    # Tabla de clases de equivalencia y valores límite
 ├─ Escenarios-BDD.md         # 11 escenarios Given – When – Then
 ├─ Resultados-Cobertura.md   # Análisis del reporte JaCoCo
 └─ img/
     └─ jacoco_report.png     # Captura del reporte de cobertura

defectos.md                   # Registro de 3 defectos documentados
```

---

## 🚀 Cómo ejecutar

### Prerrequisitos

- Java 21+
- Maven 3.6+

### Compilar y ejecutar pruebas

```bash
cd pruebasunitarias

# Compilar y ejecutar todas las pruebas
mvn clean test

# Generar reporte de cobertura JaCoCo
mvn clean verify

# Ver el reporte
open target/site/jacoco/index.html   # macOS
# o
xdg-open target/site/jacoco/index.html  # Linux
```

### Resultado esperado de `mvn clean test`

```
[INFO] T E S T S
[INFO] Running edu.unisabana.tyvs.AppTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running edu.unisabana.tyvs.domain.service.RegistryTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0

[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📊 Cobertura JaCoCo

| Paquete | Instrucciones | Branches | Lines |
|---|---|---|---|
| `domain.service` (Registry) | **100%** ✅ | **100%** ✅ | **100%** ✅ |
| `domain.model` | **93%** | n/a | 91% |
| Total global | **91%** | **100%** | 88% |

> El paquete de dominio puro (`domain.service`) alcanza **100%** de cobertura. El total global del 91% supera el requisito mínimo del 80%.

---

## 🧪 Pruebas implementadas

| # | Método de prueba | Qué valida | Resultado |
|---|---|---|---|
| 1 | `shouldRegisterValidPerson` | Camino feliz — persona completamente válida | `VALID` |
| 2 | `shouldRejectDeadPerson` | Persona fallecida no puede votar | `DEAD` |
| 3 | `shouldReturnInvalidWhenPersonIsNull` | Referencia null es rechazada | `INVALID` |
| 4 | `shouldRejectWhenIdIsZero` | ID = 0 (valor límite — inválido) | `INVALID` |
| 5 | `shouldRejectWhenIdIsNegative` | ID negativo es rechazado | `INVALID` |
| 6 | `shouldRejectInvalidAgeNegative` | Edad = -1 (valor límite — imposible) | `INVALID_AGE` |
| 7 | `shouldRejectInvalidAgeOver120` | Edad = 121 (valor límite — supera máximo) | `INVALID_AGE` |
| 8 | `shouldRejectUnderageAt17` | Edad = 17 (valor límite — menor de edad) | `UNDERAGE` |
| 9 | `shouldAcceptAdultAt18` | Edad = 18 (valor límite — mayoría de edad) | `VALID` |
| 10 | `shouldAcceptMaxAge120` | Edad = 120 (valor límite — máximo válido) | `VALID` |
| 11 | `shouldRejectDuplicateId` | Mismo ID dos veces — duplicado | `DUPLICATED` |

---

## 📖 Documentación completa (Wiki)

Toda la documentación técnica está en la carpeta [`wiki/`](wiki/):

| Documento | Contenido |
|---|---|
| [🏠 Home](wiki/Home.md) | Dominio, alcance y equipo |
| [🔴🟢🔵 TDD Historia](wiki/TDD-Historia.md) | 7 ciclos Red → Green → Refactor con código real |
| [🧩 Patrón AAA](wiki/Patron-AAA.md) | Estructura Arrange – Act – Assert con ejemplos |
| [🧮 Clases de Equivalencia](wiki/Clases-Equivalencia.md) | Tabla de casos y valores límite justificados |
| [🤝 Escenarios BDD](wiki/Escenarios-BDD.md) | 11 escenarios Given – When – Then |
| [📊 Resultados Cobertura](wiki/Resultados-Cobertura.md) | Análisis JaCoCo con imagen y reflexión |
| [🪲 Defectos](defectos.md) | 3 defectos reales documentados del proceso TDD |

---

## 🔑 Conceptos aplicados

### TDD — Red → Green → Refactor

7 ciclos documentados donde cada `if` del servicio `Registry` fue introducido primero como una prueba que fallaba, luego implementado con el código mínimo, y finalmente refactorizado.

### Patrón AAA

Todas las pruebas siguen `// Arrange`, `// Act`, `// Assert` con comentarios explícitos. Una responsabilidad por prueba, una acción por Act.

### Clases de Equivalencia y Valores Límite

10 clases de equivalencia identificadas. Se probaron los bordes críticos: `id=0`, `id=-5`, `edad=-1`, `edad=17`, `edad=18`, `edad=120`, `edad=121`.

### BDD — Given – When – Then

Cada prueba tiene su escenario BDD documentado en el Wiki y como Javadoc en el código, permitiendo trazabilidad directa entre requisitos y tests.

### Clean Architecture

El dominio (`domain/model` y `domain/service`) no tiene ninguna dependencia externa. Ningún import de frameworks, base de datos o HTTP. La lógica de negocio es pura y verificable de forma aislada.

---

## 📋 Checklist de entrega

- [x] Proyecto compila con `mvn clean test` sin errores
- [x] `.gitignore` configurado (excluye `target/`, IDE files)
- [x] Rama principal compilable y funcional
- [x] 11 pruebas unitarias con patrón AAA
- [x] 7 ciclos TDD Red → Green → Refactor documentados
- [x] 10 clases de equivalencia cubiertas
- [x] 11 escenarios BDD (Given – When – Then)
- [x] Cobertura JaCoCo: **91% global**, **100% dominio** (≥ 80% ✅)
- [x] `defectos.md` con 3 defectos documentados
- [x] Wiki completo con 5 secciones
- [x] Constantes extraídas (`MIN_VOTING_AGE`, `MAX_VALID_AGE`)
- [x] Código sin duplicación ni números mágicos
