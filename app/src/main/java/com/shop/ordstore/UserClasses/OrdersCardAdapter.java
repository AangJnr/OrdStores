package com.shop.ordstore.userClasses;

/**
 * Created by AangJnr on 6/27/16.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.utilities.DateUtil;
import com.shop.ordstore.utilities.ExpandableItemLayout;
import com.shop.ordstore.R;
import com.shop.ordstore.sharedPreferences.SharedPreference;
import com.shop.ordstore.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersCardAdapter extends RecyclerView.Adapter<OrdersCardAdapter.OrdersViewHolder> {

    DatabaseHelper ordersDataBaseHelper;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    static SharedPreference sharedPreference;
    private List<OrderTile> ordertile;
    private Context context;
    private int lastAnimatedPosition = -1;
    private DatabaseReference merchantUidRef;
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();


    /**
     * Constructor
     *
     * @param ordertile
     **/

    public OrdersCardAdapter(Context context, List<OrderTile> ordertile) {
        this.ordertile = ordertile;
        this.context = context;
        sharedPreference = new SharedPreference();
        merchantUidRef = FirebaseDatabase.getInstance().getReference().child("merchants");
        ordersDataBaseHelper = new DatabaseHelper(context);

    }




    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return ordertile.size();
        //return (null != ordertile ? ordertile.size() : 0);

    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_cardview_item, viewGroup, false);

        return new OrdersViewHolder(v);
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
    public void onBindViewHolder(OrdersViewHolder viewHolder, final int position) {
        OrderTile order = ordertile.get(position);


        viewHolder.productName.setText(order.getProductName());
        viewHolder.itemCode.setText(order.getItemCode());
        viewHolder.quantity.setText(order.getItemQuantity());
        viewHolder.price.setText(order.getItemPrice());
        viewHolder.extra_info_edittext_layout.getEditText().setText(order.getDetails());
//        viewHolder.size.setText(order.size);
        //       viewHolder.itemColor.setText(order.itemColor);
        //       viewHolder.details.setText(order.details);

        if (order.photoId != "") {
            Picasso.with(context).load(order.getPhotoId()).into(viewHolder.photoId);

        } else {

            viewHolder.photoId.setImageResource(R.drawable.icon_splash_screen);

        }



        if (order.timestamp != "" && order.timestamp != null) {

            viewHolder.dateText.setText(DateUtil.convertStringToPrettyTime(order.getTimestamp()));


        }




        if (ordersDataBaseHelper.starredOrderExists(order.getTimestamp())) {

            viewHolder.star.setImageResource(R.drawable.ic_star_24dp);
            viewHolder.star.setTag("starred");
        } else {
            viewHolder.star.setImageResource(R.drawable.ic_star_outline_24dp);
            viewHolder.star.setTag("unStarred");
        }



        if(ordersDataBaseHelper.getOrderStatus(order.getTimestamp()).equalsIgnoreCase("pending")){

            viewHolder.checkout.setBackgroundResource(R.drawable.border_button_background_accent);
            viewHolder.checkout.setText("Pending");
            viewHolder.checkout.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else if(ordersDataBaseHelper.getOrderStatus(order.getTimestamp()).equalsIgnoreCase("true")){
            viewHolder.checkout.setText("Confirmed");
            viewHolder.checkout.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            viewHolder.checkout.setBackgroundResource(R.drawable.border_button_background_primary);

        }else if (ordersDataBaseHelper.getOrderStatus(order.getTimestamp()).equalsIgnoreCase("false")){
            viewHolder.checkout.setText("Out of stock");
            viewHolder.checkout.setTextColor(ContextCompat.getColor(context, R.color.edittext_error_color));
            viewHolder.checkout.setBackgroundResource(R.drawable.border_button_background_red);

        }


        if (order.getMerchantUid() != null && ordersDataBaseHelper.storeExists(order.getMerchantUid())) {
            //viewHolder.add_store.setText("Store added!");
            viewHolder.add_store.setVisibility(View.GONE);

        }


    }



    public class OrdersViewHolder extends RecyclerView.ViewHolder{

        ExpandableItemLayout cv;
        LinearLayout addstore_linear_layout;
        ImageView star;
        TextView dateText;
        Button checkout;

        TextView productName;
        TextView itemCode;
        TextView quantity;
        TextView price;
        TextView size;
        TextView itemColor;
        TextView details;
        ImageView photoId;
        TextView add_store;

        TextInputLayout extra_info_edittext_layout;


        OrdersViewHolder(final View itemView) {
            super(itemView);
            cv = (ExpandableItemLayout) itemView.findViewById(R.id.cv);
            addstore_linear_layout = (LinearLayout) itemView.findViewById(R.id.add_store_layout);
            dateText = (TextView) itemView.findViewById(R.id.date);
            star = (ImageView) itemView.findViewById(R.id.star);
            checkout = (Button) itemView.findViewById(R.id.checkout_button);

            extra_info_edittext_layout = (TextInputLayout) itemView.findViewById(R.id.input_layout_extra_info);

            productName = (TextView) itemView.findViewById(R.id.store_name);
            itemCode = (TextView) itemView.findViewById(R.id.item_code);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            price = (TextView) itemView.findViewById(R.id.order_price);
            /*size = (TextView) itemView.findViewById(R.id.size);
            itemColor = (TextView) itemView.findViewById(R.id.color);
            details = (TextView) itemView.findViewById(R.id.order_details);*/
            photoId = (ImageView) itemView.findViewById(R.id.photo_id);


            add_store = (TextView) itemView.findViewById(R.id.add_store);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    cv.toggleExpansion();


                }
            });

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Item clicked at " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    final int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        final OrderTile order = ordertile.get(position);
                        String tag = star.getTag().toString();
                        if (tag.equalsIgnoreCase("unStarred")) {


                            star.setTag("starred");
                            animateHeartButton(star);


                            ordersDataBaseHelper.addStarredOrder(order);
                            Toast.makeText(context, "Item added to Favorites!", Toast.LENGTH_SHORT).show();


                        } else {

                            star.setTag("unStarred");
                            star.setImageResource(R.drawable.ic_star_outline_24dp);
                            ordersDataBaseHelper.deleteStarredOrder(order.getItemCode());
                            Toast.makeText(context, "Item removed from Favorites!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });


            add_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OrderTile order = ordertile.get(getAdapterPosition());
                    Toast.makeText(context, "Adding store.", Toast.LENGTH_SHORT).show();

                    final String merchant_id = order.getMerchantUid();

                    merchantUidRef.child(merchant_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String storeName = String.valueOf(dataSnapshot.child("name").getValue());
                            String logo = String.valueOf(dataSnapshot.child("logo").getValue());


                            if (storeName != null && ordersDataBaseHelper.addStore(storeName, logo, merchant_id)) {
                                Toast.makeText(context, "Store Added", Toast.LENGTH_SHORT).show();
                                add_store.setVisibility(View.GONE);
                                notifyDataSetChanged();


                                final StoreTile dataToAdd = new StoreTile(storeName, logo, merchant_id);
                                StoresFragment.storetile.add(dataToAdd);
                                StoresFragment.adapter.notifyDataSetChanged();
                                StoresFragment.emptyView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "Something has happened!", Toast.LENGTH_SHORT).show();

                        }
                    });



                }
            });




            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkout.getText().toString().equalsIgnoreCase("checkout") ||
                            checkout.getText().toString().equalsIgnoreCase("out of stock")) {

                        Toast.makeText(context, "Sending order.", Toast.LENGTH_SHORT).show();

                        final OrderTile pending_order = ordertile.get(getAdapterPosition());

                        String merchant_uid, user_uid, user_name, user_phone, order_timestamp, order_details,
                                order_product_name, order_item_code, order_quantity, order_product_price, order_photo_id;

                        merchant_uid = pending_order.getMerchantUid();
                        user_uid = MainActivity.get_user_uid();
                        user_name = MainActivity.get_name();
                        user_phone = MainActivity.get_phone();


                        order_timestamp = pending_order.getTimestamp();
                        order_details = pending_order.getDetails();
                        order_product_name = pending_order.getProductName();
                        order_item_code = pending_order.getItemCode();
                        order_quantity = pending_order.getItemQuantity();
                        order_product_price = pending_order.getItemPrice();
                        order_photo_id = pending_order.getPhotoId();


                        SentOrders sent_order = new SentOrders(user_name, user_phone, user_uid,
                                order_timestamp, order_details, order_product_name, order_item_code,
                                order_quantity, order_product_price,
                                order_photo_id);

                        merchantUidRef.child(merchant_uid).child("pending_orders").push().setValue(sent_order)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Your order has been sent!",
                                                    Toast.LENGTH_SHORT).show();

                                            if (ordersDataBaseHelper.setIsPending(pending_order.getTimestamp())) {
                                                checkout.setBackgroundResource(R.drawable.border_button_background_accent);
                                                checkout.setText("Pending");
                                            }

                                        } else {
                                            Toast.makeText(context, "Error occurred, please try again later!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else if(checkout.getText().toString().equalsIgnoreCase("pending")){
                        Toast.makeText(context, "Order is in pending status",
                                Toast.LENGTH_SHORT).show();

                    }

                }
            });


        }



    }




    private void animateHeartButton(final ImageView  holder) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.setImageResource(R.drawable.ic_star_24dp);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationsMap.remove(holder);
               // dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        //heartAnimationsMap.put(holder, animatorSet);
    }

    }



