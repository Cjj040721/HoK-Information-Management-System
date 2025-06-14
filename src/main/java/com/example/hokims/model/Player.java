package com.example.hokims.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * The Player class represents a game participant with stats and associations.
 * It extends the Person class, inheriting id and name fields.
 *
 * Lombok Annotations:
 * - @Data: Generates getters, setters, toString(), equals(), hashCode()
 * - @EqualsAndHashCode(callSuper = true): Includes superclass fields in equality checks
 * - @NoArgsConstructor / @AllArgsConstructor: Provide default and full constructors
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Player extends Person {

    // Player's current level in the game
    private int level;

    // Player's win rate (e.g., 0.75 means 75%)
    private double winRate;

    // Player's current rank (e.g., "Gold", "Platinum", "Diamond")
    private String rank;

    // Hero currently selected or used by the player
    private Hero hero;

    // The team the player belongs to (excluded from toString to prevent circular references)
    @ToString.Exclude
    private Team team;

    // List of hero IDs (or names) that this player has used
    private List<String> heroes;
}
