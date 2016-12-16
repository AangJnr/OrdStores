package com.shop.ordstore.storesListClasses;

/**
 * Created by AangJnr on 7/19/16.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.R;
import com.shop.ordstore.userClasses.StoreTile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class StoresListViewAdapter extends RecyclerView.Adapter<StoresListViewAdapter.ViewHolder> {
    static OnItemClickListener mItemClickListener;
    Context mContext;
    private List<StoreTile> storesListItems;
    private int lastPosition = -1;
    private final static int FADE_DURATION = 500;


    public StoresListViewAdapter(Context mContext, List<StoreTile> storesListItems) {
        this.storesListItems = storesListItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stores_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StoreTile item = storesListItems.get(position);


        holder.name.setText(Html.fromHtml(item.getStoreName()));


        if(item.getLogo() != null || item.getLogo() != "null") {
            Picasso.with(mContext).load(item.getLogo()).into(holder.logo);
        }else{

            holder.logo.setImageResource(R.drawable.icon_splash_screen);

        }



        Picasso.with(mContext)
                .load(item.getLogo())
                .into(holder.logo, new Callback() {
                    @Override
                    public void onSuccess() {
                        //use your bitmap or something

                        Bitmap photo = ((BitmapDrawable) holder.logo.getDrawable()).getBitmap();


                        if (photo != null) {
                            holder.place_holder.setVisibility(View.GONE);
                            Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    int mutedLight = palette.getMutedColor(mContext.getResources().getColor(R.color.drawable_grey));
                                    holder.nameHolder.setBackgroundColor(mutedLight);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });


        DatabaseHelper storesDbHelper = new DatabaseHelper(mContext);

        if (storesDbHelper.storeExists(item.getMerchantUid())) {

            holder.follow.setText("FOLLOWING");


        } else {
            holder.follow.setText("FOLLOW");

        }

        setFadeAnimation(holder.itemView, position);



    }

    @Override
    public int getItemCount() {
        return storesListItems.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }


    private void setScaleAnimation(View view, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
            lastPosition = position;
        }
    }

    private void setFadeAnimation(View view, int position) {
        if (position > lastPosition) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
            lastPosition = position;
        }
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView logo;
        View mainHolder;
        TextView follow;
        RelativeLayout nameHolder;
        ImageView place_holder;

        public ViewHolder(View view) {
            super(view);


            name = (TextView) view.findViewById(R.id.list_item_name);
            logo = (ImageView) view.findViewById(R.id.list_item_image);
            mainHolder = (View) view.findViewById(R.id.mainHolder);
            nameHolder = (RelativeLayout) itemView.findViewById(R.id.store_list_nameholder);
            follow = (TextView) view.findViewById(R.id.follow);
            place_holder = (ImageView) view.findViewById(R.id.stores_list_placeholder);
            follow.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());


            }
        }


    }

}