# 🤝 Escenarios BDD — Given, When, Then

## ¿Qué es BDD?

**BDD (Behavior-Driven Development)** es una forma de expresar las pruebas usando un lenguaje cercano al negocio, estructurado en:

- **Given (Dado):** el contexto o estado inicial del sistema.
- **When (Cuando):** la acción que se ejecuta.
- **Then (Entonces):** el resultado esperado.

Su objetivo es que tanto desarrolladores como analistas y usuarios puedan entender qué hace el sistema sin leer código.

---

## Escenarios BDD para `Registry.registerVoter()`

---

### Escenario 1: Registro exitoso (camino feliz)

```gherkin
Escenario: Registrar una persona válida
  Dado  que existe una persona llamada "Ana Gómez" con ID 1001, 30 años, viva
  Cuando intento registrarla como votante
  Entonces el resultado debe ser VALID
```

**Conexión con el código:**
```java
// Test: shouldRegisterValidPerson()
Person person = new Person("Ana Gómez", 1001, 30, Gender.FEMALE, true);
Assert.assertEquals(RegisterResult.VALID, registry.registerVoter(person));
```

---

### Escenario 2: Persona fallecida

```gherkin
Escenario: Rechazar una persona muerta
  Dado  que existe una persona llamada "Carlos Ruiz" con ID 1002, 40 años, fallecida
  Cuando intento registrarla como votante
  Entonces el resultado debe ser DEAD
```

**Conexión con el código:**
```java
// Test: shouldRejectDeadPerson()
Person dead = new Person("Carlos Ruiz", 1002, 40, Gender.MALE, false);
Assert.assertEquals(RegisterResult.DEAD, registry.registerVoter(dead));
```

---

### Escenario 3: Referencia nula

```gherkin
Escenario: Rechazar una referencia nula
  Dado  que no existe ninguna persona (referencia null)
  Cuando intento registrarla como votante
  Entonces el resultado debe ser INVALID
```

**Conexión con el código:**
```java
// Test: shouldReturnInvalidWhenPersonIsNull()
Assert.assertEquals(RegisterResult.INVALID, registry.registerVoter(null));
```

---

### Escenario 4: ID igual a cero

```gherkin
Escenario: Rechazar persona con ID = 0
  Dado  que existe una persona con ID = 0 (ID inválido), 25 años, viva
  Cuando intento registrarla como votante
  Entonces el resultado debe ser INVALID
```

**Conexión con el código:**
```java
// Test: shouldRejectWhenIdIsZero()
Person person = new Person("Luis Torres", 0, 25, Gender.MALE, true);
Assert.assertEquals(RegisterResult.INVALID, registry.registerVoter(person));
```

---

### Escenario 5: ID negativo

```gherkin
Escenario: Rechazar persona con ID negativo
  Dado  que existe una persona con ID = -5, 25 años, viva
  Cuando intento registrarla como votante
  Entonces el resultado debe ser INVALID
```

**Conexión con el código:**
```java
// Test: shouldRejectWhenIdIsNegative()
Person person = new Person("Sofía Pérez", -5, 25, Gender.FEMALE, true);
Assert.assertEquals(RegisterResult.INVALID, registry.registerVoter(person));
```

---

### Escenario 6: Edad negativa (biológicamente imposible)

```gherkin
Escenario: Rechazar persona con edad negativa
  Dado  que existe una persona con ID válido, viva, pero con edad = -1
  Cuando intento registrarla como votante
  Entonces el resultado debe ser INVALID_AGE
```

**Conexión con el código:**
```java
// Test: shouldRejectInvalidAgeNegative()
Person person = new Person("Mario León", 2001, -1, Gender.MALE, true);
Assert.assertEquals(RegisterResult.INVALID_AGE, registry.registerVoter(person));
```

---

### Escenario 7: Edad mayor a 120

```gherkin
Escenario: Rechazar persona con edad superior al máximo permitido
  Dado  que existe una persona con ID válido, viva, pero con edad = 121
  Cuando intento registrarla como votante
  Entonces el resultado debe ser INVALID_AGE
```

**Conexión con el código:**
```java
// Test: shouldRejectInvalidAgeOver120()
Person person = new Person("Abuela Rosa", 3001, 121, Gender.FEMALE, true);
Assert.assertEquals(RegisterResult.INVALID_AGE, registry.registerVoter(person));
```

---

### Escenario 8: Menor de 18 años (valor límite superior del rango de menores)

```gherkin
Escenario: Rechazar persona menor de edad (17 años)
  Dado  que existe una persona viva de exactamente 17 años con ID válido
  Cuando intento registrarla como votante
  Entonces el resultado debe ser UNDERAGE
```

**Conexión con el código:**
```java
// Test: shouldRejectUnderageAt17()
Person person = new Person("Joven Juan", 4001, 17, Gender.MALE, true);
Assert.assertEquals(RegisterResult.UNDERAGE, registry.registerVoter(person));
```

---

### Escenario 9: Exactamente 18 años (valor límite inferior del rango de adultos)

```gherkin
Escenario: Aceptar persona con exactamente 18 años
  Dado  que existe una persona viva de exactamente 18 años con ID válido
  Cuando intento registrarla como votante
  Entonces el resultado debe ser VALID
```

**Conexión con el código:**
```java
// Test: shouldAcceptAdultAt18()
Person person = new Person("Adulto Joven", 5001, 18, Gender.MALE, true);
Assert.assertEquals(RegisterResult.VALID, registry.registerVoter(person));
```

---

### Escenario 10: Exactamente 120 años (valor límite superior del rango válido)

```gherkin
Escenario: Aceptar persona con exactamente 120 años
  Dado  que existe una persona viva de exactamente 120 años con ID válido
  Cuando intento registrarla como votante
  Entonces el resultado debe ser VALID
```

**Conexión con el código:**
```java
// Test: shouldAcceptMaxAge120()
Person person = new Person("Centenaria María", 6001, 120, Gender.FEMALE, true);
Assert.assertEquals(RegisterResult.VALID, registry.registerVoter(person));
```

---

### Escenario 11: Documento duplicado

```gherkin
Escenario: Rechazar inscripción con documento ya registrado
  Dado  que una persona con ID = 777 ya fue registrada exitosamente
  Cuando intento registrar otra persona con el mismo ID = 777
  Entonces el resultado del segundo intento debe ser DUPLICATED
```

**Conexión con el código:**
```java
// Test: shouldRejectDuplicateId()
registry.registerVoter(new Person("Pedro Ramírez", 777, 30, Gender.MALE, true));
Person second = new Person("Pedro Copia", 777, 25, Gender.MALE, true);
Assert.assertEquals(RegisterResult.DUPLICATED, registry.registerVoter(second));
```

---

## Tabla de trazabilidad: Regla de negocio → Escenario BDD → Test JUnit

| Regla de negocio | Escenario BDD | Método JUnit |
|---|---|---|
| Persona no puede ser null | Escenario 3 | `shouldReturnInvalidWhenPersonIsNull` |
| ID debe ser positivo | Escenarios 4 y 5 | `shouldRejectWhenIdIsZero`, `shouldRejectWhenIdIsNegative` |
| Solo personas vivas | Escenario 2 | `shouldRejectDeadPerson` |
| Edad en rango [0, 120] | Escenarios 6 y 7 | `shouldRejectInvalidAgeNegative`, `shouldRejectInvalidAgeOver120` |
| Mayor de 18 años | Escenarios 8 y 9 | `shouldRejectUnderageAt17`, `shouldAcceptAdultAt18` |
| No duplicar ID | Escenario 11 | `shouldRejectDuplicateId` |
| Persona completamente válida | Escenarios 1 y 10 | `shouldRegisterValidPerson`, `shouldAcceptMaxAge120` |
