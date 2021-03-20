package com.mycompany.infs2605;

public class SupplierEmployee extends User{
    private String supplier_ID;

    public SupplierEmployee(String supplier_ID, int user_ID, String username, String password, String full_name, String user_type) {
        super(user_ID, username, password, full_name, user_type);
        this.supplier_ID = supplier_ID;
    }

    public String getSupplier_ID() {
        return supplier_ID;
    }

    public void setSupplier_ID(String supplier_ID) {
        this.supplier_ID = supplier_ID;
    }
    
    
}
