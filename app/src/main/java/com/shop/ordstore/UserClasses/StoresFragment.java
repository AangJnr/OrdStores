package com.shop.ordstore.UserClasses;

/**
 * Created by AangJnr on 4/7/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.ordstore.R;
import com.shop.ordstore.StoreProductList.StoreProductListActivity;
import com.shop.ordstore.DatabaseHelper;

import java.util.List;


public class StoresFragment extends Fragment {
    public static StoresFragmentAdapter adapter;
    public static RecyclerView grid_recycler;
    public static List<StoreTile> storetile;
    static RelativeLayout emptyView;
    public View grid_rootView;
    DatabaseHelper storesDbHelper;
    Animation fadeIn;



    StoresFragmentAdapter.OnItemClickListener onItemClickListener = new StoresFragmentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            // Toast.makeText(getActivity(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
            StoreTile store = storetile.get(position);

            Intent intent = new Intent(getActivity(), StoreProductListActivity.class);
            // intent.putExtra(StoresFragment.EXTRA_PARAM_ID, position);

            intent.putExtra("store_name", store.getStoreName());
            intent.putExtra("store_photoID", store.getLogo());
            intent.putExtra("merchant_uid", store.getMerchantUid());
            //startActivity(intent);


            ImageView storeImage = (ImageView) view.findViewById(R.id.store_photo);

// 2
            //Pair<View, String> imagePair = Pair.create((View) storeImage, "tImage");
// 3
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    storeImage, "tImage");

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());


        }
    };



    private GridLayoutManager storesGridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storesDbHelper = new DatabaseHelper(getActivity());
        grid_rootView = inflater.inflate(R.layout.fragment_stores, container, false);

        fadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
        emptyView = (RelativeLayout) grid_rootView.findViewById(R.id.empty_view_placeHolder);
        grid_recycler = (RecyclerView) grid_rootView.findViewById(R.id.fragment_stores_recyclerview);
        grid_recycler.setHasFixedSize(true);

        storesGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        grid_recycler.setLayoutManager(storesGridLayoutManager);

        storetile = storesDbHelper.getAllStores();


        if (storetile == null) {
            emptyView.setVisibility(View.VISIBLE);


        } else {


            if (storetile.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);

            }


            if (storetile != null) {
                emptyView.setVisibility(View.GONE);
                adapter = new StoresFragmentAdapter(getActivity().getApplicationContext(), storetile);
                grid_recycler.setAdapter(adapter);

            }


        }

        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(onItemClickListener);


        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(grid_recycler);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(300);
        itemAnimator.setRemoveDuration(300);
        grid_recycler.setItemAnimator(itemAnimator);


        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        grid_recycler.addItemDecoration(decoration);

        return grid_rootView;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onCreate(savedInstanceState);

        /*float offsetPx = getResources().getDimension(R.dimen.recycler_bottom_space);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        grid_recycler.addItemDecoration(bottomOffsetDecoration);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (storetile.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);

        } else {
            emptyView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

        }


    }


}