package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entropy.hypesdk.HypeListener;
import com.entropy.hypesdk.HypeSDK;
import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.hypesdk.model.HypeSurvey;
import com.entropy.promoenginedemoapp.BaseActivity;
import com.entropy.promoenginedemoapp.R;
import com.entropy.promoenginedemoapp.ScanQRActivity;

public class PrizesListAdapter extends BaseAdapter implements HypeListener {
	
	private ArrayList<HypeItem> myList;
	private LayoutInflater mInflater;
	private Context context;
	private HypeSDK hypeSDK;
	private ProgressDialog pDialog;
	private Activity activity;
	
	public PrizesListAdapter(Context context, ArrayList<HypeItem> myList, Activity activity) {
		hypeSDK = new HypeSDK(context, activity);
		this.myList	 = myList;
	    this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void updateAdapter(ArrayList<HypeItem> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_prizes_list_adapter, null);
			viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.tvPrizeName);
			viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			viewHolder.name.setText(myList.get(position).getName());
			if(myList.get(position).getImage() != null) {
				try {
					try {
						viewHolder.ivImage.setImageBitmap(BaseActivity.decodeBase64(myList.get(position).getImage()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showAlertDialog(myList.get(position));
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder {
		 TextView name;
		 ImageView ivImage;
	}
	
	private void showSuccessResponse(HypeItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false);
		builder.setTitle("Success");
		builder.setMessage("You have selected " + item.getName() + " as your prize.");
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				activity.finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showAlertDialog(final HypeItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("Redeem " + item.getName())
		   .setTitle("Confirmation")
		   .setCancelable(false)
		   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		    	   pDialog = ProgressDialog.show(activity, null, "Loading ...");
		    	   redeeemPromo(item);
		       }
		   })
		   .setNegativeButton("No", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            //close alert dialog
		       }
		   });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void redeeemPromo(HypeItem item) {
		hypeSDK.redeemPromo(this, ScanQRActivity.promoFound, ScanQRActivity.branchFound.getId(), item);
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
	public void failure(String error) {
		pDialog.dismiss();
		showAlertDialog(error, "Error");
	}

	@Override
	public void getPromoFromQRCodeCompletion(ArrayList<HypeItem> prizes,
			HypePromo promo, HypeBranch branch) {}

	@Override
	public void completion(String result) {}
	
	private void showAlertDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

	@Override
	public void syncFinished(boolean hasChanges) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redemptionForPromo(HypePromo promo, HypeItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redeemPromoCompletion(HypePromo promo, HypeItem item,
			String branchId) {
		pDialog.dismiss();
		showSuccessResponse(item);
	}
}
