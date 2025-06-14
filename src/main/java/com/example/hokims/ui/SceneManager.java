package com.example.hokims.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * SceneManager is a utility class responsible for switching scenes and opening dialogs.
 * It holds a reference to the primary application stage and provides methods to manage UI navigation.
 */
public class SceneManager {
    private static Stage primaryStage;

    /**
     * Initializes the main application stage.
     * Should be called once during application startup.
     *
     * @param stage the primary stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Honor of Kings IMS");
    }

    /**
     * Switches the current scene to a new FXML view.
     *
     * @param fxmlPath the path to the FXML file (e.g., "/fxml/dashboard.fxml")
     */
    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to switch scene to: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Opens a modal dialog window containing the given node.
     *
     * @param content the node to display in the dialog
     * @param title   the title of the dialog window
     */
    public static void showDialog(Node content, String title) {
        try {
            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.initOwner(primaryStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(new Pane(content)));
            dialog.setResizable(false);
            dialog.showAndWait(); // Blocks until dialog is closed
        } catch (Exception e) {
            System.err.println("Failed to open dialog: " + title);
            e.printStackTrace();
        }
    }

    /**
     * Returns the primary stage instance.
     *
     * @return the main application stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
