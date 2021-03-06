package com.shop.ordstore.userClasses;

/**
 * Created by AangJnr on 9/11/16.
 */
public class User {
    //name and address string
    private String name;
    private String email;
    private String phone;
    private String photo;


    public User() {
      /*Blank default constructor essential for Firebase*/
    }


    public User(String username, String email, String phone, String photo) {
        this.name = username;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
