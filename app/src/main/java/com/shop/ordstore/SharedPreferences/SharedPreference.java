package com.shop.ordstore.sharedPreferences;

/**
 * Created by AangJnr on 7/30/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.shop.ordstore.userClasses.OrderTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<OrderTile> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }


    public void addFavorite(Context context, OrderTile ordertile) {
        List<OrderTile> starred_Order = getFavorites(context);
        if (starred_Order == null)
            starred_Order = new ArrayList<OrderTile>();
        starred_Order.add(ordertile);
        saveFavorites(context, starred_Order);
    }

    public void removeFavorite(Context context, OrderTile ordertile) {
        ArrayList<OrderTile> starred_Order = getFavorites(context);
        if (starred_Order != null) {
            starred_Order.remove(ordertile);
            saveFavorites(context, starred_Order);
        }
    }

    public ArrayList<OrderTile> getFavorites(Context context) {
        SharedPreferences settings;
        List<OrderTile> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            OrderTile[] favoriteItems = gson.fromJson(jsonFavorites,
                    OrderTile[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<OrderTile>(favorites);
        } else
            return null;

        return (ArrayList<OrderTile>) favorites;
    }


    public void deleteAllData(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit().clear();
        editor.commit();


    }

}