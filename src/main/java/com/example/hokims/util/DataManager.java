package com.example.hokims.util;

import com.example.hokims.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * DataManager handles reading and writing JSON data for various models in the system.
 * It provides both generic and model-specific methods for working with local JSON files.
 * Jackson is used for serialization/deserialization.
 */
public class DataManager {

    // Shared ObjectMapper with support for Java 8 date/time types
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    /* =================== Generic Read/Write =================== */

    /**
     * Writes any Java object to the specified JSON file.
     *
     * @param path The file path.
     * @param data The object to serialize.
     */
    public static <T> void writeToFile(String path, T data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads any Java object from the specified JSON file using a TypeReference.
     *
     * @param path    The file path.
     * @param typeRef TypeReference for generic type resolution.
     * @return The deserialized object, or null if an error occurs.
     */
    public static <T> T readFromFile(String path, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(new File(path), typeRef);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =================== Model-Specific Load/Save Methods =================== */

    // Save and load methods for Player
    public static void savePlayers(List<Player> players, String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), players);
    }

    public static List<Player> loadPlayers(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<>() {});
    }

    // Load Runes from file using array-based deserialization
    public static List<Rune> loadRunes(String filePath) throws IOException {
        return Arrays.asList(mapper.readValue(new File(filePath), Rune[].class));
    }

    // Load Equipments
    public static List<Equipment> loadEquipments(String filePath) throws IOException {
        return Arrays.asList(mapper.readValue(new File(filePath), Equipment[].class));
    }

    // Load Matches
    public static List<Match> loadMatches(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<>() {});
    }

    // Save and load Heroes
    public static void saveHeroes(List<Hero> heroes, String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), heroes);
    }

    public static List<Hero> loadHeroes(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<>() {});
    }

    // Save and load Teams
    public static void saveTeams(List<Team> teams, String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), teams);
    }

    public static List<Team> loadTeams(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<>() {});
    }

    // Save and load Users (fixed file path)
    public static void saveUsers(List<User> users) {
        writeToFile("data/users.json", users);
    }

    public static List<User> loadUsers() {
        return readFromFile("data/users.json", new TypeReference<List<User>>() {});
    }

    /* =================== Generic CRUD for JSON Files =================== */

    /**
     * Appends a new object to the JSON file list.
     */
    public static <T extends Identifiable> void addObject(String filePath, T newObj, TypeReference<List<T>> typeRef) throws IOException {
        List<T> list = readFromFile(filePath, typeRef);
        if (list == null) list = new java.util.ArrayList<>();
        list.add(newObj);
        writeToFile(filePath, list);
    }

    /**
     * Updates an object in a JSON file by matching its ID.
     */
    public static <T extends Identifiable> void updateObjectById(String filePath, String id, T updatedObj, TypeReference<List<T>> typeRef) throws IOException {
        List<T> list = readFromFile(filePath, typeRef);
        if (list == null) throw new IOException("File or data not found.");

        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (id.equals(list.get(i).getId())) {
                list.set(i, updatedObj);
                found = true;
                break;
            }
        }
        if (!found) throw new IOException("ID not found.");
        writeToFile(filePath, list);
    }

    /**
     * Deletes an object from a JSON file by ID.
     */
    public static <T extends Identifiable> void deleteObjectById(String filePath, String id, TypeReference<List<T>> typeRef) throws IOException {
        List<T> list = readFromFile(filePath, typeRef);
        if (list == null) throw new IOException("File or data not found.");

        boolean removed = list.removeIf(obj -> id.equals(obj.getId()));
        if (!removed) throw new IOException("ID not found.");
        writeToFile(filePath, list);
    }

    /* =================== UI Layer API (String-Based) =================== */

    /**
     * Adds a new object by type using a raw JSON string.
     */
    public static void add(String type, String json) throws IOException {
        switch (type) {
            case "player" -> {
                Player obj = mapper.readValue(json, Player.class);
                addObject("data/players.json", obj, new TypeReference<List<Player>>() {});
            }
            case "hero" -> {
                Hero obj = mapper.readValue(json, Hero.class);
                addObject("data/heroes.json", obj, new TypeReference<List<Hero>>() {});
            }
            case "equipment" -> {
                Equipment obj = mapper.readValue(json, Equipment.class);
                addObject("data/equipments.json", obj, new TypeReference<List<Equipment>>() {});
            }
            case "team" -> {
                Team obj = mapper.readValue(json, Team.class);
                addObject("data/teams.json", obj, new TypeReference<List<Team>>() {});
            }
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    /**
     * Updates an existing object by type, ID, and raw JSON string.
     */
    public static void update(String type, String id, String json) throws IOException {
        switch (type) {
            case "player" -> {
                Player obj = mapper.readValue(json, Player.class);
                updateObjectById("data/players.json", id, obj, new TypeReference<List<Player>>() {});
            }
            case "hero" -> {
                Hero obj = mapper.readValue(json, Hero.class);
                updateObjectById("data/heroes.json", id, obj, new TypeReference<List<Hero>>() {});
            }
            case "equipment" -> {
                Equipment obj = mapper.readValue(json, Equipment.class);
                updateObjectById("data/equipments.json", id, obj, new TypeReference<List<Equipment>>() {});
            }
            case "team" -> {
                Team obj = mapper.readValue(json, Team.class);
                updateObjectById("data/teams.json", id, obj, new TypeReference<List<Team>>() {});
            }
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    /**
     * Deletes an object by type and ID.
     */
    public static void delete(String type, String id) throws IOException {
        switch (type) {
            case "player" -> deleteObjectById("data/players.json", id, new TypeReference<List<Player>>() {});
            case "hero" -> deleteObjectById("data/heroes.json", id, new TypeReference<List<Hero>>() {});
            case "equipment" -> deleteObjectById("data/equipments.json", id, new TypeReference<List<Equipment>>() {});
            case "team" -> deleteObjectById("data/teams.json", id, new TypeReference<List<Team>>() {});
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
