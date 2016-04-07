package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.promoenginedemoapp.BaseActivity;
import com.entropy.promoenginedemoapp.R;
import com.entropy.promoenginedemoapp.SubscriptionActivity;

public class SubscriptionAdapter extends BaseAdapter {

	private ArrayList<HypeSubscription> myList = new ArrayList<HypeSubscription>();
	private Context context;
//	public static HypeBranch hypeBranch;
	public static ArrayList<HypeItem> redeemedItems;
	 
	public SubscriptionAdapter(Context context, ArrayList<HypeSubscription> myList) {
		this.myList.clear();
		this.myList.addAll(myList);
		this.context = context;
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
	
	public void updateAdapter(ArrayList<HypeSubscription> results) {
		myList.clear();
		myList.addAll(results);
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflater.inflate(R.layout.row, parent, false);
			convertView = inflater.inflate(R.layout.adapter_subscription, parent, false);
			viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.tvPromoName);
			viewHolder.subsId = (TextView) convertView.findViewById(R.id.tvSubsId);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			final HypePromo promo = BaseActivity.hypeSDK.getPromoFromId(myList.get(position).getPromo().getId());
			
			viewHolder.name.setText(promo.getName());
			viewHolder.subsId.setText(myList.get(position).getId());
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(myList.get(position).getPromo() != null) {
						redeemedItems = myList.get(position).getItems();
						Intent intent = new Intent(context, SubscriptionActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", myList.get(position).getId());
						intent.putExtra("code", myList.get(position).getCode().toString());
						intent.putExtra("subsDate", myList.get(position).getSubscriptionDate().toString());
					    intent.putExtra("redeemed", myList.get(position).isRedeemed());
					    intent.putExtra("promoId", myList.get(position).getPromo().getId());
					    intent.putExtra("expiryDate", myList.get(position).getPromo().getEndDuration().toString());
//					    String itemsName = "";
//					    for(int a = 0;a < myList.get(position).getItems().size(); a++) {
//					    	itemsName = itemsName + ", " +  myList.get(position).getItems().get(a).getName();
//					    }
//					    intent.putExtra("redeemedItem", itemsName);
					    if(myList.get(position).getRedemptionDate() != null) {
					    	intent.putExtra("redemptionDate", myList.get(position).getRedemptionDate().toString());
					    } else {
					    	intent.putExtra("redemptionDate","");
					    }
					    if(myList.get(position).getData() != null) {
					    	intent.putExtra("data", myList.get(position).getData().toString());
					    } else {
					    	intent.putExtra("data","");
					    }
					    intent.putExtra("branchName", myList.get(position).getRedemptionBranch().getName());
					    if(myList.get(position).getRedemptionBranch().getLocation() != null) {
					    	intent.putExtra("lat", myList.get(position).getRedemptionBranch().getLocation().getLatitude());
							intent.putExtra("longi", myList.get(position).getRedemptionBranch().getLocation().getLongitude());
							if(myList.get(position).getRedemptionBranch().getPolygon() != null) {
								intent.putExtra("polygon", myList.get(position).getRedemptionBranch().getPolygon().toString());
							} else {
								intent.putExtra("polygon", "");
							}
							intent.putExtra("accuracy", myList.get(position).getRedemptionBranch().getRadius()+"");
					    }
						context.startActivity(intent);
					}
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder {
		 TextView name;
		 TextView subsId;
	}

}
