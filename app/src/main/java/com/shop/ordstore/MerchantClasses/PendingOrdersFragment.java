package com.shop.ordstore.merchantClasses;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/14/16.
 */
public class PendingOrdersFragment extends Fragment {
    View rootView;
    RecyclerView recycler;
    static RelativeLayout emptyView;
    static List<PendingOrder> pending_orders = new ArrayList<>();
    static PendingOrdersAdapter adapter;
    DatabaseHelper pendingOrdersDataBaseHelper;


    public PendingOrdersFragment() {
        super();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pending_orders_fragment_layout, container, false);

        pendingOrdersDataBaseHelper = new DatabaseHelper(getActivity());

        pending_orders = pendingOrdersDataBaseHelper.getAllPendingOrders();


        recycler = (RecyclerView) rootView.findViewById(R.id.po_recyclerview);
        emptyView = (RelativeLayout) rootView.findViewById(R.id.po_empty_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        //recycler.setItemAnimator(new RecyclerItemViewAnimator());




        if (pending_orders == null) {
            emptyView.setVisibility(View.VISIBLE);


        } else {


            if (pending_orders.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);

            }


            if (pending_orders != null) {
                emptyView.setVisibility(View.GONE);
                adapter = new PendingOrdersAdapter(getActivity(), pending_orders);
                recycler.setAdapter(adapter);

            }
        }

        adapter.notifyDataSetChanged();


        initSwipe();

        RecyclerSpacesItemDecoration decoration = new RecyclerSpacesItemDecoration(16);
        recycler.addItemDecoration(decoration);


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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


    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final PendingOrder p_order = pending_orders.get(viewHolder.getAdapterPosition());
                onItemRemove(viewHolder, recycler);


            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos,
                                RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }






    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {


        final int adapterPosition = viewHolder.getAdapterPosition();
        final PendingOrder order = pending_orders.get(adapterPosition);

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.merchant_cord_lay), "ORDER REMOVED", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pending_orders.add(adapterPosition, order);
                        pendingOrdersDataBaseHelper.addPendingOrder(order);

                        adapter.notifyItemInserted(adapterPosition);
                        recyclerView.scrollToPosition(adapterPosition);

                    }
                });
        snackbar.show();
        pending_orders.remove(adapterPosition);

        pendingOrdersDataBaseHelper.deletePendingOrder(order.getUserTimestamp());

        adapter.notifyItemRemoved(adapterPosition);
        recyclerView.scrollToPosition(adapterPosition);

        if (pending_orders.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);

        }

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


    @Override
    public void onResume() {
        super.onResume();
        if (pending_orders.size() == 0) {


            emptyView.setVisibility(View.VISIBLE);

        } else {
            emptyView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }


    }

}
