package com.entropy.promoenginedemoapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.entropy.hypesdk.HypeSDK;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final HypeSDK sdk = new HypeSDK(getApplicationContext(), this);
		
		if(sdk.getRegions().size() > 0) {
			Log.d("HypeSDK", sdk.getRegions().toString());
		}  else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.d("HypeSDK", "handler : " + sdk.getRegions().toString());
				}
			}, 2000);
		}
	}

}
