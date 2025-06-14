package com.example.hokims.ui;

import com.example.hokims.util.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the settings view.
 * Provides functionality to manage (add, update, delete) data entities like players, heroes, equipment, and teams.
 */
public class SettingsController {

    @FXML private ComboBox<String> dataTypeCombo;   // Dropdown for selecting the type of data
    @FXML private TextField dataIdField;            // Text field to input the ID of the entity to update/delete
    @FXML private TextField editJsonField;          // Text field to input JSON string for updates

    /**
     * Initializes the combo box with data types.
     */
    @FXML
    public void initialize() {
        dataTypeCombo.getItems().addAll("player", "hero", "equipment", "team");
    }

    /**
     * Handles deletion of a data entity.
     */
    @FXML
    private void handleDelete() {
        try {
            DataManager.delete(dataTypeCombo.getValue(), dataIdField.getText());
            showAlert("Deleted successfully.");
        } catch (Exception e) {
            showAlert("Delete failed: " + e.getMessage());
        }
    }

    /**
     * Handles updating a data entity with provided JSON.
     */
    @FXML
    private void handleUpdate() {
        try {
            DataManager.update(dataTypeCombo.getValue(), dataIdField.getText(), editJsonField.getText());
            showAlert("Updated successfully.");
        } catch (Exception e) {
            showAlert("Update failed: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to enter JSON and adds a new player.
     */
    @FXML
    private void handleAddPlayer() {
        showTextPrompt("Enter player JSON:", json -> DataManager.add("player", json));
    }

    /**
     * Prompts the user to enter JSON and adds a new hero.
     */
    @FXML
    private void handleAddHero() {
        showTextPrompt("Enter hero JSON:", json -> DataManager.add("hero", json));
    }

    /**
     * Prompts the user to enter JSON and adds new equipment.
     */
    @FXML
    private void handleAddEquipment() {
        showTextPrompt("Enter equipment JSON:", json -> DataManager.add("equipment", json));
    }

    /**
     * Prompts the user to enter JSON and adds a new team.
     */
    @FXML
    private void handleAddTeam() {
        showTextPrompt("Enter team JSON:", json -> DataManager.add("team", json));
    }

    /**
     * Utility method to show an information alert dialog.
     */
    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }

    /**
     * Utility method to show a text input dialog for JSON input and execute the given action.
     */
    private void showTextPrompt(String title, DataAction action) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(title);
        dialog.showAndWait().ifPresent(json -> {
            try {
                action.execute(json);
                showAlert("Added successfully.");
            } catch (Exception e) {
                showAlert("Failed to add: " + e.getMessage());
            }
        });
    }

    /**
     * Functional interface for passing data actions (add/update).
     */
    @FunctionalInterface
    interface DataAction {
        void execute(String json) throws Exception;
    }
}
