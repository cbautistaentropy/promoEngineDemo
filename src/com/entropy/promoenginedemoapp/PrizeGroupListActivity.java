package com.entropy.promoenginedemoapp;

import com.entropy.promoenginedemoapp.adapter.ItemListAdapter;
import com.entropy.promoenginedemoapp.adapter.PrizeGroupAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class PrizeGroupListActivity extends BaseActivity {
	
	private TextView tvName;
	private TextView tvCount;
	private ListView lvItems;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize_group);
		
		mTitleTextView.setText(getIntent().getExtras().getString("name"));
		tvActionNext.setVisibility(View.INVISIBLE);
		imageButton.setVisibility(View.INVISIBLE);
		
		lvItems = (ListView) findViewById(R.id.lvItems);
		tvName = (TextView) findViewById(R.id.tvNameValue);
		tvCount = (TextView) findViewById(R.id.tvCountValue);
		
		tvCount.setText(getIntent().getExtras().getString("redemptionCount"));
		tvName.setText(getIntent().getExtras().getString("name"));
		
		if (lvItems.getAdapter() == null) {
			lvItems.setAdapter(new ItemListAdapter(getApplicationContext(), PrizeGroupAdapter.items, getIntent().getExtras().getString("id")));
    	} else {
			 ((ItemListAdapter)lvItems.getAdapter()).updateAdapter(PrizeGroupAdapter.items);
		}
	}
}
