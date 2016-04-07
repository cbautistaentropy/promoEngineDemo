package com.entropy.promoenginedemoapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DataActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Data");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		TextView tvData = (TextView) findViewById(R.id.tvData);
		if(getIntent().getExtras() != null) {
			try {
				tvData.setText(new JSONObject(getIntent().getExtras().getString("data")) + "  ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	
}
