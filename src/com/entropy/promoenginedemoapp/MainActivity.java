package com.entropy.promoenginedemoapp;

import android.content.Intent;
import android.os.Bundle;

import com.entropy.hypesdk.HypeSDK;

public class MainActivity extends BaseActivity {
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hypeSDK = new HypeSDK(getApplicationContext(), MainActivity.this);
		
		if(hypeSDK.isActivated()) {
			startActivity(new Intent(MainActivity.this, SecondActivity.class));
			finish();
		} else {
			startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
			finish();
		}
		
	}
}
