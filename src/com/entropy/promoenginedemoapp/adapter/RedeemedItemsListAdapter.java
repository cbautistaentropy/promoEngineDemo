package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypeItem;
import com.entropy.promoenginedemoapp.BaseActivity;
import com.entropy.promoenginedemoapp.ItemImageActivity;
import com.entropy.promoenginedemoapp.R;

public class RedeemedItemsListAdapter extends BaseAdapter{

	private ArrayList<HypeItem> myList;
	private LayoutInflater mInflater;
	private Context context;
	private String subsId = "";
	 
	public RedeemedItemsListAdapter(Context context, String subsId, ArrayList<HypeItem> myList) {
		this.myList	 = myList;
	    this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.subsId = subsId;
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
			convertView = mInflater.inflate(R.layout.adapter_redeemed_items, null);
			viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.itemName);
			viewHolder.count = (TextView) convertView.findViewById(R.id.itemCount);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			viewHolder.name.setText(myList.get(position).getName());
			viewHolder.count.setText(BaseActivity.hypeSDK.getRedeemedQuantityForItem(subsId, myList.get(position).getId()) + "");
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ItemImageActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("name", myList.get(position).getName());
					intent.putExtra("image", myList.get(position).getImage());
					context.startActivity(intent);
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder {
		 TextView name;
		 TextView count;
	}

}
