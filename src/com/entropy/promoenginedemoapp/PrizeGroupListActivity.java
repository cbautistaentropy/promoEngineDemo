package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;
import com.entropy.promoenginedemoapp.adapter.PrizeGroupAdapter;


public class PrizeGroupListActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);

		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Prize Groups");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		final ListView listview = (ListView) findViewById(R.id.myList);
		if (listview.getAdapter() == null) {
			PrizeGroupAdapter adap2 = new PrizeGroupAdapter(PrizeGroupListActivity.this, InRangeAdapter.listPrizeGroups);
			listview.setAdapter(adap2);
		} else {
		    ((PrizeGroupAdapter)listview.getAdapter()).updateAdapter(InRangeAdapter.listPrizeGroups);
		}
	}
}
