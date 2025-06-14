package com.example.hokims.ui;

import com.example.hokims.model.Match;
import com.example.hokims.model.TeamInMatch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Matches view.
 * Displays a list of matches and enables viewing detailed match information in a popup.
 */
public class MatchesController implements Initializable {

    // UI components bound via FXML
    @FXML private TableView<Match> matchTable;
    @FXML private TableColumn<Match, String> colMatchId;
    @FXML private TableColumn<Match, String> colDate;
    @FXML private TableColumn<Match, String> colTeamA;
    @FXML private TableColumn<Match, Integer> colScoreA;
    @FXML private TableColumn<Match, String> colTeamB;
    @FXML private TableColumn<Match, Integer> colScoreB;
    @FXML private TableColumn<Match, String> colWinner;

    /**
     * Initialization method called when the view is loaded.
     * Sets up column mappings and loads match data from JSON file.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure table columns to match Match fields
        colMatchId.setCellValueFactory(new PropertyValueFactory<>("matchId"));

        colDate.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return new SimpleStringProperty(
                    cellData.getValue().getTimestamp().format(formatter));
        });

        colTeamA.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTeamA().getName()));

        colScoreA.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTeamA().getScore()).asObject());

        colTeamB.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTeamB().getName()));

        colScoreB.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTeamB().getScore()).asObject());

        colWinner.setCellValueFactory(new PropertyValueFactory<>("winner"));

        loadMatchData(); // Load data from file

        // ✅ Handle double-click event to show match details
        matchTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !matchTable.getSelectionModel().isEmpty()) {
                Match selectedMatch = matchTable.getSelectionModel().getSelectedItem();
                showMatchDetailDialog(selectedMatch);
            }
        });
    }

    /**
     * Loads match data from a JSON file and populates the table.
     */
    private void loadMatchData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            File file = new File("data/match.json");
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }

            List<Match> matches = mapper.readValue(file, new TypeReference<List<Match>>() {});
            ObservableList<Match> matchList = FXCollections.observableArrayList(matches);
            matchTable.setItems(matchList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a detailed dialog with information about the selected match.
     *
     * @param match the selected match object
     */
    private void showMatchDetailDialog(Match match) {
        StringBuilder sb = new StringBuilder();
        sb.append("Match ID: ").append(match.getMatchId()).append("\n");
        sb.append("Date: ").append(match.getTimestamp()).append("\n\n");

        sb.append("Team A: ").append(match.getTeamA().getName()).append(" (Score: ").append(match.getTeamA().getScore()).append(")\n");
        for (String player : match.getTeamA().getPlayers()) {
            sb.append(" - ").append(player).append(": ").append(match.getHeroPicks().get(player)).append("\n");
        }

        sb.append("\nTeam B: ").append(match.getTeamB().getName()).append(" (Score: ").append(match.getTeamB().getScore()).append(")\n");
        for (String player : match.getTeamB().getPlayers()) {
            sb.append(" - ").append(player).append(": ").append(match.getHeroPicks().get(player)).append("\n");
        }

        sb.append("\nWinner: ").append(match.getWinner());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Match Details");
        alert.setHeaderText("Match Info: " + match.getMatchId());
        alert.setContentText(sb.toString());
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
