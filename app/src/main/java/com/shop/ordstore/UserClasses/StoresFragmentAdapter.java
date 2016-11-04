package com.shop.ordstore.UserClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.ordstore.R;
import com.shop.ordstore.DatabaseHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by AangJnr on 5/6/16.
 */
public class StoresFragmentAdapter extends RecyclerView.Adapter<StoresFragmentAdapter.FragmentStoresViewHolder> {

    View v;
    Context mContext;
    OnItemClickListener mItemClickListener;
    private List<StoreTile> storetile;


    public StoresFragmentAdapter(Context mContext, List<StoreTile> storetile) {
        this.storetile = storetile;
        this.mContext = mContext;

    }


    @Override
    public FragmentStoresViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_item, viewGroup, false);


        return new FragmentStoresViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FragmentStoresViewHolder fragmentStoresViewHolder, final int position) {

        StoreTile store = storetile.get(position);

        fragmentStoresViewHolder.storeName.setText(store.storeName);


        Picasso.with(mContext)
                .load(store.getLogo())
                .into(fragmentStoresViewHolder.photoId, new Callback() {
                    @Override
                    public void onSuccess() {
                        //use your bitmap or something

                        Bitmap photo = ((BitmapDrawable) fragmentStoresViewHolder.photoId.getDrawable()).getBitmap();


                        if (photo != null) {
                            fragmentStoresViewHolder.place_holder.setVisibility(View.GONE);
                            Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    int mutedLight = palette.getMutedColor(mContext.getResources().getColor(R.color.drawable_grey));
                                    fragmentStoresViewHolder.nameHolder.setBackgroundColor(mutedLight);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });


    }


    @Override
    public int getItemCount() {
        return storetile.size();


    }


    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(storetile, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(storetile, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }


    public void onItemDismiss(int position) {
        StoreTile store = storetile.get(position);
        DatabaseHelper storesDbHelper = new DatabaseHelper(mContext);
        storesDbHelper.deleteStore(store.getStoreName());
        storetile.remove(position);
        notifyItemRemoved(position);


    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class FragmentStoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout gridcardview;
        public TextView storeName;
        public ImageView photoId;
        RelativeLayout nameHolder;
        LinearLayout selectableItem;
        ImageView place_holder;

        FragmentStoresViewHolder(View itemView) {
            super(itemView);
            gridcardview = (RelativeLayout) itemView.findViewById(R.id.grid_layout_card_view);
            storeName = (TextView) itemView.findViewById(R.id.store_name);
            photoId = (ImageView) itemView.findViewById(R.id.store_photo);
            nameHolder = (RelativeLayout) itemView.findViewById(R.id.storenameHolder);
            selectableItem = (LinearLayout) itemView.findViewById(R.id.selectable);
            place_holder = (ImageView) itemView.findViewById(R.id.stores_placeholder);
            selectableItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());


            }
        }

    }

}