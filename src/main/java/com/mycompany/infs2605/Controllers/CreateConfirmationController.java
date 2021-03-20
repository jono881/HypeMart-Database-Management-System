package com.mycompany.infs2605;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;


public class CreateConfirmationController {
    @FXML
    HBox dashboardHBox;
    @FXML
    HBox supplierHBox;
    @FXML
    HBox ordersHBox;
    @FXML
    static HBox newOrderHBox;
    @FXML
    static HBox orderStatusHBox;
    @FXML
    static HBox aboutUsHBox;
    Database database = new Database();
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    @FXML
    private void switchToSupplier() throws IOException {
        database.checkPermission("sp", "supplier", "supplierView");
    }
    
    @FXML
    private void switchToCreateOrder() throws IOException {
        database.checkPermission("sp", "new order", "orderView");
    }
    
    @FXML
    private void switchToDashboard() throws IOException {
        database.checkPermission("sp", "dashboard", "dashboardView");
    }
    
    @FXML
    private void switchToChangeOrder() throws IOException{
        database.checkPermission("sp", "modify", "changeOrderView");
    }
    
    @FXML
    private void switchToEditOrder() throws IOException{
        database.checkPermission("sp", "my orders", "modifyOrders");
    }
    
    @FXML
    private void switchToOrderStatuses() throws IOException{
        database.checkPermission("st", "order status", "orderStatus");
        }
    
    @FXML
    private void switchToAboutUs() throws IOException{
        App.setRoot("about");
    }
}
