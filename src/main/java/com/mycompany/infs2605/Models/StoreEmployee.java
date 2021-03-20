package com.mycompany.infs2605;


public class StoreEmployee extends User{
    private String store_ID;

    public StoreEmployee(String store_ID, int user_ID, String username, String password, String full_name, String user_type) {
        super(user_ID, username, password, full_name, user_type);
        this.store_ID = store_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }
    
}
