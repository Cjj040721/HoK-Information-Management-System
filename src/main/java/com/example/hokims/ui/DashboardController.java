package com.example.hokims.ui;

import com.example.hokims.core.UserContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Main Dashboard Controller: handles left-side navigation and switching between sub-pages.
 */
public class DashboardController {

    /* ========== Injected UI Components ========== */
    @FXML private AnchorPane contentPane;        // Main content area to load views into
    @FXML private Label      currentUserLabel;   // Label to display the current user's name
    @FXML private Button     simulateMatchButton; // Button to open match simulator

    /* ========== Initialization (called after FXML is loaded) ========== */
    @FXML
    public void initialize() {
        // Display the current logged-in user's username
        currentUserLabel.setText(
                "Logged in as: " + UserContext.getCurrentUser().getUsername());

        // Disable match simulation button if user is not an admin
        simulateMatchButton.setDisable(!UserContext.getCurrentUser().isAdmin());

        // Load the default welcome page
        loadView("/fxml/welcome.fxml");
    }

    /* ========== Navigation Button Handlers (Left Sidebar) ========== */
    @FXML private void showPlayerView()         { loadView("/fxml/players.fxml"); }
    @FXML private void showHeroView()           { loadView("/fxml/heroes.fxml"); }
    @FXML private void showEquipmentView()      { loadView("/fxml/equipment.fxml"); }
    @FXML private void showRuneView()           { loadView("/fxml/runes.fxml"); }
    @FXML private void showTeamView()           { loadView("/fxml/teams.fxml"); }
    @FXML private void showMatchView()          { loadView("/fxml/matches.fxml"); }
    @FXML private void showAnalyticsView()      { loadView("/fxml/analytics.fxml"); }
    @FXML private void showRecommendationView() { loadView("/fxml/recommendations.fxml"); }
    @FXML private void showSettingsView()       { loadView("/fxml/settings.fxml"); }

    /* ========== Business Button Events ========== */

    /**
     * Handle the "Simulate Match" button click — opens the match simulator dialog.
     */
    @FXML
    private void handleSimulateMatch() {
        loadDialog("/fxml/match_simulator.fxml", "Match Simulator");
    }

    /**
     * Handle the "Logout" button click — switches back to login scene.
     */
    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/fxml/login.fxml");
    }

    /* ========== Utility Methods ========== */

    /**
     * Loads a sub-view into the main content pane by FXML path.
     *
     * @param fxmlPath path to the FXML file to load
     */
    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view,    0.0);
            AnchorPane.setRightAnchor(view,  0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view,   0.0);
        } catch (Exception e) {
            System.err.println("Failed to load sub-view: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Loads a dialog pop-up window by FXML path with a specified title.
     *
     * @param fxmlPath path to the dialog's FXML file
     * @param title    title of the dialog window
     */
    private void loadDialog(String fxmlPath, String title) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource(fxmlPath));
            SceneManager.showDialog(root, title);
        } catch (Exception e) {
            System.err.println("Failed to load dialog: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
