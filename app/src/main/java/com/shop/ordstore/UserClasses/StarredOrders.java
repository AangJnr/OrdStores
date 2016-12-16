package com.shop.ordstore.userClasses;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.ordstore.R;
import com.shop.ordstore.sharedPreferences.SharedPreference;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by AangJnr on 7/30/16.
 */


public class StarredOrders extends AppCompatActivity {
    public static DatabaseHelper ordersDataBaseHelper;
    public static RelativeLayout emptyView;
    List<OrderTile> orderTile;
    RecyclerView recycler;
    SharedPreference sharedPreference;
    FloatingActionButton fab_delete;
    private OrdersCardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.starred_orders_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(7);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);



        emptyView = (RelativeLayout) findViewById(R.id.empty_view_placeHolder);

        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        recycler = (RecyclerView) findViewById(R.id.starred_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        /*RecyclerSpacesItemDecoration decoration = new RecyclerSpacesItemDecoration(32);
        recycler.addItemDecoration(decoration);*/
        ordersDataBaseHelper = new DatabaseHelper(this);

        orderTile = ordersDataBaseHelper.getAllStarredOrders();


        if (orderTile == null) {

            emptyView.setVisibility(View.VISIBLE);

        } else {


            if (orderTile.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);

            }


            if (orderTile != null) {

                mAdapter = new OrdersCardAdapter(this, orderTile);
                recycler.setAdapter(mAdapter);

                /*recycler.addItemDecoration(
                        new HorizontalDividerItemDecoration.Builder(StarredOrders.this)
                                .color(ContextCompat.getColor(StarredOrders.this, R.color.divider))
                                .sizeResId(R.dimen.divider)
                                .marginResId(R.dimen.divider_left_margin, R.dimen.divider_right_margin)
                                .build());
*/
                initSwipe();


                if (orderTile.size() > 0) {


                    fab_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                                /*for (int i = 0; i <= orderTile.size(); i++) {
                                    orderTile.remove(i);
                                    mAdapter.notifyItemRemoved(i);
                                }*/

                            orderTile.clear();
                            emptyView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                            ordersDataBaseHelper.deleteAllStarredOrders();


                            OrdersFragment.adapter.notifyDataSetChanged();

                            CoordinatorLayout starred_cordLayout = (CoordinatorLayout) findViewById(R.id.starred_orders_cordinator_layout);
                            Snackbar snackbar = Snackbar
                                    .make(starred_cordLayout, "Favorites Deleted", Snackbar.LENGTH_LONG);

                            snackbar.show();

                        }
                    });
                }


            }


        }


    }


    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {


                final int adapterPosition = viewHolder.getAdapterPosition();
                final OrderTile checkOrder = orderTile.get(adapterPosition);


                //onItemRemove(viewHolder, recycler);
                orderTile.remove(checkOrder);

                ordersDataBaseHelper.deleteStarredOrder(checkOrder.getItemCode());
                Toast.makeText(StarredOrders.this, "Order deleted", Toast.LENGTH_SHORT).show();

                mAdapter.notifyItemRemoved(adapterPosition);

                if (orderTile.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (orderTile.size() == 0) {


            emptyView.setVisibility(View.VISIBLE);

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

}
