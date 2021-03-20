package com.mycompany.infs2605;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Database database = new Database();
    
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        loadDatabase();
        scene = new Scene(loadFXML("primary"), 900, 600);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("HypeMart Inventory Management System");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void loadDatabase() throws SQLException {
        Database.createUserTable();
        Database.createSupplierTable();
        Database.createStoreTable();
        Database.createSPUserTable();
        Database.createSTUserTable();
        Database.createProductTable();
        Database.createOrdersTable();
        Database.createOrderItemTable();
        Database.createOrderStatusUpdateTable();
    }
    
    
}