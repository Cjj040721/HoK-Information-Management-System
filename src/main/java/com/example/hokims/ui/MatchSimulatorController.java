package com.example.hokims.ui;

import com.example.hokims.combat.CombatReport;
import com.example.hokims.model.Team;
import com.example.hokims.repository.TeamRepository;
import com.example.hokims.service.MatchSimulator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

/**
 * Controller for the Match Simulator UI.
 * Allows users to select two teams and simulate a turn-based match with customizable round limits.
 */
public class MatchSimulatorController {

    // FXML UI components
    @FXML private ComboBox<Team> cboTeamA;
    @FXML private ComboBox<Team> cboTeamB;
    @FXML private Spinner<Integer> spRounds;
    @FXML private Button btnStart;
    @FXML private TableView<CombatReport.RoundRecord> tblLog;

    /**
     * Initializes the UI components after the FXML file is loaded.
     * Sets up team selection, round spinner, and match log table.
     */
    @FXML
    private void initialize() {
        /* ---------- ComboBoxes for selecting teams ---------- */
        var teams = FXCollections.observableArrayList(TeamRepository.findAll());
        cboTeamA.setItems(teams);
        cboTeamB.setItems(teams);

        // Define how team objects are displayed in the ComboBoxes
        StringConverter<Team> cvt = new StringConverter<>() {
            @Override public String toString(Team t){ return t == null ? "" : t.getName(); }
            @Override public Team fromString(String s){ return null; } // Not used
        };
        cboTeamA.setConverter(cvt);
        cboTeamB.setConverter(cvt);

        /* ---------- Spinner for round count: range 1-100, default 30 ---------- */
        SpinnerValueFactory<Integer> vf =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 30);
        spRounds.setValueFactory(vf);
        spRounds.setEditable(true); // Allow manual input

        /* ---------- TableView for match round logs ---------- */
        tblLog.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add columns using a helper method and lambda expressions
        addCol("Round",   60,  r -> r.round());
        addCol("Attacker",120, r -> r.attacker());
        addCol("Defender",120, r -> r.defender());
        addCol("Damage",  80,  r -> r.damage());
        addCol("Action",  100, r -> r.actionTag());
    }

    /**
     * Utility method to add a column to the match log table.
     *
     * @param title Column header
     * @param w     Column width
     * @param f     Function to extract the cell value from a RoundRecord
     */
    private <T> void addCol(String title, int w, java.util.function.Function<CombatReport.RoundRecord, T> f) {
        TableColumn<CombatReport.RoundRecord, T> c = new TableColumn<>(title);
        c.setPrefWidth(w);
        c.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(f.apply(cell.getValue())));
        tblLog.getColumns().add(c);
    }

    /* ---------- Start the battle simulation ---------- */

    /**
     * Called when the "Start" button is pressed.
     * Validates input, simulates the match using selected teams and rounds,
     * updates the table with round-by-round results, and shows the winner.
     */
    @FXML private void handleStart() {
        Team a = cboTeamA.getValue(), b = cboTeamB.getValue();
        if (a == null || b == null || a == b) return; // Prevent null or same team selection

        int rounds = spRounds.getValue();
        CombatReport rep = MatchSimulator.INSTANCE.simulate(a, b, rounds);

        // Populate table with round records
        tblLog.setItems(FXCollections.observableArrayList(rep.getRounds()));

        // Show match result in alert dialog
        new Alert(Alert.AlertType.INFORMATION,
                rep.getWinner() == null ? "Draw!" : rep.getWinner().getName() + " wins!")
                .showAndWait();
    }
}
