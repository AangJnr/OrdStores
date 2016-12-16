package com.shop.ordstore.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shop.ordstore.merchantClasses.PendingOrder;
import com.shop.ordstore.userClasses.OrderTile;
import com.shop.ordstore.userClasses.StoreTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 8/2/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "ordStoresDatabase.db";
    //Store DB
    public static final String STORE_TABLE_NAME = "store";
    public static final String STORE_COLUMN_ID = "_id";
    public static final String STORE_COLUMN_NAME = "storeName";
    public static final String STORE_COLUMN_LOGO = "storeLogo";
    public static final String STORE_UID = "storeUid";

    //Orders DB
    public static final String ORDERS_TABLE_NAME = "orders_list";
    public static final String ORDERS_COLUMN_ID = "orders_id";

    public static final String STARRED_ORDERS_TABLE_NAME = "starred_orders_list";


    public static final String STORE_LIST_TABLE_NAME = "store_list";
    public static final String ORDERS_COLUMN_PRODUCT_NAME = "product_name";
    public static final String ORDERS_COLUMN_CODE = "product_code";
    public static final String ORDERS_COLUMN_QUANTITY = "quantity";
    public static final String ORDERS_COLUMN_PRICE = "product_price";
    public static final String ORDERS_COLUMN_SIZE = "product_size";
    public static final String ORDERS_COLUMN_COLOR = "product_color";
    public static final String ORDERS_COLUMN_DETAILS = "product_details";
    public static final String ORDERS_COLUMN_IMAGE = "product_image";
    public static final String ORDERS_COLUMN_MERCHANT_UID = "product_merchant_uid";
    public static final String ORDERS_COLUMN_TIMESTAMP = "time_stamp";
    public static final String ORDERS_COLUMN_ISPENDING = "is_pending";
    public static final String ORDERS_COLUMN_CONFIRMED = "is_confirmed";



    public static final String PO_TABLE_NAME = "pending_orders_list";
    public static final String PO_COLUMN_ID = "pending_orders_id";

    public static final String PO_COLUMN_BUYERS_NAME = "buyers_name";
    public static final String PO_COLUMN_BUYERS_PHONE = "buyers_phone_no";
    public static final String PO_COLUMN_BUYERS_UID = "buyers_uid";
    public static final String PO_COLUMN_BUYERS_TIMESTAMP = "buyers_timestamp";
    public static final String PO_COLUMN_EXTRAINFO = "extra_info";

    public static final String PO_COLUMN_PRODUCT_NAME = "product_name";
    public static final String PO_COLUMN_CODE = "product_code";
    public static final String PO_COLUMN_QUANTITY = "quantity";
    public static final String PO_COLUMN_PRICE = "product_price";
    public static final String PO_COLUMN_IMAGE = "product_image";
    public static final String PO_COLUMN_TIMESTAMP = "timestamp";
    public static final String PO_COLUMN_STATUS = "order_status";


    public static final String PO_HISTORY_TABLE_NAME = "pending_orders_history";
    public static final String PO_HISTORY_COLUMN_ID = "pending_order_history_id";
    public static final String  PO_HISTORY_COLUMN_TIMESTAMP = "pending_order_history_timestamp";
    public static final String PO_HISTORY_COLUMN_UID = "pending_order_history_uid";



    public static final String USER_HISTORY_TABLE_NAME = "user_orders_history";
    public static final String USER_HISTORY_COLUMN_ID = "user_order_history_id";
    public static final String USER_HISTORY_COLUMN_TIMESTAMP = "user_order_history_timestamp";
    public static final String USER_HISTORY_COLUMN_ORDER_UID = "user_order_history_uid";




    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STORE_TABLE_NAME + "(" +
                STORE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                STORE_COLUMN_NAME + " TEXT, " +
                STORE_COLUMN_LOGO + " TEXT, " +
                STORE_UID + " TEXT " + ")");


        db.execSQL("CREATE TABLE " + PO_HISTORY_TABLE_NAME + "(" +
                PO_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PO_HISTORY_COLUMN_TIMESTAMP + " TEXT, " +
                PO_HISTORY_COLUMN_UID + " TEXT " + ")");

        db.execSQL("CREATE TABLE " + USER_HISTORY_TABLE_NAME + "(" +
                USER_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                USER_HISTORY_COLUMN_TIMESTAMP + " TEXT, " +
                USER_HISTORY_COLUMN_ORDER_UID + " TEXT " + ")");


        db.execSQL("CREATE TABLE " + ORDERS_TABLE_NAME + "(" +
                ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                ORDERS_COLUMN_PRODUCT_NAME + " TEXT, " +
                ORDERS_COLUMN_CODE + " TEXT, " +
                ORDERS_COLUMN_QUANTITY + " TEXT, " +
                ORDERS_COLUMN_PRICE + " TEXT, " +
                ORDERS_COLUMN_SIZE + " TEXT, " +
                ORDERS_COLUMN_COLOR + " TEXT, " +
                ORDERS_COLUMN_DETAILS + " TEXT, " +
                ORDERS_COLUMN_IMAGE + " TEXT, " +
                ORDERS_COLUMN_MERCHANT_UID + " TEXT, " +
                ORDERS_COLUMN_TIMESTAMP + " TEXT, " +
                ORDERS_COLUMN_CONFIRMED + " TEXT" + ")");


        db.execSQL("CREATE TABLE " + STARRED_ORDERS_TABLE_NAME + "(" +
                ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                ORDERS_COLUMN_PRODUCT_NAME + " TEXT, " +
                ORDERS_COLUMN_CODE + " TEXT, " +
                ORDERS_COLUMN_QUANTITY + " TEXT, " +
                ORDERS_COLUMN_PRICE + " TEXT, " +
                ORDERS_COLUMN_SIZE + " TEXT, " +
                ORDERS_COLUMN_COLOR + " TEXT, " +
                ORDERS_COLUMN_DETAILS + " TEXT, " +
                ORDERS_COLUMN_IMAGE + " TEXT, " +
                ORDERS_COLUMN_MERCHANT_UID + " TEXT, " +
                ORDERS_COLUMN_TIMESTAMP + " TEXT, " +
                ORDERS_COLUMN_CONFIRMED + " TEXT" + ")");




        db.execSQL("CREATE TABLE " + PO_TABLE_NAME + "(" +
                PO_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PO_COLUMN_BUYERS_NAME + " TEXT, " +
                PO_COLUMN_BUYERS_PHONE + " TEXT, " +
                PO_COLUMN_BUYERS_UID + " TEXT, " +
                PO_COLUMN_BUYERS_TIMESTAMP + " TEXT, " +
                PO_COLUMN_EXTRAINFO + " TEXT, " +
                PO_COLUMN_PRODUCT_NAME + " TEXT, " +
                PO_COLUMN_CODE + " TEXT, " +
                PO_COLUMN_QUANTITY + " TEXT, " +
                PO_COLUMN_PRICE + " TEXT, " +
                PO_COLUMN_IMAGE + " TEXT, " +
                PO_COLUMN_TIMESTAMP + " TEXT, " +
                PO_COLUMN_STATUS + " TEXT" + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STORE_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + STORE_LIST_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + STARRED_ORDERS_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + PO_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + PO_HISTORY_TABLE_NAME);
        onCreate(db);


    }



    public void deleteAllUserTables() {
        SQLiteDatabase user_db = this.getWritableDatabase();
        user_db.delete(STORE_TABLE_NAME, null, null);
        user_db.delete(STARRED_ORDERS_TABLE_NAME, null, null);
        user_db.delete(ORDERS_TABLE_NAME, null, null);
        user_db.delete(USER_HISTORY_TABLE_NAME, null, null);
        user_db.close();
    }


    public void deleteAllMerchantTables() {
        SQLiteDatabase merchant_db = this.getWritableDatabase();
        merchant_db.delete(PO_TABLE_NAME, null, null);
        merchant_db.delete(PO_HISTORY_TABLE_NAME, null, null);
        merchant_db.close();
    }







    public boolean addStore(String store_name, String store_logo, String store_name_prefix) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STORE_COLUMN_NAME, store_name);
        contentValues.put(STORE_COLUMN_LOGO, store_logo);
        contentValues.put(STORE_UID, store_name_prefix);
        db.insert(STORE_TABLE_NAME, null, contentValues);
        return true;
    }

    // Getting All Stores
    public List<StoreTile> getAllStores() {
        List<StoreTile> storeList = new ArrayList<StoreTile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + STORE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                StoreTile store = new StoreTile(cursor.getString(1), cursor.getString(2), cursor.getString(3));

                // Adding contact to list
                storeList.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return storeList;
    }


    public Integer deleteStore(String store_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(STORE_TABLE_NAME,
                STORE_COLUMN_NAME + " = ? ",
                new String[]{store_name});

    }


    public Boolean storeExists(String store_uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, STORE_TABLE_NAME, STORE_UID + " = ? ",
                new String[]{store_uid}) > 0;

    }



    public boolean addOrderToHistory(String timestamp, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_HISTORY_COLUMN_TIMESTAMP, timestamp);
        contentValues.put(USER_HISTORY_COLUMN_ORDER_UID, id);
        db.insert(USER_HISTORY_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }


    public Boolean orderExists(String user_timestamp) {
        SQLiteDatabase po_db = this.getReadableDatabase();
        boolean  status = DatabaseUtils.queryNumEntries(po_db, ORDERS_TABLE_NAME,
                ORDERS_COLUMN_TIMESTAMP + " = ? ",
                new String[]{user_timestamp}) > 0;
        return status;
    }


    public Boolean orderExisted(String _uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, USER_HISTORY_TABLE_NAME,
                USER_HISTORY_COLUMN_ORDER_UID + " = ? ",
                new String[]{_uid}) > 0;

    }





    public boolean addPendingOrderToHistory(String timestamp, String uid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PO_HISTORY_COLUMN_TIMESTAMP, timestamp);
        contentValues.put(PO_HISTORY_COLUMN_UID, uid);
        db.insert(PO_HISTORY_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }



    public String getPendingOrderPushId(String timestamp){
        String _pushId = null;
        String selectQuery = "SELECT  * FROM " + PO_HISTORY_TABLE_NAME + " WHERE " +
                PO_HISTORY_COLUMN_TIMESTAMP + " ='" + timestamp + "'";
        Log.i("QUERY", selectQuery);

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                _pushId = cursor.getString(cursor.getColumnIndex(PO_HISTORY_COLUMN_UID));

            }while (cursor.moveToNext());
        }
        db.close();
        return _pushId;
    }



    public Boolean pendingOrderExists(String user_timestamp) {
        SQLiteDatabase po_db = this.getReadableDatabase();
        boolean  status = DatabaseUtils.queryNumEntries(po_db, PO_TABLE_NAME,
                PO_COLUMN_BUYERS_TIMESTAMP + " = ? ",
                new String[]{user_timestamp}) > 0;

        return status;
    }



    public Boolean pendingOrderExisted(String _uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, PO_HISTORY_TABLE_NAME, PO_HISTORY_COLUMN_UID + " = ? ",
                new String[]{_uid}) > 0;

    }


    public boolean addOrdertile(OrderTile ordertile) {
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(ORDERS_COLUMN_PRODUCT_NAME, ordertile.getProductName());
        orderValues.put(ORDERS_COLUMN_CODE, ordertile.getItemCode());
        orderValues.put(ORDERS_COLUMN_QUANTITY, ordertile.getItemQuantity());
        orderValues.put(ORDERS_COLUMN_PRICE, ordertile.getItemPrice());
        orderValues.put(ORDERS_COLUMN_SIZE, ordertile.getSize());
        orderValues.put(ORDERS_COLUMN_COLOR, ordertile.getItemColor());
        orderValues.put(ORDERS_COLUMN_DETAILS, ordertile.getDetails());
        orderValues.put(ORDERS_COLUMN_IMAGE, ordertile.getPhotoId());
        orderValues.put(ORDERS_COLUMN_MERCHANT_UID, ordertile.getMerchantUid());
        orderValues.put(ORDERS_COLUMN_TIMESTAMP, ordertile.getTimestamp());
        orderValues.put(ORDERS_COLUMN_CONFIRMED, "neutral");

        order_db.insert(ORDERS_TABLE_NAME, null, orderValues);
        order_db.close();
        return true;
    }




    public boolean addPendingOrder(PendingOrder pending_order) {
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues pendingOrderValues = new ContentValues();
        pendingOrderValues.put(PO_COLUMN_BUYERS_NAME, pending_order.getUserName());
        pendingOrderValues.put(PO_COLUMN_BUYERS_PHONE, pending_order.getUserPhone());
        pendingOrderValues.put(PO_COLUMN_BUYERS_UID, pending_order.getUserUid());
        pendingOrderValues.put(PO_COLUMN_BUYERS_TIMESTAMP, pending_order.getUserTimestamp());
        pendingOrderValues.put(PO_COLUMN_EXTRAINFO, pending_order.getUserExtraInfo());
        pendingOrderValues.put(PO_COLUMN_PRODUCT_NAME, pending_order.getProductName());
        pendingOrderValues.put(PO_COLUMN_CODE, pending_order.getProductCode());
        pendingOrderValues.put(PO_COLUMN_QUANTITY, pending_order.getProductQuantity());
        pendingOrderValues.put(PO_COLUMN_PRICE, pending_order.getProductPrice());
        pendingOrderValues.put(PO_COLUMN_IMAGE, pending_order.getProductPhotoId());
        pendingOrderValues.put(PO_COLUMN_TIMESTAMP, pending_order.getTimestamp());
        pendingOrderValues.put(PO_COLUMN_STATUS, "neutral");
        order_db.insert(PO_TABLE_NAME, null, pendingOrderValues);
        order_db.close();
        return true;
    }


    public Integer deletePendingOrder(String timestamp) {
        SQLiteDatabase order_db = this.getWritableDatabase();
        return order_db.delete(PO_TABLE_NAME,
                PO_COLUMN_BUYERS_TIMESTAMP + " = ? ",
                new String[]{timestamp});
    }






    public List<PendingOrder> getAllPendingOrders() {
        List<PendingOrder> pendingOrders = new ArrayList<PendingOrder>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PO_TABLE_NAME;

        SQLiteDatabase order_db = this.getWritableDatabase();
        Cursor cursor = order_db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                PendingOrder pending_order = new PendingOrder(cursor.getString(cursor.getColumnIndex(PO_COLUMN_BUYERS_NAME)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_BUYERS_PHONE)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_BUYERS_UID)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_BUYERS_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_EXTRAINFO)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(PO_COLUMN_TIMESTAMP)) );

                pendingOrders.add(pending_order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        order_db.close();
        return pendingOrders;
    }




    public Boolean checkPendingOrderStatusApproved(String timestamp) {
        SQLiteDatabase order_db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(order_db, PO_TABLE_NAME,
                PO_COLUMN_TIMESTAMP + " = ? and " + PO_COLUMN_STATUS + " = ? ",
                new String[]{timestamp, "true"}) > 0;
    }



    public Boolean checkPendingOrderStatusDeclined(String timestamp) {
        SQLiteDatabase order_db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(order_db, PO_TABLE_NAME,
                PO_COLUMN_TIMESTAMP + " = ? and " + PO_COLUMN_STATUS + " = ? ",
                new String[]{timestamp, "false"}) > 0;
    }



    public Boolean setpendingOrderStatusApproved(String timestamp){
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues update_order_status = new ContentValues();
        update_order_status.put(PO_COLUMN_STATUS, "true");
        int ret = order_db.update(PO_TABLE_NAME, update_order_status, PO_COLUMN_TIMESTAMP +
                " = ? ", new String[]{timestamp});
        order_db.close();

        if(ret >= 1){
            return true;
        }
        else{
            return false;
        }

    }


    public Boolean setpendingOrderStatusDeclined(String timestamp){
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues update_order_status = new ContentValues();
        update_order_status.put(PO_COLUMN_STATUS, "false");
        int ret = order_db.update(PO_TABLE_NAME, update_order_status, PO_COLUMN_TIMESTAMP +
                " = ? ", new String[]{timestamp});
        order_db.close();

        if(ret >= 1){
            return true;
        }
        else{
            return false;
        }

    }


    public Integer deleteOrder(String timestamp) {
        SQLiteDatabase order_db = this.getWritableDatabase();
        return order_db.delete(ORDERS_TABLE_NAME,
                ORDERS_COLUMN_TIMESTAMP + " = ? ",
                new String[]{timestamp});
    }


    // Getting All Orders
    public List<OrderTile> getAllOrders() {
        List<OrderTile> orderList = new ArrayList<OrderTile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ORDERS_TABLE_NAME;

        SQLiteDatabase order_db = this.getWritableDatabase();
        Cursor cursor = order_db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                OrderTile order = new OrderTile(cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_SIZE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_COLOR)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_MERCHANT_UID)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_TIMESTAMP)) );

                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        order_db.close();
        // return contact list
        return orderList;
    }





    public String getOrderStatus(String timestamp){
        String _status = null;
        String selectQuery = "SELECT  * FROM " + ORDERS_TABLE_NAME + " WHERE " +
                ORDERS_COLUMN_TIMESTAMP + " ='" + timestamp + "'";
        Log.i("QUERY", selectQuery);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                _status = cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_CONFIRMED));

            }while (cursor.moveToNext());
        }
        db.close();
        return _status;
    }



    public Boolean setIsPending(String timestamp){
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues update_ispending = new ContentValues();
        update_ispending.put(ORDERS_COLUMN_CONFIRMED, "pending");
        int ret = order_db.update(ORDERS_TABLE_NAME, update_ispending, ORDERS_COLUMN_TIMESTAMP + " = ? ", new String[]{timestamp});
        order_db.close();

        if(ret >= 1){
        return true;
        }
        else{
            return false;
        }

    }



    public Boolean setConfirmationStatus(String product_code, String timestamp, String confirmation_status){
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues update_confirm = new ContentValues();
        update_confirm.put(ORDERS_COLUMN_CONFIRMED, confirmation_status );
        int ret = order_db.update(ORDERS_TABLE_NAME, update_confirm,
                ORDERS_COLUMN_CODE + " = ? and " +  ORDERS_COLUMN_TIMESTAMP + " = ? " , new String[]{product_code, timestamp});
        order_db.close();

        if(ret >= 1){
            return true;
        }
        else{
            return false;
        }

    }



    public boolean addStarredOrder(OrderTile ordertile) {
        SQLiteDatabase order_db = getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(ORDERS_COLUMN_PRODUCT_NAME, ordertile.getProductName());
        orderValues.put(ORDERS_COLUMN_CODE, ordertile.getItemCode());
        orderValues.put(ORDERS_COLUMN_QUANTITY, ordertile.getItemQuantity());
        orderValues.put(ORDERS_COLUMN_PRICE, ordertile.getItemPrice());
        orderValues.put(ORDERS_COLUMN_SIZE, ordertile.getSize());
        orderValues.put(ORDERS_COLUMN_COLOR, ordertile.getItemColor());
        orderValues.put(ORDERS_COLUMN_DETAILS, ordertile.getDetails());
        orderValues.put(ORDERS_COLUMN_IMAGE, ordertile.getPhotoId());
        orderValues.put(ORDERS_COLUMN_MERCHANT_UID, ordertile.getMerchantUid());
        orderValues.put(ORDERS_COLUMN_TIMESTAMP, ordertile.getTimestamp());
        orderValues.put(ORDERS_COLUMN_CONFIRMED, "neutral");

        order_db.insert(STARRED_ORDERS_TABLE_NAME, ordertile.getTimestamp(), orderValues);
        order_db.close();
        return true;
    }


    public Integer deleteStarredOrder(String pdt_code) {
        SQLiteDatabase order_db = this.getWritableDatabase();
        return order_db.delete(STARRED_ORDERS_TABLE_NAME,
                ORDERS_COLUMN_CODE + " = ? ",
                new String[]{pdt_code});
    }


    public void deleteAllStarredOrders() {
        SQLiteDatabase order_db = this.getWritableDatabase();
        order_db.delete(STARRED_ORDERS_TABLE_NAME, null, null);
        order_db.close();
    }


    // Getting All Orders
    public List<OrderTile> getAllStarredOrders() {
        List<OrderTile> orderList = new ArrayList<OrderTile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + STARRED_ORDERS_TABLE_NAME;

        SQLiteDatabase order_db = this.getWritableDatabase();
        Cursor cursor = order_db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                OrderTile order = new OrderTile(cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_PRODUCT_NAME)), cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_QUANTITY)), cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_SIZE)), cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_COLOR)), cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_IMAGE)), cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_MERCHANT_UID)),
                        cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_TIMESTAMP)) );

                // Adding contact to list
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        order_db.close();
        // return contact list
        return orderList;
    }


    public Boolean starredOrderExists(String timestamp) {
        SQLiteDatabase order_db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(order_db, STARRED_ORDERS_TABLE_NAME, ORDERS_COLUMN_TIMESTAMP + " = ? ",
                new String[]{timestamp}) > 0;
    }


}
