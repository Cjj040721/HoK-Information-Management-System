package com.example.hokims.repository;

import com.example.hokims.model.*;
import com.example.hokims.util.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DataStore class acts as a central in-memory repository for application data.
 * It manages the loading, saving, and mocking of domain models such as users, players,
 * heroes, equipment, runes, teams, and matches.
 *
 * Data is initially loaded from JSON files on startup. If loading fails,
 * mock data is used as a fallback.
 */
public class DataStore {

    // Global static lists for storing various domain objects
    public static List<User> users = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();
    public static List<Hero> heroes = new ArrayList<>();
    public static List<Equipment> equipments = new ArrayList<>();
    public static List<Team> teams = new ArrayList<>();
    public static List<Rune> runes = new ArrayList<>();
    public static List<Match> matches = new ArrayList<>();

    // File paths for loading/saving JSON data
    private static final String PLAYER_FILE = "data/players.json";
    private static final String HERO_FILE = "data/heroes.json";
    private static final String TEAM_FILE = "data/teams.json";
    private static final String RUNE_FILE = "data/runes.json";
    private static final String EQUIPMENT_FILE = "data/equipments.json";
    private static final String MATCH_FILE = "data/match.json";

    // Static block to automatically load data when the class is first accessed
    static {
        load();
    }

    /**
     * Loads all model data from their respective JSON files.
     * If user data is missing, default mock users are created.
     * If loading fails due to I/O errors, mock data is initialized instead.
     */
    public static void load() {
        try {
            players = DataManager.loadPlayers(PLAYER_FILE);
            heroes = DataManager.loadHeroes(HERO_FILE);
            teams = DataManager.loadTeams(TEAM_FILE);
            runes = DataManager.loadRunes(RUNE_FILE);
            equipments = DataManager.loadEquipments(EQUIPMENT_FILE);
            matches = DataManager.loadMatches(MATCH_FILE);
            users = DataManager.loadUsers();

            if (users == null || users.isEmpty()) {
                mockUsers(); // fallback if no user data exists
            }

            System.out.println("✅ Data loaded from JSON files.");
        } catch (IOException e) {
            System.out.println("⚠️ Failed to load data from files, falling back to mock data.");
            e.printStackTrace(); // Print detailed error info
            mockInitialize();    // Fallback mock data setup
        }
    }

    /**
     * Saves selected model data (players, heroes, teams) back to their JSON files.
     * Other types like runes and matches may be added later as needed.
     */
    public static void save() {
        try {
            DataManager.savePlayers(players, PLAYER_FILE);
            DataManager.saveHeroes(heroes, HERO_FILE);
            DataManager.saveTeams(teams, TEAM_FILE);

            System.out.println("✅ Data saved to JSON files.");
        } catch (IOException e) {
            System.out.println("❌ Failed to save data: " + e.getMessage());
        }
    }

    /**
     * Initializes a default set of users if none exist.
     * Used for demo, testing, or fallback scenarios.
     */
    public static void mockUsers() {
        users = List.of(
                new User("admin", "admin123", "Admin"),
                new User("player", "player123", "Player")
        );
    }

    /**
     * Initializes fallback data in case loading from disk fails.
     * Currently, the actual logic is commented out. You can uncomment and customize
     * this section for development or demonstration purposes.
     */
    public static void mockInitialize() {
        // Users
        mockUsers();

        /*
        // Example of mock data setup (currently commented out)

        Equipment blade = new Equipment("E001", "Endless Blade");
        Equipment staff = new Equipment("E002", "Arcane Staff");
        equipments = List.of(blade, staff);

        Hero hero1 = new Hero("H001", "Diaochan", "Mage", List.of(staff));
        Hero hero2 = new Hero("H002", "Hou Yi", "Marksman", List.of(blade));
        heroes = List.of(hero1, hero2);

        Player player1 = new Player();
        player1.setId("P001");
        player1.setName("Alice");
        player1.setLevel(20);
        player1.setWinRate(0.8);
        player1.setHeroes(List.of(hero1));

        Player player2 = new Player();
        player2.setId("P002");
        player2.setName("Bob");
        player2.setLevel(18);
        player2.setWinRate(0.65);
        player2.setHeroes(List.of(hero2));

        Team team = new Team();
        team.setName("King Squad");
        team.setMembers(List.of(player1, player2));

        // Link team back to players
        player1.setTeam(team);
        player2.setTeam(team);

        players = List.of(player1, player2);
        teams = List.of(team);
        */
    }
}
