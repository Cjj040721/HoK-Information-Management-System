package com.example.hokims.ui;

import com.example.hokims.model.Equipment;
import com.example.hokims.model.Hero;
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
 * Controller for the Equipment view.
 * Displays a table of all equipment items along with their usage counts across heroes.
 */
public class EquipmentsController implements Initializable {

    // FXML UI components
    @FXML private TableView<Equipment> equipmentTable;
    @FXML private TableColumn<Equipment, String> colId;
    @FXML private TableColumn<Equipment, String> colName;
    @FXML private TableColumn<Equipment, Integer> colUsageCount;

    /**
     * Initialization method called automatically when the UI is loaded.
     * Sets up the table and populates it with equipment usage statistics.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set column bindings to Equipment fields
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsageCount.setCellValueFactory(new PropertyValueFactory<>("usageCount"));

        // Count how many times each equipment is used by heroes
        Map<String, Integer> usageMap = new HashMap<>();
        for (Hero hero : DataStore.heroes) {
            hero.getEquipments().forEach(equipment -> {
                usageMap.put(equipment.getId(), usageMap.getOrDefault(equipment.getId(), 0) + 1);
            });
        }

        // Create the list of equipment with usage counts filled in,
        // then sort by usage count in descending order
        List<Equipment> equipmentList = DataStore.equipments.stream()
                .peek(e -> e.setUsageCount(usageMap.getOrDefault(e.getId(), 0)))
                .sorted(Comparator.comparingInt(Equipment::getUsageCount).reversed())
                .collect(Collectors.toList());

        // Populate the table with the equipment list
        equipmentTable.setItems(FXCollections.observableArrayList(equipmentList));
    }
}
