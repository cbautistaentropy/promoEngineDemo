package com.entropy.promoenginedemoapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy.hypesdk.model.HypeSurveyQuestion;

public class SurveyAdapter extends BaseAdapter {
	
	private ArrayList<HypeSurveyQuestion> myList;
	private LayoutInflater mInflater;
	private Context context;
	private Button dynamicBtn;
	
	public SurveyAdapter(Context context, ArrayList<HypeSurveyQuestion> myList) {
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
	
	public void updateAdapter(ArrayList<HypeSurveyQuestion> results) {
		this.myList = results;
        notifyDataSetChanged();
	}

	@Override
	public View getView(final int position,  View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_survey, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.btns);
			viewHolder.question = (TextView) convertView.findViewById(R.id.tvQuestion);
			
			final View convertview2 = convertView;
			for(int a = 0; a < myList.get(position).getOptions().size(); a++) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		                LinearLayout.LayoutParams.WRAP_CONTENT);
		        dynamicBtn = new Button(context);
		        dynamicBtn.setId(a);
		        final int id_ = dynamicBtn.getId();
		        viewHolder.layout.addView(dynamicBtn, params);
		        
		        dynamicBtn = ((Button) convertView.findViewById(id_));
		        dynamicBtn.setBackground(context.getResources().getDrawable(R.drawable.button_survey));
		        dynamicBtn.setTextColor(Color.parseColor("#146ab8"));
		        dynamicBtn.setText(myList.get(position).getOptions().get(a));
		        dynamicBtn.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		            	for (int i = 0; i < myList.get(position).getOptions().size(); i++){
		            		 ((Button)convertview2.findViewById(i)).setBackground(context.getResources().getDrawable(R.drawable.button_survey));
		                      view.setBackgroundColor(context.getResources().getColor(R.color.AliceBlue));
		                }
		        		for(HashMap<String, String> found : SurveyActivity.questionAndAnswer) {
			    			if(found.get("questionId").equals(myList.get(position).getId())) {
			    				Log.d("BeamSDK", "contains :" + myList.get(position).getOptions().indexOf(((Button)view).getText().toString()));
			    				found.put("answerIndex", myList.get(position).getOptions().indexOf(((Button)view).getText().toString())+"");
			    				break;
			    			}
		        		}
		            }
		        });
			}
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(myList.size() > 0) {
			HashMap<String, String> question = new HashMap<String, String>();
			question.put("questionId", myList.get(position).getId());
			SurveyActivity.questionAndAnswer.add(question);
			
			viewHolder.question.setText(myList.get(position).getQuestion());
		}
		
		return convertView;
	}

	class ViewHolder {
		 TextView question;
		 LinearLayout layout;
	}
}
