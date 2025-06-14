package com.example.hokims.model;

/**
 * Identifiable is a marker interface that requires implementing classes
 * to provide a unique identifier through the getId() method.
 *
 * This interface is typically used to generalize the concept of identity
 * across various model classes such as Hero, Equipment, Rune, etc.
 */
public interface Identifiable {

    /**
     * Retrieves the unique identifier for the implementing object.
     *
     * @return a String representing the object's ID
     */
    String getId();
}
