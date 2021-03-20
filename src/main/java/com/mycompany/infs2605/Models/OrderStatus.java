package com.mycompany.infs2605;

import java.time.LocalDateTime;

public class OrderStatus {
    
    private int order_ID;
    private String statusUpdate;
    private LocalDateTime status_update_timestamp;
    private int user_ID;
    private String full_name;
    
    public OrderStatus(int order_ID, String statusUpdate, LocalDateTime status_update_timestamp, int user_ID, String full_name) {
        this.order_ID = order_ID;
        this.statusUpdate = statusUpdate;
        this.status_update_timestamp = status_update_timestamp;
        this.user_ID = user_ID;
        this.full_name = full_name;
    }
    
    public int getOrder_ID() {
        return order_ID;
    }

    public void setOrder_ID(int order_ID) {
        this.order_ID = order_ID;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public LocalDateTime getStatus_update_timestamp() {
        return status_update_timestamp;
    }

    public void setStatus_update_timestamp(LocalDateTime status_update_timestamp) {
        this.status_update_timestamp = status_update_timestamp;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }
    
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    
}