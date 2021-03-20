package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class EditOrderItemsController {
    Database database = new Database();   
    OrderController oc = new OrderController();
    int index = -1;
    int currentOrderID;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    //Display Supplier 
     @FXML              
     private Label displaySupplier;
    
    //Product Table
    @FXML
    Text productLabel;
    @FXML
    TextField searchProducts;
   
    //Nodes for adding a product to the order
    @FXML    
    private Button transferBtn;
    @FXML    
    private TextField inputTransferQty;
    


        
    //Order Item Table
    @FXML    
    private TableView<OrderItem> orderItemsTable;
    @FXML    
    private TableColumn<OrderItem, Integer> o_orderID;
    @FXML    
    private TableColumn<OrderItem, String> o_productID;
    @FXML    
    private TableColumn<OrderItem, Integer> o_quantity;
    @FXML    
    private TableColumn<OrderItem, Integer> o_orderItemPrice;

    
    //Order Item Buttons
    @FXML    
    private Button deleteProductItemBtn;
    @FXML    
    private Button editQtyBtn;
    @FXML    
    private TextField editQtyNum;      
    @FXML    
    private Button confirmOrderBtn;
    
    //Feedback
    @FXML    
    private Label feedbackLabel;
    
    //Headings & Descriptions
    @FXML
    private Text description1;
    @FXML
    private Text description2;
    @FXML
    private Label yourOrder;
    

    //search bar
    @FXML    
    private TableView<Product> productstable;
    @FXML    
    private TableColumn<Product, String> productID;
    @FXML    
    private TableColumn<Product, String> productName;
    @FXML    
    private TableColumn<Product, Integer> productPrice;
    @FXML    
    private TableColumn<Product, String> productType;
    @FXML    
    private TextField searchproducts;
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
    public void initialize() throws SQLException, IOException {
        createProductSearchBar();           /* Initialise product search bar */
        initialiseOrderItemTable();         /* Initialise product tableview */
        displaySupplier.setText(            /* Retrieve supplier name for the order */
        database.getSupplierNameFromID(ModifyOrdersController.supplierName));
    }  
    

    //Create a tableview for order items for a given order
    @FXML
    private void initialiseOrderItemTable() throws SQLException, IOException{
        o_orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        o_productID.setCellValueFactory(new PropertyValueFactory<>("product_ID"));
        o_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        o_orderItemPrice.setCellValueFactory(new PropertyValueFactory<>("orderItemprice"));
        ObservableList<OrderItem> orderItemList = 
                database.getOrderItems(ModifyOrdersController.orderID);     
        orderItemsTable.setItems(orderItemList);
        System.out.println("ORDER ITEM TABLE REFRESHED");
    }
    
    /* Add a new order item to the order when the 
     * user clicks the 'Add Product' button
     * - ensure quantity inputted is a valid number (greater than 0)
     * - ensure product does not already exist in the order (prevent product duplication)
    */
    @FXML 
    private void transferBtnClicked() throws SQLException, IOException{
        String quantity = inputTransferQty.getText();        
        if(OrderController.errorCheck(quantity)&& getProductInfoClicked()!= null){
            /* The quantity inputted is a valid number and a product
             * from the product table is selected
             */            
            if(database.checkOIExists(getProductInfoClicked())){  
                //The product already exists within the order, do not create a new order item
                feedbackLabel.setText("Error: product already exists in the "
                        + "order. Please edit the quantity if required.");
            } else{
                //The product does not exist in the order - Create a new order item and display it
                database.createNewOrderItem(getProductInfoClicked());
                initialiseOrderItemTable(); 
                feedbackLabel.setText("Success: product was added to the order");  
                inputTransferQty.setText("");
            }
        } else{
            //If quantity is not a valid number, do not add product to the order
            feedbackLabel.setText("Error: product was not transferred because "
                    + "quantity must be a number");
        }
    }
    
    /* 
     * User wishes to update the quantity of the order item
     */
    @FXML
    public void editBtnClicked() throws SQLException, IOException{
        String alertMessage = "Are you sure you want to edit this order item?";
        String quantity = editQtyNum.getText();

        if(OrderController.createAlert(alertMessage) && errorCheck(quantity) && 
                getOISelected() != null){
            /* Update order item if quantity is a valid number and the desired
             * order item row to be updated is selected
             */            
            database.editOrderItem(getOISelected(), editQtyNum.getText(), 
                    getProductPrice());
            initialiseOrderItemTable();
            feedbackLabel.setText("An order item was updated: " 
                    + formatter.format(LocalDateTime.now()));
        } else{
            //If the user decides to cancel the edit, do not make changes
            feedbackLabel.setText("No changes were made: ensure a product is "
                    + "selected and the quantity inputted is a number " 
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    /* User wishes to delete an order item */
    @FXML
    public void deleteBtnClicked() throws SQLException, IOException{
        String message = "Are you sure you want to delete this order item?";
        if(OrderController.createAlert(message)&& getOISelected()!= null){
            database.deleteOrderItem(getOISelected());
            initialiseOrderItemTable();
            feedbackLabel.setText("An order item was deleted: " 
                    + formatter.format(LocalDateTime.now()));
        } else{
            System.out.println("DELETE WAS ABORTED");
            feedbackLabel.setText("No changes were made: ensure a product "
                    + "from your order is selected " 
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    /* Calculate initial price of the order item
     * quantity x price of product
     * return price as string
     */
    private String calculateOITotalPrice(){
        String[] inputProduct = getProductSelected();
        int quantity = Integer.parseInt(inputTransferQty.getText());
        int price = Integer.parseInt(inputProduct[1]);
        return Integer.toString(quantity*price);
    }
    
    //Retrieve the price of the product selected in the products table
    private int getProductPrice() throws SQLException{
         return database.getProductPrice(o_productID.getCellData(index));
    }
    
    /*  Retrieve product ID and product price information from the products
     *  table when user selects a row on the products table
    */
    @FXML
    private String[] getProductSelected(){
       index = productstable.getSelectionModel().getSelectedIndex();
       String[] productArray = new String[2];
        if (index >-1){
            productArray[0] = productID.getCellData(index).toString();    //product id
            productArray[1] = productPrice.getCellData(index).toString(); //product price
            return productArray;
        } else{
            return null; 
        }
    }
    
    /*
     * Retrieve information about the product selected on the products table 
     * into a string array - retrieves orderID, productID, quantity, price
    */
    protected String[] getProductInfoClicked() throws SQLException{
        String[] selectedProduct = new String[4];
        String[] inputProduct = getProductSelected();
        if(inputProduct!= null){
            selectedProduct[0] = Integer.toString(database.getCurrentOrderID());           //orderID
            selectedProduct[1] = inputProduct[0];                                          //productID
            selectedProduct[2] = inputTransferQty.getText();                               //quantity
            selectedProduct[3] = calculateOITotalPrice();                                  //price
            return selectedProduct;
        }else{
            System.out.println("PRODUCT SELECTION IS NULL");
            return null;
        }
    }
    
    //retrieve orderID and product ID when a user selects a row on the order items table
    @FXML
    private String[] getOISelected(){
       index = orderItemsTable.getSelectionModel().getSelectedIndex();
       String[] productArray = new String[2];
        if (index >-1){
            productArray[0] = o_orderID.getCellData(index).toString();                  //order ID
            productArray[1] = o_productID.getCellData(index).toString();                //product ID
            System.out.println("ORDER ITEM HAS BEEN SELECTED");
            return productArray;
        } else{
            return null; //fix this
        }
    }


    
    //Initialise product search bar and table
    @FXML
    public void createProductSearchBar()throws SQLException{
        //Initialise product table
        ObservableList<Product> ProductsList = database.getProducts();
        productID.setCellValueFactory(new PropertyValueFactory<>("product_ID"));
        productName.setCellValueFactory(new PropertyValueFactory<> ("product_name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<> ("price"));
        productType.setCellValueFactory(new PropertyValueFactory<> ("product_type")); 
        
        //Create filtered list of products
        FilteredList<Product> filteredProducts = new FilteredList<>(database.getProducts(), b -> true);
        productstable.setItems(filteredProducts);
        
        
        searchproducts.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(Product -> {
               if (newValue == null || newValue.isEmpty()) {
                   return true; 
               }
               String filterInLowerCase = newValue.toLowerCase();
               if (Product.getProduct_name().toLowerCase().contains(filterInLowerCase)) {
                   return true;
               } else if (Product.getProduct_ID().toLowerCase().contains(filterInLowerCase)) {
                   return true;
               } else if (Product.getProduct_type().toLowerCase().contains(filterInLowerCase)) {
                   return true;
               }
               return false;
            });   
        });
        
        //Create sorted list from filtered product list
        SortedList<Product> sortedData = new SortedList<>(filteredProducts);
        sortedData.comparatorProperty().bind(productstable.comparatorProperty());
        productstable.setItems(filteredProducts); //Add sorted list to products table
    }
    
    /* Check that the user-inputted quantity is valid
     * - ensure it is a number
     * - ensure it is greater than 0
    */
    protected static boolean errorCheck(String quantity){
        if(OrderController.checkQtyIsInt(quantity)){
            //return true if quantity is a positive number above 0
            return Integer.parseInt(quantity) > 0; 
        }else{
            return false;   //quantity is not a valid number
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
    
    @FXML
    private void confirmBtnClicked() throws IOException{
        App.setRoot("createConfirmation");
    }
}