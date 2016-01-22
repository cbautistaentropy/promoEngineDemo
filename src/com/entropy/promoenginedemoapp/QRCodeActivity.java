package com.entropy.promoenginedemoapp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class QRCodeActivity extends BaseActivity {

	private ImageView ivQrCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(getIntent().getExtras().getString("code"));
		tvActionNext.setVisibility(View.INVISIBLE);
		
		ivQrCode = (ImageView) findViewById(R.id.imageView1);
		
		if(getIntent().getExtras() != null) {
			try {
				generateQRCode_general(getIntent().getExtras().getString("code"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void generateQRCode_general(String data) throws WriterException {
	    com.google.zxing.MultiFormatWriter writer = new MultiFormatWriter();
	    String finaldata = Uri.encode(data, "utf-8");

	    BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE,150, 150);
	    Bitmap ImageBitmap = Bitmap.createBitmap(150, 150,Config.ARGB_8888);

	    for (int i = 0; i < 150; i++) {//width
	        for (int j = 0; j < 150; j++) {//height
	            ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
	        }
	    }

	    if (ImageBitmap != null) {
	        ivQrCode.setImageBitmap(ImageBitmap);
	    } else {
	        Toast.makeText(getApplicationContext(), "Something went wrong!\n Please try again later.", Toast.LENGTH_SHORT).show(); 
	    }
	}
	
}
