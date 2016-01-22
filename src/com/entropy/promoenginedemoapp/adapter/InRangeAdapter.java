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
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.promoenginedemoapp.PromoDetailsActivity;
import com.entropy.promoenginedemoapp.R;

public class InRangeAdapter extends BaseAdapter{

	public static ArrayList<HypeBranch> listBranches = new ArrayList<HypeBranch>();
	public static ArrayList<HypeItem> listItems = new ArrayList<HypeItem>();
	private List<HypePromo> myList;
	private LayoutInflater mInflater;
	private Context context;
	 
	public InRangeAdapter(Context context, List<HypePromo> myList) {
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
	
	public void updateAdapter(ArrayList<HypePromo> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_in_range_adapter, null);
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
					listBranches.clear();
					Intent intent = new Intent(context, PromoDetailsActivity.class);
					PromoDetailsActivity.promo = myList.get(position);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					for(int a = 0; a < myList.get(position).getBranches().size(); a++) {
						if(myList.get(position).getBranches().get(a).getName() != null) {
							listBranches.add(myList.get(position).getBranches().get(a));
						}
					}
					//listBranches = myList.get(position).getBranches();
					listItems = myList.get(position).getPrizes();
//					intent.putExtra("id", myList.get(position).getId());
//					intent.putExtra("name", myList.get(position).getName());
//					intent.putExtra("type", myList.get(position).getType());
//					intent.putExtra("start", myList.get(position).getStartDuration().toString());
//				    intent.putExtra("end", myList.get(position).getEndDuration().toString());
//					intent.putExtra("concurrencyLimit", myList.get(position).getConcurrency());
//					intent.putExtra("allowedToSubscribe", myList.get(position).isAllowedToSubscribe());
//					intent.putExtra("content", myList.get(position).getContent());
//					intent.putStringArrayListExtra("hours", myList.get(position).getHours());
//					intent.putStringArrayListExtra("days", myList.get(position).getDays());
//					intent.putStringArrayListExtra("weekdays", myList.get(position).getWeekdays());
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
