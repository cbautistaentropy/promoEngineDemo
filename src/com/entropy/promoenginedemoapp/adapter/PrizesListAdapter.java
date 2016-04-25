package com.entropy.promoenginedemoapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.entropy.hypesdk.HypeSDK;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.promoenginedemoapp.BaseActivity;
import com.entropy.promoenginedemoapp.PrizesListActivity;
import com.entropy.promoenginedemoapp.R;
import com.entropy.promoenginedemoapp.ScanQRActivity;

public class PrizesListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	private LayoutInflater mInflater;
	private Context context;
	private HypeSDK hypeSDK;
	private int totalCount = 0;

	public PrizesListAdapter(Context context, ArrayList<HashMap<String, String>> myList, Activity activity) {
		hypeSDK = new HypeSDK(context, activity);
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

	public void updateAdapter(ArrayList<HashMap<String, String>> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_prizes_list, null);
			viewHolder = new ViewHolder();

			viewHolder.name = (TextView) convertView.findViewById(R.id.tvPrizeName);
			viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
			viewHolder.plus = (Button) convertView.findViewById(R.id.btnPlus);
			viewHolder.minus = (Button) convertView.findViewById(R.id.btnMinus);
			viewHolder.count = (TextView) convertView.findViewById(R.id.tvItemCount);
			viewHolder.maxCount = (TextView) convertView.findViewById(R.id.tvMaxCount);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(myList.size() > 0) {
			viewHolder.name.setText(myList.get(position).get("name"));
			if(myList.get(position).containsKey("image")) {
				try {
					viewHolder.ivImage.setImageBitmap(BaseActivity.decodeBase64(myList.get(position).get("image")));
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewHolder.maxCount.setText("Max: " + hypeSDK.getMaxRedemptionForItem(ScanQRActivity.prizeGroupFound.getId(), myList.get(position).get("id")));
				viewHolder.count.setText(myList.get(position).get("count"));
//				myList.get(position).put("count", viewHolder.count.getText().toString());
			}

			if(totalCount == ScanQRActivity.prizeGroupFound.getRedemptionCount()) {
				BaseActivity.tvActionNext.setEnabled(true);
				BaseActivity.tvActionNext.setTextColor(Color.WHITE);
				viewHolder.minus.setEnabled(true);
				viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
				viewHolder.plus.setEnabled(false);
				viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
			} else {
				BaseActivity.tvActionNext.setEnabled(false);
				BaseActivity.tvActionNext.setTextColor(Color.LTGRAY);
				if(Integer.parseInt(viewHolder.count.getText().toString()) == Integer.parseInt(viewHolder.maxCount.getText().toString().replace("Max: ", "")))  {
					viewHolder.plus.setEnabled(false);
					viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
				} else {
					viewHolder.plus.setEnabled(true);
					viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_enabled));
				}

				if(viewHolder.count.getText().toString().equals("0")) {
					viewHolder.minus.setEnabled(false);
					viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_disabled));
				} else {
					viewHolder.minus.setEnabled(true);
					viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
				}
			}

			viewHolder.plus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(viewHolder.count.getText().toString().equals("0")) {
						myList.get(position).put("count", "1");
						totalCount = totalCount + 1;
					} else {
						totalCount = totalCount + 1;
						myList.get(position).put("count", Integer.parseInt(viewHolder.count.getText().toString()) + 1 + "");
					}
					viewHolder.count.setText(myList.get(position).get("count"));

					if(totalCount == ScanQRActivity.prizeGroupFound.getRedemptionCount()) {
						BaseActivity.tvActionNext.setEnabled(true);
						BaseActivity.tvActionNext.setTextColor(Color.WHITE);
						viewHolder.minus.setEnabled(true);
						viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
						viewHolder.plus.setEnabled(false);
						viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
					} else {
						BaseActivity.tvActionNext.setEnabled(false);
						BaseActivity.tvActionNext.setTextColor(Color.LTGRAY);
						if(Integer.parseInt(viewHolder.count.getText().toString()) == Integer.parseInt(viewHolder.maxCount.getText().toString().replace("Max: ", "")))  {
							viewHolder.plus.setEnabled(false);
							viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
						} else {
							viewHolder.plus.setEnabled(true);
							viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_enabled));
						}

						if(viewHolder.count.getText().toString().equals("0")) {
							viewHolder.minus.setEnabled(false);
							viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_disabled));
						} else {
							viewHolder.minus.setEnabled(true);
							viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
						}
					}

					try {
						PrizesListActivity.items.put(myList.get(position).get("id"), Integer.parseInt(viewHolder.count.getText().toString()));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			viewHolder.minus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(!viewHolder.count.getText().toString().equals("0")) {
						totalCount = totalCount - 1;
						myList.get(position).put("count", (Integer.parseInt("" + viewHolder.count.getText().toString()) - 1) + "");
					}
					viewHolder.count.setText(myList.get(position).get("count"));
					PrizesListActivity.items.remove(myList.get(position).get("id"));

					if(totalCount == ScanQRActivity.prizeGroupFound.getRedemptionCount()) {
						BaseActivity.tvActionNext.setEnabled(true);
						BaseActivity.tvActionNext.setTextColor(Color.WHITE);
						viewHolder.minus.setEnabled(true);
						viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
						viewHolder.plus.setEnabled(false);
						viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
					} else {
						BaseActivity.tvActionNext.setEnabled(false);
						BaseActivity.tvActionNext.setTextColor(Color.LTGRAY);
						if(Integer.parseInt(viewHolder.count.getText().toString()) == Integer.parseInt(viewHolder.maxCount.getText().toString().replace("Max: ", "")))  {
							viewHolder.plus.setEnabled(false);
							viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_disabled));
						} else {
							viewHolder.plus.setEnabled(true);
							viewHolder.plus.setBackground(context.getResources().getDrawable(R.drawable.plus_enabled));
						}

						if(viewHolder.count.getText().toString().equals("0")) {
							viewHolder.minus.setEnabled(false);
							viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_disabled));
						} else {
							viewHolder.minus.setEnabled(true);
							viewHolder.minus.setBackground(context.getResources().getDrawable(R.drawable.minus_enabled));
						}
					}
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		 TextView name;
		 Button plus;
		 Button minus;
		 TextView count;
		 TextView maxCount;
		 ImageView ivImage;
	}
}
