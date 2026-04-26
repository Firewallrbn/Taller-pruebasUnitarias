# 🔴🟢🔵 TDD — Historia del Ciclo Red → Green → Refactor

Este documento registra los 7 ciclos TDD aplicados durante el desarrollo del servicio `Registry`.

> **Principio TDD:** Escribe la prueba primero (falla = Rojo), implementa lo mínimo para que pase (Verde), luego mejora el código sin romper pruebas (Refactor).

---

## Ciclo 1 — Camino Feliz: Persona válida

### 🔴 RED — La prueba (falla inicialmente)

```java
@Test
public void shouldRegisterValidPerson() {
    // Arrange
    Registry registry = new Registry();
    Person person = new Person("Ana Gómez", 1001, 30, Gender.FEMALE, true);

    // Act
    RegisterResult result = registry.registerVoter(person);

    // Assert
    Assert.assertEquals(RegisterResult.VALID, result);
}
```

**¿Por qué falla?** La clase `Registry` aún no existe. El compilador lanza error.

### 🟢 GREEN — Implementación mínima

```java
public class Registry {
    public RegisterResult registerVoter(Person p) {
        return RegisterResult.VALID; // siempre devuelve VALID (mínimo para pasar)
    }
}
```

**Resultado:** ✅ La prueba pasa.

### 🔵 REFACTOR

No hay duplicación ni mejora obvia aún. El código queda como está.

---

## Ciclo 2 — Persona muerta debe ser rechazada

### 🔴 RED

```java
@Test
public void shouldRejectDeadPerson() {
    Registry registry = new Registry();
    Person dead = new Person("Carlos Ruiz", 1002, 40, Gender.MALE, false);

    RegisterResult result = registry.registerVoter(dead);

    Assert.assertEquals(RegisterResult.DEAD, result);
}
```

**¿Por qué falla?** La implementación mínima siempre devuelve `VALID`. Esta prueba espera `DEAD`, así que falla (🔴).

### 🟢 GREEN — Implementación mínima

```java
public RegisterResult registerVoter(Person p) {
    if (!p.isAlive()) return RegisterResult.DEAD;
    return RegisterResult.VALID;
}
```

**Resultado:** ✅ Ambas pruebas pasan.

### 🔵 REFACTOR

Nos damos cuenta de que debemos manejar el caso `null` antes de llamar a `p.isAlive()` (evitar `NullPointerException`). Preparamos el terreno.

---

## Ciclo 3 — Persona null → INVALID

### 🔴 RED

```java
@Test
public void shouldReturnInvalidWhenPersonIsNull() {
    RegisterResult result = registry.registerVoter(null);
    Assert.assertEquals(RegisterResult.INVALID, result);
}
```

**¿Por qué falla?** Si se pasa `null`, `p.isAlive()` lanza `NullPointerException` (🔴).

### 🟢 GREEN

```java
public RegisterResult registerVoter(Person p) {
    if (p == null) return RegisterResult.INVALID;
    if (!p.isAlive()) return RegisterResult.DEAD;
    return RegisterResult.VALID;
}
```

### 🔵 REFACTOR

Movemos la validación `null` al inicio del método. Es el primer guardián defensivo. ✅

---

## Ciclo 4 — ID inválido (cero o negativo)

### 🔴 RED

```java
@Test
public void shouldRejectWhenIdIsZero() {
    Person person = new Person("Luis Torres", 0, 25, Gender.MALE, true);
    Assert.assertEquals(RegisterResult.INVALID, registry.registerVoter(person));
}
```

**¿Por qué falla?** No hay validación de ID. Devuelve `VALID` cuando debería devolver `INVALID` (🔴).

### 🟢 GREEN

```java
if (p.getId() <= 0) return RegisterResult.INVALID;
```

### 🔵 REFACTOR

Añadimos también la prueba con `id = -5` para verificar que toda la clase inválida está cubierta. ✅

---

## Ciclo 5 — Edad fuera de rango biológico

### 🔴 RED

```java
@Test
public void shouldRejectInvalidAgeNegative() {
    Person person = new Person("Mario León", 2001, -1, Gender.MALE, true);
    Assert.assertEquals(RegisterResult.INVALID_AGE, registry.registerVoter(person));
}
```

**¿Por qué falla?** No hay validación de rango de edad. Devuelve `VALID` (🔴).

### 🟢 GREEN

```java
if (p.getAge() < 0 || p.getAge() > MAX_VALID_AGE) return RegisterResult.INVALID_AGE;
```

### 🔵 REFACTOR

Extraemos la constante `MAX_VALID_AGE = 120` para eliminar el número mágico. ✅

---

## Ciclo 6 — Menor de edad → UNDERAGE

### 🔴 RED

```java
@Test
public void shouldRejectUnderageAt17() {
    Person person = new Person("Joven Juan", 4001, 17, Gender.MALE, true);
    Assert.assertEquals(RegisterResult.UNDERAGE, registry.registerVoter(person));
}
```

**¿Por qué falla?** Sin la regla de mayoría de edad, un joven de 17 pasaría como `VALID` (🔴).

### 🟢 GREEN

```java
if (p.getAge() < MIN_VOTING_AGE) return RegisterResult.UNDERAGE;
```

### 🔵 REFACTOR

Extraemos la constante `MIN_VOTING_AGE = 18`. Añadimos la prueba en el valor límite exacto (18 → VALID, 17 → UNDERAGE). ✅

---

## Ciclo 7 — Documento duplicado → DUPLICATED

### 🔴 RED

```java
@Test
public void shouldRejectDuplicateId() {
    Person first  = new Person("Pedro Ramírez", 777, 30, Gender.MALE, true);
    Person second = new Person("Pedro Copia",   777, 25, Gender.MALE, true);
    registry.registerVoter(first);
    RegisterResult result = registry.registerVoter(second);
    Assert.assertEquals(RegisterResult.DUPLICATED, result);
}
```

**¿Por qué falla?** No hay memoria de IDs ya registrados. El segundo pasa como `VALID` (🔴).

### 🟢 GREEN

```java
private final Set<Integer> registeredIds = new HashSet<>();

// dentro del método:
if (registeredIds.contains(p.getId())) return RegisterResult.DUPLICATED;
registeredIds.add(p.getId());
```

### 🔵 REFACTOR

El `HashSet` es la estructura correcta (O(1) para `contains`). Los 7 ciclos quedaron completos con un `Registry` robusto. ✅

---

## Clase `Registry` final (resultado de todos los refactors)

```java
public class Registry {
    private static final int MIN_VOTING_AGE = 18;
    private static final int MAX_VALID_AGE  = 120;

    private final Set<Integer> registeredIds = new HashSet<>();

    public RegisterResult registerVoter(Person p) {
        if (p == null)                                    return RegisterResult.INVALID;
        if (p.getId() <= 0)                               return RegisterResult.INVALID;
        if (!p.isAlive())                                 return RegisterResult.DEAD;
        if (p.getAge() < 0 || p.getAge() > MAX_VALID_AGE) return RegisterResult.INVALID_AGE;
        if (p.getAge() < MIN_VOTING_AGE)                  return RegisterResult.UNDERAGE;
        if (registeredIds.contains(p.getId()))            return RegisterResult.DUPLICATED;
        registeredIds.add(p.getId());
        return RegisterResult.VALID;
    }
}
```

> **Lección clave:** Cada `if` en `Registry` corresponde exactamente a una prueba que primero falló (Rojo) y luego fue implementada (Verde). Eso es TDD puro.
