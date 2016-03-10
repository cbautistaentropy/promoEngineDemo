package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;
import com.entropy.promoenginedemoapp.adapter.NameListAdapter;


public class BranchListActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);

		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Branches");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		final ListView listview = (ListView) findViewById(R.id.myList);
		if (listview.getAdapter() == null) {
			NameListAdapter adap = new NameListAdapter(getApplicationContext(), InRangeAdapter.listBranches);
			listview.setAdapter(adap);
		} else {
		    ((NameListAdapter)listview.getAdapter()).updateAdapter(InRangeAdapter.listBranches);
		}
	}
}
