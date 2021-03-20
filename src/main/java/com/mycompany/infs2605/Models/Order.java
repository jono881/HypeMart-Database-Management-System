package com.mycompany.infs2605;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int order_ID;
    private String supplier_ID;
    private String store_ID;
    private String status;
    private LocalDateTime order_timestamp;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Order(int order_ID, String supplier_ID, String store_ID, String status, LocalDateTime order_timestamp) {
        this.order_ID = order_ID;
        this.supplier_ID = supplier_ID;
        this.store_ID = store_ID;
        this.status = status;
        this.order_timestamp = order_timestamp;
    }

    public int getOrder_ID() {
        return order_ID;
    }

    public void setOrder_ID(int order_ID) {
        this.order_ID = order_ID;
    }

    public String getSupplier_ID() {
        return supplier_ID;
    }

    public void setSupplier_ID(String supplier_ID) {
        this.supplier_ID = supplier_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrder_timestamp() {
        return order_timestamp;
    }

    public void setOrder_timestamp(LocalDateTime order_timestamp) {
        this.order_timestamp = order_timestamp;
    }
}
