package com.entropy.promoenginedemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ItemImageActivity extends BaseActivity {

	private ImageView itemImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize_image);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(getIntent().getExtras().getString("name"));
		tvActionNext.setVisibility(View.INVISIBLE);
		
		itemImage = (ImageView) findViewById(R.id.ivPrize);
		if(!getIntent().getExtras().getString("image").equals("")) {
			try {
				itemImage.setImageBitmap(BaseActivity.decodeBase64(getIntent().getExtras().getString("image")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			itemImage.setImageBitmap(null);
		}
	}
}
