package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


public class OrderStatusController {
    @FXML
    private Text heading;

    // Table of Order Statuses of a Particular Supplier
    @FXML
    private TableView<Order> selectiveTable;


    @FXML
    private TableColumn<Order, Integer> orderID;

    @FXML
    private TableColumn<Order, String> orderStatus;
    
    // Table of Order Status Updates of a Particular Supplier
    @FXML
    private TableView<OrderStatus> statusUpdatesTable;

    @FXML
    private TableColumn<OrderStatus, Integer> order_ID;

    @FXML
    private TableColumn<OrderStatus, String> statusUpdate;
    
    @FXML
    private TableColumn<OrderStatus, LocalDateTime> timestamp;
    
    @FXML
    private TableColumn<OrderStatus, Integer> user;
    @FXML
    private TableColumn<OrderStatus, String> fullname;
    
   @FXML
    private Text command;

    @FXML
    private Text subheading1;

    @FXML
    private Label subheading2;
    
    // Buttons
    @FXML
    private Button updateBtn;
    
    // search bars
    @FXML
    private TextField searchOrders;
    
    @FXML
    private TextField searchUpdates;
    
    Database database = new Database();
    int index = -1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /*
     * Text Fields for updating the status
     * - protected because other class needs to access these
     */
    @FXML
    protected ChoiceBox choiceBox;
    
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
    private Label feedback;
    
    public void initialize() throws SQLException {
        choiceBox.setValue("Update Status");
        choiceBox.getItems().addAll("Order Placed", "In Transit", "Delivered", "Cancelled");
        updateTable();  
    }
    
    //Initialise and update the Order Statuses Table and Order Status History table
    public void updateTable() throws SQLException {
        // [Order Status Table] Overview of orders and their statuses 
        orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));  
        
        // SEARCH BAR for selective Order Statuses Table
        // Filters by the Order ID that the user inputs
        FilteredList<Order> chosenOrder = new FilteredList<>(database.getOrdersOfEmpSupplier(), b -> true);
        selectiveTable.setItems(chosenOrder);
        
        searchOrders.textProperty().addListener((observable, oldValue, newValue) -> {
            chosenOrder.setPredicate(Order -> {
                
                if (newValue == null || newValue.isEmpty()) {
                   return true; 
                }
                
                // Search by order ID
                if (Integer.toString(Order.getOrder_ID()).contains(newValue)) {
                    return true;
                }
               return false;
            });   
        });
        
        //Create sorted list
        SortedList<Order> sortedOrders = new SortedList<>(chosenOrder);
        sortedOrders.comparatorProperty().bind(selectiveTable.comparatorProperty());
        selectiveTable.setItems(chosenOrder);
        
        // [Order Status Update History Table] History table i.e. Order Status Update Table
        // statusUpdatesTable.setItems(database.getOrderStatusUpdateOfEmpSupplier());
        order_ID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        statusUpdate.setCellValueFactory(new PropertyValueFactory<>("statusUpdate"));
        timestamp.setCellValueFactory(new PropertyValueFactory<> ("status_update_timestamp"));
        user.setCellValueFactory(new PropertyValueFactory<> ("user_ID"));
        fullname.setCellValueFactory(new PropertyValueFactory<> ("full_name"));
        //format datetime of the timestamp
        timestamp.setCellFactory(column -> new TableCell<OrderStatus, LocalDateTime>() {
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
        
        // SEARCH BAR for Order Status Updates Table
        // Create filtered list of status updates
        FilteredList<OrderStatus> filteredUpdates = new FilteredList<>(database.getOrderStatusUpdateOfEmpSupplier(), b -> true);
        statusUpdatesTable.setItems(filteredUpdates);
        
        searchUpdates.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUpdates.setPredicate(OrderStatus -> {
                
                if (newValue == null || newValue.isEmpty()) {
                   return true; 
                }
                String filterInLowerCase = newValue.toLowerCase();
                
                // Search by order ID, user ID, or user's fullname
                if (Integer.toString(OrderStatus.getOrder_ID()).contains(newValue)) {
                    return true;
                } else if (Integer.toString(OrderStatus.getUser_ID()).contains(newValue)) {
                   return true;
                } else if (OrderStatus.getFull_name().toLowerCase().contains(filterInLowerCase)) {
                   return true;
                }
               return false;
            });   
        });
        
        //Create sorted list
        SortedList<OrderStatus> sortedData = new SortedList<>(filteredUpdates);
        sortedData.comparatorProperty().bind(statusUpdatesTable.comparatorProperty());
        statusUpdatesTable.setItems(filteredUpdates); //Add sorted list to status updates table  
    }
    
    //retrieve orderID when a user selects a row on the order status table
    @FXML
    private String getOrderSelected(){
        index = selectiveTable.getSelectionModel().getSelectedIndex();
        if (index >-1){
            String order = orderID.getCellData(index).toString();               // order ID             
            System.out.println("ORDER HAS BEEN SELECTED");
            return order;
        } else{
            return null;
        }
    }

    //Changes status of the order
    @FXML
    public void updateBtnClicked() throws SQLException, IOException{
        String message = "Are you sure you want to update the status of this order?";
        if(createAlert(message)&& getOrderSelected()!= null){
            //If user presses okay on the alert box to confirm a order update
            database.editOrder(getOrderSelected(), choiceBox.getValue().toString());
            
            /*
             * Creates a string array containing information about the order status update
             * Information includes: orderID, new status, current time and current user's ID
            */
            String[] orderStatusUpdateArray = new String[5];
            orderStatusUpdateArray[0] = getOrderSelected();                      // orderID
            orderStatusUpdateArray[1] = choiceBox.getValue().toString();         // STATUS_UPDATE
            orderStatusUpdateArray[2] = formatter.format(LocalDateTime.now());   // UPDATE_TIMESTAMP 
            orderStatusUpdateArray[3] = database.getCurrentUserID();             // user_ID
            orderStatusUpdateArray[4] = database.getCurrentFullName();
            //Update status of the order in the database
            database.updateOrderStatusUpdateTable(orderStatusUpdateArray);
            updateTable();
            feedback.setText("UPDATE WAS MADE "
                    + formatter.format(LocalDateTime.now()));
        } else {
            //If user chooses to cancel the status update, make no changes
            feedback.setText("UPDATE WAS ABORTED: Please ensure an order"
                    + " is selected "+ formatter.format(LocalDateTime.now()));
        }
    }
    
    //Template for an alert box
    @FXML
    private boolean createAlert(String message){
        boolean confirmAdd = false;
        ButtonType proceed = new ButtonType("PROCEED", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,message );
        System.out.println("Alert created");
        alert.setTitle("Confirm changes");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            System.out.println("YES");
            confirmAdd = true;
        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("NO");
            confirmAdd = false;         
        }
        return confirmAdd;
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