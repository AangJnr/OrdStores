package com.shop.ordstore.Search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.ordstore.R;
import com.shop.ordstore.UserClasses.StoreTile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by AangJnr on 7/28/16.
 */


public class StoresContentProvider extends ContentProvider {
    private final static String FEED_URL = "https://raw.githubusercontent.com/AangJnr/cerebro_wallpapers/master/ls.json";
    List<StoreTile> stores;
    DatabaseReference merchantsDatabaseRef;
    String _name, _logo, merchant_Uid;

    @Override
    public boolean onCreate() {
        merchantsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("merchants");

        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if (stores == null || stores.isEmpty()) {

            try {
                merchantsDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Show progress here

                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            merchant_Uid = snapshot.getKey();

                            if(merchant_Uid != null || merchant_Uid != "null") {

                                _name = (String) snapshot.child("name").getValue();
                                _logo = (String) snapshot.child("logo").getValue();


                                StoreTile item;
                                item = new StoreTile();

                                item.setMerchantUid(merchant_Uid);
                                item.setStoreName(_name);
                                item.setLogo(_logo);

                                stores.add(item);



                            }

                        }




                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }



                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }







        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );
        if (stores != null) {
            String query = uri.getLastPathSegment().toUpperCase();
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            int length = stores.size();
            for (int i = 0; i < length && cursor.getCount() < limit; i++) {
                StoreTile store = stores.get(i);
                String names = store.getStoreName();

                if (names.toUpperCase().contains(query)) {
                    cursor.addRow(new Object[]{i, names, i});
                }
            }
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    //

}

