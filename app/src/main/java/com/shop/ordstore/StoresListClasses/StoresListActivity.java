package com.shop.ordstore.storesListClasses;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.R;
import com.shop.ordstore.userClasses.SpacesItemDecoration;
import com.shop.ordstore.userClasses.StoreTile;
import com.shop.ordstore.userClasses.StoresFragment;
import java.util.ArrayList;
import java.util.List;

public class StoresListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public List<StoreTile> storesListItems = new ArrayList<>();
    DatabaseHelper storesDbHelper;
    String store_name, store_logo, merchant_uid;
    RelativeLayout emptyView_nosignal_layout;
    private RecyclerView mRecycler;
    private StoresListViewAdapter mAdapter;
    String _name, _logo;





    private DatabaseReference merchantsDatabaseRef;


    String merchant_Uid;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private boolean isListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        merchantsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("merchants");

        setContentView(R.layout.activity_stores_list);


        emptyView_nosignal_layout = (RelativeLayout) findViewById(R.id.empty_view_placeHolder_nosignal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stores List");
        toolbar.setFitsSystemWindows(true);
        setSupportActionBar(toolbar);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecycler = (RecyclerView) findViewById(R.id.listview);

        mRecycler.setLayoutManager(mStaggeredLayoutManager);
        mRecycler.setHasFixedSize(true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        mRecycler.addItemDecoration(decoration);

        storesDbHelper = new DatabaseHelper(this);
        isListView = true;

        if(storesListItems != null){
            storesListItems.clear();
        }




        merchantsDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Show progress here



                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    merchant_Uid = snapshot.getKey();

                if(merchant_Uid != null || merchant_Uid != "null") {

                    _name = (String) snapshot.child("name").getValue();
                    _logo = (String) snapshot.child("logo").getValue();

                    StoreTile item;
                    item = new StoreTile();

                    item.setMerchantUid(merchant_Uid);
                    item.setStoreName(_name);
                    item.setLogo(_logo);

                    storesListItems.add(item);


                }else{

                    Toast.makeText(StoresListActivity.this, "Uh-oh! Store is missing",
                            Toast.LENGTH_SHORT).show();
                }

        }

                setAdapter();


}



            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(StoresListActivity.this, "Oops! something went wrong. Try again later",
                        Toast.LENGTH_SHORT).show();

//                    Show dialog here//

                if (getApplicationContext() != null) {


                    final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(StoresListActivity.this)
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

           }



        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, StoresListActivity.class)));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle) {
            if (isListView) {
                mStaggeredLayoutManager.setSpanCount(2);
                item.setIcon(R.drawable.ic_view_list_white_24dp);
                item.setTitle("Show as list");
                isListView = false;
            } else {
                mStaggeredLayoutManager.setSpanCount(1);
                item.setIcon(R.drawable.ic_grid_on_white_24dp);
                item.setTitle("Show as grid");
                isListView = true;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: " + uri, Toast.LENGTH_SHORT).show();
        }
    }

    StoresListViewAdapter.OnItemClickListener onItemClickListener = new StoresListViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            //Get item at position
            StoreTile item = storesListItems.get(position);


            final int lastItem = StoresFragment.storetile.size();



            merchant_uid = item.getMerchantUid();
            store_name = item.getStoreName();
            store_logo = item.getLogo();

            if (storesDbHelper.storeExists(store_name)) {

                storesDbHelper.deleteStore(store_name);
                Toast.makeText(getApplicationContext(), "Store removed", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();

                if (StoresFragment.storetile != null) {

                    for (StoreTile store : StoresFragment.storetile) {
                        if (store.getStoreName() != null && store.getStoreName() == store_name) {
                            StoresFragment.storetile.remove(store);
                            StoresFragment.adapter.notifyDataSetChanged();

                        }


                    }

                }

            } else {

                if (storesDbHelper.addStore(store_name, store_logo, merchant_uid)) {
                    Toast.makeText(getApplicationContext(), "Store Added", Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();

                    StoreTile dataToAdd = new StoreTile(store_name, store_logo, merchant_uid);
                    StoresFragment.storetile.add(lastItem, dataToAdd);
                    StoresFragment.adapter.notifyDataSetChanged();


                }
            }


        }
    };

@Override
public void onBackPressed(){
    super.onBackPressed();
}

    public void setAdapter(){

        emptyView_nosignal_layout.setVisibility(View.GONE);
        mAdapter = new StoresListViewAdapter(StoresListActivity.this, storesListItems);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onResume(){
        super.onResume();

    }


}





