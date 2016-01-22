package com.entropy.promoenginedemoapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.entropy.hypesdk.HypeListener;
import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.hypesdk.model.HypeSurvey;


public class ScanQRActivity extends BaseActivity implements HypeListener {

	private static final int REQUEST_BARCODE = 0;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_qr);
		//hypeSDK = new HypeSDK(getApplicationContext(), ScanQRActivity.this);
		imageButton.setVisibility(View.VISIBLE);
		imageButton.setImageDrawable(getResources().getDrawable(R.drawable.left_white));
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mTitleTextView.setText("Scan QR Code");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");  
	    startActivityForResult(intent, REQUEST_BARCODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                pDialog = ProgressDialog.show(ScanQRActivity.this, null, "Loading ...");
                hypeSDK.getPromoFromQRCode(contents, this);
               // new GetPromoFromQRCode(contents).execute((Void) null);
            } else if (resultCode == RESULT_CANCELED) {
            	finish();
            }
        }
	}

	@Override
	public void inRangePromo(HypePromo promo) {}

	@Override
	public void inRangePromosUpdate(ArrayList<HypePromo> promos) {}

	@Override
	public void outOfRange(HypePromo promo) {}

	@Override
	public void subscriptionUpdate(ArrayList<HypeSubscription> subscriptions) {}

	@Override
	public void triggerSurveys(ArrayList<HypeSurvey> survey) {}

	@Override
	public void failure(final String error) {
		pDialog.dismiss();
		Handler mHandler = new Handler(Looper.getMainLooper());
	    mHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	showAlertDialog(error, "Error");
	        }
	    });
	}

	public static ArrayList<HypeItem> prizesFound = new ArrayList<HypeItem>();
	public static HypePromo promoFound = new HypePromo();
	public static HypeBranch branchFound = new HypeBranch();
	
	@Override
	public void getPromoFromQRCodeCompletion(ArrayList<HypeItem> prizes,
			HypePromo promo, HypeBranch branch) {
		pDialog.dismiss();
		prizesFound = prizes;
		promoFound = promo;
		branchFound = branch;
		finish();
		startActivity(new Intent(ScanQRActivity.this, PrizesListActivity.class));
	}

	@Override
	public void completion(String result) {}
	
	private void showAlertDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ScanQRActivity.this);
		builder.setMessage(message)
		   .setTitle(title)
		   .setCancelable(false)
		   .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            //close alert dialog
		    	   finish();
		       }
		   });
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void syncFinished(boolean hasChanges) {}

	@Override
	public void redemptionForPromo(HypePromo promo, HypeItem item) {}

	@Override
	public void redeemPromoCompletion(HypePromo promo, HypeItem item,
			String branchId) {}
}
