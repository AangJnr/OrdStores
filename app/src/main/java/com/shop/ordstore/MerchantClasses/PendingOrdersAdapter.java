package com.shop.ordstore.merchantClasses;

/**
 * Created by AangJnr on 9/21/16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.utilities.DateUtil;
import com.shop.ordstore.utilities.ExpandableItemLayout;
import com.shop.ordstore.R;
import com.shop.ordstore.userClasses.UserOrderStatus;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by AangJnr on 6/27/16.
 */


public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.PendingOrdersViewHolder> {

    DatabaseHelper pending_ordersDataBaseHelper;
    private List<PendingOrder> pendingOrders;
    private Context context;

    private DatabaseReference usersRef;



    /**
     * Constructor
     *
     * @param pendingOrders
     **/

    public PendingOrdersAdapter(Context context, List<PendingOrder> pendingOrders) {
        this.pendingOrders = pendingOrders;
        this.context = context;
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        pending_ordersDataBaseHelper = new DatabaseHelper(context);

    }




    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return pendingOrders.size();
        //return (null != ordertile ? ordertile.size() : 0);

    }

    @Override
    public PendingOrdersAdapter.PendingOrdersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pending_orders_item, viewGroup, false);

        return new PendingOrdersAdapter.PendingOrdersViewHolder(v);
    }




    @Override
    public void onBindViewHolder(PendingOrdersViewHolder viewHolder, final int position) {
        PendingOrder pending_order = pendingOrders.get(position);

        viewHolder.buyers_name.setText(pending_order.getUserName());
        viewHolder.buyers_phone.setText(pending_order.getUserPhone());
        viewHolder.productName.setText(pending_order.getProductName());
        viewHolder.itemCode.setText(pending_order.getProductCode());
        viewHolder.quantity.setText(pending_order.getProductQuantity());
        viewHolder.price.setText(pending_order.getProductPrice());
        viewHolder.extra_info.setText(pending_order.getUserExtraInfo());

        viewHolder.timestamp.setText(DateUtil.convertStringToPrettyTime(pending_order.getTimestamp()));


        int _quantity = Integer.parseInt(pending_order.getProductQuantity());
        float _price = Float.parseFloat(pending_order.getProductPrice().substring(1));
        String currency = pending_order.getProductPrice().substring(0, 1);

        String _total = currency + String.valueOf( _quantity * _price);

        viewHolder.total.setText( _total);



        if (pending_order.getProductPhotoId() != "") {
            Picasso.with(context).load(pending_order.getProductPhotoId()).into(viewHolder.photoId);

        } else {

            viewHolder.photoId.setImageResource(R.drawable.icon_splash_screen);

        }



        if(pending_ordersDataBaseHelper.checkPendingOrderStatusApproved(pending_order.getTimestamp()))
        {
            viewHolder.status_text.setText("Approved!");
            viewHolder.status_text.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        }else if(pending_ordersDataBaseHelper.checkPendingOrderStatusDeclined(pending_order.getTimestamp())){
            viewHolder.status_text.setText("Declined!");
            viewHolder.status_text.setTextColor(ContextCompat.getColor(context,R.color.edittext_error_color));

        }

    }



    public class PendingOrdersViewHolder extends RecyclerView.ViewHolder{


        Button confirm, decline;
        ExpandableItemLayout cv;
        TextView buyers_name, buyers_phone, productName, itemCode, quantity, price, total, extra_info, timestamp, status_text;
        ImageView photoId, dots;




        PendingOrdersViewHolder(final View itemView) {
            super(itemView);

            buyers_name = (TextView) itemView.findViewById(R.id.buyers_name);
            buyers_phone = (TextView) itemView.findViewById(R.id.buyer_phone);
            cv = (ExpandableItemLayout) itemView.findViewById(R.id.cv);
            productName = (TextView) itemView.findViewById(R.id.pdt_name);
            itemCode = (TextView) itemView.findViewById(R.id.product_code);
            quantity = (TextView) itemView.findViewById(R.id.pdt_quantity);
            price = (TextView) itemView.findViewById(R.id.product_price);
            photoId = (ImageView) itemView.findViewById(R.id.pdt_image);
            total = (TextView) itemView.findViewById(R.id.product_price_total);
            timestamp = (TextView) itemView.findViewById(R.id.po_timestamp);
            extra_info = (TextView) itemView.findViewById(R.id.po_extra_info);
            status_text = (TextView) itemView.findViewById(R.id.status_text);
            dots = (ImageView) itemView.findViewById(R.id.ic_dots);

            confirm = (Button) itemView.findViewById(R.id.po_confirm_button);
            decline = (Button) itemView.findViewById(R.id.po_decline_button);



            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                PendingOrder po = pendingOrders.get(getAdapterPosition());
                   // String _userUid, _pdtCode, _userTimestamp;
                    String order_id = pending_ordersDataBaseHelper.getPendingOrderPushId(po.getTimestamp());


                    if(showAlert(po.getUserUid(), po.getProductCode(), po.getUserTimestamp(), order_id, "true")){
                        if(pending_ordersDataBaseHelper.setpendingOrderStatusApproved(po.getTimestamp())){
                            status_text.setText("Approved!");
                            status_text.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

                        }
                    }


                }
            });


            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PendingOrder po = pendingOrders.get(getAdapterPosition());
                    String order_id = pending_ordersDataBaseHelper.getPendingOrderPushId(po.getTimestamp());
                    //Show Alert
                    if(showAlert(po.getUserUid(), po.getProductCode(), po.getUserTimestamp(), order_id, "false")){
                        if(pending_ordersDataBaseHelper.setpendingOrderStatusDeclined(po.getTimestamp())){

                            status_text.setText("Declined!");
                            status_text.setTextColor(ContextCompat.getColor(context, R.color.edittext_error_color));
                        }
                    }


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    cv.toggleExpansion();


                }
            });









        }
    }
    boolean response;

    public boolean showAlert(final String user_uid, final String order_code, final String user_timestamp, final String order_id,
                             String confirm){


        final UserOrderStatus user_status = new UserOrderStatus(order_code, user_timestamp, confirm);

        final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("User will be notified duly")
                .setCancelable(true)
                .setIcon(R.drawable.attention_96)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Sending...",
                                Toast.LENGTH_SHORT).show();


                DatabaseReference order_status_data = usersRef.child(user_uid).child("order_status").child(order_id);
                        order_status_data.setValue(user_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "User has been notified.",
                                            Toast.LENGTH_SHORT).show();

                                    response = true;


                                }else{
                                    response = false;
                                    Toast.makeText(context, "Failed. Please check your wifi or try again soon",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        response = false;
                        dialog.dismiss();
                    }
                });
        AlertDialog logout_dialog = logout_alert_builder.create();
        logout_dialog.show();

            return response;
    }

}