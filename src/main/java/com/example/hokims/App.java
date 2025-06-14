package com.example.hokims;

import com.example.hokims.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point of the JavaFX application.
 * Launches the UI and initializes the first scene (login page).
 */
public class App extends Application {

    /**
     * This method is called after the application is launched.
     * It initializes the primary stage and loads the initial scene.
     *
     * @param primaryStage The main window of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize the primary stage in the SceneManager
        SceneManager.initialize(primaryStage);

        // Switch to the login screen (initial scene)
        SceneManager.switchScene("/fxml/login.fxml");
    }

    /**
     * The application's main method.
     * Launches the JavaFX application lifecycle.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
