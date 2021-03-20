package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class PrimaryController {
    
    public static String usernameUsed; 
    public static String currentStore_ID;
    public static String currentUserType;

    @FXML
    Button supplierViewBtn;
    
    @FXML
    Button createOrder;
    
    @FXML
    Label result;
    
    @FXML
    TextField userField;
    
    @FXML
    PasswordField passField;
    
    @FXML
    ChoiceBox choiceBox;
    
    @FXML
    VBox errorMessage;
    
    Database database = new Database();
    
    @FXML
    public void initialize() throws IOException{
         choiceBox.setValue("Store");
         choiceBox.getItems().addAll("Store", "Supplier");
    }
    
    @FXML
    private void switchToSecondary() throws IOException, SQLException {
        String username = userField.getText();
        String password = passField.getText();
        usernameUsed = userField.getText();                                                                          
        
        String userType = choiceBox.getValue().toString();
        String discriminator = database.returnUserType(userType);
        
        if (database.login(username, password) && database.getUserType(discriminator)) {
            currentStore_ID = database.getCurrentStoreID();
            boolean action = database.getUserType("st");
            if(action){
                App.setRoot("dashboardView");
            } else {
                App.setRoot("orderStatus");
            }   

        } 
            else {
                
                errorMessage.setVisible(true);
        }
    }
    
}