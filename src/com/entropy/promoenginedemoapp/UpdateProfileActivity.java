package com.entropy.promoenginedemoapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfileActivity extends BaseActivity {
	
	private EditText etFullname;
	private EditText etEmail;
	private TextView tvBdate;
	private EditText etMobileNumber;
	private TextView tvGender;
	private Spinner spLocation;
	private Calendar myCalendar = Calendar.getInstance();
	private int bYear;
	private int bMonth;
	private int bDay;
	private HashMap<String, String> userData;
	public static String lastInrangePromoId = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		userData = hypeSDK.getProfile();
		
		String[] parts = null;
	    if(userData.get("birthdate").contains("-")) {
	    	parts = userData.get("birthdate").split("-");
	    }
	    if(parts != null) {
	    	String[] parts2 = parts[2].split("T");
		    bYear = Integer.valueOf(parts[0]);
		    bMonth =Integer.valueOf(parts[1]) - 1;
	        bDay = Integer.valueOf(parts2[0]);
	    }
        
		imageButton.setVisibility(View.VISIBLE);
		imageButton.setImageDrawable(getResources().getDrawable(R.drawable.left_white));
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userData.put("name", etFullname.getText().toString());
				userData.put("location", BaseActivity.findIdByRegionName(spLocation.getSelectedItem().toString()));
				userData.put("msisdn", etMobileNumber.getText().toString());
				userData.put("birthdate", tvBdate.getText().toString());
				userData.put("gender", tvGender.getText().toString());
				userData.put("email", etEmail.getText().toString());
				String response = hypeSDK.updateProfile(userData);
				if(response.equals("ok")) {
					finish();
				} else {
					Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		mTitleTextView.setText("User Profile");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		tvGender = (TextView) findViewById(R.id.tvGenderValue);	
		etFullname = (EditText) findViewById(R.id.etName);	
		etEmail = (EditText) findViewById(R.id.etEmail);	
		etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);	
		etMobileNumber.setEnabled(false);
		spLocation = (Spinner) findViewById(R.id.etLocationValue);	
		tvBdate = (TextView) findViewById(R.id.tvBdateValue);	
		tvBdate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	 DatePickerDialog dialog = new DatePickerDialog(UpdateProfileActivity.this, date, 
	        			 myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
		                 myCalendar.get(Calendar.DAY_OF_MONTH));
	        	 dialog.show();
	        }
	    });
		
		tvGender.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            displayDialog();
	        }
	    });	
		
        myCalendar.set(Calendar.YEAR, bYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, bDay);
        myCalendar.set(Calendar.MONTH, bMonth);
        
		tvGender.setText(userData.get("gender"));
		etFullname.setText(userData.get("name"));
		etEmail.setText(userData.get("email"));
		etMobileNumber.setText(userData.get("msisdn"));
		tvBdate.setText(df1.format(myCalendar.getTime()));
		getRegions();
	}
	
	private void getRegions() {
		if(hypeSDK.getRegions().size() > 0) {
			ArrayList<String> regionName = new ArrayList<String>();
			for(HashMap<String, String> region : hypeSDK.getRegions()) {
				regionName.add(region.get("name").toString());
			}
			ArrayAdapter<String> widgetModeAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, regionName);
			widgetModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spLocation.setAdapter(widgetModeAdapter);
			spLocation.setSelection(widgetModeAdapter.getPosition(BaseActivity.findRegionNameById(userData.get("location"))));
		}  else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					getRegions();
				}
			}, 2000);
		}
	}
	
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	        myCalendar.set(Calendar.YEAR, year);
	        myCalendar.set(Calendar.MONTH, monthOfYear);
	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        updateLabel();
	    }
	};
	
	String gender = "";
	private void displayDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this)
        .setTitle("Select Gender");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				tvGender.setText(gender);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
	      	
			}
		});
		final FrameLayout frameView = new FrameLayout(UpdateProfileActivity.this);
		builder.setView(frameView);
		
		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.select_gender, frameView);
		final Spinner spMode = (Spinner) dialoglayout.findViewById(R.id.spMode);
		gender = spMode.getSelectedItem().toString();
		spMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1,
	                int arg2, long arg3) {
	        	gender = spMode.getSelectedItem().toString();
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {}       

	    });
		alertDialog.show();
	}
	
	private void updateLabel() {
	    tvBdate.setText(df1.format(myCalendar.getTime()));
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
