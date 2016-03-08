package com.entropy.promoenginedemoapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.entropy.hypesdk.HypeListener;
import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePrizeGroup;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.hypesdk.model.HypeSurvey;
import com.entropy.promoenginedemoapp.adapter.PrizesListAdapter;

public class PrizesListActivity extends BaseActivity implements HypeListener {

	private TextView tvPromoName;
	private TextView tvPrizeGroupName;
	private TextView tvMaxItemCount;
	private HorizontalListView prizes;
	private ProgressDialog pDialog;
	public static JSONObject items = new JSONObject();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_item);

		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Redemption");
		tvActionNext.setVisibility(View.VISIBLE);
		tvActionNext.setText("Redeem");
		tvActionNext.setEnabled(false);
		tvActionNext.setTextColor(Color.LTGRAY);
		tvActionNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				showAlertDialog(prizeGroupId, "");
				if (items.toString().equals("{}")) {
					//TODO!
				} else {
					pDialog = ProgressDialog.show(PrizesListActivity.this, null, "Loading ...");
			    	redeeemPromo(items, ScanQRActivity.prizeGroupFound.getId().toString());
				}

			}
		});
		tvPromoName = (TextView) findViewById(R.id.tvPromoName);
		tvPrizeGroupName = (TextView) findViewById(R.id.tvPrizeGroupName);
		tvMaxItemCount = (TextView) findViewById(R.id.tvMaxItemCount);

		tvPromoName.setText(ScanQRActivity.promoFound.getName());
		tvPrizeGroupName.setText(ScanQRActivity.prizeGroupFound.getName());
		tvMaxItemCount.setText(ScanQRActivity.prizeGroupFound.getRedemptionCount()+ "");

		prizes = (HorizontalListView) findViewById(R.id.lvItems);
		ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
		if(ScanQRActivity.prizeGroupFound != null)  {
			for(HypeItem item : ScanQRActivity.prizeGroupFound.getItems()) {
				HashMap<String, String> itm = new HashMap<String, String>();
				itm.put("name", item.getName());
				itm.put("id", item.getId());
				itm.put("count", "0");
				if (item.getImage() != null) {
					itm.put("image", item.getImage());
				}
				items.add(itm);
			}
		}
		prizes.setAdapter(new PrizesListAdapter(getApplicationContext(), items, PrizesListActivity.this));
	}

	private void showAlertDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		   .setTitle(title)
		   .setCancelable(false)
		   .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            //close alert dialog
		       }
		   });
		AlertDialog alert = builder.create();
		alert.show();
	}

//	private void showAlertDialog(final HashMap<String, String> prizeGroupId, String name) {
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setMessage("Redeem " + name)
//		   .setTitle("Confirmation")
//		   .setCancelable(false)
//		   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//		       public void onClick(DialogInterface dialog, int id) {
//		    	   pDialog = ProgressDialog.show(PrizesListActivity.this, null, "Loading ...");
//		    	   redeeemPromo(prizeGroupId);
//		       }
//		   })
//		   .setNegativeButton("No", new DialogInterface.OnClickListener() {
//		       public void onClick(DialogInterface dialog, int id) {
//		            //close alert dialog
//		       }
//		   });
//		AlertDialog alert = builder.create();
//		alert.show();
//	}

	private void redeeemPromo(JSONObject items, String prizeGroupId) {
		hypeSDK.redeemPromo(this, ScanQRActivity.promoFound, ScanQRActivity.branchFound.getId(), prizeGroupId, items);
	}

	private void showSuccessResponse(ArrayList<HypeItem> items) {
		String names = "";
		for(int a=0; a < items.size(); a++) {
			names = names + ", " + items.get(a).getName();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle("Success");
		builder.setMessage("You have selected " + names + " as your prize.");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void inRangePromo(HypePromo promo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inRangePromosUpdate(ArrayList<HypePromo> promos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void outOfRange(HypePromo promo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscriptionUpdate(ArrayList<HypeSubscription> subscriptions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void syncFinished(boolean hasChanges) {
		// TODO Auto-generated method stub

	}

	@Override
	public void redemptionForPromo(HypePromo promo, HypeItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void triggerSurveys(ArrayList<HypeSurvey> survey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failure(String error) {
		pDialog.dismiss();
		showAlertDialog(error, "Error");
	}

	@Override
	public void completion(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void redeemPromoCompletion(HypePromo promo, ArrayList<HypeItem> items, String branchId) {
		pDialog.dismiss();
		showSuccessResponse(items);
	}

	@Override
	public void getPromoFromQRCodeCompletion(HypePrizeGroup prizes,
			HypePromo promo, HypeBranch branch) {
		// TODO Auto-generated method stub

	}
}
