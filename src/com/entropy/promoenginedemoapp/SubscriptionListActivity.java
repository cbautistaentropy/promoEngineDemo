package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.entropy.promoenginedemoapp.adapter.SubscriptionAdapter;

public class SubscriptionListActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);

		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Subscription");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		ListView listview = (ListView) findViewById(R.id.myList);
		if (listview.getAdapter() == null) {
			SubscriptionAdapter subsAdap = new SubscriptionAdapter(SubscriptionListActivity.this, hypeSDK.getAllSubscriptionByPromoId(PromoDetailsActivity.promo.getId()));
			listview.setAdapter(subsAdap);
		} else {
		    ((SubscriptionAdapter)listview.getAdapter()).updateAdapter(hypeSDK.getAllSubscriptionByPromoId(PromoDetailsActivity.promo.getId()));
		}
	}
}
