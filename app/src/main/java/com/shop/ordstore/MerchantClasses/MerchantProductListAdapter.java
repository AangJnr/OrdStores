package com.shop.ordstore.merchantClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.ordstore.R;
import com.shop.ordstore.storeProductList.Product;
import com.shop.ordstore.utilities.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AangJnr on 8/10/16.
 */
public class MerchantProductListAdapter extends RecyclerView.Adapter<MerchantProductListAdapter.ViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    static OnItemClickListener mItemClickListener;
    static PopupMenu.OnMenuItemClickListener mMenuItemClickListener;
    private List<Product> products;
    private Context context;
    private int lastAnimatedPosition = -1;

    int menuPosition;



    /**
     * Constructor
     *
     * @param products
     **/

    public MerchantProductListAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;


    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return products.size();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_cardview, viewGroup, false);

        return new ViewHolder(v);
    }


    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        Product product = products.get(position);


        viewHolder.pdtName.setText(product.getProductName());
        viewHolder.pdtItemCode.setText(product.getItemCode());
        viewHolder.pdtPrice.setText(product.getPrice());


        if (product.getPhotoId() != "" || product.getPhotoId() != null) {
            Picasso.with(context).load(product.getPhotoId()).into(viewHolder.pdtPhoto, new Callback() {
                @Override
                public void onSuccess() {
                    //use your bitmap or something

                    Bitmap photo = ((BitmapDrawable) viewHolder.pdtPhoto.getDrawable()).getBitmap();


                    if (photo != null) {

                        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                int mutedLight = palette.getVibrantColor(context.getResources().getColor(R.color.drawable_grey));
                                viewHolder.text_holder.setBackgroundColor(mutedLight);
                            }
                        });
                    }
                }

                @Override
                public void onError() {

                }
            });


        }
    }





    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnMenuItemClickListener(final PopupMenu.OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }





    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;

        RelativeLayout text_holder;
        TextView pdtName;
        TextView pdtItemCode;
        TextView pdtPrice;
        ImageView pdtPhoto;
        ImageView overflow;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            text_holder = (RelativeLayout) itemView.findViewById(R.id.text_holder);

            pdtName = (TextView) itemView.findViewById(R.id.pdtname);
            pdtItemCode = (TextView) itemView.findViewById(R.id.pdtCode);
            pdtPrice = (TextView) itemView.findViewById(R.id.pdtprice);
            pdtPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);

            //pdtPhoto.setOnClickListener(this);


            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPopupMenu(overflow, getAdapterPosition());

                }
            });

        }

        private void showPopupMenu(View view, int position) {
            // inflate menu
            menuPosition = position;
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.merchant_product_list_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
            popup.show();
        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());


            }

        }

    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;
        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {


            switch (menuItem.getItemId()) {
                case R.id.share:

                    //Share on media

                    return true;

                default:
            }
            return false;
        }
    }






}


