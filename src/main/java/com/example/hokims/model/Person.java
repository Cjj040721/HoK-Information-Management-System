package com.example.hokims.model;

import lombok.Data;

/**
 * The Person class is an abstract base class representing any identifiable person in the system.
 * It provides common attributes such as id and name, and implements the Identifiable interface.
 *
 * Lombok's @Data annotation automatically generates:
 * - Getters and setters for all fields
 * - equals() and hashCode()
 * - toString()
 */
@Data
public abstract class Person implements Identifiable {

    // Unique identifier for the person (e.g., user ID, player ID)
    private String id;

    // The person's name
    private String name;

    /**
     * Returns the unique ID of the person.
     * Required by the Identifiable interface.
     */
    @Override
    public String getId() {
        return id;
    }
}
