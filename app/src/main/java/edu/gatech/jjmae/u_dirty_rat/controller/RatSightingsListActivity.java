package edu.gatech.jjmae.u_dirty_rat.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.gatech.jjmae.u_dirty_rat.R;
import edu.gatech.jjmae.u_dirty_rat.model.RatSightingDataItem;
import edu.gatech.jjmae.u_dirty_rat.model.SampleModel;

/**
 * displays rat data in a list
 */
public class RatSightingsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_sighting_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), NewRatSightingActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        View recyclerView = findViewById(R.id.dataitem_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /**
     * sets up the recycler view
     * @param recyclerView returns the recycler view that's set up
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<RatSightingDataItem> items = SampleModel.INSTANCE.getItems();
        Collections.sort(items);
        Collections.reverse(items);
        recyclerView.setAdapter(new SampleItemRecyclerViewAdapter(items));
    }

    /**
     * Class for the Recycler view adapter that displays rat data
     */
    public class SampleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SampleItemRecyclerViewAdapter.ViewHolder> {

        private final List<RatSightingDataItem> mValues;

        /**
         * constructor to create new recycler view
         * @param items rat data to be displayed in the list
         */
        public SampleItemRecyclerViewAdapter(List<RatSightingDataItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rat_item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            String date = holder.mItem.get_Date().toString();
            // the date string sometimes shows up as GMT which adds extra characters so taking that
            // into account here
            final int startIndexGMT = 30;
            final int endIndexGMT = 34;
            final int normStart = 24;
            final int normEnd = 28;

            try {
                holder.mDateView.setText(date.substring(0, 10) + " " + date.substring(startIndexGMT,
                        endIndexGMT));
            } catch (IndexOutOfBoundsException e) {
                holder.mDateView.setText(date.substring(0, 10) + " " + date.substring(normStart,
                        normEnd));
            }

            holder.mBoroughView.setText(holder.mItem.get_Borough());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RatSightingViewDetailActivity.class);
                        Log.d("MY APP", "Switch to detailed view for item: " +
                                holder.mItem.get_ID());
                        intent.putExtra(RatSightingDetailFragment.ARG_ITEM_ID,
                                holder.mItem.get_ID());

                        context.startActivity(intent);
                    }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * View Holder class to display the recycler view
         */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mDateView;
        private final TextView mBoroughView;
        private RatSightingDataItem mItem;

            /**
             * constructor that takes in a view for the view holder
             * @param view view used for view holder
             */

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = view.findViewById(R.id.date);
            mBoroughView = view.findViewById(R.id.borough);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'" +  mBoroughView.getText()
                    + " '";
        }
    }
    }


}
