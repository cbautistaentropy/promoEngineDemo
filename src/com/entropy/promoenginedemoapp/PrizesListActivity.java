package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.entropy.promoenginedemoapp.adapter.PrizesListAdapter;

public class PrizesListActivity extends BaseActivity {
	
	private TextView tvPromoName;
	private HorizontalListView items;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_item);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Redemption");
		tvActionNext.setVisibility(View.INVISIBLE);
		tvPromoName = (TextView) findViewById(R.id.tvPromoName);
		
		tvPromoName.setText(ScanQRActivity.promoFound.getName());
		items = (HorizontalListView) findViewById(R.id.lvItems);
		items.setAdapter(new PrizesListAdapter(getApplicationContext(), ScanQRActivity.prizesFound, PrizesListActivity.this));
	}
}
