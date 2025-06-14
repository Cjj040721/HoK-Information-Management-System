package com.example.hokims.repository;

import com.example.hokims.model.Team;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * The TeamRepository class is a utility-style, read-only repository
 * for loading a list of Team objects from a JSON file into memory.
 *
 * This repository uses a static cache to avoid reloading data multiple times.
 * It is designed to be used as a shared, immutable source of team data.
 */
public final class TeamRepository {

    // Cached list of all teams loaded from the JSON file
    private static final List<Team> CACHE = loadFromJson();

    /**
     * Returns the list of all teams from the static cache.
     *
     * @return a list of Team objects (possibly empty if loading failed)
     */
    public static List<Team> findAll() {
        return CACHE;
    }

    /* ---------- Private ---------- */

    /**
     * Loads team data from the "data/combat_teams.json" file using Jackson.
     *
     * If the file does not exist or fails to parse, an empty list is returned.
     *
     * @return a List of Team objects, or an empty list on error
     */
    private static List<Team> loadFromJson() {
        Path path = Paths.get("data", "combat_teams.json");

        // Check if the file exists
        if (!Files.exists(path)) {
            System.err.println("⚠️  " + path + " not found, return empty list");
            return Collections.emptyList();
        }

        try (InputStream is = Files.newInputStream(path)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<List<Team>>() {});
        } catch (Exception e) {
            e.printStackTrace(); // Print error details for debugging
            return Collections.emptyList(); // Fallback to empty list on error
        }
    }

    // Private constructor to prevent instantiation (utility class)
    private TeamRepository() {}
}
