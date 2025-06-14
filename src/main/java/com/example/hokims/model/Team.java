package com.example.hokims.model;

import lombok.*;
import java.util.*;
import com.example.hokims.model.Player;
import com.example.hokims.model.Hero;

/**
 * The Team class represents a group of players participating together
 * in matches. Each team has its own identity, name, and statistics.
 *
 * Lombok Annotations:
 * - @Data: Generates getters, setters, toString(), equals(), and hashCode()
 * - @NoArgsConstructor: Provides a no-argument constructor
 * - @AllArgsConstructor: Provides a full-arguments constructor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Identifiable {

    // Unique identifier for the team
    private String id;

    // Name of the team (e.g., "Stormbreakers", "Shadow Clan")
    private String name;

    // List of players who belong to this team
    private List<Player> members;

    // Average level of all team members
    private double avgLevel;

    // Average win rate across all players in the team
    private double avgWinRate;

    // A string representation of all team members (e.g., for display or logging)
    private String membersString;

    /**
     * Extracts and returns a list of heroes currently selected by the team's players.
     *
     * This method is typically used by the battle engine to retrieve the units
     * that will participate in combat.
     *
     * Note: The Player class must have a getHero() method for this to work.
     *
     * @return List of Hero objects from all team members who have selected one
     */
    public List<Hero> getHeroes() {
        List<Hero> list = new ArrayList<>();
        if (members != null) {
            for (Player p : members) {
                Hero h = p.getHero();
                if (h != null) list.add(h);
            }
        }
        return list;
    }

    /**
     * Returns the unique ID of the team.
     * Required by the Identifiable interface.
     */
    @Override
    public String getId() {
        return id;
    }
}
