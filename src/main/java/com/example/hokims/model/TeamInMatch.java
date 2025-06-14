package com.example.hokims.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The TeamInMatch class represents a simplified snapshot of a team
 * participating in a specific match. It is used to store match-specific
 * data rather than full team details.
 *
 * Lombok Annotations:
 * - @Data: Generates getters, setters, equals(), hashCode(), and toString()
 * - @Builder: Enables the builder pattern for constructing instances
 * - @NoArgsConstructor / @AllArgsConstructor: Provide default and all-args constructors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamInMatch {

    // Name of the team as displayed during the match
    private String name;

    // Score achieved by the team in this match (e.g., kills, objectives, points)
    private int score;

    // List of player identifiers (e.g., user IDs or names) participating in the match
    private List<String> players;
}
