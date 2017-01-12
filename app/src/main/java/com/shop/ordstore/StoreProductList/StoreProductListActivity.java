package com.shop.ordstore.storeProductList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.ordstore.R;
import com.shop.ordstore.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 8/10/16.
 */


public class StoreProductListActivity extends AppCompatActivity  {

    String pdt_code, pdt_name, pdt_price, pdt_photo, pdt_details;

    public List<Product> productList = new ArrayList<>();
    public StoreProductsListAdapter mAdapter;
    RecyclerView mRecycler;
    RelativeLayout emptyView_nosignal_layout;
    String get_store_name;
    static String get_merchant_uid;
    String get_store_logo;
    private GridLayoutManager productsGridLayoutManager;


    private DatabaseReference merchantsDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_products_list_activity);


        Intent intent = getIntent();

        get_store_name = intent.getStringExtra("store_name");
        get_store_logo = intent.getStringExtra("store_photoID");
        get_merchant_uid = intent.getStringExtra("merchant_uid");

        merchantsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("merchants");

        TextView store_name_textView = (TextView) findViewById(R.id.store_name);
        ImageView image_view = (ImageView) findViewById(R.id.store_photoID);

        store_name_textView.setText(get_store_name);
        Picasso.with(getApplicationContext()).load(get_store_logo).into(image_view);



        emptyView_nosignal_layout = (RelativeLayout) findViewById(R.id.empty_view_placeHolder_nosignal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initCollapsingToolbar();




        mRecycler = (RecyclerView) findViewById(R.id.products_recyclerView);
        mRecycler.setHasFixedSize(true);

        //mRecycler.setItemAnimator(new RecyclerItemViewAnimator());

        productsGridLayoutManager = new GridLayoutManager(this, 3);
        mRecycler.setLayoutManager(productsGridLayoutManager);


        SpacesGridItemDecoration decoration = new SpacesGridItemDecoration(8);
        mRecycler.addItemDecoration(decoration);


        if(productList != null){
            productList.clear();
        }


        merchantsDatabaseRef.child(get_merchant_uid).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Progress show


                for(final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Get product code
                    pdt_code = snapshot.getKey();

                    //Get product name/price and details
                    if(pdt_code != "null"){

                        pdt_name = (String) snapshot.child("name").getValue();
                        pdt_price = (String) snapshot.child("price").getValue();
                        pdt_details = (String) snapshot.child("details").getValue();
                        pdt_photo = (String) snapshot.child("photo_id").getValue();


                        Product product;
                        product = new Product();
                        product.setItemCode(pdt_code);
                        product.setProductName(pdt_name);
                        product.setPrice(pdt_price);
                        product.setDetails(pdt_details);
                        product.setPhotoId(pdt_photo);

                        productList.add(product);


                        Log.i("creds ", pdt_code + " " + pdt_name + " "
                                + pdt_price + " " + pdt_details +" "+ pdt_photo);

                    }





                }
                //Dismiss progress dialog




                setAdapter();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Progress dismiss


                final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(StoreProductListActivity.this, R.style.DialogTheme)
                        .setTitle("No connection!")
                        .setMessage("Oops! something went wrong. Try again later")
                        .setCancelable(false)
                        .setIcon(R.drawable.attention_96)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                                emptyView_nosignal_layout.setVisibility(View.VISIBLE);
                                finish();

                            }
                        });
                AlertDialog logout_dialog = logout_alert_builder.create();
                logout_dialog.show();


            }
        });




    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                //super.onBackPressed();
                supportFinishAfterTransition();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(get_store_name);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.productList_appbar);
        appBarLayout.setExpanded(true);


        // hiding & showing the title when toolbar expanded & collapsed
      /*  appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(get_store_name);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });*/
    }




    public void setAdapter(){
        emptyView_nosignal_layout.setVisibility(View.GONE);

        mAdapter = new StoreProductsListAdapter(StoreProductListActivity.this, productList);
        mRecycler.setAdapter(mAdapter);

        startContentAnimation();
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.notifyDataSetChanged();



    }



    StoreProductsListAdapter.OnItemClickListener onItemClickListener = new StoreProductsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            //Get item at position
            //Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();

            Product _product = productList.get(position);
            Intent intent = new Intent(StoreProductListActivity.this, ExpandedView.class);

            intent.putExtra("product_name", _product.getProductName());
            intent.putExtra("product_price", _product.getPrice());
            intent.putExtra("product_code", _product.getItemCode());
            intent.putExtra("product_photo", _product.getPhotoId());
            intent.putExtra("product_details", _product.getDetails());

            ImageView productPhoto = (ImageView) view.findViewById(R.id.product_photo);
            TextView productCode = (TextView) view.findViewById(R.id.pdtCode);


            Pair<View, String> imagePair = Pair.create((View) productPhoto, "productPhotoExpand");
            //Pair<View, String> namePair = Pair.create((View) productCode, "productNameExpand");
// 3
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(StoreProductListActivity.this,
                    imagePair);

            ActivityCompat.startActivity(StoreProductListActivity.this, intent, options.toBundle());








        }
    };


    private void startContentAnimation() {
        final int screenHeight = Utils.getScreenHeight(getApplicationContext());

        mRecycler.setTranslationY( screenHeight );
        mRecycler.animate()
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                //.setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(700)
                .start();
    }



/*
    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {

        final View view = rootView.findViewById(R.id.reveal_view);

        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if(reveal){
            Animator revealAnimator = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                        w / 2, h / 2, 0, maxRadius);
            }
            view.setVisibility(View.VISIBLE);
            revealAnimator.start();
        } else {
            Animator anim = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);
            }

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();
        }
    }
*/

    public static String merchantID(){
        String uid = get_merchant_uid;
        return uid;
    }



    public class SpacesGridItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesGridItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {



            outRect.bottom = mSpace;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1 || parent.getChildAdapterPosition(view) == 2) {
                outRect.top = mSpace * 2;
            }else{

                outRect.top = mSpace;

            }

            if(parent.getChildAdapterPosition(view) % 3 == 0){
                outRect.right = mSpace * 2;
                outRect.left = mSpace;

            }else if(parent.getChildAdapterPosition(view) % 3 == 2){
                outRect.left = mSpace * 2;
                outRect.right = mSpace;

            }

        }

    }


}
