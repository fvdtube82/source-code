package com.download.fvd.activity.pushnotification;

import android.content.Context;
import com.google.android.gcm.GCMBroadcastReceiver;
public class GCMReceiver extends GCMBroadcastReceiver 
{
		protected String getGCMIntentServiceClassName(Context context) 
			{
				return "com.download.fvd.activity.pushnotification.GCMIntentService";
			}
		
}
