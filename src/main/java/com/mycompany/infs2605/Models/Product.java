package com.mycompany.infs2605;


public class Product {
    private String product_ID;
    private String product_name;
    private int price;
    private String product_type;

    public Product(String product_ID, String product_name, int price, String product_type) {
        this.product_ID = product_ID;
        this.product_name = product_name;
        this.price = price;
        this.product_type = product_type;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
    
    
}
