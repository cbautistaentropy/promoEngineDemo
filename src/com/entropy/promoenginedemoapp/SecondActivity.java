package com.entropy.promoenginedemoapp;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.entropy.hypesdk.HypeListener;
import com.entropy.hypesdk.HypeSDK;
import com.entropy.hypesdk.model.HypeBranch;
import com.entropy.hypesdk.model.HypeItem;
import com.entropy.hypesdk.model.HypePrizeGroup;
import com.entropy.hypesdk.model.HypePromo;
import com.entropy.hypesdk.model.HypeSubscription;
import com.entropy.hypesdk.model.HypeSurvey;
import com.entropy.promoenginedemoapp.adapter.InRangeAdapter;
import com.entropy.promoenginedemoapp.adapter.SubscriptionAdapter;
import com.entropy.promoenginedemoapp.gcm.GCMSample;
import com.entropy.promoenginedemoapp.gcm.QuickstartPreferences;
import com.entropy.promoenginedemoapp.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;

public class SecondActivity extends BaseActivity implements HypeListener {

	private LinearLayout llScan;
	private LinearLayout llProfile;
	private LinearLayout llUnlink;
	private LinearLayout llGCM;
	//private LinearLayout llAllPromos;
	private ListView lvInRanges;
	private ListView lvSubscriptions;
	private Dialog dialog; 
	private static Activity activity;
	private boolean isAnsweringSurvey;
	public static HypeSurvey surveyFound;
	
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "BeamSDK";
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		activity = (Activity) SecondActivity.this;
		imageButton.setVisibility(View.INVISIBLE);
		imageButton.setImageDrawable(getResources().getDrawable(R.drawable.left_white));
		mTitleTextView.setText("Hype Demo");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		llProfile = (LinearLayout)findViewById(R.id.llUpdateProfile);
		lvInRanges = (ListView) findViewById(R.id.lvName);
		llGCM = (LinearLayout) findViewById(R.id.llGCM);
		llScan = (LinearLayout) findViewById(R.id.llScan);
		llUnlink = (LinearLayout) findViewById(R.id.llUnlink);
		lvSubscriptions = (ListView) findViewById(R.id.lvSubs);
//		llAllPromos = (LinearLayout) findViewById(R.id.llAllPromos);
		
		llScan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SecondActivity.this, ScanQRActivity.class));
			}
		});
		
//		llAllPromos.setVisibility(View.GONE);
//		llAllPromos.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(SecondActivity.this, DisplayAllPromo.class));
//			}
//		});
		
		llUnlink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAlertDialog();                                                          
			}
		});
		
		llProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SecondActivity.this, UpdateProfileActivity.class));                                                      
			}
		});
		llGCM.setVisibility(View.GONE);
		llGCM.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SecondActivity.this, GCMSample.class));                                            
			}
		});
		hypeSDK.addListener(this);
		if(RegistrationActivity.registrationCalled) {
			registerToken();
			RegistrationActivity.registrationCalled = false;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		activity = (Activity) SecondActivity.this;
		hypeSDK.addListener(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
	}
	
	private void registerToken() {
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                	Log.d("BeamSDK", "Token retrieved and sent to server! You can now use gcmsender to "
                    		+ " send downstream messages to this app.");
                } else {
                	Log.d("BeamSDK", "An error occurred while either fetching the InstanceID token,"
                    		+ " sending the fetched token to the server or subscribing to the PubSub topic. Please try"
                    		+ " running the sample again.");
                }
            }
        };
        
        if (checkPlayServices()) {
        	// Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
	}
	
	public void redemtionForPromo(Bundle data) {
		if(hypeSDK == null) {
			hypeSDK = new HypeSDK(getApplicationContext(), SecondActivity.this);
		}
		hypeSDK.redemptionForPromo(data, this);
	}
	
	private void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
		builder.setMessage("Are you sure that you really want to unlink this account?")
		   .setTitle("Confirmation")
		   .setCancelable(false)
		   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		    	   hypeSDK.unlinkAccount();
		    	   Thread thread = new Thread(new Runnable() {
		  			 @Override
		  		     public void run() {
		  				 try {
		  						InstanceID iid = InstanceID.getInstance(getApplicationContext());
		  						iid.deleteToken("885631035018","GCM");
		  						iid.deleteInstanceID();
		  					} catch (IOException e) {
		  						e.printStackTrace();
		  					}
		  			 }
		  		});
		  		thread.start();
				   startActivity(new Intent(SecondActivity.this, RegistrationActivity.class));
				   finish();
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
	
	public static void showAlertDialog(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
	    alertDialog.setTitle("GCM Message");
	   	alertDialog.setMessage(message);
	   	alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
	   	    new DialogInterface.OnClickListener() {
	   	        public void onClick(DialogInterface dialog, int which) {
    	            dialog.dismiss();
    	        }
	   		});
	    alertDialog.show();
	}
	
	private void displayDialog(Bitmap bmp) {
		try {
			if(dialog != null) {
		    	dialog.dismiss();
			} 
			
			dialog = new Dialog(SecondActivity.this, R.style.Theme_Dialog);
			dialog.setContentView(R.layout.activity_popup);
			
			try {
				ImageView imgView = (ImageView) dialog.findViewById(R.id.content);
				imgView.setImageBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("BeamSDK", e.toString());
			}
		
		    if(!isFinishing()) {
		    	dialog.show();	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void showAlertDialogSurvey(final HypeSurvey survey) {
		try {
			isAnsweringSurvey = true;
			surveyFound = new HypeSurvey();
			surveyFound = survey;
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage("Would you like to take a survey?")
			   .setTitle("Survey")
			   .setCancelable(false)
			   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
			    	   startActivity(new Intent(SecondActivity.this, SurveyActivity.class));
			       }
			   })
			   .setNegativeButton("Later", new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
			            //close alert dialog
			    	  // isAnsweringSurvey = false;
			       }
			   });
			if(!isFinishing()) {
				builder.create().show();
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("BeamSDK", e.toString());
		}
	}


	@Override
	public void failure(String error) {}

	
	
	@Override
	protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isApplicationRunning(getApplicationContext())) {
			isAnsweringSurvey = false;
		}
	}
	
	/**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true);
    	//super.onBackPressed();
    }

	@Override
	public void inRangePromo(final HypePromo promo) {
		Handler mHandler = new Handler(Looper.getMainLooper());
		mHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	if(UpdateProfileActivity.lastInrangePromoId.toString().equals(promo.getId().toString())) {
	        		
	        	} else {
	        		try {
	        			try {
	        				UpdateProfileActivity.lastInrangePromoId = promo.getId().toString();
	        				displayDialog(decodeBase64(promo.getContent()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	}
	        }
	    });
	}

	@Override
	public void inRangePromosUpdate(final ArrayList<HypePromo> promos) {
				lvInRanges.post(new Runnable() {
		    	    public void run() {
		    	    	if (lvInRanges.getAdapter() == null) {
		    	    		InRangeAdapter adap = new InRangeAdapter(getApplicationContext(), promos);
							lvInRanges.setAdapter(adap);
						} else {
						    ((InRangeAdapter)lvInRanges.getAdapter()).updateAdapter(promos);
						}
		    	    }
		    	});
		
	}

	@Override
	public void outOfRange(HypePromo promo) {
		Log.d("BeamSDK", "outOfRange promo : " + promo.getName());
	}

	@Override
	public void subscriptionUpdate(final ArrayList<HypeSubscription> subscriptions) {
		lvSubscriptions.post(new Runnable() {
    	    public void run() {
    	    	if (lvSubscriptions.getAdapter() == null) {
    	    		lvSubscriptions.setAdapter(new SubscriptionAdapter(SecondActivity.this, subscriptions));
    	    	} else {
    				 ((SubscriptionAdapter)lvSubscriptions.getAdapter()).updateAdapter(subscriptions);
    			}
    	    }
		});
	}

	@Override
	public void redemptionForPromo(HypePromo promo, HypeItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerSurveys(final ArrayList<HypeSurvey> surveys) {
		Handler mHandler = new Handler(Looper.getMainLooper());
	    mHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	if(!isAnsweringSurvey) {
	    			if(surveys.size() > 0) {
	    				showAlertDialogSurvey(surveys.get(0));
	    			}
	    		}
	        }
	    });
	}

	@Override
	public void redeemPromoCompletion(HypePromo promo, ArrayList<HypeItem> item,
			String branchId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getPromoFromQRCodeCompletion(HypePrizeGroup prizes,
			HypePromo promo, HypeBranch branch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syncFinished(boolean hasChanges) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completion(String result) {
		// TODO Auto-generated method stub
		
	}
}