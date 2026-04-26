# 🧮 Clases de Equivalencia y Valores Límite

## ¿Qué son las clases de equivalencia?

Una **clase de equivalencia** es un grupo de valores de entrada que el sistema trata exactamente de la misma forma. Si probamos un representante de cada clase, tenemos confianza de que todos los demás valores de esa clase se comportarán igual.

Los **valores límite** son los bordes entre clases — los puntos donde el sistema cambia de comportamiento. Son los más propensos a tener errores de programación (off-by-one errors).

---

## Espacio de entradas de `registerVoter(Person p)`

### Dimensión 1: Nulidad de la persona

| Clase | Representante | Resultado esperado | Test que lo cubre |
|---|---|---|---|
| `person == null` | `null` | `INVALID` | `shouldReturnInvalidWhenPersonIsNull` |
| `person != null` | `new Person(...)` | continúa evaluación | todos los demás tests |

---

### Dimensión 2: Identificador (`id`)

| Clase | Rango | Representante | Valor límite | Resultado | Test |
|---|---|---|---|---|---|
| ID inválido negativo | `id < 0` | `-5` | `-1` (borde sup. inválido) | `INVALID` | `shouldRejectWhenIdIsNegative` |
| ID cero (inválido) | `id = 0` | `0` | `0` (borde exacto) | `INVALID` | `shouldRejectWhenIdIsZero` |
| ID válido único | `id > 0`, no registrado | `1001` | `1` (borde inf. válido) | continúa | `shouldRegisterValidPerson` |
| ID duplicado | `id > 0`, ya registrado | `777` | — | `DUPLICATED` | `shouldRejectDuplicateId` |

> **Justificación del borde:** `id = 0` es especialmente importante porque muchos sistemas aceptan 0 erróneamente. El borde inferior válido es `1`.

---

### Dimensión 3: Estado de vida (`alive`)

| Clase | Valor | Resultado | Test |
|---|---|---|---|
| Muerta | `false` | `DEAD` | `shouldRejectDeadPerson` |
| Viva | `true` | continúa evaluación | todos los demás |

---

### Dimensión 4: Edad (`age`)

| Clase | Rango | Representante | Valores límite | Resultado | Test |
|---|---|---|---|---|---|
| Edad imposible (negativa) | `age < 0` | `-1` | `-1` (borde sup. inválido) | `INVALID_AGE` | `shouldRejectInvalidAgeNegative` |
| Menor de edad | `0 ≤ age < 18` | `10` | `17` (borde sup. menor) | `UNDERAGE` | `shouldRejectUnderageAt17` |
| Mayor de edad válida | `18 ≤ age ≤ 120` | `30` | `18` (borde inf.), `120` (borde sup.) | `VALID` | `shouldAcceptAdultAt18`, `shouldAcceptMaxAge120` |
| Edad imposible (excesiva) | `age > 120` | `121` | `121` (borde inf. inválido) | `INVALID_AGE` | `shouldRejectInvalidAgeOver120` |

> **Justificación de los bordes 17/18 y 120/121:**
> - `17 → UNDERAGE`, `18 → VALID`: el límite de mayoría de edad en Colombia es 18 años.
> - `120 → VALID`, `121 → INVALID_AGE`: 120 años es el límite biológico razonable para un ser humano.

---

## Tabla consolidada de pruebas

| # | Nombre del test | Entrada representativa | Resultado esperado | Tipo de caso |
|---|---|---|---|---|
| 1 | `shouldReturnInvalidWhenPersonIsNull` | `null` | `INVALID` | Nulidad |
| 2 | `shouldRejectWhenIdIsZero` | `id=0, edad=25, viva` | `INVALID` | Valor límite ID |
| 3 | `shouldRejectWhenIdIsNegative` | `id=-5, edad=25, viva` | `INVALID` | Clase inválida ID |
| 4 | `shouldRejectDeadPerson` | `id=1002, edad=40, muerta` | `DEAD` | Clase muerta |
| 5 | `shouldRejectInvalidAgeNegative` | `id=2001, edad=-1, viva` | `INVALID_AGE` | Valor límite edad |
| 6 | `shouldRejectInvalidAgeOver120` | `id=3001, edad=121, viva` | `INVALID_AGE` | Valor límite edad |
| 7 | `shouldRejectUnderageAt17` | `id=4001, edad=17, viva` | `UNDERAGE` | Valor límite edad |
| 8 | `shouldAcceptAdultAt18` | `id=5001, edad=18, viva` | `VALID` | Valor límite edad |
| 9 | `shouldAcceptMaxAge120` | `id=6001, edad=120, viva` | `VALID` | Valor límite edad |
| 10 | `shouldRejectDuplicateId` | `id=777` (dos veces) | `DUPLICATED` | Unicidad |
| 11 | `shouldRegisterValidPerson` | `id=1001, edad=30, viva` | `VALID` | Camino feliz |

---

## Diagrama de decisión del `Registry`

```
registerVoter(p)
│
├─ p == null? ─────────────────────────────────────────► INVALID
│
├─ p.getId() <= 0? ────────────────────────────────────► INVALID
│
├─ !p.isAlive()? ──────────────────────────────────────► DEAD
│
├─ p.getAge() < 0 || p.getAge() > 120? ────────────────► INVALID_AGE
│
├─ p.getAge() < 18? ───────────────────────────────────► UNDERAGE
│
├─ registeredIds.contains(p.getId())? ─────────────────► DUPLICATED
│
└─ registeredIds.add(p.getId()) ───────────────────────► VALID
```

Cada rama del diagrama corresponde a una clase de equivalencia con al menos un test que la cubre.

---

## ¿Qué escenarios NO se cubrieron?

| Escenario omitido | Justificación |
|---|---|
| Nombre `null` o vacío | El README no define reglas sobre el nombre; `Person` lo acepta como String sin restricción |
| Género `UNIDENTIFIED` | El sistema no discrimina por género; registra igual que cualquier género |
| Edad exactamente `0` | Es un representante de la clase "menor" (0 ≤ age < 18); cubierta por el caso de 17 |
| IDs muy grandes (`Integer.MAX_VALUE`) | No hay regla de negocio que lo limite; es `id > 0`, pasa como válido |
