package com.shop.ordstore.merchantClasses;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.ordstore.R;
import com.shop.ordstore.storeProductList.Product;
import com.shop.ordstore.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/14/16.
 */
public class ProductsListFragment extends Fragment {

    public List<Product> productList = new ArrayList<>();
    public MerchantProductListAdapter mAdapter;
    RecyclerView mRecycler;
    private GridLayoutManager productsGridLayoutManager;
    private DatabaseReference merchantsDatabaseRef;
    View rootView;
    RelativeLayout emptyView;
    FloatingActionButton fab;

    String pdt_code, pdt_name, pdt_price, pdt_photo, pdt_details;


    public ProductsListFragment() {
        super();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.merchant_product_list_fragment, container, false);
        merchantsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("merchants");



        mRecycler = (RecyclerView) rootView.findViewById(R.id.merchant_productlist_recyclerview);
        emptyView = (RelativeLayout) rootView.findViewById(R.id.merchant_empty_view_placeHolder);


        //fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_main);

        //mRecycler.setItemAnimator(new RecyclerItemViewAnimator());
        productsGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecycler.setLayoutManager(productsGridLayoutManager);
        mRecycler.setHasFixedSize(true);

        SpacesGridItemDecoration decoration = new SpacesGridItemDecoration(8);
        mRecycler.addItemDecoration(decoration);









        if(productList != null){
            productList.clear();
        }





        merchantsDatabaseRef.child(MerchantMainActivity.getUid()).child("products").addValueEventListener(new ValueEventListener() {
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


                    }





                }
                //Dismiss progress dialog




                setAdapter();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Progress dismiss




            }
        });


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*recycler.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(getResources().getColor(R.color.divider))
                        .sizeResId(R.dimen.divider)
                        .build());*/


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // Always call the superclass so it can save the view hierarchy state
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }






    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();


    }



    public void setAdapter(){
        emptyView.setVisibility(View.GONE);
        mAdapter = new MerchantProductListAdapter(getActivity(), productList);
        mRecycler.setAdapter(mAdapter);
        startContentAnimation();
        mAdapter.notifyDataSetChanged();



    }



    private void startContentAnimation() {
        final int screenHeight = Utils.getScreenHeight(getContext());

        mRecycler.setTranslationY( screenHeight );
        mRecycler.animate()
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                //.setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(700)
                .start();
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
