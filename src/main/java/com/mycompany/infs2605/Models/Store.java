package com.mycompany.infs2605;


public class Store {
    private String store_ID;
    private String address;
    private int phone_number;
    private int store_manager_ID;

    public Store(String store_ID, String address, int phone_number, int store_manager_ID) {
        this.store_ID = store_ID;
        this.address = address;
        this.phone_number = phone_number;
        this.store_manager_ID = store_manager_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public String getAddress() {
        return address;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public int getStore_manager_ID() {
        return store_manager_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public void setStore_manager_ID(int store_manager_ID) {
        this.store_manager_ID = store_manager_ID;
    }
    
}
