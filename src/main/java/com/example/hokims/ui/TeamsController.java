package com.example.hokims.ui;

import com.example.hokims.model.Player;
import com.example.hokims.model.Team;
import com.example.hokims.repository.DataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the Team Management View.
 * Displays a list of teams, their members, average level, and win rate.
 */
public class TeamsController implements Initializable {

    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team, String> colId;
    @FXML private TableColumn<Team, String> colName;
    @FXML private TableColumn<Team, String> colMembers;
    @FXML private TableColumn<Team, String> colAvgLevel;
    @FXML private TableColumn<Team, String> colWinRate;

    /**
     * Initializes the table view and sets up the column value factories and click listener.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ID and Name binding
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Show member names separated by commas
        colMembers.setCellValueFactory(cellData -> {
            String members = cellData.getValue().getMembers().stream()
                    .map(Player::getName)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(members);
        });

        // Calculate and format average level
        colAvgLevel.setCellValueFactory(cellData -> {
            double avgLevel = calculateAvgLevel(cellData.getValue());
            return new SimpleStringProperty(String.format("%.1f", avgLevel));
        });

        // Calculate and format average win rate
        colWinRate.setCellValueFactory(cellData -> {
            double avgWinRate = calculateAvgWinRate(cellData.getValue());
            return new SimpleStringProperty(String.format("%.3f", avgWinRate));
        });

        // Load data into table
        ObservableList<Team> teams = FXCollections.observableArrayList(DataStore.teams);
        teamTable.setItems(teams);

        // Show team details on double-click
        teamTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
                if (selectedTeam != null) {
                    showTeamDetails(selectedTeam);
                }
            }
        });
    }

    /**
     * Calculates the average level of a team's members.
     */
    private double calculateAvgLevel(Team team) {
        List<Player> members = team.getMembers();
        if (members.isEmpty()) return 0.0;
        double sum = members.stream().mapToInt(Player::getLevel).sum();
        return sum / members.size();
    }

    /**
     * Calculates the average win rate of a team's members.
     */
    private double calculateAvgWinRate(Team team) {
        List<Player> members = team.getMembers();
        if (members.isEmpty()) return 0.0;
        double sum = members.stream().mapToDouble(Player::getWinRate).sum();
        return sum / members.size();
    }

    /**
     * Displays a detailed dialog with full information about the selected team.
     */
    private void showTeamDetails(Team team) {
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(team.getId()).append("\n");
        details.append("Name: ").append(team.getName()).append("\n");
        details.append("Average Level: ").append(String.format("%.1f", calculateAvgLevel(team))).append("\n");
        details.append("Average Win Rate: ").append(String.format("%.3f", calculateAvgWinRate(team))).append("\n\n");

        details.append("Members:\n");
        for (Player player : team.getMembers()) {
            details.append(" - ").append(player.getName())
                    .append(" (Level: ").append(player.getLevel())
                    .append(", Win Rate: ").append(String.format("%.3f", player.getWinRate()))
                    .append(", Rank: ").append(player.getRank())
                    .append(")\n");
        }

        // Show information in a resizable alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Team Details");
        alert.setHeaderText("Details of " + team.getName());

        TextArea textArea = new TextArea(details.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getButtonTypes().setAll(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.showAndWait();
    }
}
