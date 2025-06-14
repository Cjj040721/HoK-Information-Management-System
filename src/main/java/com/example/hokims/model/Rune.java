package com.example.hokims.model;

import lombok.*;

/**
 * The Rune class represents a magical enhancement that can be attached to a hero
 * to improve their battle performance by providing various stat bonuses.
 *
 * Lombok Annotations:
 * - @Data: Generates getters, setters, equals(), hashCode(), toString()
 * - @NoArgsConstructor: Generates a no-argument constructor
 * - @AllArgsConstructor: Generates a constructor with all fields
 * - @Builder: Enables builder pattern for flexible object creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rune implements Identifiable, StatBonus {

    // Unique identifier for the rune
    private String id;

    // Name of the rune (e.g., "Rune of Vitality", "Rune of Speed")
    private String name;

    // How many times this rune has been used
    private int usageCount;

    // Stat bonuses provided by the rune
    private int hpBonus;       // Bonus to HP (Health Points)
    private int attackBonus;   // Bonus to attack power
    private int defenseBonus;  // Bonus to defense
    private int agilityBonus;  // Bonus to agility/speed
    private int critBonus;     // Bonus to critical hit chance
    private int dodgeBonus;    // Bonus to dodge chance

    /**
     * Returns the unique ID of this rune.
     * Required by the Identifiable interface.
     *
     * @return rune's ID as a string
     */
    @Override
    public String getId() {
        return id;
    }
}
