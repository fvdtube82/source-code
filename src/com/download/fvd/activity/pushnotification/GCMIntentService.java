package com.download.fvd.activity.pushnotification;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {
	protected static final int MODE_PRIVATE = 0;
	String deviceId;
	String push_url;
    Context context;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	Location location;
	Double MyLat, MyLong;
	String CountryName, StateName;

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	Handler mHandler = new Handler();

	public String getDeviceId() {
		if (deviceId != null && deviceId.length() > 0) {
			// do_nothing
		} else {
			String android_id = Secure.getString(getContentResolver(),
					Secure.ANDROID_ID);
			deviceId = android_id;
			System.out.println("getdevice id"+deviceId);
		}
		return deviceId;
	}
	
	protected void onRegistered(final Context context, String registrationId) {
		//Log.i(TAG, "Device registered: regId = " + registrationId);
		System.out.println("online_movieTokenId " + registrationId);
		Log.e("Call onRegister method=","Yes call");
           // put URL_PUSHNOTIFICATION & IP_address
		
		/*push_url = Constants.URL_PUSHNOTIFICATION + registrationId
				+ "&device_id=" + getDeviceId() + "&c_ip="
				+ Constants.COUNTRY_NAME;
		*/
		 push_url = Constants.PUSH_NOTIFICATION_URL+"token="+registrationId+"&device_id="+getDeviceId()+"&state="+""+"&cname="+Constants.IP_address;
		 
         System.out.println("push_url=="+push_url);
		SharedPreferences myPrefs = context.getSharedPreferences("fvdTube_prefs", MODE_PRIVATE);

		String is_registered = myPrefs.getString(
				"is_registered_on_application_server","");
		

		if (is_registered != null && is_registered.length() > 0
				&& is_registered.equalsIgnoreCase("Yes")) {
			// device already registered on application server. no need to do
			// anything.
			System.out.println("mregistered on application server yes yes");
			//Toast.makeText(getApplicationContext(), "already register on server..",Toast.LENGTH_LONG).show();
			
		} 
		else 
		{
			System.out.println("mregistered on application server no no");
			//Toast.makeText(getApplicationContext(), "Not register on server..",Toast.LENGTH_LONG).show();

			new AsyncTask<Void, Void, Void>() {
				
				String response_text;
				String message_text, response_type;
				@Override
				protected Void doInBackground(Void... params) {

					JSONObject object=null;
					if(Constants.IP_address!="")
					{
						
						 	
							try {
								
								 object = FVDJsonParser.getInstance()
											.getJSONFromUrl(push_url);
									 
									 
								
						 if(object!=null)
											
						 {	
								response_text = object.getString("status");
								message_text = object.getString("message");
								response_type = object.getString("responseType");
								Log.e("Message text ==",String.valueOf(message_text));
								if (response_text.equalsIgnoreCase("y")) {

									SharedPreferences myPrefs = context
											.getSharedPreferences(
													"fvdTube_prefs",
													MODE_PRIVATE);
									SharedPreferences.Editor prefsEditor = myPrefs
											.edit();
									prefsEditor.putString("is_registered_on_application_server",
											"Yes");
									prefsEditor.commit();
								}
								} else {
									// do nothing
								}

							} catch (JSONException e) {
								e.printStackTrace();
								Log.e("GCMIntent SERVICE=",e.getMessage());
								
							}
						 Log.e("yes  ip Address is not null go to server for register=","ip is not  null");
					}
					else
					{
						Log.e("yes IP address is null so not go to server=","yes not go to server");
						
					}
					
					


					return null;
				}

				protected void onPostExecute(Void result) {

					super.onPostExecute(result);
					
				if(Constants.IP_address!="")
				{
					//check refid or other data submited or not on server
					SharedPreferences myPrefs = context
							.getSharedPreferences("fvdTube_prefs",MODE_PRIVATE);
					String is_registered = myPrefs.getString(
							"is_registered_on_application_server","");
					
					if(is_registered != null && is_registered.length() > 0 && is_registered.equalsIgnoreCase("Yes"))
					{
					
					if (response_text.equalsIgnoreCase("y")) {

						mHandler.post(new Runnable() {

							@Override
							public void run() {
								/*
								 * Toast.makeText(context, message_text,
								 * Toast.LENGTH_LONG).show();
								 */
							}
						});

					} else {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								/*
								 * Toast.makeText(context, message_text,
								 * Toast.LENGTH_LONG).show();
								 */
							}
						});

					}
				}
				}
				};
			}.execute();
		}

	}

	protected void onUnregistered(final Context context, final String text) {
		//Log.i(TAG, "unregistered = " + text);

	}

	protected void onMessage(Context context, Intent intent) {

		// final String title = intent.getExtras().getString("title");
		// final String description = intent.getExtras().getString("desc");

//		System.out.println("mValue message recied "
//				+ intent.getExtras().getString("message"));
		
	//	SharedPreferences myPrefs = context.getSharedPreferences("fvdTube_prefs", MODE_PRIVATE);

		/*String is_registered = myPrefs.getString(
				"is_registered_on_application_server","");
		
Log.e("Call onMessage check prefence=",is_registered);
*/

	Log.e("Call OnMessage=","yes call OnMessage");
		try
		{
		Intent i = new Intent(context, PushActivity.class);
		i.putExtra("title", intent.getExtras().getString("title"));
		i.putExtra("desc", intent.getExtras().getString("desc"));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		}
		catch(Exception ex){}
		// Intent intent1 = new Intent(context, Hair1.class);
		// intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent1);

		// NotificationManager mNotificationManager = (NotificationManager)
		// context
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		//
		// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		// new Intent(context, Makeup1.class), 0);
		//
		// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		// context)
		// .setSmallIcon(R.drawable.makeuptext)
		// .setContentTitle("MCW Now Notification")
		// .setAutoCancel(true)
		// .setStyle(
		// new NotificationCompat.BigTextStyle().bigText(intent
		// .getExtras().getString("message")))
		// .setContentText(intent.getExtras().getString("message"));
		//
		// mBuilder.setContentIntent(contentIntent);
		// mNotificationManager.notify(1, mBuilder.build());

		//Log.i(TAG, "new message= ");
	}

	protected void onError(final Context context, final String errorId) {

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// Toast.makeText(context, "error occurred " + errorId,
				// Toast.LENGTH_LONG).show();
			}
		});

		//Log.i(TAG, "Received error: " + errorId);
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	// //////////////////////////////////////////////////////////////////////GETTING
	// COUNTRY NAME

	/**
	 * Check the type of GPS Provider available at that instance and collect the
	 * location informations
	 * 
	 * @Output Latitude and Longitude
	 * */

}