package com.mycompany.infs2605;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class Database {
    public static Connection conn; 
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                            Locale.ENGLISH);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // method to open connection to database
    public static void openConnection() { 
        if(conn != null){
            try{
                conn.close();
            } catch(SQLException sqle){
                sqle.printStackTrace();
            }
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
        } catch (SQLException e) {
            System.out.println("Database connection could not be opened");
        }  
    }
    // method to close connection to the database
    public static void closeConnection() {
        try{
            conn.close();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    //Access restrictions depending on the type of user
    protected void checkPermission(String userType, String screen, String view){
        try{
            boolean supplierUser = getUserType(userType);
        if(supplierUser){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Sorry, you do not "
                    + "have permission to view the " + screen +  " screen");
            alert.setTitle("Invalid user permissions");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                System.out.println("ALERT POPUP");
            } 
        } else{
            App.setRoot(view);
        }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // Create User Table
    public static void createUserTable() { 
        try{
            openConnection(); 
            PreparedStatement createUserTable; 
            PreparedStatement insertData; 
            createUserTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS USER" 
                    + "("
                    + " USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," 
                    + " USERNAME TEXT NOT NULL UNIQUE," 
                    + " PASSWORD TEXT NOT NULL," 
                    + " FULL_NAME TEXT NOT NULL," 
                    + " USER_TYPE TEXT NOT NULL" 
                    + ");"); 
            createUserTable.execute();
            createUserTable.close();
            System.out.println("TABLE CREATED: User");

            // Batch inserting dummy data for the users
            insertData = conn.prepareStatement("INSERT OR IGNORE INTO USER(USER_ID, USERNAME,"
                    + "PASSWORD, FULL_NAME, USER_TYPE) VALUES (?,?,?,?,?);"); 

            int[] userIds = {1,2,3,4,5,6};
            String[] usernames = {"adam02","trent03","bob04","lucas05", "jenny06", "marc01"};
            String[] passwords = {"dogs","frogs","pears","apples","cat","sunny"};
            String[] fullnames = {"Adam Lee", "Trent Ca", "Ryan Da", "Lucas Tran","Jenny Smith","Marc Sen"};
            String[] usertypes = {"st","st","sp","sp","sp","sp"};                         // SUBTYPE DISCRIMINATOR: St = store, and Sp = supplier

            for (int i=0; i < userIds.length; i++) {
                insertData.setInt(1, userIds[i]);
                insertData.setString(2, usernames[i]);
                insertData.setString(3, passwords[i]);
                insertData.setString(4, fullnames[i]);
                insertData.setString(5, usertypes[i]);
                insertData.execute();
        }
        insertData.close(); 
        closeConnection(); 
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    // Creating the Supplier Table 
    public static void createSupplierTable() {
        try{
            PreparedStatement createSupplierTable; 
            PreparedStatement insertData; 
            openConnection(); 

            createSupplierTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS SUPPLIER"
                    + "("
                    + " SUPPLIER_ID TEXT PRIMARY KEY,"
                    + " SUPPLIER_NAME TEXT NOT NULL,"
                    + " PHONE_NUMBER INTEGER NOT NULL,"
                    + " ADDRESS TEXT NOT NULL"
                    + ");");
            createSupplierTable.execute();
            createSupplierTable.close();
            System.out.println("TABLE CREATED: Supplier");

            // Batch inserting dummy data for the suppliers
            insertData = conn.prepareStatement("INSERT OR IGNORE INTO SUPPLIER(SUPPLIER_ID,"
                    + " SUPPLIER_NAME, PHONE_NUMBER, ADDRESS) VALUES (?,?,?,?);");

            String[] supplierIds = {"S001","S002","S003"};
            String[] supplierNames = {"woolies","koles","westfarms"};
            int[] supplierNumbers = {87659245,98461137,97094820};
            String[] supplierAddresses = {"67 Alice Street Bella Vista "
                    + "NSW 2130", "54 Boulevard Rd Burwood NSW 2000", "43 Clark St "
                    + "Kensington NSW 2790"}; 

            for (int i=0; i < supplierIds.length; i++) {
                insertData.setString(1, supplierIds[i]);
                insertData.setString(2, supplierNames[i]);
                insertData.setInt(3, supplierNumbers[i]);
                insertData.setString(4, supplierAddresses[i]);
                insertData.execute();
            }
            insertData.close();
            closeConnection();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    // Creating the Store Table 
    public static void createStoreTable() {
        try{
            openConnection(); 
            PreparedStatement createStoreTable,insertData;
            createStoreTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS STORE"
                    + "("
                    + " STORE_ID TEXT PRIMARY KEY,"
                    + " ADDRESS TEXT NOT NULL,"
                    + " PHONE_NUMBER INTEGER NOT NULL,"
                    + " STORE_MANAGER_ID INTEGER NOT NULL,"
                    + " FOREIGN KEY(STORE_MANAGER_ID) REFERENCES USER(USER_ID)"
                    + ");");
            createStoreTable.execute();
            createStoreTable.close();
            System.out.println("TABLE CREATED: Store");

            //Batch inserting dummy data for the stores
            insertData = conn.prepareStatement("INSERT OR IGNORE INTO STORE(STORE_ID,"
                    + " ADDRESS, PHONE_NUMBER, STORE_MANAGER_ID) VALUES (?,?,?,?);");

            String[] storeIds = {"ST01","ST02"};
            String[] storeAddresses = {"64 Carr Street Frenchs Forest NSW 2100", 
                "50 Baker Rd Paddington NSW 2200"};
            int[] storeNumbers = {88659245,98671137};
            int[] storeManagers = {1,2};

            for (int i=0; i < storeIds.length; i++) {
                insertData.setString(1, storeIds[i]);
                insertData.setString(2, storeAddresses[i]);
                insertData.setInt(3, storeNumbers[i]);
                insertData.setInt(4, storeManagers[i]);
                insertData.execute();
            }
            insertData.close();
            closeConnection();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
        
    // Creating the Supplier Employee Users Table 
    public static void createSPUserTable() {
        try{
            openConnection(); 
            PreparedStatement createSPUserTable,insertData;
            createSPUserTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                    + " SUPPLIER_EMP("
                    + " USER_ID INTEGER NOT NULL,"
                    + " SUPPLIER_ID TEXT NOT NULL,"
                    + " FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID),"
                    + " FOREIGN KEY(SUPPLIER_ID) REFERENCES SUPPLIER(SUPPLIER_ID),"
                    + " PRIMARY KEY(USER_ID, SUPPLIER_ID)"
                    + ");");
            createSPUserTable.execute();
            createSPUserTable.close();
            System.out.println("TABLE CREATED: Supplier Employee");

            // Batch inserting dummy data for the Supplier Employee Users
            insertData = conn.prepareStatement("INSERT OR IGNORE INTO SUPPLIER_EMP"
                    + "(USER_ID, SUPPLIER_ID) VALUES (?,?);");

            int[] spEmpIds = {3,4,5,6};
            String[] spEmpSupplierIDs = {"S001","S002","S003","S001"};

            for (int i=0; i < spEmpIds.length; i++) {
                insertData.setInt(1, spEmpIds[i]);
                insertData.setString(2, spEmpSupplierIDs[i]);
                insertData.execute();
            }
            insertData.close();
            closeConnection();       
            
        } catch(SQLException e){
            e.printStackTrace();
        }
    }    
    
    // Creating the Store Employee Users Table 
    public static void createSTUserTable() {
    try{
        openConnection();
        PreparedStatement createSTUserTable, insertData;      
        createSTUserTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                + " STORE_EMP"
                + "("
                + " USER_ID INTEGER NOT NULL,"
                + " STORE_ID TEXT NOT NULL,"
                + " FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID),"
                + " FOREIGN KEY(STORE_ID) REFERENCES STORE(STORE_ID),"
                + " PRIMARY KEY(USER_ID, STORE_ID)"
                + ");");
        createSTUserTable.execute();
        createSTUserTable.close();
        System.out.println("TABLE CREATED: Store Employee");
        
        // Batch inserting dummy data for the Supplier Employee Users
        insertData = conn.prepareStatement("INSERT OR IGNORE INTO STORE_EMP"
                + "(USER_ID, STORE_ID) VALUES (?,?);");
        int[] stEmpIds = {1,2};
        String[] stEmpStoreIDs = {"ST01","ST02"};
        
        for (int i=0; i < stEmpIds.length; i++) {
            insertData.setInt(1, stEmpIds[i]);
            insertData.setString(2, stEmpStoreIDs[i]);
            insertData.execute();
        }
        insertData.close();
        closeConnection();    
        } catch(SQLException e){
            e.printStackTrace();
        }   
    }

    // Creating the Store Employee Users Table 
    public static void createProductTable() {
        try{
            openConnection(); 
            PreparedStatement createProductTable, insertData;
            createProductTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                    + " PRODUCT"
                    + "("
                    + " PRODUCT_ID TEXT PRIMARY KEY,"
                    + " PRODUCT_NAME TEXT NOT NULL,"
                    + " PRICE INTEGER NOT NULL,"
                    + " PRODUCT_TYPE TEXT NOT NULL"
                    + ");");
            createProductTable.execute();
            createProductTable.close();
            System.out.println("TABLE CREATED: Products");

            // Batch inserting dummy data for the Supplier Employee Users
            insertData = conn.prepareStatement("INSERT OR IGNORE INTO PRODUCT"
                    + "(PRODUCT_ID, PRODUCT_NAME, PRICE, PRODUCT_TYPE) VALUES "
                    + "(?,?,?,?);");

            String[] productIds = {"P001","P002","P003","P004","P005","P006","P007","P008"};
            String[] productNames = {"banana","toilet paper","bread","milk","pasta","panadol","detergent", "dog treats"};                 // Must always be in lower case
            int[] productPrices = {200, 1500, 300, 300, 200, 500, 500, 800};
            String[] productTypes = {"fruit","essentials","bakery","dairy","pantry","pharmacy product","household cleaning", "pet supply"};

            for (int i=0; i < productIds.length; i++) {
                insertData.setString(1, productIds[i]);
                insertData.setString(2, productNames[i]);
                insertData.setInt(3, productPrices[i]);
                insertData.setString(4, productTypes[i]);
                insertData.execute();
            }
            insertData.close();
            closeConnection();
        } catch(SQLException e){
            e.printStackTrace();
        }   
    }
 
    // Create Order Table
    public static void createOrdersTable() {
        try{
            openConnection();
            PreparedStatement createOrdersTable;
            createOrdersTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                    + " ORDERS"
                    + "("
                    + " ORDER_ID INTEGER PRIMARY KEY,"
                    + " SUPPLIER_ID TEXT NOT NULL,"
                    + " STORE_ID TEXT NOT NULL,"
                    + " STATUS TEXT NOT NULL DEFAULT 'Order Placed',"
                    + " ORDER_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + " FOREIGN KEY(SUPPLIER_ID) REFERENCES SUPPLIER(SUPPLIER_ID),"
                    + " FOREIGN KEY(STORE_ID) REFERENCES STORE(STORE_ID)"
                    + ");");
            createOrdersTable.execute();
            System.out.println("TABLE CREATED: Orders");
            createOrdersTable.close();
            closeConnection();  
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // Create Order Item Table
    public static void createOrderItemTable()  {
        try{
            openConnection();
            PreparedStatement createOrderItemTable;
            createOrderItemTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                    + " ORDER_ITEM"
                    + "("
                    + " ORDER_ID INTEGER NOT NULL,"
                    + " PRODUCT_ID TEXT NOT NULL,"
                    + " QUANTITY INT NOT NULL,"
                    + " ORDER_ITEM_PRICE INTEGER NOT NULL,"
                    + " FOREIGN KEY(ORDER_ID) REFERENCES ORDERS(ORDER_ID),"
                    + " FOREIGN KEY(PRODUCT_ID) REFERENCES PRODUCT(PRODUCT_ID),"
                    + " PRIMARY KEY(ORDER_ID, PRODUCT_ID)"
                    + ");");
            createOrderItemTable.execute();
            System.out.println("TABLE CREATED: Order Items");
            createOrderItemTable.close();
            closeConnection();   
        } catch(SQLException e){
            e.printStackTrace();
        } 
    }
    
    // Create OrderStatusUpdate Table
    public static void createOrderStatusUpdateTable()  {
        try{
            openConnection();
            PreparedStatement createOrderStatusUpdateTable;
            createOrderStatusUpdateTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS"
                    + " ORDER_STATUS_UPDATE"
                    + "("
                    + " ORDER_ID INTEGER NOT NULL,"
                    + " STATUS_UPDATE TEXT NOT NULL,"
                    + " UPDATE_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + " USER_ID INTEGER NOT NULL,"
                    + " FULL_NAME TEXT NOT NULL,"
                    + " FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID),"
                    + " FOREIGN KEY(ORDER_ID) REFERENCES ORDERS(ORDER_ID),"
                    + " FOREIGN KEY(FULL_NAME) REFERENCES USER(FULL_NAME),"
                    + " PRIMARY KEY(ORDER_ID, UPDATE_TIMESTAMP)"
                    + ");");
            createOrderStatusUpdateTable.execute();
            System.out.println("TABLE CREATED: Order status update");
            createOrderStatusUpdateTable.close();
            closeConnection();      
        } catch(SQLException e){
            e.printStackTrace();
        }     
    }

    //[SUPPLIER CONTROLLER] Retrieve suppliers for supplier table 
    public ObservableList<Supplier> getSuppliers() {
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
        try{
            openConnection();
            String selectQuery = "SELECT SUPPLIER_ID, SUPPLIER_NAME, "
                    + "PHONE_NUMBER, ADDRESS FROM SUPPLIER;";
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                supplierList.add(new Supplier(rs.getString(1),rs.getString(2),
                        rs.getInt(3),rs.getString(4)));
            }
            rs.close();
            ps.close();
            closeConnection();
            return supplierList;
        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            return supplierList;
        } 
    }
    
    //[ORDER CONTROLLER] Verifies whether the user has inputted an existing supplier                    //add try catch
    public boolean checkSuppliers(String supplier){
        boolean exists = false;
        try{
            openConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT SUPPLIER_NAME FROM SUPPLIER");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals(supplier)){
                    exists = true;
                }
            }
            rs.close();
            ps.close();

            closeConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
           return exists;
        }
    }
    
    //[SUPPLIER CONTROLLER] Insert new supplier for supplier table
    public boolean insertSupplierStatement(String[] insertValues) {
        boolean success = false;
        try{
            openConnection();
            conn.setAutoCommit(false);
            String insertString = "INSERT OR IGNORE INTO SUPPLIER"
                    + "(SUPPLIER_ID, SUPPLIER_NAME, PHONE_NUMBER, ADDRESS)"
                    + "VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertString);

            ps.setString(1, insertValues[0]);
            ps.setString(2, insertValues[1]);
            ps.setInt(3, Integer.parseInt(insertValues[2]));
            ps.setString(4, insertValues[3]);

            ps.execute();
            conn.commit();
            ps.close();
            closeConnection(); 
            success = true;
        }catch(SQLException e){
            e.printStackTrace();
            success = false;
        }catch(NumberFormatException nfe){  //ensure phone number is an int
            nfe.printStackTrace();
            success = false;
        } finally{
            return success;
        }
    }

    //[SUPPLIER CONTROLLER] Deletes a supplier from the supplier table
    public void deleteSupplierStatement(String[] deleteValues) {
        try{
            openConnection();
            String deleteQuery = ("DELETE FROM SUPPLIER WHERE SUPPLIER_ID = ? AND "
                    + "SUPPLIER_NAME = ? AND PHONE_NUMBER = ?"); 
            PreparedStatement ps = conn.prepareStatement(deleteQuery);
            ps.setString(1, deleteValues[0]);
            ps.setString(2, deleteValues[1]);
            ps.setInt(3, Integer.parseInt(deleteValues[2]));

            ps.execute();
            ps.close();
            closeConnection(); 
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    //[MODIFY ORDER CONTROLLER] Delete an order from the order table
    public void deleteOrder(String orderID) throws SQLException {
        try{
            openConnection();
            PreparedStatement ps;
            // Order cannot be deleted if it has been delivered
            
            String checkStatus = ("SELECT STATUS FROM ORDERS WHERE ORDER_ID = ?;");
            ps = conn.prepareStatement(checkStatus);
            ps.setInt(1, Integer.parseInt(orderID));
            ResultSet rs = ps.executeQuery();
            
           while (rs.next()) {
                if (rs.getString(1).equals("Delivered")) {
                    ModifyOrdersController.deleteOrderAlertBox("This order cannot be deleted because it has been delivered");
            } else {
                    String deleteQuery = ("DELETE FROM ORDERS WHERE ORDER_ID = ? AND STATUS IS NOT 'Delivered';");
                    ps = conn.prepareStatement(deleteQuery);
                    ps.setInt(1, Integer.parseInt(orderID));
                    ps.execute();
                    System.out.println("ORDER DELETED");
            
                    ps = conn.prepareStatement("DELETE FROM ORDER_ITEM WHERE ORDER_ID = ?");
                    ps.setInt(1, Integer.parseInt(orderID));
                    ps.execute();
                    System.out.println("ORDER ITEMS DELETED");

                    ModifyOrdersController.deleteOrderAlertBox("Your order has been deleted");
                }
           }
           rs.close();
           ps.close();
           closeConnection(); 
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    //[SUPPLIER CONTROLLED] Update existing supplier information
    public boolean editSupplierStatement(String[] editValues) {
        boolean success = false;
        try{
            openConnection();
            String updateQuery = "UPDATE SUPPLIER SET SUPPLIER_ID = ?, "
                    + "SUPPLIER_NAME = ?, PHONE_NUMBER = ?, ADDRESS = ?"
                    + "WHERE SUPPLIER_ID = ?";                                      //NEED TO FIX THIS WHERE PART
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, editValues[0]);
            ps.setString(2, editValues[1]);
            ps.setInt(3, Integer.parseInt(editValues[2]));
            ps.setString(4, editValues[3]);
            ps.setString(5, editValues[0]);

            ps.execute();
            ps.close();
            closeConnection();
            success = true;
        }catch(SQLException e){
            e.printStackTrace();
            success = false;
        }catch(NumberFormatException nfe){  //ensure phone number is an integer
            nfe.printStackTrace();
            success = false;
        } finally{
            return success;
        }
    }
    
    //[ORDER CONTROLLER] Populates products table in orderView
    public ObservableList<Product> getProducts() {
        ObservableList<Product> ProductsList = FXCollections.observableArrayList();
        try{
            openConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM PRODUCT;");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                ProductsList.add(new Product(rs.getString(1), rs.getString(2), 
                                    rs.getInt(3), rs.getString(4))); 
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return ProductsList;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return ProductsList;
        }
    }

    
    //[PRIMARY CONTROLLER] Check login details
    public boolean login(String username, String password) {
        boolean success = false;
        try{
            openConnection();
            // compare username and password from formal parameters of method to usernames and passwords on database
            String query = "SELECT * FROM USER";
            PreparedStatement ps = conn.prepareStatement(query);            // add for or while loop??
            ResultSet rs = ps.executeQuery();

            // compares usernames and passwords
            while(rs.next()){
                if(username.equals(rs.getString(2)) && password.equals(rs.getString(3))){
                    success = true;
                    return success;
                }
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return success;
        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            return success;
        }
    }
   
    //[ORDER CONTROLLER] Create a new order
    public void createNewOrder(String[] info) {
        try{
            openConnection();
            String insertQuery = "INSERT OR IGNORE INTO ORDERS(SUPPLIER_ID, STORE_ID)"
                    + "VALUES (?,?);"; 
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, info[0] );     
            ps.setString(2, info[1]);
            ps.execute();
            ps.close();
            closeConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
    } 
    
    /*
     * [ORDER CONTROLLER] + [ORDER STATUS CONTROLLER] 
     *  Retrieves the userID of the current user 
    */
    public String getCurrentUserID(){
        String userID = null;
        try{
            String username = PrimaryController.usernameUsed;
            openConnection();

            String query = "SELECT USER_ID "
                    + " FROM USER "
                    + " WHERE USERNAME = ?;";                                       
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                 userID = rs.getString(1);
            }
            System.out.println("CURRENT USER ID: " + userID);
            rs.close();
            ps.close();
            closeConnection(); 
            return userID;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return userID;
        }
    }
    
    //[ORDER STATUS CONTROLLER] Retrieves the fullname of the current user
    public String getCurrentFullName() {
        String currentFullName = null;
        String currentUserId = getCurrentUserID();
        try{
            openConnection();
            String getFullNameQuery = "SELECT FULL_NAME "
                    + " FROM USER "
                    + " WHERE USER_ID = ?;";
            PreparedStatement ps = conn.prepareStatement(getFullNameQuery);
            ps.setString(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                 currentFullName = rs.getString(1);
             }
            System.out.println("Current Full Name: " + currentFullName);
            rs.close();
            ps.close();
            closeConnection(); 
            return currentFullName;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return currentFullName;
        }
    }
    
    //[ORDER CONTROLLER] Retrieves the storeID of the current (store) user
    public String getCurrentStoreID() {
        String currentStoreID = getCurrentUserID();
        try{
            openConnection();
            String getStoreIDQuery = "SELECT STORE_ID "
                    + " FROM STORE_EMP "
                    + " WHERE USER_ID = ?;";
            PreparedStatement ps = conn.prepareStatement(getStoreIDQuery);
            ps.setString(1,currentStoreID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                 currentStoreID = rs.getString(1);
             }
            System.out.println("CURRENT STORE ID: " + currentStoreID);
            rs.close();
            ps.close();
            closeConnection(); 
            return currentStoreID;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return currentStoreID;
        }
    }

    //[ORDER CONTROLLER] Retrieve supplier ID from supplier Name
    public String getSupplierIDFromName(String supplierName)  {
        String supplierID = null;
        try{
            openConnection();
            String query = "SELECT SUPPLIER_ID FROM SUPPLIER WHERE SUPPLIER_NAME = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, supplierName);

            ResultSet rs = ps.executeQuery();
             while(rs.next()) {
                supplierID = rs.getString(1);             
             }
            rs.close();
            ps.close();
            closeConnection(); 
            return supplierID;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return supplierID;
        }
    }

    //[ORDER CONTROLLER] Gets existing order item objects
    public ObservableList<OrderItem> getOrderItems()  {
        ObservableList<OrderItem> orderItemList = FXCollections.observableArrayList();
        try{
            openConnection();
            System.out.println(getCurrentOrderID());
            String selectQuery = "SELECT ORDER_ID, PRODUCT_ID, "
                    + " QUANTITY, ORDER_ITEM_PRICE FROM ORDER_ITEM"
                    + " WHERE ORDER_ID IN "
                    + " (SELECT ORDER_ID FROM ORDERS WHERE STORE_ID = ?);";

            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setString(1, PrimaryController.currentStore_ID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                orderItemList.add(new OrderItem(rs.getInt(1),
                        rs.getString(2),rs.getInt(3), rs.getInt(4)));
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return orderItemList;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return orderItemList;
        }
    }
    
    //[ORDER CONTROLLER] Gets existing order item objects
    public ObservableList<OrderItem> getOrderItems(int orderID)  {
        ObservableList<OrderItem> orderItemList = FXCollections.observableArrayList();
        try{
            openConnection();
            String selectQuery = "SELECT ORDER_ID, PRODUCT_ID, "
                    + " QUANTITY, ORDER_ITEM_PRICE "
                    + " FROM ORDER_ITEM "
                    + " WHERE ORDER_ID = ?";

            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                orderItemList.add(new OrderItem(rs.getInt(1),
                        rs.getString(2),rs.getInt(3), rs.getInt(4)));
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return orderItemList;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return orderItemList;
        }
    }
    

    //[MODIFY ORDER CONTROLLER] Gets existing order objects for the store
     public ObservableList<Order> getOrders()  {
       ObservableList<Order> orderList = FXCollections.observableArrayList();
       try{
            openConnection();
            String selectQuery = "SELECT ORDER_ID, SUPPLIER_ID, STORE_ID, STATUS, "
                    + "ORDER_TIMESTAMP FROM ORDERS"
                    + " WHERE ORDER_ID IN "
                    + " (SELECT ORDER_ID FROM ORDERS WHERE STORE_ID = ?);";
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setString(1, PrimaryController.currentStore_ID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                orderList.add(new Order(rs.getInt(1),rs.getString(2),
                        rs.getString(3), rs.getString(4), LocalDateTime.parse(rs.getString(5), formatter)));
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return orderList;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
           return orderList;
       }
    }
    
    
    //[ORDER CONTROLLER] Returns the number of existing orders - which is the current orderID
    public int getCurrentOrderID()  {
        int orderIDReturn = 0;
        try{
            openConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM ORDERS");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                 orderIDReturn = rs.getInt(1);
            }
            System.out.println("Current Order ID: " + orderIDReturn);
            rs.close();
            ps.close();
            closeConnection(); 
            return orderIDReturn;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return orderIDReturn;
        }
    }
    
    /*
     * [ORDER CONTROLLER] + [MODIFY ORDER CONTROLLER] 
     * Ensure product is not duplicated within a single order
     */
    public boolean checkOIExists(String[] info){
        boolean success = false;
        try{
            openConnection();
            //Retrieve the order items within the current order
            String query = "SELECT ORDER_ID, PRODUCT_ID FROM ORDER_ITEM"
                    + " WHERE ORDER_ID = ? AND PRODUCT_ID = ?";
            PreparedStatement ps1 = conn.prepareStatement(query);
            ps1.setInt(1, Integer.parseInt(info[0]));
            ps1.setString(2, info[1]);
            ResultSet rs1 = ps1.executeQuery();
            
            //Check if the selected product already exists in the order
            while(rs1.next()){
                success = Integer.parseInt(info[0]) == (rs1.getInt(1)) && info[1].equals(rs1.getString(2));
            } //if the product already exists in the order, return true
            //if the product is not in the order, return false
            rs1.close();
            ps1.close();
        }catch(SQLException e){
            e.printStackTrace();
            success = false;
        } finally{
            return success;
        }   
    }
    
    //[ORDER CONTROLLER] Creates a new order item
    public boolean createNewOrderItem(String[] info)  {
        boolean success = false;
        try{
            openConnection();
            //normal create order item
            String query = "INSERT OR IGNORE INTO ORDER_ITEM(ORDER_ID, PRODUCT_ID,"
                    + "QUANTITY, ORDER_ITEM_PRICE)"
                    + "VALUES (?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(query);
           
            ps.setInt(1, Integer.parseInt(info[0]));     
            ps.setString(2, info[1]);       //
            ps.setInt(3, Integer.parseInt(info[2])); 
            ps.setInt(4, Integer.parseInt(info[3])); 
            ps.execute();

            ps.close();
            closeConnection();  
            success = true;
        }catch(SQLException e){
            e.printStackTrace();
            success = false;
        } finally{
            return success;
        }
    }
    
    //[ORDER CONTROLLER] Deletes an order item 
    public void deleteOrderItem(String[] selectedItem)  {
        try{
            openConnection();
            String deleteQuery = ("DELETE FROM ORDER_ITEM WHERE ORDER_ID = ? AND "
                    + "PRODUCT_ID = ?"); 
            PreparedStatement ps = conn.prepareStatement(deleteQuery);

            ps.setInt(1, Integer.parseInt(selectedItem[0]));   
            ps.setString(2, selectedItem[1]);
            ps.execute();

            System.out.println("PRODUCT HAS BEEN DELETED");
            ps.close();
            closeConnection(); 
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
 
    //[ORDER CONTROLLER] Updates the quantity of an order item
    public void editOrderItem(String[] oldItem, String newQty, int productPrice){
        try{
            openConnection();
            String updateQuery = "UPDATE ORDER_ITEM SET QUANTITY = ?,"
                    + "ORDER_ITEM_PRICE = ?"
                    + "WHERE ORDER_ID = ? AND PRODUCT_ID = ?";                                      //NEED TO FIX THIS WHERE PART
            PreparedStatement ps = conn.prepareStatement(updateQuery);

            ps.setInt(1, Integer.parseInt(newQty)); 
            ps.setInt(2, Integer.parseInt(newQty)*productPrice);
            ps.setInt(3, Integer.parseInt(oldItem[0])); 
            ps.setString(4, oldItem[1]);        
            System.out.println("ORDER ITEM HAS BEEN UPDATED");
            ps.execute();
            ps.close();
            closeConnection(); 
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    //[ORDER CONTROLLER] Used to caluclate the new 'total order item' price (quantity x product price)
    public int getProductPrice(String productID){
        int price = 0;
        try{
            openConnection();
            String getPPrice = "SELECT PRICE FROM PRODUCT WHERE PRODUCT_ID =?";              //double check this
            PreparedStatement ps = conn.prepareStatement(getPPrice);
            ps.setString(1, productID);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                price = rs.getInt(1);
            }
            rs.close();       
            ps.close();
            closeConnection(); 
           return price;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return price;
        }
    }
    
    // optional method
    public boolean getUserType(String type)  {
        boolean success = false;
        try{
            openConnection();
            String currentUsername = PrimaryController.usernameUsed;

            String query = "SELECT USER_TYPE FROM USER WHERE USERNAME = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUsername);
            ResultSet rs = ps.executeQuery();

             while(rs.next()){
                if(type.equals(rs.getString(1))){
                    success = true;
                    return success;
                }
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return success;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return success;
        }
    }
    
    // check whether or not the user is from a supplier + which supplier they work at; returns supplierID
    public String checkUserSupplier() {
        String supplier = null;
        try{
            openConnection();

            String query = "SELECT USER_TYPE FROM USER WHERE USERNAME = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, PrimaryController.usernameUsed);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                if(rs.getString(1).equals("sp")){
                    String userSupplier = "SELECT SUPPLIER_ID FROM SUPPLIER_EMP "
                            + "JOIN USER USING (USER_ID) WHERE USERNAME =?;";
                    ps = conn.prepareStatement(userSupplier);
                    ps.setString(1, PrimaryController.usernameUsed);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        supplier = rs.getString(1);
                    }
                }
            }
            rs.close();
            ps.close();
            return supplier;
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            return supplier;
        }
        
    }
    
    // [DASHBOARD CONTROLLER] Get tableview of orders with status that is searched for
    // Result depends on the store
    public ObservableList<Order> ordersWithStatus(String status)   {
        ObservableList<Order> ordersWithStatus = FXCollections.observableArrayList();
        try{
            openConnection();
            String query = "SELECT * FROM ORDERS WHERE ORDER_ID IN "
                    + "(SELECT ORDER_ID FROM ORDERS WHERE STORE_ID = ?)"
                    + "AND STATUS = '" + status + "'";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, PrimaryController.currentStore_ID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ordersWithStatus.add(new Order(rs.getInt(1),rs.getString(2),
                        rs.getString(3), rs.getString(4), LocalDateTime.parse(rs.getString(5), formatter)));
            }
            rs.close();
            ps.close();
            closeConnection(); 
            return ordersWithStatus;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return ordersWithStatus;
        }
    }
    
    // [DASBHOARD CONTROLLER] Get tableview of orders with the product that is searched for
    public ObservableList<Order> ordersWithProduct(String product)   {
        ObservableList<Order> ordersWithProduct = FXCollections.observableArrayList();
        try{
            openConnection();
            String query = "SELECT DISTINCT ORDER_ID, SUPPLIER_ID, STORE_ID,"
                    + " STATUS, ORDER_TIMESTAMP FROM ORDERS "
                    + " JOIN ORDER_ITEM USING (ORDER_ID) "
                    + " JOIN PRODUCT p USING (PRODUCT_ID) "
                    + " WHERE ORDER_ID IN "
                    + " (SELECT ORDER_ID FROM ORDERS WHERE STORE_ID = ?)"
                    + " AND p.PRODUCT_NAME = ?;";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, PrimaryController.currentStore_ID);
            ps.setString(2, product);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ordersWithProduct.add(new Order(rs.getInt(1),rs.getString(2),
                        rs.getString(3), rs.getString(4), LocalDateTime.parse(rs.getString(5), formatter)));
            }
            rs.close();
            ps.close();
            closeConnection(); 
        return ordersWithProduct;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return ordersWithProduct;
        }
    }
  
    // [DASHBOARD CONTROLLER] Get number of orders that are placed for a specific status
    public int getOrdersWithStatus(String status)   {
        int ordersPlaced = 0;
        try{
            openConnection();
            String query = "SELECT COUNT(STATUS) FROM ORDERS WHERE STATUS = ?"
                    + " AND ORDER_ID IN (SELECT ORDER_ID FROM ORDERS WHERE STORE_ID = ?);";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, PrimaryController.currentStore_ID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ordersPlaced = rs.getInt(1);
            }
            rs.close();
            ps.close();
            return ordersPlaced;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return ordersPlaced;
        }
    }

    // [ORDER STATUS CONTROLLER] Get orders of the employee's supplier
    public ObservableList<Order> getOrdersOfEmpSupplier(){
	ObservableList<Order> specificOrderList = FXCollections.observableArrayList();
        try{
            openConnection();
            String supplier = checkUserSupplier();

            System.out.println("The supplier is: " + supplier);

            if (supplier != null) { // If Statement may be redundant if a non-supplier user is not even given access to the screen

                String query = "SELECT * FROM ORDERS JOIN SUPPLIER USING "
                    + "(SUPPLIER_ID) WHERE SUPPLIER_ID = ?;"  ;
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, supplier);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                specificOrderList.add(new Order(rs.getInt(1), rs.getString(2), 
                                    rs.getString(3), rs.getString(4), 
                        LocalDateTime.parse(rs.getString(5), formatter)));
                
                }
                rs.close();
                ps.close();
            }     
            closeConnection();
            return specificOrderList;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return specificOrderList;
        }
    }
    
    
    public String returnUserType(String type){
        if(type.equals("Supplier")){
            return "sp";
        }
        return "st";
    }
    
    //[ORDER STATUS CONTROLLER] Updates the status of an order
    public void editOrder(String oldOrder, String newStatus){
        try{
            openConnection();
            String updateQuery = "UPDATE ORDERS SET STATUS = ?"
                    + " WHERE ORDER_ID = ?";
            PreparedStatement ps = conn.prepareStatement(updateQuery);

            ps.setString(1, newStatus); //updated status  
            ps.setInt(2, Integer.parseInt(oldOrder)); //order id
            ps.executeUpdate();
            
            ps.close();
            closeConnection(); 
        }catch(SQLException e){
            e.printStackTrace();
        }   
    }
    
    // [ORDER STATUS CONTROLLER] Records the status update in the OrderStatusUpdateTable
    public void updateOrderStatusUpdateTable(String[] newStatusUpdate) throws SQLException {
        try{
            openConnection();
            conn.setAutoCommit(false);
            String insertQuery = "INSERT OR IGNORE INTO ORDER_STATUS_UPDATE(ORDER_ID, STATUS_UPDATE, UPDATE_TIMESTAMP, USER_ID, FULL_NAME)"
                    + " VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
 
            ps.setInt(1, Integer.parseInt(newStatusUpdate[0]));                           // order id
            ps.setString(2, newStatusUpdate[1]);                                          // status_update
            ps.setString(3, newStatusUpdate[2]);                                          // timestamp
            ps.setInt(4, Integer.parseInt(newStatusUpdate[3])); 
            ps.setString(5, newStatusUpdate[4]);                                            // fullname
            
            ps.execute();
            System.out.println("NEW ORDER STATUS HAS BEEN CREATED"
                    + " ORDER_ID = " +  Integer.parseInt(newStatusUpdate[0])
                    + " STATUS_UPDATE = " + newStatusUpdate[1]
                    + " UPDATE_TIMESTAMP = " +  newStatusUpdate[2]
                    + " USER_ID = " + Integer.parseInt(newStatusUpdate[3])
                    + " FULL_B = " + newStatusUpdate[4]);
       
            conn.commit();
            ps.close();
            closeConnection();    
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    // [ORDER STATUS CONTROLLER] Displays OrderStatusUpdate Table based on the supplier of the user
    public ObservableList<OrderStatus> getOrderStatusUpdateOfEmpSupplier() throws SQLException {
	ObservableList<OrderStatus> specificOrderStatusUpdatesList = FXCollections.observableArrayList();
        try{
            openConnection();
            String supplier = checkUserSupplier();
            String query = "SELECT os.ORDER_ID, os.STATUS_UPDATE, os.UPDATE_TIMESTAMP, os.USER_ID, os.FULL_NAME "
                + " FROM ORDER_STATUS_UPDATE 'os' "
                + " JOIN ORDERS 'o'"
                + " USING (ORDER_ID)"
                + " JOIN SUPPLIER 's'"
                + " USING (SUPPLIER_ID)"
                + " WHERE SUPPLIER_ID = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, supplier);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                specificOrderStatusUpdatesList.add(new OrderStatus(rs.getInt(1), rs.getString(2), 
                                   LocalDateTime.parse(rs.getString(3), formatter), rs.getInt(4), rs.getString(5)));
            }
            rs.close();
            ps.close();
            closeConnection();
            return specificOrderStatusUpdatesList;
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return specificOrderStatusUpdatesList;
        }
    }
    
//[Modify Orders] Returns the supplier of the selected order
    public String getSupplierNameFromID(String supplierID){
        String supplier = null;
        try{
            openConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT SUPPLIER_NAME "
                    + "FROM SUPPLIER WHERE SUPPLIER_ID = ?");
            ps.setString(1, supplierID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                supplier = rs.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            return supplier;
        }
    }
}