package com.example.hokims.ui;

import com.example.hokims.model.Player;
import com.example.hokims.model.Hero;
import com.example.hokims.repository.DataStore;
import com.example.hokims.service.PlayerService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import java.util.Comparator;

/**
 * Controller for the Players view.
 * Handles player table population, filtering, sorting, and displaying detailed player info.
 */
public class PlayersController implements Initializable {

    // FXML UI components
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, Integer> colId;
    @FXML private TableColumn<Player, String> colName;
    @FXML private TableColumn<Player, Integer> colLevel;
    @FXML private TableColumn<Player, String> colRank;
    @FXML private TableColumn<Player, String> colTeam;
    @FXML private TextField searchField;
    @FXML private Button sortByRankButton;
    @FXML private Button sortByLevelButton;

    private final PlayerService playerService = new PlayerService();

    /**
     * Initializes the controller after FXML loading.
     * Sets up table columns, event listeners, and default data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind table columns to Player properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));

        // Show team name or "None"
        colTeam.setCellValueFactory(cellData -> {
            Player p = cellData.getValue();
            String name = (p.getTeam() != null && p.getTeam().getName() != null)
                    ? p.getTeam().getName()
                    : "None";
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        // Add search box listener for filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPlayerList(newValue);
        });

        // Sort buttons
        sortByRankButton.setOnAction(e -> sortPlayersByRank());
        sortByLevelButton.setOnAction(e -> sortPlayersByLevel());

        // Load all players into the table
        ObservableList<Player> players = playerService.getAllPlayers();
        playerTable.setItems(players);

        playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Show player details on double-click
        playerTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Player selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    showPlayerDetails(selectedPlayer);
                }
            }
        });
    }

    /**
     * Converts a list of hero IDs to hero names, joined by commas.
     */
    private String resolveHeroNames(List<String> heroIds) {
        return DataStore.heroes.stream()
                .filter(h -> heroIds.contains(h.getId()))
                .map(Hero::getName)
                .collect(Collectors.joining(", "));
    }

    /**
     * Displays detailed information about the selected player in an alert dialog.
     */
    private void showPlayerDetails(Player player) {
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(player.getId()).append("\n");
        details.append("Name: ").append(player.getName()).append("\n");
        details.append("Level: ").append(player.getLevel()).append("\n");
        details.append("Win Rate: ").append(player.getWinRate()).append("\n");
        details.append("Rank: ").append(player.getRank()).append("\n");
        details.append("Team: ").append(player.getTeam() != null ? player.getTeam().getName() : "None").append("\n\n");

        details.append("Heroes:\n");
        for (String heroId : player.getHeroes()) {
            Hero hero = DataStore.heroes.stream()
                    .filter(h -> h.getId().equals(heroId))
                    .findFirst()
                    .orElse(null);
            if (hero != null) {
                details.append("  • ").append(hero.getName())
                        .append(" (").append(hero.getType()).append(")\n");
                details.append("    Equipments:\n");
                for (var eq : hero.getEquipments()) {
                    details.append("      - ").append(eq.getName()).append("\n");
                }
                details.append("    Runes:\n");
                for (var rune : hero.getRunes()) {
                    details.append("      - ").append(rune.getName()).append("\n");
                }
                details.append("\n");
            }
        }

        // Show details in a non-editable, scrollable text area
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Details");
        alert.setHeaderText("Details of " + player.getName());

        TextArea textArea = new TextArea(details.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.getButtonTypes().setAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.showAndWait();
    }

    /**
     * Filters the player list based on the text input.
     */
    private void filterPlayerList(String filter) {
        if (filter == null || filter.isEmpty()) {
            playerTable.setItems(playerService.getAllPlayers());
        } else {
            String lowerCaseFilter = filter.toLowerCase();
            ObservableList<Player> filteredList = playerService.getAllPlayers().filtered(
                    player -> player.getName().toLowerCase().contains(lowerCaseFilter)
            );
            playerTable.setItems(filteredList);
        }
    }

    /**
     * Sorts players by rank based on a custom defined rank order.
     */
    private void sortPlayersByRank() {
        List<String> rankOrder = List.of("King", "Master", "Diamond", "Platinum", "Gold", "Silver", "Bronze");
        Comparator<Player> rankComparator = Comparator.comparingInt(
                p -> rankOrder.indexOf(p.getRank())
        );
        FXCollections.sort(playerTable.getItems(), rankComparator);
    }

    /**
     * Sorts players by level in descending order.
     */
    private void sortPlayersByLevel() {
        ObservableList<Player> players = playerTable.getItems();
        FXCollections.sort(players, Comparator.comparing(Player::getLevel).reversed());
    }
}
