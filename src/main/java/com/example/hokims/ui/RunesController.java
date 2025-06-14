package com.example.hokims.ui;

import com.example.hokims.model.Hero;
import com.example.hokims.model.Rune;
import com.example.hokims.repository.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the Rune statistics view.
 * Displays rune usage data by aggregating usage across all heroes.
 */
public class RunesController implements Initializable {

    @FXML private TableView<Rune> runeTable;                     // Table view for displaying rune data
    @FXML private TableColumn<Rune, String> colId;              // Column for rune ID
    @FXML private TableColumn<Rune, String> colName;            // Column for rune name
    @FXML private TableColumn<Rune, Integer> colUsageCount;     // Column for rune usage count

    /**
     * Initializes the rune table with data and usage statistics.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind table columns to Rune properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsageCount.setCellValueFactory(new PropertyValueFactory<>("usageCount"));

        // Count how many times each rune is used by all heroes
        Map<String, Integer> usageMap = new HashMap<>();
        for (Hero hero : DataStore.heroes) {
            hero.getRunes().forEach(rune -> {
                usageMap.put(rune.getId(), usageMap.getOrDefault(rune.getId(), 0) + 1);
            });
        }

        // Build and sort the rune list by usage count (descending)
        List<Rune> runeList = DataStore.runes.stream()
                .peek(r -> r.setUsageCount(usageMap.getOrDefault(r.getId(), 0)))
                .sorted(Comparator.comparingInt(Rune::getUsageCount).reversed())
                .collect(Collectors.toList());

        // Set the data in the TableView
        ObservableList<Rune> observableRunes = FXCollections.observableArrayList(runeList);
        runeTable.setItems(observableRunes);
    }
}
