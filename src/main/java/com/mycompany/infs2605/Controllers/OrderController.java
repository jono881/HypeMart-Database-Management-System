package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class OrderController {
    Database database = new Database();    
    int index = -1;
    int currentOrderID;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    LocalDateTime now;
    
    @FXML Button dashboardBtn;
    
    @FXML
    ImageView stage1;
    @FXML
    ImageView stage2;
    
    //product table
    @FXML Text productLabel;
    @FXML TextField searchProducts; 
    @FXML Text productHeading;
   
    
    //Create an order middle
    @FXML
    private Button transferBtn;
    @FXML
    private TextField inputTransferQty;
    

    //Supplier Nodes
    @FXML
    private TextField inputSupplier;
    @FXML
    private Button createOrderBtn;

        
    //Order Item Table
    @FXML
    private TableView orderItemsTable;
    @FXML
    private TableColumn<OrderItem, Integer> o_orderItemID;
    @FXML
    private TableColumn<OrderItem, Integer> o_orderID;
    @FXML
    private TableColumn<OrderItem, String> o_productID;
    @FXML
    private TableColumn<OrderItem, Integer> o_quantity;
    @FXML
    private TableColumn<OrderItem, Integer> o_orderItemPrice;

    
    //Order Buttons
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
    @FXML
    private Text description1;
    @FXML
    private Text description2;
    @FXML
    private Label yourOrder;
    
    // change image of progress bar
    @FXML
    private ImageView image;
    
    
    //Product search bar
    @FXML
    private TableView productstable;
    @FXML
    private TableColumn<Product, String> productID;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productPrice; //not sure why int doesn't work
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
    private HBox newOrderHBox1;
    
    //ON START: create product table and search bar, hide order item table
    @FXML
    public void initialize() throws SQLException {
        
        createProductSearchBar();
        initialiseOrderItemTable();
        setVisibility(false);
    }  
      
    /*
     * Prevent access to order modification before an order is created
     * and a supplier is provided
     */
    @FXML
    public void setVisibility(boolean visibility){
        orderItemsTable.setVisible(visibility);
        deleteProductItemBtn.setVisible(visibility);
        editQtyBtn.setVisible(visibility);
        editQtyNum.setVisible(visibility);
        confirmOrderBtn.setVisible(visibility);
        inputTransferQty.setVisible(visibility);
        transferBtn.setVisible(visibility);
        description1.setVisible(visibility);
        description2.setVisible(visibility);
        yourOrder.setVisible(visibility);
        productstable.setVisible(visibility);
        productHeading.setVisible(visibility);
        searchproducts.setVisible(visibility);
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
    
    /*
     * Order created by Store_EMP when create order btn clicked
     * - ensure supplier exists in the database
     * - make order item modification nodes visible AFTER an order is created
     */
    @FXML
    private void createOrderBtnClicked() {
        //Alert Message
        String message = "Are you sure you want to create a new order?";
        
        //Check if supplier exists
        String supplier = inputSupplier.getText().toLowerCase();
        boolean supplierExists = database.checkSuppliers(supplier);
        
        
        if(createAlert(message)&& supplierExists ){
            //Create String array to store order ID and store ID
            String[] newOrder = new String[2];
            newOrder[0] = database.getSupplierIDFromName(supplier);     //gets supplier ID from supplier name
            newOrder[1] = database.getCurrentStoreID();                 //gets the store ID of the current user
            database.createNewOrder(newOrder);                          //insert new order object in database using supplier ID + store ID
            System.out.println("New Order has been created with supplier: " + supplier);
            //after the order has been created, make order item table and buttons viewable.
            setVisibility(true);
            createOrderBtn.setVisible(false);                           //prevent user from pressing create an order button
            inputSupplier.setEditable(false);                           //prevent user from editing the supplier name
            initialiseOrderItemTable();                                 //refresh order item table
            stage2.setVisible(true);
            stage1.setVisible(false);
        } else if(!inputSupplier.getText().isEmpty()||inputSupplier.getText() != null){
            //If no supplier is inputted, prevent order creation
            feedbackLabel.setText("Please ensure you input a valid supplier.");
        } else {
            //If order aborts order creation or the supplier already exists, make no changes
            feedbackLabel.setText("ORDER WAS NOT CREATED: Please ensure a "
                    + "valid supplier is inputted");              
        }
    }
    
    //Alert creation template - detailed message as parameter
    @FXML
    protected static boolean createAlert(String message){
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
    
    //Creates OrderItem Table - products within the order
    @FXML
    private void initialiseOrderItemTable() {
        o_orderID.setCellValueFactory(new PropertyValueFactory<>("order_ID"));
        o_productID.setCellValueFactory(new PropertyValueFactory<>("product_ID"));
        o_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        o_orderItemPrice.setCellValueFactory(new PropertyValueFactory<>("orderItemprice"));
        ObservableList<OrderItem> orderItemList = database.getOrderItems(database.getCurrentOrderID());
        orderItemsTable.setItems(orderItemList);
        System.out.println("ORDER ITEM TABLE REFRESHED");
    }

    //Redirect user to create confirmation page when confirm order btn clicked
    @FXML
    private void confirmBtnClicked() throws IOException{
        App.setRoot("createConfirmation");
    }
    
    /*  Retrieve product ID and product price information from the products
     *  table when user selects a row on the products table
    */
    @FXML
    private String[] getProductSelected(){
       index = productstable.getSelectionModel().getSelectedIndex();
       String[] productArray = new String[2];
        if (index >-1){
            productArray[0] = productID.getCellData(index).toString();  //product id
            productArray[1] = productPrice.getCellData(index).toString(); //product price
            System.out.println("PRODUCT SELECTED: getproductselected" +productID.getCellData(index).toString() );
            return productArray;
        } else{
            return null; //fix this
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
            System.out.println("FAIL ORDER ITEM HAS BEEN SELECTED");
            return null; //fix this
        }
    }
    
    /* Add a new order item to the order when the 
     * user clicks the 'Add Product' button
     * - ensure quantity inputted is a valid number (greater than 0)
     * - ensure product does not already exist in the order (prevent product duplication)
     */
    @FXML 
    private void transferBtnClicked() {
        String inputQty = inputTransferQty.getText();
        
        //Ensure quantity inputted is a number
        if(errorCheck(inputQty)&&getProductInfoClicked()!= null){
            /* The quantity inputted is a valid number and a product
             * from the product table is selected
             */    
            if(database.checkOIExists(getProductInfoClicked())){                                       
                //The product already exists within the order, do not create a new order item
                feedbackLabel.setText("Error: product already exists in the "
                        + "order. Please edit the quantity if required. " 
                        + formatter.format(LocalDateTime.now()));
            } else{
                 //The product does not exist in the order - Create a new order item and display it
                database.createNewOrderItem(getProductInfoClicked());
                initialiseOrderItemTable();
                feedbackLabel.setText("Success: product was added to the order " 
                        + formatter.format(LocalDateTime.now()));
            }
        } else{
            //If quantity is not a number, do not add product to the order
            feedbackLabel.setText("Error: Ensure a product is selected and the "
                    + "quantity is a number higher than 0 " 
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    //Removes order item when delete button is clicked (depends on what row the user clicks in OI table
    @FXML
    public void deleteBtnClicked() throws SQLException{
        String message = "Are you sure you want to delete this order item?";
        boolean delete = createAlert(message);
        if(delete && getOISelected()!= null){
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
    
    //Changes quantity of the order item 
    @FXML
    public void editBtnClicked() throws SQLException{
        String message = "Are you sure you want to edit this order item?";
        String quantity = editQtyNum.getText();
        //If the user presses confirm to edit the order item
        if(createAlert(message)&& errorCheck(quantity)&& getOISelected() != null){
            //edit order item if quantity is a number and greater than 0
            database.editOrderItem(getOISelected(), editQtyNum.getText(), 
                    getProductPrice());
            initialiseOrderItemTable();
            feedbackLabel.setText("An order item was updated: " 
                    + formatter.format(LocalDateTime.now()));
        } else{
            //if the user decides to cancel the edit, do not make changes
            feedbackLabel.setText("No changes were made: ensure a product is "
                    + "selected and the quantity inputted is a number " 
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    /*
     * Retrieve information about the product selected on the products table 
     * into a string array - retrieves orderID, productID, quantity, price
    */
    protected String[] getProductInfoClicked() {
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
    
    /* Calculate initial price of the order item
     *  calculation: quantity x price of product
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
    
    
    
    // Check that the user's quantity inputted is an integer

    protected static boolean checkQtyIsInt(String quantity){
        try{
            int qty = Integer.parseInt(quantity);
            return true;
        } catch(NumberFormatException nfe){
            return false; }
    }
    
    /* Certify that the quantity inputted is a number greater than 0 
     * - it would be redundant for an order item to have a quantity of 0
     */
    protected static boolean errorCheck(String quantity){
        if(checkQtyIsInt(quantity)){
            return Integer.parseInt(quantity) > 0;
        }else{
            System.out.println("ERROR CHECK: FALSE");
            return false;
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