package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;


public class SupplierController {
    Database database = new Database();
    int index = -1;
    @FXML
    private Label supplierOutput;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    //Supplier Table
    @FXML
    private TableView supplierTable;
    @FXML
    private TableColumn supplierID;
    @FXML
    private TableColumn supplierName;
    @FXML
    private TableColumn phoneNumber;
    @FXML
    private TableColumn address;
    
    //Text Fields for adding/editing/deleting 
    @FXML
    protected TextField inputSupplierID;
    @FXML
    protected TextField inputSupplierName;
    @FXML
    protected TextField inputPhoneNumber;
    @FXML
    protected TextField inputAddress;
    
    //Buttons
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    
    //Navigation Panel
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
    
    //Initialise Supplier TableView
    @FXML
    public void updateTable() throws SQLException{
        supplierID.setCellValueFactory(new PropertyValueFactory<>("supplier_ID"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplier_name"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        ObservableList<Supplier> supplierList = database.getSuppliers();
        supplierTable.setItems(supplierList);
    }
    
    /*Set textfields with the supplier the user has selected
     * return a boolean - true if user has selected a row, false no row is selected
     */
    @FXML
    private boolean getSelected(){
        index = supplierTable.getSelectionModel().getSelectedIndex();
        if (index >-1){
            inputSupplierID.setText(supplierID.getCellData(index).toString());
            inputSupplierName.setText(supplierName.getCellData(index).toString());
            inputPhoneNumber.setText(phoneNumber.getCellData(index).toString());
            inputAddress.setText(address.getCellData(index).toString());
            System.out.println("selected");
            return true;
        }else{
            //return false if a row was not selected
            System.out.println("not selected");
            return false;
        } 
    }
    
    @FXML
    private boolean inputEmpty(){
        index = supplierTable.getSelectionModel().getSelectedIndex();
        if(inputSupplierID.getText().equals("")
                ||inputSupplierName.getText().equals("")
                ||inputPhoneNumber.getText().equals("")
                ||inputAddress.getText().equals("")){
           return true;
        }else{
            return false;
        }
    }
    
    //Retrieve supplier information for a row the user has selected
    private String[] getSupplierInput(){
        String supplier_ID = (inputSupplierID).getText();
        String supplier_Name = (inputSupplierName).getText();
        String phone_Number = (inputPhoneNumber).getText();
        String supplier_address = (inputAddress).getText();

        String[] supplierInfo = {supplier_ID,supplier_Name,phone_Number,
            supplier_address};
        return supplierInfo;
    }
      
    /*Create a new supplier when the add button is clicked (if conditions are met)
     * Conditions: phone number must be a number
    */
    @FXML
    private void addBtnClicked() throws SQLException{
        boolean confirmAdd = OrderController.createAlert("Are you sure you want "
                + "to add this supplier?");
        if(confirmAdd){
            if(inputEmpty()==false){
                if(OrderController.checkQtyIsInt(inputPhoneNumber.getText())){
                    if(database.insertSupplierStatement(getSupplierInput())){
                        updateTable(); 
                        supplierOutput.setText("A supplier was added: " 
                        +   formatter.format(LocalDateTime.now()));
                    }else{
                        supplierOutput.setText("No changes were made: a database "
                                + "error has occurred "
                        + formatter.format(LocalDateTime.now()));
                    }
                }else{
                    supplierOutput.setText("No changes were made: please ensure the "
                    + "phone number is a number "
                    + formatter.format(LocalDateTime.now()));
                }
            }else{
                supplierOutput.setText("No changes were made: please ensure "
                            + "all text fields are filled "
                            + formatter.format(LocalDateTime.now()));
            }
        }else{
            supplierOutput.setText("No changes were made: action was cancelled "
                    + formatter.format(LocalDateTime.now()));
        }
    }
           
    // Delete a supplier the user has selected
    @FXML
    private void deleteBtnClicked() throws SQLException{
        boolean confirmDelete = OrderController.createAlert("Are you sure you "
                + "want to delete this supplier?");
        if(confirmDelete){
            if(getSelected()&& inputEmpty()==false){
                database.deleteSupplierStatement(getSupplierInput());
                updateTable();
                supplierOutput.setText("A supplier was deleted: " 
                        + formatter.format(LocalDateTime.now()));      
            }else{
                supplierOutput.setText("No changes were made: please ensure a "
                        + "supplier row is selected before pressing the button "
                    + formatter.format(LocalDateTime.now()));
            }
        }else{
            supplierOutput.setText("No changes were made: action was cancelled "
                    + formatter.format(LocalDateTime.now()));
        }
    }
    
    // Edit a supplier the user has selected
    @FXML
    private void editBtnClicked() throws SQLException{
        boolean confirmEdit = OrderController.createAlert("Are you sure you "
                + "want to edit this supplier?");
        
        if(confirmEdit){
            if(inputEmpty()==false){
                if(OrderController.checkQtyIsInt(inputPhoneNumber.getText())){
                    if(database.editSupplierStatement(getSupplierInput())){
                        updateTable(); 
                        supplierOutput.setText("A supplier was edited: " 
                        +   formatter.format(LocalDateTime.now()));
                    }else{
                        supplierOutput.setText("No changes were made: a database error has occurred "
                        + formatter.format(LocalDateTime.now()));
                    }
                }else{
                    supplierOutput.setText("No changes were made: please ensure the "
                    + "phone number is a number "
                    + formatter.format(LocalDateTime.now()));
                }
            }else{
                supplierOutput.setText("No changes were made: please ensure "
                            + "all text fields are filled "
                            + formatter.format(LocalDateTime.now()));
            }
        }else{
            supplierOutput.setText("No changes were made: action was cancelled "
                    + formatter.format(LocalDateTime.now()));
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