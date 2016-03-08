package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePrizeGroup;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.promoenginedemoapp.PrizeGroupListActivity;
import com.entropy.promoenginedemoapp.PromoDetailsActivity;
import com.entropy.promoenginedemoapp.R;

public class PrizeGroupAdapter extends BaseAdapter{

	public static ArrayList<HypeItem> items = new ArrayList<HypeItem>();
	private List<HypePrizeGroup> myList;
	private LayoutInflater mInflater;
	private Context context;
	 
	public PrizeGroupAdapter(Context context, List<HypePrizeGroup> myList) {
		this.myList	 = myList;
	    this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	
	public void updateAdapter(ArrayList<HypePrizeGroup> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_in_range, null);
			viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			viewHolder.name.setText(myList.get(position).getName());
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					items = myList.get(position).getItems();
					Intent intent = new Intent(context, PrizeGroupListActivity.class);
					intent.putExtra("name", myList.get(position).getName());
					intent.putExtra("redemptionCount", myList.get(position).getRedemptionCount() + "");
					intent.putExtra("id", myList.get(position).getId());
					context.startActivity(intent);
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder {
		 TextView name;
	}

}
