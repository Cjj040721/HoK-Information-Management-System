package com.example.hokims.ui;

import com.example.hokims.dto.HeroRecommendation;
import com.example.hokims.service.RecommendationService;
import com.example.hokims.service.RecommendationServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.control.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Hero Recommendation view.
 * Allows users to select positions, bans, and enemy picks to receive personalized hero suggestions.
 */
public class RecommendationsController {

    // FXML UI components
    @FXML private ComboBox<String> positionCombo;
    @FXML private CheckBox ownedOnlyCheck;
    @FXML private TableView<HeroRecommendation> heroTable;

    @FXML private TableColumn<HeroRecommendation, String> colName;
    @FXML private TableColumn<HeroRecommendation, Double> colScore;
    @FXML private TableColumn<HeroRecommendation, Double> colWr;
    @FXML private TableColumn<HeroRecommendation, Double> colPop;
    @FXML private TableColumn<HeroRecommendation, Double> colMas;

    @FXML private ListView<String> enemyList;
    @FXML private ListView<String> banList;

    private final RecommendationService recommendationService = new RecommendationServiceImpl();
    private final ObservableList<HeroRecommendation> heroData = FXCollections.observableArrayList();

    /**
     * Initializes the controller and sets up UI components.
     */
    @FXML
    public void initialize() {
        // Position filter dropdown
        positionCombo.getItems().addAll("All", "Tank", "Warrior", "Assassin", "Mage", "Marksman", "Support");
        positionCombo.getSelectionModel().selectFirst();

        // Table column bindings to HeroRecommendation fields
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colWr.setCellValueFactory(new PropertyValueFactory<>("winRate"));
        colPop.setCellValueFactory(new PropertyValueFactory<>("pickRate"));
        colMas.setCellValueFactory(new PropertyValueFactory<>("mastery"));

        heroTable.setItems(heroData);
        heroTable.setPlaceholder(new Label("No recommendations yet."));

        // Initial sample data
        enemyList.setItems(FXCollections.observableArrayList("Direnjie", "Sun Shangxiang"));
        banList.setItems(FXCollections.observableArrayList("Yao", "Ming Shiyin"));

        enableDragAndDrop();      // Enable dragging hero names to lists
        refreshRecommendations(); // Load initial recommendations
    }

    /**
     * Called when "Refresh" button is clicked.
     * Recalculates hero recommendations based on current input.
     */
    @FXML private void handleRefresh() {
        refreshRecommendations();
    }

    /**
     * Clears enemy and ban lists and refreshes recommendations.
     */
    @FXML private void handleClearTags() {
        enemyList.getItems().clear();
        banList.getItems().clear();
        refreshRecommendations();
    }

    /**
     * Stub for saving template (placeholder).
     */
    @FXML private void handleSaveTemplate() {
        new Alert(Alert.AlertType.INFORMATION, "Template saved (stub).").showAndWait();
    }

    /**
     * Stub for exporting recommendations (placeholder).
     */
    @FXML private void handleExport() {
        new Alert(Alert.AlertType.INFORMATION, "Exported (stub).").showAndWait();
    }

    /**
     * Updates the recommendation table using current filters (position, bans, enemies, owned-only).
     */
    private void refreshRecommendations() {
        String pos = positionCombo.getValue();
        boolean only = ownedOnlyCheck.isSelected();
        List<String> enemies = new ArrayList<>(enemyList.getItems());
        List<String> bans = new ArrayList<>(banList.getItems());

        heroData.setAll(recommendationService.getHeroRecommendations(pos, only, enemies, bans));
    }

    /**
     * Enables drag-and-drop of hero names from the table to enemy/ban lists.
     */
    private void enableDragAndDrop() {
        // Enable dragging hero names from the table
        heroTable.setRowFactory(tv -> {
            TableRow<HeroRecommendation> row = new TableRow<>();
            row.setOnDragDetected(e -> {
                if (!row.isEmpty()) {
                    Dragboard db = row.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(row.getItem().getName());
                    db.setContent(cc);
                    e.consume();
                }
            });
            return row;
        });

        // Allow dropping into enemy and ban lists
        ListView<String>[] targets = new ListView[]{enemyList, banList};
        for (ListView<String> lv : targets) {
            lv.setOnDragOver(e -> {
                if (e.getDragboard().hasString()) {
                    e.acceptTransferModes(TransferMode.COPY);
                }
                e.consume();
            });

            lv.setOnDragDropped(e -> {
                String name = e.getDragboard().getString();
                if (!lv.getItems().contains(name)) {
                    lv.getItems().add(name);
                    refreshRecommendations(); // Update after adding
                }
                e.setDropCompleted(true);
                e.consume();
            });
        }
    }
}
