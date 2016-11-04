package com.shop.ordstore.UserClasses;


/**
 * Created by AangJnr on 4/23/16.
 */


//This is an Object Class which initializes the item/order resources

public class OrderTile {

    String productName;
    String itemCode;
    String quantity;
    String price;
    String size;
    String itemColor;
    String details;
    String photoId;
    String merchant_uid;
    String timestamp;


    public OrderTile(String productName, String itemCode, String quantity, String price,
                     String size, String itemColor, String details, String photoId, String merchant_uid, String timestamp) {
        this.productName = productName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.itemColor = itemColor;
        this.details = details;
        this.photoId = photoId;
        this.merchant_uid = merchant_uid;
        this.timestamp = timestamp;
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


    public String getItemQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getItemPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
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


    public String getMerchantUid() {
        return merchant_uid;
    }


    public void setMerchantUid(String storeName) {
        this.merchant_uid = storeName;
    }


    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
