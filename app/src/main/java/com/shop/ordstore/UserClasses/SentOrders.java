package com.shop.ordstore.UserClasses;

import com.google.firebase.database.Exclude;

/**
 * Created by AangJnr on 9/21/16.
 */
public class SentOrders {

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


    public SentOrders() {
      /*Blank default constructor essential for Firebase*/
    }

    public SentOrders (String user_name, String user_phone, String user_uid, String user_timestamp, String user_extra_info,
                       String product_name, String product_code, String quantity,  String product_price,
                       String product_photoId) {

        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_uid = user_uid;
        this.user_timestamp = user_timestamp;
        this.user_extra_info = user_extra_info;

        this.product_name = product_name;
        this.product_code = product_code;
        this.quantity = quantity;
        this.product_price = product_price;
        this.product_photoId = product_photoId;
    }




}
