package com.example.hokims.model;

import lombok.*;

/**
 * The Equipment class represents a piece of equipment that can be used by a hero or character.
 * It provides various stat bonuses and tracks how often it has been used.
 *
 * This class uses Lombok annotations to generate boilerplate code:
 * - @Data: Generates getters, setters, toString(), equals(), and hashCode()
 * - @NoArgsConstructor: Generates a no-argument constructor
 * - @AllArgsConstructor: Generates a constructor with all fields
 * - @Builder: Enables the builder pattern for object construction
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment implements Identifiable, StatBonus {

    // Unique identifier for the equipment
    private String id;

    // Name of the equipment (e.g., "Steel Sword", "Mystic Cloak")
    private String name;

    // Tracks how many times this equipment has been used
    private int usageCount;

    // Stat bonuses provided by this equipment
    private int hpBonus;       // Extra health points
    private int attackBonus;   // Extra attack power
    private int defenseBonus;  // Extra defense value
    private int agilityBonus;  // Extra agility/speed
    private int critBonus;     // Extra critical hit chance
    private int dodgeBonus;    // Extra chance to dodge attacks

    /**
     * Gets the unique ID of this equipment.
     * Required by the Identifiable interface.
     *
     * @return the ID of the equipment
     */
    @Override
    public String getId() {
        return id;
    }
}
