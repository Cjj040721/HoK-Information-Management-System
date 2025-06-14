package com.example.hokims.ui;

import com.example.hokims.model.Hero;
import com.example.hokims.model.Player;
import com.example.hokims.repository.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the Heroes UI page.
 * Displays a list of heroes in a table and shows details about equipment, runes,
 * and players who own the selected hero.
 */
public class HeroesController implements Initializable {

    // Table and columns for hero basic info
    @FXML private TableView<Hero> heroTable;
    @FXML private TableColumn<Hero, String> colId;
    @FXML private TableColumn<Hero, String> colName;
    @FXML private TableColumn<Hero, String> colType;
    @FXML private TableColumn<Hero, Integer> colBaseHp;
    @FXML private TableColumn<Hero, Integer> colBaseAttack;

    // Lists for displaying selected hero's details
    @FXML private ListView<String> equipmentList;
    @FXML private ListView<String> runeList;
    @FXML private ListView<String> playerList;

    /**
     * Initializes the hero table and binds columns to Hero model properties.
     * Also sets up click behavior to display hero details on selection.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind table columns to Hero properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBaseHp.setCellValueFactory(new PropertyValueFactory<>("baseHp"));
        colBaseAttack.setCellValueFactory(new PropertyValueFactory<>("baseAttack"));

        // Populate the table with heroes from DataStore
        heroTable.setItems(FXCollections.observableArrayList(DataStore.heroes));
        heroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add click listener to show hero details when selected
        heroTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Hero selectedHero = heroTable.getSelectionModel().getSelectedItem();
                if (selectedHero != null) {
                    showHeroDetails(selectedHero);
                }
            }
        });
    }

    /**
     * Displays detailed information for the selected hero.
     * Populates equipment, runes, and player lists.
     *
     * @param hero the selected Hero object
     */
    private void showHeroDetails(Hero hero) {
        // Display equipped items
        List<String> eqNames = hero.getEquipments().stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
        equipmentList.setItems(FXCollections.observableArrayList(eqNames));

        // Display associated runes
        List<String> runeNames = hero.getRunes().stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());
        runeList.setItems(FXCollections.observableArrayList(runeNames));

        // Display players who own this hero
        List<String> playerNames = DataStore.players.stream()
                .filter(p -> p.getHeroes().contains(hero.getId()))
                .map(Player::getName)
                .collect(Collectors.toList());
        playerList.setItems(FXCollections.observableArrayList(playerNames));
    }
}
