package edu.unisabana.tyvs.domain.model;

/**
 * Clase que representa una Persona en el dominio de la Registraduría.
 *
 * Es inmutable (todos los campos son final) porque los datos de una persona
 * no cambian una vez creada. Esto facilita las pruebas y evita efectos secundarios.
 */
public class Person {

    private final String name;
    private final int id;
    private final int age;
    private final Gender gender;
    private final boolean alive;

    /**
     * Constructor principal.
     *
     * @param name   Nombre completo de la persona.
     * @param id     Número de documento de identidad (debe ser positivo).
     * @param age    Edad de la persona en años.
     * @param gender Género de la persona (MALE, FEMALE, UNIDENTIFIED).
     * @param alive  true si la persona está viva, false si no.
     */
    public Person(String name, int id, int age, Gender gender, boolean alive) {
        this.name   = name;
        this.id     = id;
        this.age    = age;
        this.gender = gender;
        this.alive  = alive;
    }

    public String getName()   { return name; }
    public int getId()        { return id; }
    public int getAge()       { return age; }
    public Gender getGender() { return gender; }
    public boolean isAlive()  { return alive; }
}
