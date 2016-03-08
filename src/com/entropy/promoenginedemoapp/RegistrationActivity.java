package com.entropy.promoenginedemoapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.entropy.hypesdk.HypeListener;
import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePrizeGroup;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.hypesdk.model.HypeSurvey;

public class RegistrationActivity extends BaseActivity implements HypeListener {

	private EditText etFullname;
	private EditText etEmail;
	private TextView tvBdate;
	private EditText etMobileNumber;
	private TextView tvGender;
	private Spinner spLocation;
	private ProgressDialog pDialog;
	private Calendar myCalendar = Calendar.getInstance();
	public static boolean registrationCalled;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		//hypeSDK = new HypeSDK(getApplicationContext(), RegistrationActivity.this);
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Registration");
		tvActionNext.setText("Next");
		registrationCalled = true;
		
		tvGender = (TextView) findViewById(R.id.tvGenderValue);	
		etFullname = (EditText) findViewById(R.id.etName);	
		etEmail = (EditText) findViewById(R.id.etEmail);	
		etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);	
		spLocation = (Spinner) findViewById(R.id.etLocationValue);	
		tvBdate = (TextView) findViewById(R.id.tvBdateValue);	
		tvBdate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	 DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this, date, 
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
		
		tvActionNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait", "Loading ...");
				HashMap<String, String> userData = new HashMap<String, String>();
				userData.put("name", etFullname.getText().toString());
				if(spLocation.getSelectedItem() != null) {
					userData.put("location", BaseActivity.findIdByRegionName(spLocation.getSelectedItem().toString()));
				} 
				userData.put("msisdn", etMobileNumber.getText().toString());
				userData.put("birthdate", tvBdate.getText().toString());
				userData.put("gender", tvGender.getText().toString());
				userData.put("email", etEmail.getText().toString());
				new RegisterUserAsync(userData).execute((Void) null);
				//registerUser(userData);
			}
		});
		
		getRegions();
		
	}
	private void registerUser(HashMap<String, String> userData) {
		hypeSDK.activateWithMobileNumber(this, userData);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this)
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
		final FrameLayout frameView = new FrameLayout(RegistrationActivity.this);
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
	
    private class RegisterUserAsync extends AsyncTask<Void, Void, String> {
		
    	HashMap<String, String> userData = new HashMap<String, String>();
		
		public RegisterUserAsync(HashMap<String, String> userData) {
			this.userData = userData;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			registerUser(userData);
			return null;
	    }
	   
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
	public void failure(final String error) {
		pDialog.dismiss();
		Handler mHandler = new Handler(getMainLooper());
	    mHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	showAlertDialog(error, "Error");
	        }
	    });
	}

	@Override
	public void getPromoFromQRCodeCompletion(HypePrizeGroup prizes,
			HypePromo promo, HypeBranch branch) {}

	@Override
	public void completion(String result) {
		pDialog.dismiss();
		startActivity(new Intent(RegistrationActivity.this, SecondActivity.class));
		finish();
	}
	
	private void showAlertDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
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
	public void syncFinished(boolean hasChanges) {}

	@Override
	public void redemptionForPromo(HypePromo promo, HypeItem item) {}

	@Override
	public void redeemPromoCompletion(HypePromo promo, ArrayList<HypeItem> item,
			String branchId) {}

}