package com.mycompany.infs2605;

import java.time.format.DateTimeFormatter;

public class OrderItem {

    private int order_ID;
    private String product_ID;
    private int quantity;
    private int orderItemprice;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderItem(int order_ID, String product_ID, int quantity, int orderItemprice) {

        this.order_ID = order_ID;
        this.product_ID = product_ID;
        this.quantity = quantity;
        this.orderItemprice = orderItemprice;
    }



    public int getOrder_ID() {
        return order_ID;
    }

    public void setOrder_ID(int order_ID) {
        this.order_ID = order_ID;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderItemprice() {
        return orderItemprice;
    }

    public void setOrderItemprice(int orderItemprice) {
        this.orderItemprice = orderItemprice;
    }
    
    
}
