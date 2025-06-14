package com.example.hokims.service;

import com.example.hokims.model.Player;
import com.example.hokims.model.Team;
import com.example.hokims.repository.DataStore;

import java.util.List;
import java.util.Optional;

/**
 * TeamService provides utility methods to access and analyze team-related data
 * stored in the application’s in-memory DataStore.
 */
public class TeamService {

    /**
     * Retrieves a list of all registered teams from the data store.
     *
     * @return List of Team objects
     */
    public List<Team> getAllTeams() {
        return DataStore.teams;
    }

    /**
     * Searches for a team by its name (case-insensitive).
     *
     * @param name the team name to look for
     * @return Optional containing the Team if found, or empty if not found
     */
    public Optional<Team> getTeamByName(String name) {
        return DataStore.teams.stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Retrieves all players that belong to a specific team.
     *
     * @param teamName the name of the team
     * @return List of Player objects; empty if team is not found
     */
    public List<Player> getTeamMembers(String teamName) {
        return getTeamByName(teamName)
                .map(Team::getMembers)
                .orElse(List.of()); // Return an empty list if team not found
    }

    /**
     * Calculates the average win rate of all members in a given team.
     *
     * @param teamName the name of the team
     * @return the average win rate (0.0 if the team is not found or has no members)
     */
    public double getTeamAverageWinRate(String teamName) {
        List<Player> members = getTeamMembers(teamName);
        if (members.isEmpty()) return 0.0;

        return members.stream()
                .mapToDouble(Player::getWinRate) // Extract individual win rates
                .average()                       // Compute average
                .orElse(0.0);                    // Default to 0 if no values to average
    }
}
