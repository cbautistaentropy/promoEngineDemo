package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.entropy.promoenginedemoapp.adapter.RedeemedItemsListAdapter;
import com.entropy.promoenginedemoapp.adapter.SubscriptionAdapter;

public class RedeemedItemListActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);

		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Redeemed Items");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		final ListView listview = (ListView) findViewById(R.id.myList);
		listview.setAdapter(new RedeemedItemsListAdapter(RedeemedItemListActivity.this, getIntent().getExtras().getString("id"), SubscriptionAdapter.redeemedItems));
//		if (listview.getAdapter() == null) {
//			listview.setAdapter(new RedeemedItemsListAdapter(RedeemedItemListActivity.this, getIntent().getExtras().getString("id"), SubscriptionAdapter.redeemedItems));
//    	} else {
//			 ((RedeemedItemsListAdapter)listview.getAdapter()).updateAdapter(SubscriptionAdapter.redeemedItems);
//		}
	}
}
