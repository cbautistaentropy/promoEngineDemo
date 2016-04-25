package com.entropy.promoenginedemoapp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy.hypesdk.HypeSDK;

public class BaseActivity extends Activity {
	
	public SimpleDateFormat df1 = new SimpleDateFormat("MMMM/dd/yyyy");
	public SimpleDateFormat df2 = new SimpleDateFormat("MMM dd,yyyy, h:mm:ss a");
	public SimpleDateFormat df = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy");
	
	public static String promoId = "";
	public static HypeSDK hypeSDK;
	public static TextView mTitleTextView;
	public static TextView tvActionNext;
	public static ImageView imageButton;
	
	public static SharedPreferences sharedPref;
	public static DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
	private static Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getApplicationContext();
		sharedPref =  getSharedPreferences("promoEngine", Context.MODE_PRIVATE);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams actionBarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		 
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    mCustomView.setLayoutParams(actionBarParams);
		imageButton = (ImageView) mCustomView.findViewById(R.id.menuIcon);
		 
		mTitleTextView = (TextView) mCustomView.findViewById(R.id.tvTitleActionBar);
		tvActionNext = (TextView) mCustomView.findViewById(R.id.tvActionNext);

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true); 
	}
	
	public static String getSharedPref(Context cont, SharedPreferences sharedpreferences, String key){
		if(sharedpreferences.contains(key)) {
			return sharedpreferences.getString(key, "");
		} else {
			return "";
		}
	}
	
	public static void setSharedPref(Context cont,SharedPreferences sharedpreferences, String key, String details){
		sharedpreferences.edit().remove(key).commit();
		sharedpreferences.edit().putString(key, details).commit();	
	}
	
	public static String findIdByRegionName(String regionName) {
		ArrayList<HashMap<String, String>> regions = hypeSDK.getRegions();
		for(HashMap<String, String> region : regions) {
			if(regionName.equals(region.get("name"))) {
				return region.get("id");
			}
		}
		
		return "";
	}
	
	public static String findRegionNameById(String id) {
		ArrayList<HashMap<String, String>> regions = hypeSDK.getRegions();
		for(HashMap<String, String> region : regions) {
			if(id.equals(region.get("id"))) {
				return region.get("name");
			}
		}
		
		return "";
	}
	
	public static Bitmap decodeBase64(String input) {
		try {
			return ImageBase64.decodeBase64(input, ctx);
//			Bitmap bitmap = ImageBase64.decodeBase64(input, ctx);
//			byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
//			BitmapFactory.Options opt = new BitmapFactory.Options(); 
//			opt.inJustDecodeBounds = false;
//			opt.inSampleSize = calculateInSampleSize(opt, 100, 100);
//			return  BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, opt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Bitmap decodeSampledBitmapFromResource(byte[] decodedByte) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(decodedByte, 0, 100, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 100, 100);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(decodedByte, 0, 100, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	public static ArrayList<HashMap<String, String>> removeDuplicates(ArrayList<HashMap<String, String>> list) {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		HashSet<HashMap<String, String>> set = new HashSet<HashMap<String, String>>();
		for (HashMap<String, String> item : list) {
			if (!set.contains(item)) {
				result.add(item);
				set.add(item);
		    }
		}
		return result;
	}
	
	
	public boolean isApplicationRunning(final Context context) {
		 ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		    List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		    for(int i = 0; i < procInfos.size(); i++)  {
		        if(procInfos.get(i).processName.equals(context.getPackageName())) {
		          return true;
		        }
		    }
		    return false;
		
	}
	
	public boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	    	ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	        	return true;
	        }
	    }
	    return false;
	}

}
