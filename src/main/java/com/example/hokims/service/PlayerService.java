package com.example.hokims.service;

import com.example.hokims.model.Hero;
import com.example.hokims.model.Player;
import com.example.hokims.repository.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerService {

    // JavaFX observable list of players
    private final ObservableList<Player> playerList;

    public PlayerService() {
        // Initialize the observable list using static player data from DataStore
        this.playerList = FXCollections.observableArrayList(DataStore.players);
    }

    /**
     * Returns all players as an observable list (useful for JavaFX binding).
     *
     * @return ObservableList of players
     */
    public ObservableList<Player> getAllPlayers() {
        return playerList;
    }

    /**
     * Adds a new player to both the observable list and the DataStore, and saves the data.
     *
     * @param player the new player to add
     */
    public void addPlayer(Player player) {
        playerList.add(player);
        DataStore.players.add(player);
        DataStore.save();
    }

    /**
     * Removes a player from both the observable list and the DataStore, and saves the data.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        playerList.remove(player);
        DataStore.players.remove(player);
        DataStore.save();
    }

    /**
     * Returns a list of players sorted by win rate in descending order.
     *
     * @return List of players sorted by win rate
     */
    public List<Player> getPlayersByWinRateDesc() {
        return playerList.stream()
                .sorted(Comparator.comparingDouble(Player::getWinRate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the list of Hero objects for a given player ID.
     *
     * @param playerId the ID of the player
     * @return List of Hero objects owned by the player
     */
    public List<Hero> getHeroesByPlayerId(String playerId) {
        return playerList.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .map(p -> p.getHeroes().stream() // This is a List<String> of hero IDs
                        .map(id -> DataStore.heroes.stream()
                                .filter(h -> h.getId().equals(id))
                                .findFirst().orElse(null))
                        .filter(java.util.Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * Calculates and returns the average win rate of all players.
     *
     * @return average win rate, or 0.0 if no players exist
     */
    public double getAverageWinRate() {
        return playerList.stream()
                .mapToDouble(Player::getWinRate)
                .average()
                .orElse(0.0);
    }
}
