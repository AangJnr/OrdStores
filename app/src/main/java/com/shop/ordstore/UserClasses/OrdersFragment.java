package com.shop.ordstore.userClasses;

/**
 * Created by AangJnr on 4/7/16.
 */

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.shop.ordstore.utilities.RecyclerItemViewAnimator;
import com.shop.ordstore.R;
import com.shop.ordstore.utilities.DatabaseHelper;

import java.util.List;



public class OrdersFragment extends Fragment {
    public static List<OrderTile> orderTile;
    DatabaseHelper ordersDataBaseHelper;
    static RelativeLayout emptyView;
    public static OrdersCardAdapter adapter;

    RecyclerView recycler;
    View rootView;
    FloatingActionButton fab;


    public OrdersFragment() {
        super();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersDataBaseHelper = new DatabaseHelper(getActivity());


        orderTile = ordersDataBaseHelper.getAllOrders();


        recycler = (RecyclerView) rootView.findViewById(R.id.fragment_orders_recyclerview);
        emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_view_placeHolder);


        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_main);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new RecyclerItemViewAnimator());









        if (orderTile == null) {
            emptyView.setVisibility(View.VISIBLE);


        } else {


            if (orderTile.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);

            }


            if (orderTile != null) {
                emptyView.setVisibility(View.GONE);
                adapter = new OrdersCardAdapter(getActivity().getApplicationContext(), orderTile);

                recycler.setAdapter(adapter);
                startContentAnimation();

                        recycler.smoothScrollToPosition(0);
                        adapter.notifyDataSetChanged();

            }
        }


        RecyclerSpacesItemDecoration decoration = new RecyclerSpacesItemDecoration(32);
        //recycler.addItemDecoration(decoration);


        /*recycler.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(ContextCompat.getColor(getActivity(), R.color.divider))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.divider_left_margin, R.dimen.divider_right_margin)
                        .build());*/

        initSwipe();


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {// Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    public class RecyclerSpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public RecyclerSpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            outRect.right = mSpace;
            outRect.left = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;


        }

    }


    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final OrderTile order = orderTile.get(viewHolder.getAdapterPosition());
                onItemRemove(viewHolder, recycler);


            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {


        final int adapterPosition = viewHolder.getAdapterPosition();
        final OrderTile order = orderTile.get(adapterPosition);

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.cord_lay), "ORDER REMOVED", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderTile.add(adapterPosition, order);
                        ordersDataBaseHelper.addOrdertile(order);

                        adapter.notifyItemInserted(adapterPosition);
                        recyclerView.smoothScrollToPosition(adapterPosition);
                        //ordertile.remove(order);
                    }
                });
        snackbar.show();
        orderTile.remove(adapterPosition);
        ordersDataBaseHelper.deleteOrder(order.getTimestamp());
        adapter.notifyItemRemoved(adapterPosition);
        recyclerView.smoothScrollToPosition(adapterPosition);
        //adapter.notifyItemRangeChanged(adapterPosition, adapter.getItemCount());

        if (orderTile.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (orderTile.size() == 0) {


            emptyView.setVisibility(View.VISIBLE);

        } else {
            emptyView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }


    }








    private void startContentAnimation() {
        recycler.setAlpha(0f);
        recycler.animate()
                .setStartDelay(500)
                .setDuration(700)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }




}



