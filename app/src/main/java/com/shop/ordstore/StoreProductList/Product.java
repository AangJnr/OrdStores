package com.shop.ordstore.StoreProductList;

/**
 * Created by AangJnr on 8/10/16.
 */
public class Product {


    String productName;
    String itemCode;
    String price;
    String details;
    String photoId;


    public Product() {
        super();

    }


    //setter and getter methods which sets and gets PhotoId's of stores from server

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}