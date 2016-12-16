package com.shop.ordstore.userClasses;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by AangJnr on 5/19/16.
 */
public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    private final StoresFragmentAdapter mAdapter;


    public SimpleItemTouchHelper(StoresFragmentAdapter adapter) {
        mAdapter = adapter;


    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        OrdersFragment.adapter.notifyDataSetChanged();

        if (StoresFragment.storetile.size() == 0) {


            StoresFragment.emptyView.setVisibility(View.VISIBLE);

        } else {
            StoresFragment.emptyView.setVisibility(View.GONE);
        }


    }

}