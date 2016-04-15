package com.download.fvd.activity.pushnotification;




import com.download.fvd.activity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;

public class PushActivity extends Activity {
	protected void onCreate(android.os.Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		String title, desc;

		try {
			title = getIntent().getExtras().getString("title");
			desc = getIntent().getExtras().getString("desc");
		} catch (Exception e) {
			title = "invalid";
			desc = "invalid";
		}

		try
		{
		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vib.vibrate(2000);
		Notify(title, desc);
		}
		catch(Exception ex){}

	}

	@SuppressWarnings("deprecation")
	private void Notify(String notificationTitle, String notificationMessage) {
		
		try
		{
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.fvd_icon,
				"New Message", System.currentTimeMillis());
		notification.flags = Notification.DEFAULT_LIGHTS
				| Notification.FLAG_AUTO_CANCEL;

		//Intent notificationIntent = new Intent(this, CircleActivity.class);
		
		 Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.fvdtube.com/"));
		 PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(this, notificationTitle,
				notificationMessage, pendingIntent);
		notificationManager.notify(121, notification);
		finish();
		}
		catch(Exception ex){}
	}

}
