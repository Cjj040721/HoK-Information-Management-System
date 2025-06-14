package com.example.hokims.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The Match class represents a recorded game match between two teams.
 * It includes match metadata such as time, teams involved, winner, and hero picks.
 *
 * Lombok annotations are used to auto-generate boilerplate code:
 * - @Data: Generates getters, setters, toString(), equals(), and hashCode()
 * - @Builder: Enables the builder pattern for flexible object creation
 * - @NoArgsConstructor / @AllArgsConstructor: Provide constructors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    // Unique identifier for the match
    private String matchId;

    // Timestamp indicating when the match occurred
    private LocalDateTime timestamp;

    // Team A participating in the match (contains players, stats, etc.)
    private TeamInMatch teamA;

    // Team B participating in the match
    private TeamInMatch teamB;

    // The ID or name of the winning team
    private String winner;

    // Map recording which player picked which hero: key = playerId, value = heroId
    private Map<String, String> heroPicks;
}
