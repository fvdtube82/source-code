package com.download.fvd.checkversion;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.download.fvd.activity.R;
import com.downoad.fdv.volley_resource.AppController;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class VersionCheckService extends IntentService{

	

	Context context;
	WindowManager.LayoutParams params;
	WindowManager wm;
	LayoutInflater inflater;
	View myView;
	LinearLayout layout;
	View cancle,install;
	public VersionCheckService() {
		super("VersionCheckService");
		// TODO Auto-generated constructor stub

	}
	@Override
	public void onCreate() {
		
		
		super.onCreate();
		
		context=getApplicationContext();
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,262184,
                PixelFormat.RGBA_8888);
        wm = (WindowManager) context.getSystemService("window");
        inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        myView = (View) inflater.inflate(R.layout.version_control_popup, null);
       layout= (LinearLayout) myView.findViewById(R.id.main_layout);
        
       cancle=myView.findViewById(R.id.cancle);
       install=myView.findViewById(R.id.install); 
       
       cancle.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			wm.removeView(myView);
			myView=null;
			stopSelf();		     
			//Toast.makeText(getApplicationContext(), "call cancle",Toast.LENGTH_LONG).show();
			
		}
	});
       install.setOnClickListener(new OnClickListener() {
   		
   		@Override
   		public void onClick(View v) {
   			
   			//Toast.makeText(getApplicationContext(), "call install",Toast.LENGTH_LONG).show();
   			//go to server for update apk
   			wm.removeView(myView);
   			myView=null;
   			Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://m.fvdtube.com/"));


   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
   			context.startActivity(intent);
   			
   			stopSelf();	
   		}
   	});
       
       
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		

		
		String  tag_string_req = "tag";
		
		//Toast.makeText(getApplicationContext(),String.valueOf(getAppVersion()) ,Toast.LENGTH_LONG).show();
        String url="http://notonlycrm.com/andriod_push/tvshows/api/verg.php";
				StringRequest strReq = new StringRequest(Method.GET,
				                url, new Response.Listener<String>() {
				 
				                    @Override
				                    public void onResponse(String response) {
				                        Log.e("stringc data=", response.toString());
				                      // Toast.makeText(getApplicationContext(), String.valueOf(response),Toast.LENGTH_LONG).show();
				                      int latest_version=Integer.valueOf(response);
				                      
				                       if(latest_version>getAppVersion())
				                       {
				                    	   wm.addView(myView, params);
				                       }
				                       
				                       		stopSelf();		                      
				 
				                    }
				                }, new Response.ErrorListener() {
				 
				                    @Override
				                    public void onErrorResponse(VolleyError error) {
				                        VolleyLog.d("error show=", "Error: " + error.getMessage());
				                       Log.e("error message=",error.getMessage());
				                       Log.e("error2=",String.valueOf(error));
				                     //  Toast.makeText(getApplicationContext(), "error in url",Toast.LENGTH_LONG).show();
				               		stopSelf();
				                      //  Toast.makeText(context, "Server or Network Probleam...", Toast.LENGTH_LONG).show();
				                        Log.e("Error Message=", String.valueOf(error));
				                        
				                    }
				                });
				 
				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
			
				
		
	}

	public int getAppVersion() {
		
		PackageInfo pInfo;
		int version = 0 ;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		  version = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
}
