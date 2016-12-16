package com.shop.ordstore.merchantClasses;

/**
 * Created by AangJnr on 9/21/16.
 */

import com.google.firebase.database.Exclude;



/**
 * Created by AangJnr on 9/21/16.
 */
public class PendingOrder {

    public String user_name;
    public String user_phone;
    public String user_uid;
    public String user_timestamp;
    public String user_extra_info;

    public String product_name;
    public String product_code;
    public String quantity;
    public String product_price;
    public String product_photoId;
    public String timestamp;


    public PendingOrder() {

    }

    public PendingOrder(String user_name, String user_phone, String user_uid, String user_timestamp, String user_extra_info, String product_name,
                        String product_code, String quantity, String product_price, String product_photoId, String timestamp) {


        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_uid = user_uid;
        this.user_timestamp = user_timestamp;
        this.user_extra_info = user_extra_info;
        this.product_name = product_name;
        this.product_code = product_code;
        this.quantity = quantity;
        this.product_price =product_price;
        this.product_photoId = product_photoId;
        this.timestamp = timestamp;

    }



    @Exclude
    public String getUserName(){return user_name;}
    @Exclude
    public String getUserPhone(){return user_phone;}
    @Exclude
    public String getUserUid(){return user_uid;}
    @Exclude
    public String getUserTimestamp(){return user_timestamp;}
    @Exclude
    public String getUserExtraInfo(){return user_extra_info;}
    @Exclude
    public String getProductName(){return product_name;}
    @Exclude
    public String getProductCode(){return product_code;}
    @Exclude
    public String getProductQuantity(){return quantity;}
    @Exclude
    public String getProductPrice(){return product_price;}
    @Exclude
    public String getProductPhotoId(){return product_photoId;}
    @Exclude
    public String getTimestamp(){return timestamp;}

    @Exclude
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
