package com.shop.ordstore.UserClasses;

/**
 * Created by AangJnr on 9/22/16.
 */
public class UserOrderStatus {
    private String order_code, user_timestamp, confirmed;




    public UserOrderStatus(){}




    public UserOrderStatus(String order_code, String user_timestamp, String confirmed){
        this.order_code = order_code;
        this.user_timestamp = user_timestamp;
        this.confirmed = confirmed;

    }




    public String getorderCode() {
        return order_code;
    }

    public void setorderCode(String order_code) {
        this.order_code = order_code;
    }

    public String getuserTimestamp() {
        return user_timestamp;
    }

    public void setuserTimestamp(String user_timestamp) {
        this.user_timestamp = user_timestamp;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void getConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }
}
