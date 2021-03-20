package com.mycompany.infs2605;


public class Supplier {
    private String supplier_ID;
    private String supplier_name;
    private int phoneNumber;
    private String address;

    public Supplier(String supplier_ID, String supplier_name, int phoneNumber, String address) {
        this.supplier_ID = supplier_ID;
        this.supplier_name = supplier_name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getSupplier_ID() {
        return supplier_ID;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setSupplier_ID(String supplier_ID) {
        this.supplier_ID = supplier_ID;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
