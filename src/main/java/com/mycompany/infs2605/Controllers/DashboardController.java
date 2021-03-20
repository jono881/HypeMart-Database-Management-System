package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


public class DashboardController {
    
    // Order Statuses Table
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, String> orderID;
    @FXML
    private TableColumn<Order, String> orderStatus;
    @FXML
    private Text heading;
    
    // Pie Chart
    @FXML
    private PieChart chart;
    
    // Search orders statuses table
    @FXML
    private TextField searchOrders;
    
    // Buttons and Labels
    @FXML
    private Button searchBtn;
    @FXML
    private Button allOrdersBtn; 
    @FXML
    private Label percentage;
    @FXML
    private Text command;
   
    // Sidebar navigation panel
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
    
    // User's search and database
    public static String searchInput;
    Database database = new Database();
    
    // Alert Box which appears when a supplier user attempts to access
    protected static void dashboardAlertBox(){
        Alert alert = new Alert(Alert.AlertType.ERROR,"Sorry, you do not have "
                + "permission to view the dashboard");
        alert.setTitle("Invalid permissions for the dashboard");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            System.out.println("ALERT POPUP");
        } 
    }    
   
    public void initialize() throws SQLException {
        ObservableList<Order> OrdersList = database.getOrders();
        ordersTable.setItems(OrdersList);
        orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
        
        // Creates the Pie Chart
        ObservableList<PieChart.Data> orderStatusData;
        orderStatusData = FXCollections.observableArrayList(
            new PieChart.Data("Placed", 
                    database.getOrdersWithStatus("Order Placed")),
            new PieChart.Data("In Transit", 
                    database.getOrdersWithStatus("In Transit")),
            new PieChart.Data("Delivered", 
                    database.getOrdersWithStatus("Delivered")),
            new PieChart.Data("Cancelled", 
                    database.getOrdersWithStatus("Cancelled")));
        
        System.out.println("Pie Chart data added");
        chart.setData(orderStatusData); 
        
        // Allows the percentage of each slice of the pie chart to be displayed
        for (final PieChart.Data data: chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                double sum = 0;
                for (PieChart.Data d : chart.getData()) {
                    sum += d.getPieValue();
                    }
                percentage.setText("Percentage: " + String.format("%.0f%%", 
                        100*data.getPieValue()/sum));
            });
        }
    }
    
    // Search button handler method
    public void handleSearchBtn() throws SQLException {
        searchInput = searchOrders.getText().toLowerCase();
        ObservableList<Order> OrdersList = database.getOrders();
        
        // When user's input is empty, display all orders
        if (searchInput.equals("")) {
            ordersTable.setItems(OrdersList);
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
            System.out.println("Emoty search is working");
            
        // When the user searches for orders that have been placed
        } else if (searchInput.equals("order placed")) {
            ordersTable.setItems(database.ordersWithStatus("Order Placed")); 
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
            System.out.println("Orders that have been placed are shown");
            
        // When the user searches for orders that are in transit
        } else if (searchInput.equals("in transit")) {
            ordersTable.setItems(database.ordersWithStatus("In Transit"));
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status")); 
            System.out.println("Orders that are in transit are shown");
            
        // When the user searches for orders that have been delivered
        } else if (searchInput.equals("delivered")) {
            ordersTable.setItems(database.ordersWithStatus("Delivered"));
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
            System.out.println("Orders that have been delievered are shown");
            
        // When the user searches for orders that have been cancelled
        } else if (searchInput.equals("cancelled")) {
            ordersTable.setItems(database.ordersWithStatus("Cancelled"));
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
            System.out.println("Orders that have been cancelled are shown");
            
        /* When the user searches for orders based on a product. This method
        only works when the entire product name is written */
        } else if (!searchInput.equals("order placed") && 
                !searchInput.equals("in transit") && 
                !searchInput.equals("delivered") && 
                !searchInput.equals("cancelled") && 
                !searchInput.equals("")) {
            ordersTable.setItems(database.ordersWithProduct(searchInput));                         
            orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
            orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
            System.out.println("Orders with: " + searchInput + " are shown");
        }
    }
    
    // To reset the table
    public void handleAllOrdersBtn() throws SQLException {
        ObservableList<Order> OrdersList = database.getOrders();
        ordersTable.setItems(OrdersList);
        orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<> ("status"));
    }
    
    //Navigation panel
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
