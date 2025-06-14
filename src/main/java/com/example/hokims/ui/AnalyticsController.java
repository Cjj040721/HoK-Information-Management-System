package com.example.hokims.ui;

import com.example.hokims.model.Player;
import com.example.hokims.service.AnalyticsService;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import com.example.hokims.model.Hero;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.util.List;

/**
 * Controller for the Analytics UI screen.
 * Handles initialization and logic for charts and tables related to hero and player statistics.
 */
public class AnalyticsController {

    @FXML private BarChart<String, Number> heroBarChart;
    @FXML private PieChart equipPieChart;
    @FXML private TableView<Player> playerTable;
    @FXML private ComboBox<Integer> topNCombo;
    @FXML private CategoryAxis heroXAxis;

    // Business logic service providing analytics data
    private final AnalyticsService service = new AnalyticsService();

    @FXML
    public void initialize() {
        initHeroChart();   // Initialize hero win rate bar chart
        initEquipChart();  // Initialize equipment usage pie chart
        initPlayerTable(); // Initialize top players leaderboard
    }

    /* ----------  Hero Win-Rate Chart  ---------- */
    /**
     * Initializes a bar chart showing the top 10 heroes by win rate.
     * Adds basic styling, axis formatting, and double-click interaction.
     */
    private void initHeroChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<Pair<Hero, Double>> list = service.getHeroWinRates()
                .stream().limit(10).toList();

        list.forEach(p -> {
            String heroName = p.getKey().getName();
            double winRate = p.getValue() * 100; // Convert to percentage
            series.getData().add(new XYChart.Data<>(heroName, winRate));
        });

        heroBarChart.getData().add(series);

        // Set X-axis label rotation and font
        heroXAxis.setTickLabelRotation(-30);
        heroXAxis.setTickLabelFont(Font.font("Arial", FontWeight.BOLD, 12));

        // Set axis label color to white after rendering
        Platform.runLater(() -> {
            heroXAxis.lookupAll(".tick-label").forEach(node -> {
                if (node instanceof Text text) {
                    text.setStyle("-fx-fill: white;");
                }
            });
            heroXAxis.lookupAll(".axis-label").forEach(node -> {
                if (node instanceof Text text) {
                    text.setStyle("-fx-fill: white;");
                }
            });
        });

        // Print hero name on double-click (placeholder for future interaction)
        heroBarChart.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                String heroName = ((CategoryAxis) heroBarChart.getXAxis())
                        .getValueForDisplay(e.getX());
                System.out.println("double-clicked hero = " + heroName);
                // SceneManager.showHeroDetails(heroName); // Optional future feature
            }
        });
    }

    /* ----------  Equipment Usage Pie  ---------- */
    /**
     * Initializes a pie chart showing the usage count of top 8 equipment items.
     */
    private void initEquipChart() {
        service.getEquipUsageAndWin().stream().limit(8)
                .forEach(p -> equipPieChart.getData().add(
                        new PieChart.Data(p.getKey().getName(),
                                p.getValue().getKey())));   // usage count
        equipPieChart.setClockwise(false);

        // Remove default transparency styling
        equipPieChart.getData().forEach(d -> d.getNode().setStyle(""));
    }

    /* ----------  Player Leaderboard Table  ---------- */
    /**
     * Initializes a table displaying top players by win rate.
     * Includes a ComboBox for selecting how many top players to show.
     */
    private void initPlayerTable() {
        TableColumn<Player,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Player,Number> winCol = new TableColumn<>("WinRate %");
        winCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getWinRate() * 100));

        playerTable.getColumns().addAll(nameCol, winCol);

        topNCombo.getItems().addAll(5, 10, 20);
        topNCombo.setValue(10); // Default value
        topNCombo.setOnAction(e -> refreshPlayerTable());

        refreshPlayerTable();
    }

    /**
     * Refreshes the player leaderboard table based on selected top-N value.
     */
    private void refreshPlayerTable() {
        playerTable.getItems()
                .setAll(service.getTopPlayers(topNCombo.getValue()));
    }
}
