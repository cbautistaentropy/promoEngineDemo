package com.entropy.promoenginedemoapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.entropy.promoenginedemoapp.R;
import com.entropy.promoenginedemoapp.SecondActivity;

public class MyAppReceiver extends IntentService {
	

	private static final String TAG = "HypeSDK";

	public MyAppReceiver() {
		super(TAG);
	}

	private void createNotification(String msg) {
		if(!msg.equals("")) {
			Intent intent = new Intent(this, SecondActivity.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent,
		                PendingIntent.FLAG_CANCEL_CURRENT);

			RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
		    contentView.setTextViewText(R.id.text, msg);
		    contentView.setTextViewText(R.id.title, "Hype Demo");
		                    
		    Notification.Builder mBuilder =  new Notification.Builder(this)
	        	.setContent(contentView)
				.setSmallIcon(R.drawable.logo)
				.setAutoCancel(true)
			    .setContentText(msg)
				.setContentTitle("Hype")
				.setContentIntent(pendingIntent)
				.setStyle(new Notification.BigTextStyle().bigText(msg));

			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(alarmSound);
			NotificationManager mNotifyMgr =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotifyMgr.notify(msg, 0, mBuilder.build());
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "my intent : " + intent.getExtras().getString("message"));
		createNotification(intent.getExtras().getString("message"));
	}

}
