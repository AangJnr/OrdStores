package com.shop.ordstore.userClasses;

/**
 * Created by AangJnr on 4/23/16.
 */

public class StoreTile {

    public String storeName;
    public String logo;
    public String merchant_uid;


    public StoreTile(String storeName, String logo, String merchant_uid) {
        this.storeName = storeName;
        this.merchant_uid = merchant_uid;
        this.logo = logo;
    }

    public StoreTile(){
        super();
    }


    //setter and getter methods which sets and gets StoreName from server
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    //setter and getter methods which sets and gets PhotoId's of stores from server
    public String getLogo() {
        return logo;

    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMerchantUid() {
        return merchant_uid;
    }

    public void setMerchantUid(String store_name_prefix) {
        this.merchant_uid = store_name_prefix;
    }
}