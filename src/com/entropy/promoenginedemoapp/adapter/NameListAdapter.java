package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.promoenginedemoapp.BranchLocationActivity;
import com.entropy.promoenginedemoapp.R;

public class NameListAdapter extends BaseAdapter {
	
	private ArrayList<HypeBranch> myList;
	private LayoutInflater mInflater;
	private Context context;
	
	public NameListAdapter(Context context, ArrayList<HypeBranch> myList) {
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
	
	public void updateAdapter(ArrayList<HypeBranch> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_name_adapter, null);
			viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			Log.d("BeamSDK", myList.get(position).getName() + " " + myList.get(position).getId());
			viewHolder.name.setText(myList.get(position).getName());
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(myList.get(position).getLocation() != null) {
						Intent intent = new Intent(context, BranchLocationActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("branchName", myList.get(position).getName());
						intent.putExtra("lat", myList.get(position).getLocation().getLatitude());
						intent.putExtra("longi", myList.get(position).getLocation().getLongitude());
						if(myList.get(position).getPolygon() != null) {
							intent.putExtra("polygon", myList.get(position).getPolygon().toString());
						}
						intent.putExtra("accuracy", myList.get(position).getRadius()+"");
						context.startActivity(intent);
					}
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder {
		 TextView name;
	}

}