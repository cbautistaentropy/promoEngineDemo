//package com.entropy.promoenginedemoapp;
//
//import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//
//public class DisplayAllPromo extends BaseActivity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_display_all_promo);
//		//hypeSDK = new HypeSDK(getApplicationContext(), DisplayAllPromo.this);
//		imageButton.setVisibility(View.INVISIBLE);
//		mTitleTextView.setText("Hype Demo");
//		tvActionNext.setVisibility(View.INVISIBLE);
//		
//		final ListView listview = (ListView) findViewById(R.id.allPromo);
//		if(hypeSDK.getAllPromos().size() > 0) {
//    		DisplayAllPromo.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                	if (listview.getAdapter() == null) {
//                		InRangeAdapter adap = new InRangeAdapter(getApplicationContext(), hypeSDK.getAllPromos());
//		    			listview.setAdapter(adap);
//		    		} else {
//		    		    ((InRangeAdapter)listview.getAdapter()).updateAdapter(hypeSDK.getAllPromos());
//		    		}
//                }
//            });
//    		
//    	}
//	}
//}
