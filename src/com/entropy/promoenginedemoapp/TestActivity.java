package com.entropy.promoenginedemoapp;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		final HypeSDK sdk = new HypeSDK(getApplicationContext(), this);
//		
//		if(sdk.getRegions().size() > 0) {
//			Log.d("HypeSDK", sdk.getRegions().toString());
//		}  else {
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					Log.d("HypeSDK", "handler : " + sdk.getRegions().toString());
//				}
//			}, 2000);
//		}
//		Log.d("HypeSDK", "TestActivity");
//		String text = "Head over to [BAR] today and get a [freebie] from San Mig Light! {{name}}/{{branch}}";
//		Template tmpl = Mustache.compiler().compile(text);
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("name", "ok");
//		data.put("branch", "ok again");
//		Log.d("HypeSDK", tmpl.execute(data));
	}
}
