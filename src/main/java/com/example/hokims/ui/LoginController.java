package com.example.hokims.ui;

import com.example.hokims.model.User;
import com.example.hokims.core.UserContext;
import com.example.hokims.util.AuthManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller class for the login screen.
 * Handles user authentication and transition to the dashboard.
 */
public class LoginController {

    @FXML private AnchorPane rootPane;          // Root layout pane of the login screen
    @FXML private TextField usernameField;      // Input field for username
    @FXML private PasswordField passwordField;  // Input field for password
    @FXML private Label errorLabel;             // Label to display error messages

    /**
     * Initializes the login screen.
     * Plays a fade-in animation on the root pane.
     */
    @FXML
    public void initialize() {
        playFadeIn(rootPane);
    }

    /**
     * Handles login logic when the user clicks the login button.
     * Validates credentials using AuthManager and switches to the dashboard if successful.
     * Otherwise, displays an error message.
     */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = AuthManager.login(username, password);
        if (user != null) {
            // Store the authenticated user in the context
            UserContext.login(user);
            // Play fade-out animation and switch to the dashboard
            playFadeOutAndSwitch(rootPane, "/fxml/dashboard.fxml");
        } else {
            // Show error if login fails
            errorLabel.setText("❌ Invalid username or password.");
        }
    }

    /**
     * Plays a fade-in animation on the given node.
     *
     * @param node The node to animate.
     */
    private void playFadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1.5), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Plays a fade-out animation on the given node and switches to a new scene.
     *
     * @param node        The node to animate.
     * @param targetFxml  The path to the target FXML file to load after the animation.
     */
    private void playFadeOutAndSwitch(Node node, String targetFxml) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1.2), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> SceneManager.switchScene(targetFxml));
        ft.play();
    }
}
