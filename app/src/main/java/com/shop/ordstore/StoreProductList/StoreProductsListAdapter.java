package com.shop.ordstore.storeProductList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.utilities.DateUtil;
import com.shop.ordstore.R;
import com.shop.ordstore.userClasses.OrderTile;
import com.shop.ordstore.userClasses.OrdersFragment;
import com.shop.ordstore.utilities.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AangJnr on 8/10/16.
 */
public class StoreProductsListAdapter extends RecyclerView.Adapter<StoreProductsListAdapter.ViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    static OnItemClickListener mItemClickListener;
    static PopupMenu.OnMenuItemClickListener mMenuItemClickListener;
    private List<Product> products;
    private Context context;
    private int lastAnimatedPosition = -1;
    DatabaseHelper ordersDataBaseHelper;
    int menuPosition;
    final ContextThemeWrapper ctx;


    /**
     * Constructor
     *
     * @param products
     **/

    public StoreProductsListAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
        ordersDataBaseHelper = new DatabaseHelper(context);

        ctx = new ContextThemeWrapper(context, R.style.AppTheme);
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

        Product order = products.get(position);








        viewHolder.pdtName.setText(order.productName);
        viewHolder.pdtItemCode.setText(order.itemCode);
        viewHolder.pdtPrice.setText(order.price);


        if (order.photoId != "" || order.photoId != null) {
            Picasso.with(context).load(order.photoId).into(viewHolder.pdtPhoto, new Callback() {
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

            pdtPhoto.setOnClickListener(this);


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
            inflater.inflate(R.menu.product_list_menu, popup.getMenu());
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
                case R.id.action_add_order:
                    Toast.makeText(context, StoreProductListActivity.merchantID(), Toast.LENGTH_SHORT).show();
                    showDialog();

                    return true;
                case R.id.checkout:
                    Toast.makeText(context, "Add to wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }


    public void showDialog(){
        final Product product = products.get(menuPosition);



        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog, null);
        android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText edittext = (EditText) mView.findViewById(R.id.userInputDialog);

        alertDialogBuilderUserInput
                .setCancelable(true);
        final android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        mView.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _quantity = edittext.getText().toString();

                try {
                    int quantity = Integer.parseInt(_quantity);

                    if(quantity != 0 && quantity < 100) {
                        addOrder(product.getProductName(), product.getItemCode(), String.valueOf(quantity), product.getPrice(), "", "",
                                "", product.getPhotoId(), StoreProductListActivity.merchantID(), DateUtil.getDateInMillisToString());
                        alertDialogAndroid.dismiss();

                        OrdersFragment.adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(context, "Quantity cannot be 0",
                                Toast.LENGTH_SHORT).show();

                    }
                } catch (NumberFormatException e) {

                    Toast.makeText(context, "Please enter a valid quantity",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });


        mView.findViewById(R.id.negative_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAndroid.dismiss();
            }
        });

    }


    public void addOrder(String product_name, String code, String quantity, String price, String size, String color,
                         String extraInfo, String photo, String merchant_uid, String date){


        OrderTile dataToAdd = new OrderTile(product_name, code, quantity, price, size, color,
                extraInfo, photo, merchant_uid, date);
        ordersDataBaseHelper.addOrdertile(dataToAdd);

        OrdersFragment.orderTile.add(dataToAdd);
        OrdersFragment.adapter.notifyDataSetChanged();

        Toast.makeText(context, "Order added.",
                Toast.LENGTH_SHORT).show();



    }
}


