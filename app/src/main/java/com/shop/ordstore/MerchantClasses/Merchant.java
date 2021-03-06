package com.shop.ordstore.merchantClasses;

/**
 * Created by AangJnr on 9/11/16.
 */
public class Merchant {

    //name and address string
    private String name;
    private String email;
    private String phone;

    public Merchant() {
      /*Blank default constructor essential for Firebase*/
    }

    public Merchant(String username, String email, String phone) {
        this.name = username;
        this.email = email;
        this.phone = phone;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String address) {
        this.email = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
