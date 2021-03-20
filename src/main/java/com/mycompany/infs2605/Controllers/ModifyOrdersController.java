package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


public class ModifyOrdersController {
    public static int orderID;
    public static String supplierName;
    Database database = new Database();
    int index = -1;
    String[] info = new String[4];
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    //Order Table
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> orderIDCol;
    @FXML
    private TableColumn<Order, String> supplierIDCol;
    @FXML
    private TableColumn<Order, String> storeIDCol;
    @FXML
    private TableColumn<Order, String> statusCol;
    @FXML
    private TableColumn<Order, LocalDateTime> order_timestampCol;   
    
    //Buttons
    @FXML
    private Button createNewOrderBtn;
    @FXML
    private Button editOrderBtn;
    @FXML
    private Button deleteOrderBtn; 
    
    //Feedback
    @FXML
    private Label feedback;
    
    //Navigation Pane
    @FXML
    static HBox dashboardHBox;
    @FXML
    static HBox supplierHBox;
    @FXML
    static HBox ordersHBox;
    @FXML
    static HBox newOrderHBox;
    @FXML
    static HBox orderStatusHBox;
    @FXML
    static HBox aboutUsHBox;
    
   
    @FXML
    private void initialize() throws SQLException {
        updateTable();
    }
    
    /*
     * Initialise table of orders
     * - format order timestamp to of format: year-month-day hour:min:seconds
    */
    @FXML
    public void updateTable() throws SQLException{
        ObservableList<Order> orderList = database.getOrders();
        orderTable.setItems(orderList);
        
        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        supplierIDCol.setCellValueFactory(new PropertyValueFactory<>("supplier_ID"));
        storeIDCol.setCellValueFactory(new PropertyValueFactory<>("store_ID"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        order_timestampCol.setCellValueFactory(new PropertyValueFactory<>("order_timestamp"));
        
        //Format Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order_timestampCol.setCellFactory(column -> new TableCell<Order, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }
    
    // Retrieve information about the order selected
    @FXML
    private String[] getSelected(){
        index = orderTable.getSelectionModel().getSelectedIndex();
        if (index <=-1){
            return null;
        }
        info[0] = orderIDCol.getCellData(index).toString();
        info[1] = supplierIDCol.getCellData(index).toString();
        info[2] = storeIDCol.getCellData(index).toString();
        info[3] = statusCol.getCellData(index).toString();
        //info[4] = order_timestampCol.getCellData(index).toString();

        System.out.println("SELECTED: ORDER ID = " + info[0]);
        orderID = Integer.parseInt(info[0]);
        supplierName =  info[1];
        return info;
        
    }

    
    //Redirect to create order screen if "create a new order" btn is clicked
    @FXML
    private void createNewOrderBtnClicked()throws IOException {
        App.setRoot("orderView");
    }
    
    /* 
     * When the user clicks delete, delete the order of the row that the user
     * has clicked on
     */
    @FXML
    private void deleteOrderBtnClicked() throws IOException, SQLException{
        String message = "Are you sure you want to delete this order?";
        if(OrderController.createAlert(message)){
            info = getSelected();
            database.deleteOrder(info[0]);
            updateTable();
        }else{
            feedback.setText("No changes were made: " 
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    //Alert box to confirm order deletion
    protected static void deleteOrderAlertBox(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR,message);
        alert.setTitle("Invalid deletion");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
    }  
    
    //Redirect to screen to edit order items within the order
    @FXML
    protected void editOrderBtnClicked() throws IOException{
        String message = "Are you sure you want to edit this order?";
        if(OrderController.createAlert(message)){
            App.setRoot("editOrderItems");
        }
    }    
    
    //Navigation Panel
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