package com.entropy.promoenginedemoapp;

import java.text.ParseException;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypePromo;
import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;

public class SubscriptionActivity extends BaseActivity {
	
	private LinearLayout llPromo;
	private LinearLayout llCode;
	private LinearLayout llRedemptionDate;
	private LinearLayout llRedeemedItem;
	private LinearLayout llBranch;
	private LinearLayout llSubsDate;
	private LinearLayout llData;
	private TextView tvPromo;
	private TextView tvCode;
	private TextView tvSubsDate;
	private TextView redeemed;
	private TextView branch;
	private TextView redemptionDate;
//	private TextView redeemedItem;
	private TextView tvExpiryDate;
	private TextView expired;
//	private ListView lvRedeemedItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscription);
	//	hypeSDK = new HypeSDK(getApplicationContext(), SubscriptionActivity.this);
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(getIntent().getExtras().getString("id"));
		tvActionNext.setVisibility(View.INVISIBLE);
		
		branch = (TextView) findViewById(R.id.tvBranchName);
		redemptionDate = (TextView) findViewById(R.id.tvRedemptionDateValue);
//		redeemedItem = (TextView) findViewById(R.id.tvRedeemItemValue);
		tvExpiryDate = (TextView) findViewById(R.id.tvExpiryDateValue);
		expired = (TextView) findViewById(R.id.tvIsExpiredValue);
		llRedemptionDate = (LinearLayout) findViewById(R.id.llRedemptionDate);
		llRedeemedItem = (LinearLayout) findViewById(R.id.llRedeemedItems);
//		lvRedeemedItems = (ListView) findViewById(R.id.lvRedeemedItems);
		llBranch = (LinearLayout) findViewById(R.id.llBranch);
		llSubsDate = (LinearLayout) findViewById(R.id.llSubscription);
		llData = (LinearLayout) findViewById(R.id.llData);
		llPromo = (LinearLayout) findViewById(R.id.llPromo);
		llCode = (LinearLayout) findViewById(R.id.llCode);
		tvPromo = (TextView) findViewById(R.id.tvPromoValue);
		tvCode = (TextView) findViewById(R.id.tvCodeValue);
		tvSubsDate = (TextView) findViewById(R.id.tvSubsDate);
		redeemed = (TextView) findViewById(R.id.tvRedeemedValue);
		
//		if (lvRedeemedItems.getAdapter() == null) {
//			lvRedeemedItems.setAdapter(new RedeemedItemsListAdapter(SubscriptionActivity.this, getIntent().getExtras().getString("id"), SubscriptionAdapter.redeemedItems));
//    	} else {
//			 ((RedeemedItemsListAdapter)lvRedeemedItems.getAdapter()).updateAdapter(SubscriptionAdapter.redeemedItems);
//		}
		if(getIntent().getExtras() != null) {
			if(getIntent().getExtras().getString("code").equals("")) {
				llCode.setVisibility(View.GONE);
			} else {
				llCode.setVisibility(View.VISIBLE);
			}
			if(getIntent().getExtras().getBoolean("redeemed")) {
				branch.setText(getIntent().getExtras().getString("branchName"));
//				redeemedItem.setText(getIntent().getExtras().getString("redeemedItem"));
				
				llBranch.setVisibility(View.VISIBLE);
				llRedeemedItem.setVisibility(View.VISIBLE);
				llData.setVisibility(View.VISIBLE);
				llRedemptionDate.setVisibility(View.VISIBLE);
				llSubsDate.setVisibility(View.GONE);
			} else {
				llBranch.setVisibility(View.GONE);
				llData.setVisibility(View.GONE);
				llRedeemedItem.setVisibility(View.GONE);
				llRedemptionDate.setVisibility(View.GONE);
				llSubsDate.setVisibility(View.VISIBLE);
			}
			
			try {
				Date expiryDate = df.parse(getIntent().getExtras().getString("expiryDate"));
				Date subsDate = df.parse(getIntent().getExtras().getString("subsDate"));
				if(!getIntent().getExtras().getString("redemptionDate").equals("")) {
					Date redempDate = df.parse(getIntent().getExtras().getString("redemptionDate"));
					redemptionDate.setText(df2.format(redempDate));
				}
				tvExpiryDate.setText(df2.format(expiryDate));
				if(new Date().after(expiryDate)) {
					expired.setText("Yes");
				} else {
					expired.setText("No");
				}
				tvSubsDate.setText(df2.format(subsDate));
			} catch (ParseException e) {
				e.printStackTrace();
				Log.d("BeamSDK", e.toString());
			}
			
			
			final HypePromo promo = hypeSDK.getPromoFromId(getIntent().getExtras().getString("promoId"));
			tvPromo.setText(promo.getName().toString());
			tvCode.setText(getIntent().getExtras().getString("code"));
			if(getIntent().getExtras().getBoolean("redeemed") == true) {
				redeemed.setText("Yes");
			} else {
				redeemed.setText("No");
			}
			
			llPromo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), PromoDetailsActivity.class);
					PromoDetailsActivity.promo = promo;
//					intent.putExtra("name", promo.getName());
//					intent.putExtra("type", promo.getType());
//					intent.putExtra("start", promo.getStartDuration().toString());
//				    intent.putExtra("end", promo.getEndDuration().toString());
//				    intent.putExtra("id", promo.getId());
//					intent.putExtra("concurrencyLimit", promo.getConcurrency());
//					intent.putExtra("allowedToSubscribe", promo.isAllowedToSubscribe());
//					intent.putExtra("content", promo.getContent().toString());
//					intent.putStringArrayListExtra("hours", promo.getHours());
//					intent.putStringArrayListExtra("days", promo.getDays());
//					intent.putStringArrayListExtra("weekdays", promo.getWeekdays());
					InRangeAdapter.listBranches.clear();
					for(int a = 0; a < promo.getBranches().size(); a++) {
						if(promo.getBranches().get(a).getName() != null) {
							InRangeAdapter.listBranches.add(promo.getBranches().get(a));
						}
					}
					InRangeAdapter.listPrizeGroups = promo.getPrizegroups();
					startActivity(intent);
				}
			});
			
			llCode.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);
					intent.putExtra("code", getIntent().getExtras().getString("code"));
					startActivity(intent);
				}
			});
			
			llData.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), DataActivity.class);
					intent.putExtra("data", getIntent().getExtras().getString("data"));
					startActivity(intent);
				}
			});
			
			llRedeemedItem.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), RedeemedItemListActivity.class);
					intent.putExtra("id",  getIntent().getExtras().getString("id"));
					startActivity(intent);
				}
			});
			
			llBranch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), BranchLocationActivity.class);
					intent.putExtra("branchName", getIntent().getExtras().getString("branchName"));
					intent.putExtra("lat", getIntent().getExtras().getDouble("lat"));
					intent.putExtra("longi", getIntent().getExtras().getDouble("longi"));
					intent.putExtra("polygon", getIntent().getExtras().getString("polygon"));
					intent.putExtra("accuracy", getIntent().getExtras().getString("accuracy"));
					startActivity(intent);
				}
			});
		}
	}

}
