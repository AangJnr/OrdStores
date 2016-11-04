package com.shop.ordstore.UserClasses;

/**
 * Created by AangJnr on 9/12/16.
 */

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shop.ordstore.DatabaseHelper;
import com.shop.ordstore.DateUtil;
import com.shop.ordstore.R;
import com.skyfishjy.library.RippleBackground;

public class OrdServiceRevised extends Service {
    DatabaseHelper ordersDataBaseHelper;
    final String fashion = "CLO";
    final String edibles = "EDI";
    View removeImg;
    boolean showing = true;
    RelativeLayout ripple_background;
    OrderTile dataToAdd;
    String _code, _quantity, _size, _color, _extraInfo, merchant_uid;
    String pdt_code, pdt_name, pdt_price, pdt_photo, pdt_extraInfo, codeHolder;

    EditText codeEditText, quantityEditText, sizeEditText, itemColorEditText, detailsEditText;
    Button add_to_cart, checkout;
    TextInputLayout codeLayout, quantityLayout, extraLayout;
    LinearLayout extrasLayout;
    Handler myHandler = new Handler();
    private WindowManager windowManager;
    private RelativeLayout floatingOrdView, removeView;
    private LinearLayout order_layout;
    Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (order_layout != null) {
                order_layout.setVisibility(View.GONE);
            }
        }
    };
    private ImageView back_button;
    private TextView extra_info;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;
    private boolean mRunning;
    private DatabaseReference merchantUidRef;


    @SuppressWarnings("deprecation")

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        setTheme(R.style.AppTheme);
        super.onCreate();


        ordersDataBaseHelper = new DatabaseHelper(this);

 }

    private void handleStart() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        removeView = (RelativeLayout) inflater.inflate(R.layout.remove_layout, null);
        removeImg = removeView.findViewById(R.id.remove_img);
        final RippleBackground rippleBackground = (RippleBackground) removeView.findViewById(R.id.ripple_content);

        //int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        WindowManager.LayoutParams paramRemove = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        paramRemove.gravity = Gravity.TOP | Gravity.LEFT;


        removeView.setVisibility(View.GONE);
        windowManager.addView(removeView, paramRemove);


        WindowManager.LayoutParams ripple = (WindowManager.LayoutParams) rippleBackground.getLayoutParams();
        ripple.height = 500;
        ripple.width = 500;
        windowManager.updateViewLayout(removeView, ripple);


        floatingOrdView = (RelativeLayout) inflater.inflate(R.layout.ord, null);
        //chatheadImg = (ImageView)floatingOrdView.findViewById(R.id.ord);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = height / 2;

        windowManager.addView(floatingOrdView, params);

        floatingOrdView.setOnTouchListener(new View.OnTouchListener() {
            long time_start = 0, time_end = 0;
            boolean isLongclick = false, inBounded = false;
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //Log.d(Utils.LogTag, "Into runnable_longClick");

                    isLongclick = true;
                    removeView.setVisibility(View.VISIBLE);
                    floatingOrdOnLongClick();
                }
            };


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) floatingOrdView.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        remove_img_width = removeImg.getLayoutParams().width;
                        remove_img_height = removeImg.getLayoutParams().height;

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        if (order_layout != null) {
                            order_layout.setVisibility(View.GONE);

                            myHandler.removeCallbacks(myRunnable);
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        if (isLongclick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;

                                int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.5)) / 2);
                                int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.5) + getStatusBarHeight()));

                                rippleBackground.startRippleAnimation();



                                /*layoutParams.x = x_cord_remove + (Math.abs(removeView.getWidth() - floatingOrdView.getWidth())) / 2;
                                layoutParams.y = y_cord_remove + (Math.abs(removeView.getHeight() - floatingOrdView.getHeight())) / 2 ;

                                windowManager.updateViewLayout(floatingOrdView, layoutParams);*/

                                break;
                            } else {
                                inBounded = false;

                                rippleBackground.stopRippleAnimation();
                                removeImg.getLayoutParams().height = remove_img_height;
                                removeImg.getLayoutParams().width = remove_img_width;

                            }


                        }


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        windowManager.updateViewLayout(floatingOrdView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        removeView.setVisibility(View.GONE);
                        removeImg.getLayoutParams().height = remove_img_height;
                        removeImg.getLayoutParams().width = remove_img_width;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        if (inBounded) {

                            stopService(new Intent(OrdServiceRevised.this, OrdServiceRevised.class));
                            inBounded = false;
                            break;
                        }


                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 300) {
                                floatingOrdOnClick();
                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (floatingOrdView.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (floatingOrdView.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;

                        inBounded = false;
                        resetPosition(x_cord);

                        break;
                    default:
                        //Log.d(Utils.LogTag, "floatingOrdView.setOnTouchListener  -> event.getAction() : default");
                        break;
                }
                return true;
            }
        });


        order_layout = (LinearLayout) inflater.inflate(R.layout.bubble_layout, null);
        WindowManager.LayoutParams paramsTxt = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        paramsTxt.gravity = Gravity.BOTTOM;

        order_layout.setVisibility(View.GONE);
        windowManager.addView(order_layout, paramsTxt);

////////////////////////////////
        add_to_cart = (Button) order_layout.findViewById(R.id.bubble_add_cart);
        checkout = (Button) order_layout.findViewById(R.id.bubble_checkout);


        extra_info = (TextView) order_layout.findViewById(R.id.extra_info);
        back_button = (ImageView) order_layout.findViewById(R.id.back_button);

        codeLayout = (TextInputLayout) order_layout.findViewById(R.id.input_layout_dialog_code);
        quantityLayout = (TextInputLayout) order_layout.findViewById(R.id.input_layout_dialog_quantity);

        extraLayout = (TextInputLayout) order_layout.findViewById(R.id.input_layout_extra_info);

        codeEditText = (EditText) order_layout.findViewById(R.id.dialog_code);
        quantityEditText = (EditText) order_layout.findViewById(R.id.dialog_quantity);
        sizeEditText = (EditText) order_layout.findViewById(R.id.dialog_size);
        itemColorEditText = (EditText) order_layout.findViewById(R.id.dialog_color);
        //detailsEditText = (EditText) order_layout.findViewById(R.id.dialog_extraInfo);

////////////////////////////////


        extra_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _code = codeEditText.getText().toString().toLowerCase();
                _quantity = quantityEditText.getText().toString();
                _size = sizeEditText.getText().toString();
                _color = itemColorEditText.getText().toString();

                if (_code.isEmpty() && _quantity.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You haven't entered a code or quantity",
                            Toast.LENGTH_SHORT).show();

                } else if (_quantity.isEmpty() || _quantity == "0") {
                    Toast.makeText(getApplicationContext(), "Quantity should not be empty or 0",
                            Toast.LENGTH_SHORT).show();
                } else if (_code.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter code", Toast.LENGTH_SHORT).show();

                } else {
                    codeLayout.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.GONE);

                    extra_info.setVisibility(View.GONE);
                    back_button.setVisibility(View.VISIBLE);
                    extraLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraLayout.setVisibility(View.GONE);
                codeLayout.setVisibility(View.VISIBLE);
                quantityLayout.setVisibility(View.VISIBLE);
                back_button.setVisibility(View.GONE);
                extra_info.setVisibility(View.VISIBLE);


            }
        });


        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderToCart();

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    public void addOrderToCart() {
        merchantUidRef = FirebaseDatabase.getInstance().getReference().child("merchants_list");


        merchant_uid = null;
        pdt_photo = null;



        _code = codeEditText.getText().toString().toLowerCase();
        _quantity = quantityEditText.getText().toString();
        _size = sizeEditText.getText().toString();
        _color = itemColorEditText.getText().toString();
        _extraInfo = extraLayout.getEditText().getText().toString();


        if (_code.isEmpty() && _quantity.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You haven't entered a code or quantity", Toast.LENGTH_SHORT).show();

        } else if (_code.isEmpty()) {

            Toast.makeText(getApplicationContext(), "You haven't entered code", Toast.LENGTH_SHORT).show();


        } else if (_quantity.isEmpty() || Integer.valueOf(_quantity) == 0) {

            Toast.makeText(getApplicationContext(), "Quantity should not be empty, 0 or a float value", Toast.LENGTH_SHORT).show();


        } else {
            order_layout.setVisibility(View.GONE);
            showing = true;
            hideSoftKeyboard();
            ////////////////////Get Data, Add to OrderTile.
            Toast.makeText(getApplicationContext(), "Fetching order with code: " + _code,
                    Toast.LENGTH_LONG).show();


            String name_prefix = _code.substring(0, Math.min(_code.length(), 4));


            merchantUidRef.child(name_prefix).child("uid").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            merchant_uid = String.valueOf(dataSnapshot.getValue());

                            if (merchant_uid != "null") {
                                //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_LONG).show();


                                DatabaseReference orderItemRef = FirebaseDatabase.getInstance()
                                        .getReference().child("merchants").child(merchant_uid).child("products");

                                orderItemRef.child(_code).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                pdt_name = String.valueOf(dataSnapshot.child("name").getValue());
                                                pdt_price = String.valueOf(dataSnapshot.child("price").getValue());
                                                pdt_photo = String.valueOf(dataSnapshot.child("photo_id").getValue());

                                                if (pdt_name != "null" && pdt_price != "null") {

                                                            int lastItem = OrdersFragment.orderTile.size();

                                                            dataToAdd = new OrderTile(pdt_name, _code, _quantity, pdt_price, _size, _color,
                                                                    _extraInfo, pdt_photo, merchant_uid, DateUtil.getDateInMillisToString());

                                                            ordersDataBaseHelper.addOrdertile(dataToAdd);

                                                            if(OrdersFragment.adapter != null){
                                                                OrdersFragment.emptyView.setVisibility(View.GONE);
                                                                OrdersFragment.orderTile.add(lastItem, dataToAdd);
                                                                OrdersFragment.adapter.notifyItemInserted(lastItem);
                                                                OrdersFragment.adapter.notifyDataSetChanged();
                                                            }

                                                            Toast.makeText(getApplicationContext(), "Order added.",
                                                                    Toast.LENGTH_SHORT).show();

                                                        }


                                                 else{

                                                    Toast.makeText(getApplicationContext(), "Oops! Order missing.",
                                                            Toast.LENGTH_SHORT).show();
                                                     }


                                                initializeFloatingLayout();


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(), (CharSequence) databaseError.toException(),
                                                        Toast.LENGTH_SHORT).show();
                                                initializeFloatingLayout();
                                            }
                                        }
                                );
                            } else {

                                Toast.makeText(getApplicationContext(), "Uh-oh, Store not found.",
                                        Toast.LENGTH_SHORT).show();
                                initializeFloatingLayout();

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Activity, ", "getUser:onCancelled", databaseError.toException());
                            Toast.makeText(getApplicationContext(), (CharSequence) databaseError.toException(),
                                    Toast.LENGTH_SHORT).show();
                            initializeFloatingLayout();

                            // ...
                        }
                    });


        }


    }


    ///////////////////////////////////////
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) floatingOrdView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Log.d(Utils.LogTag, "ChatHeadService.onConfigurationChanged -> landscap");

            if (order_layout != null) {
                order_layout.setVisibility(View.GONE);
            }

            if (layoutParams.y + (floatingOrdView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (floatingOrdView.getHeight() + getStatusBarHeight());
                windowManager.updateViewLayout(floatingOrdView, layoutParams);
            }

            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Log.d(Utils.LogTag, "ChatHeadService.onConfigurationChanged -> portrait");

            if (order_layout != null) {
                order_layout.setVisibility(View.GONE);
            }

            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x);
            }

        }

    }

    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);

        } else {
            isLeft = false;
            moveToRight(x_cord_now);

        }

    }

    private void moveToLeft(final int x_cord_now) {
        final int x = szWindow.x - x_cord_now;

        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) floatingOrdView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                windowManager.updateViewLayout(floatingOrdView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;
                windowManager.updateViewLayout(floatingOrdView, mParams);
            }
        }.start();
    }

    private void moveToRight(final int x_cord_now) {
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) floatingOrdView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - floatingOrdView.getWidth();
                windowManager.updateViewLayout(floatingOrdView, mParams);
            }

            public void onFinish() {
                mParams.x = szWindow.x - floatingOrdView.getWidth();
                windowManager.updateViewLayout(floatingOrdView, mParams);
            }
        }.start();
    }

    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    //    Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
    private void floatingOrdOnClick() {
        if (showing) {
            //order_layout.startAnimation(animation);
            order_layout.setVisibility(View.VISIBLE);
            showSoftKeyboard();
            showing = false;

        } else {

            order_layout.setVisibility(View.GONE);
            initializeFloatingLayout();
            hideSoftKeyboard();
            showing = true;
        }

    }

    private void floatingOrdOnLongClick() {
        // Log.d(Utils.LogTag, "Into ChatHeadService.floatingOrdOnLongClick() ");

        WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
        int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
        int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

        param_remove.x = x_cord_remove;
        param_remove.y = y_cord_remove;

        windowManager.updateViewLayout(removeView, param_remove);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        //Log.d(Utils.LogTag, "ChatHeadService.onStartCommand() -> startId=" + startId);

        if (!mRunning) {
            mRunning = true;


            handleStart();


        }
        return Service.START_NOT_STICKY;


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mRunning = false;

        if (floatingOrdView != null) {
            windowManager.removeView(floatingOrdView);

        }

        if (order_layout != null) {
            windowManager.removeView(order_layout);
        }

        if (removeView != null) {
            windowManager.removeView(removeView);
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        //Log.d(Utils.LogTag, "ChatHeadService.onBind()");
        return null;
    }

    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }


    public void initializeFloatingLayout() {
        codeLayout.setVisibility(View.VISIBLE);
        quantityLayout.setVisibility(View.VISIBLE);

        back_button.setVisibility(View.GONE);
        extra_info.setVisibility(View.VISIBLE);
        extraLayout.setVisibility(View.GONE);

        codeEditText.getText().clear();
        quantityEditText.getText().clear();
        extraLayout.getEditText().getText().clear();

    }

}