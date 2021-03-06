package com.entropy.promoenginedemoapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypePromo;
import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;

public class PromoDetailsActivity extends BaseActivity {

	private TextView name;
	private TextView type;
	private TextView start;
	private TextView end;
	private TextView concurrencyLimit;
	private TextView allowedToSubscribe;
	private LinearLayout llPrizeGroup;
	private LinearLayout llBranches;
	private LinearLayout llSubscriptions;
	private LinearLayout llContent;
	private LinearLayout llHours;
	private LinearLayout llWeekDays;
	private LinearLayout llDays;
	private TextView hours;
	private TextView days;
	private TextView weekdays;
	public static HypePromo promo = new HypePromo();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_range);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(promo.getName());
		tvActionNext.setVisibility(View.INVISIBLE);
		
		hours = (TextView) findViewById(R.id.hours);
		days = (TextView) findViewById(R.id.days);
		weekdays = (TextView) findViewById(R.id.weekdays);
		llContent = (LinearLayout) findViewById(R.id.llContent);
		llHours = (LinearLayout) findViewById(R.id.llHours);
		llWeekDays = (LinearLayout) findViewById(R.id.llWeekDays);
		llDays = (LinearLayout) findViewById(R.id.llDays);
		name = (TextView) findViewById(R.id.name);
		type = (TextView) findViewById(R.id.type);
		start = (TextView) findViewById(R.id.start);
		end = (TextView) findViewById(R.id.end);
		concurrencyLimit = (TextView) findViewById(R.id.concurrencyLimit);
		allowedToSubscribe = (TextView) findViewById(R.id.allowedToSubscribe);
		llPrizeGroup = (LinearLayout) findViewById(R.id.llPrizeGroups);
		llBranches = (LinearLayout) findViewById(R.id.llBranches);
		llSubscriptions = (LinearLayout) findViewById(R.id.llSubscription);
		
		//setListViewHeightBasedOnChildren(prizes);
		//if(getIntent().getExtras() != null) {
			try {
				Date startD = df.parse(promo.getStartDuration().toString());
				Date endD = df.parse(promo.getEndDuration().toString());
				start.setText(df2.format(startD));
				end.setText(df2.format(endD));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(promo.getHours().toString().equals("[]")) {
				llHours.setVisibility(View.GONE);
			} else {
				llHours.setVisibility(View.VISIBLE);
				hours.setText(promo.getHours().toString().replace("[", "").replace("]", ""));
			}
			
			if(promo.getDays().toString().equals("[]")) {
				llDays.setVisibility(View.GONE);
			} else {
				llDays.setVisibility(View.VISIBLE);
				days.setText(promo.getDays().toString().replace("[", "").replace("]", ""));
			}
			
			if(promo.getWeekdays().toString().equals("[]")) {
				llWeekDays.setVisibility(View.GONE);
			} else {
				llWeekDays.setVisibility(View.VISIBLE);
				String[] weekdaysString = {"Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"};
				ArrayList<String> weekdaysValue = new ArrayList<String>();
				ArrayList<String> weekdaysIntent =  promo.getWeekdays();
				for(int i = 0; i < weekdaysIntent.size(); i++ ) {
					weekdaysValue.add(weekdaysString[Integer.valueOf(weekdaysIntent.get(i)) - 1]);
				}
				weekdays.setText(weekdaysValue.toString().replace("[", "").replace("]", ""));
			}
			
			name.setText(promo.getName());
			type.setText(promo.getType());
			concurrencyLimit.setText(promo.getConcurrency()+"");
			
			llContent.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						displayDialog(decodeBase64(promo.getContent()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			if(promo.isAllowedToSubscribe()) {
				allowedToSubscribe.setText("Yes");
			} else {
				allowedToSubscribe.setText("No");
			}
			
			llSubscriptions.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(PromoDetailsActivity.this, SubscriptionListActivity.class));
				}
			});
			InRangeAdapter.listPrizeGroups = promo.getPrizegroups();
			llPrizeGroup.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(PromoDetailsActivity.this, PrizeGroupListActivity.class));
				}
			});
			llBranches.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(PromoDetailsActivity.this, BranchListActivity.class));
				}
			});
		
			
//			if (subscriptions.getAdapter() == null) {
//				SubscriptionAdapter subsAdap = new SubscriptionAdapter(PromoDetailsActivity.this, hypeSDK.getAllSubscriptionByPromoId(promo.getId()));
//    			subscriptions.setAdapter(subsAdap);
//    		} else {
//    		    ((SubscriptionAdapter)subscriptions.getAdapter()).updateAdapter(hypeSDK.getAllSubscriptionByPromoId(promo.getId()));
//    		}
//			
//			if(InRangeAdapter.listBranches.size() > 0) {
//				NameListAdapter adap = new NameListAdapter(getApplicationContext(), InRangeAdapter.listBranches);
//				branches.setAdapter(adap);
//			}
//			
//			if(InRangeAdapter.listPrizeGroups.size() > 0) {
//				if (prizes.getAdapter() == null) {
//					PrizeGroupAdapter adap2 = new PrizeGroupAdapter(PromoDetailsActivity.this, InRangeAdapter.listPrizeGroups);
//					prizes.setAdapter(adap2);
//	    		} else {
//	    		    ((PrizeGroupAdapter)prizes.getAdapter()).updateAdapter(InRangeAdapter.listPrizeGroups);
//	    		}
//			}
		//}
	}
	
	private Dialog dialog;
	private void displayDialog(Bitmap bmp) {
		dialog = new Dialog(PromoDetailsActivity.this, R.style.Theme_Dialog);
		dialog.setContentView(R.layout.activity_popup);
		
		try {
			ImageView imgView = (ImageView) dialog.findViewById(R.id.content);
			imgView.setImageBitmap(bmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	    if(!isFinishing()) {
	    	dialog.show();	
		}
	}
}
