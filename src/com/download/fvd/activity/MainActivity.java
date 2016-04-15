/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.download.fvd.activity;

import java.io.BufferedReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.download.download2.DownloadListActivity;
import com.download.download2.DownloadState;
import com.download.download2.DownloadTask;
import com.download.download2.DownloadTaskManager;
import com.download.fvd.activity.pushnotification.Constants;
import com.download.fvd.activity.pushnotification.SharePref;
import com.download.fvd.checkversion.VersionCheckService;
import com.download.fvd.networkchecker.NetworkChecker;
import com.downoad.fdv.volley_resource.AppController;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.iinmobi.adsdk.AdSdk;

import com.mobileapptracker.MobileAppTracker;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;


public class MainActivity extends FragmentActivity implements OnClickListener{

	/** StartAppAd object declaration */
	private StartAppAd startAppAd = new StartAppAd(this);
	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private Drawable oldBackground = null;
	private int currentColor = 0xFF666666;
	EditText youtube_searchbox;
	
	ImageView downloding_list,next,prev,tag,searchbutton,share_link,home;
	LinearLayout footer_layout;
	public static int current_tab_Pos=0;
	 NetworkChecker checker;
	public MobileAppTracker  mobileAppTracker;
	SharePref share_pref;
	
	//integrate add for 9 app
	  private final String BANNER_PUB = "tarandeep2@BlueTickBanner";
	//add integrate
	  @Override
			public void onResume(){
				super.onResume();
				startAppAd.onResume();
				show_Add();
			}
			
			/**
			 * Part of the activity's life cycle, StartAppAd should be integrated here
			 * for the home button exit ad integration.
			 */
		

	//end add code
	  public void show_Add() {
			startAppAd.showAd(new AdDisplayListener() {
				
				/**
				 * Callback when Ad has been hidden
				 * @param ad
				 */
				@Override
				public void adHidden(Ad ad) {
					
					// Run second activity right after the ad was hidden
					
				}

				/**
				 * Callback when ad has been displayed
				 * @param ad
				 */
				@Override
				public void adDisplayed(Ad ad) {
					
				}

				/**
				 * Callback when ad has been clicked
				 * @param ad
				 */
				@Override
				public void adClicked(Ad arg0) {
					
				}

				@Override
				public void adNotDisplayed(Ad arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
//end add code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "101757112", "206273549", true);
		setContentView(R.layout.activity_main);
	//integrate add for 9app 
		  
        AdSdk.getInstance().setAppPub(BANNER_PUB); //Need to initialize channel interface 
        AdSdk.getInstance().start(MainActivity.this);//Initialize monitoring interface 
        AdSdk.getInstance().activityStart(MainActivity.this);
        //AdSdk.getInstance().customAppWallEntrance(MainActivity.this);
        
        
        
        
        
        AdSdk.getInstance().notWhetherStartSilentDownload();
        AdSdk.getInstance().noInstallWindow();


        AdSdk.getInstance().activityStart(this);
     //   AdSdk.getInstance().setInterstitialAd(INTERISITIAL_PUB);

        AdSdk.getInstance().setShowAppList(false);
		//end code for 9app
		
		//check version by service
	
		Intent msgIntent = new Intent(this, VersionCheckService.class);
		
		startService(msgIntent);
		inisilizeComponent ();
		//show add
		
		//internet connection
		checker=new NetworkChecker(MainActivity.this);
		if(!checker.isConnectingNetwork())
		{
			checker.showInternetDialog();
		}
		
		//call mat
		share_pref=new SharePref(MainActivity.this);
		MAT();
		SharedPreferences myPrefs = getSharedPreferences("fvdTube_prefs", MODE_PRIVATE);

		String is_registered = myPrefs.getString(
				"is_registered_on_application_server","");
		
		
		// push notification call start  
        if (share_pref.getIP_Share_pref() ==""
                || share_pref.getCountry_Share_pref() =="" || is_registered==null || is_registered=="")  {
            try {
            	
            //	Toast.makeText(getApplicationContext(),"execute asyn" ,Toast.LENGTH_LONG).show();
            	
                new AsyncgetCIP(this).execute();
                
                //System.out.println("parsestart-----------------");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } else 
        {
            Constants.IP_address = share_pref.getIP_Share_pref();
            Constants.COUNTRY_NAME = share_pref.getCountry_Share_pref();
            Log.e("get ip by shared prefence=",String.valueOf( Constants.IP_address));
            Log.e("get country code bt shared pref =",String.valueOf( Constants.COUNTRY_NAME));
          //  Toast.makeText(getApplicationContext(),"go to onRegistr" ,Toast.LENGTH_LONG).show();
        	
            doGCMRegister(MainActivity.this);
           
            
            
            //System.out.println("parseelse-----"+Constants.IP_address+"name--"+Constants.COUNTRY_NAME);
        }
        // push notification call end 
		/*getActionBar().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	*/
        
        
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		
		pager.setAdapter(adapter);
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setIndicatorHeight(5);
		tabs.setDividerColor(Color.parseColor("#000000"));
		tabs.setIndicatorColor(Color.parseColor("#FF7E00"));
		
	
		//tabs.setTextColor(Color.parseColor("#000000"));
		tabs.setViewPager(pager);
			
		
		
		tabs.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "call"+String.valueOf(pos),Toast.LENGTH_LONG).show();
				current_tab_Pos=pos;
				
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//changeColor(currentColor);
		
		//call status
		//status();
		//call service for if exit apps then trigger
		startService(new Intent(MainActivity.this,com.download.download2.DownloadOperator.PrimeService.class));
	}
	
	
	public void inisilizeComponent () {
		
		youtube_searchbox=(EditText) findViewById(R.id.youtube_searchbox);
		downloding_list=(ImageView) findViewById(R.id.youtube_download_list);
		footer_layout=(LinearLayout) findViewById(R.id.footer_include_layout);
		searchbutton=(ImageView) findViewById(R.id.youtube_search_button);
		
		tag=(ImageView) findViewById(R.id.tag);
		
		prev=(ImageView) footer_layout.findViewById(R.id.prev);
		next=(ImageView) footer_layout.findViewById(R.id.next);
		share_link=(ImageView) footer_layout.findViewById(R.id.share_link);
		home=(ImageView) findViewById(R.id.home);
		downloding_list.setOnClickListener(this);
		//youtube_searchbox.setOnClickListener(this);
		tag.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);
		share_link.setOnClickListener(this);
		home.setOnClickListener(this);
		searchbutton.setOnClickListener(this);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		//changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
			getActionBar().hide();
		}
		
		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};
	//<!--  xmlns:app1="http://schemas.android.com/apk/res/com.download.fvd.activity" -->
	//   android:layout_above="@+id/colors"
    //android:layout_below="@+id/tabs"
	public class MyPagerAdapter extends FragmentPagerAdapter implements IconTabProvider{

		//private final String[] TITLES = { "Categories", "Home", "Top Paid"};
			
		final int PAGE_COUNT = 3;
	    private int tabIcons[] = {R.drawable.youtube,R.drawable.popular,R.drawable.mp3};
	    
	    
	    
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		
		@Override
		public int getCount() {
			return tabIcons.length;
		}
		
		@Override
		public Fragment getItem(int position) {
		
			
			if(position==0)
			{
				//Toast.makeText(getApplicationContext(), "call 0",Toast.LENGTH_LONG).show();
				return new YoutubePageFragment();
				
			}
			else if(position==1)
			{
				return new PopularAppsFragment();
			}
			else if(position==2)
			{
				return new PopularGamesFragment();
			}
			else 
			{
				//Toast.makeText(getApplicationContext(), "call 1",Toast.LENGTH_LONG).show();
				return SuperAwesomeCardFragment.newInstance(position);
			}
			//Toast.makeText(getApplicationContext(),"tab number="+ String.valueOf(position),Toast.LENGTH_LONG).show();
			//return SuperAwesomeCardFragment.newInstance(position);
		}
		
		@Override
		public int getPageIconResId(int position) {
			// TODO Auto-generated method stub
			  return tabIcons[position];
		}
		
		
	}
	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.youtube_searchbox:
			//Toast.makeText(getApplicationContext(), "call search button",Toast.LENGTH_LONG).show();
			Intent google_page=new Intent(MainActivity.this,SearchPage.class);
			startActivity(google_page);
			break;
		case R.id.youtube_download_list:
			Intent i = new Intent(MainActivity.this, DownloadListActivity.class);
			 i.putExtra(DownloadListActivity.DOWNLOADED, false);
	            startActivity(i);
			//Toast.makeText(getApplicationContext(), "Downloding List",Toast.LENGTH_LONG).show();
			/*startAppAd.showAd(new AdDisplayListener() {
				
				*//**
				 * Callback when Ad has been hidden
				 * @param ad
				 *//*
				@Override
				public void adHidden(Ad ad) {
					
					// Run second activity right after the ad was hidden
					Intent i = new Intent(MainActivity.this, DownloadListActivity.class);
					 i.putExtra(DownloadListActivity.DOWNLOADED, false);
			            startActivity(i);
				}

				*//**
				 * Callback when ad has been displayed
				 * @param ad
				 *//*
				@Override
				public void adDisplayed(Ad ad) {
					
				}

				*//**
				 * Callback when ad has been clicked
				 * @param ad
				 *//*
				@Override
				public void adClicked(Ad arg0) {
					
				}

				@Override
				public void adNotDisplayed(Ad arg0) {
					// TODO Auto-generated method stub
					
				}
			});*/
			
			
			
			break;
			
		case R.id.prev:
			
			
		if(current_tab_Pos==0)
		{//go to next web page 
			
			if(YoutubePageFragment.youtube_webview.canGoBack())
			{
				YoutubePageFragment.youtube_webview.goBack();
				//Toast.makeText(getApplicationContext(), "go back",Toast.LENGTH_SHORT).show();
			}
			
		}
			
			
			
			break;
		case R.id.next:
			
			if(current_tab_Pos==0)
			{
				//go to next web page 
				if(YoutubePageFragment.youtube_webview.canGoForward())
				{
					YoutubePageFragment.youtube_webview.goForward();
					//Toast.makeText(getApplicationContext(), "go forwoed",Toast.LENGTH_SHORT).show();
				}
				
			}
			
			
		break;
		
		case R.id.tag:
			/*Intent downloading_page=new  Intent(MainActivity.this,DownlodingList.class);
			startActivity(downloading_page);*/
			//Toast.makeText(getApplicationContext(), "call tag",Toast.LENGTH_LONG).show();
			//Intent top_app=new Intent(MainActivity.this,Top_Apps.class);
			//google_page_search.putExtra("search_keyword",youtube_searchbox.getText().toString().trim());
			
			//startActivity(top_app);
			AdSdk.gotoAppList(MainActivity.this);
			break;
		
		case R.id.youtube_search_button:
			
			if(String.valueOf(youtube_searchbox.getText()).trim().length()!=0)
			{
				//do something
				Intent google_page_search=new Intent(MainActivity.this,SearchPage.class);
				google_page_search.putExtra("search_keyword",youtube_searchbox.getText().toString().trim());
				
				startActivity(google_page_search);
				youtube_searchbox.setText(" ");
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Enter Search keyword",Toast.LENGTH_LONG).show();
				
			}
			
			break;
			
		//for share app link	
		case R.id.share_link:
			
			 Intent intent = new Intent(Intent.ACTION_SEND);
             intent.setType("text/plain");
             intent.putExtra(Intent.EXTRA_TEXT, "http://m.fvdtube.com/FVDTube.apk"+"\n from best powerful video downloder !");
            // intent.putExtra(android.content.Intent.EXTRA_SUBJECT, task.getFileName()+);
             startActivity(Intent.createChooser(intent, "Share"));
		
			break;
			case R.id.home:
				
				pager.setCurrentItem(0,false);
				break;
			
		default:
			break;
		}
		
		
		//Toast.makeText(getApplicationContext(), "call",Toast.LENGTH_LONG).show();
		//Intent call_google_page=new Intent(MainActivity.this,SearchActivity.class);
		//startActivities(intents);
		
	}
	
	
	//MAT method write here (mobile add tech..)
	 private void MAT() {
	        // Initialize MAT
		 
	        MobileAppTracker.init(getApplicationContext(),
	                Constants.MAT_ADVERTISMENT_ID, Constants.MAT_CONVERSION_KEY);
	        mobileAppTracker = MobileAppTracker.getInstance();
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                // See sample code at
	                // http://developer.android.com/google/play-services/id.html
	                try {
	                    String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
	                            .getDeviceId();
	                    mobileAppTracker.setDeviceId(deviceId);
	                    
	                    Info adInfo = AdvertisingIdClient
	                            .getAdvertisingIdInfo(getApplicationContext());
	                    mobileAppTracker.setGoogleAdvertisingId(adInfo.getId(),
	                            adInfo.isLimitAdTrackingEnabled());
	                } catch (Exception e) {

	                    // Encountered an error getting Google Advertising Id, use
	                    // ANDROID_ID
	                    mobileAppTracker.setAndroidId(Secure.getString(getContentResolver(), Secure.ANDROID_ID));
	                }
	            }
	        }).start();

	    }

	 
	 
	 //create inner class 
	 public class AsyncgetCIP extends AsyncTask<Void, Void, BufferedReader> {

	        private String ip = "";
	        // private String token = "";
	        // private String device_id = "";
	        String countryname = "";
	        public static final String MyPREFERENCESCountry = "countryPref";

	        public AsyncgetCIP(Activity iNSTANCE) {
	        	//Toast.makeText(getApplicationContext(), "call asyn ready",Toast.LENGTH_LONG).show();
	        	
	        }

	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            Log.e("call onPreExecute","yes call");
	        }

	        @Override
	        protected BufferedReader doInBackground(Void... arg0) {

	            try {
	            	
	            	  Log.e("call doBackground","yes call");
	                HttpPost request = new HttpPost(
	                        "http://api.appmoney.mobi/index.php/listc/getRealIpAddr");

	                request.setHeader("Accept", "application/json");
	                request.setHeader("Content-type", "application/json");
	                // Build JSON string
	                JSONStringer myrequest = new JSONStringer().object()
	                        .endObject();

	                Log.e("Execute Asyn task for fetch ip,country code=","yes call");
	                // JSON REquest =======

	                Log.i("JSON REquest  : ", myrequest.toString());

	                StringEntity entity = new StringEntity(myrequest.toString());

	                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
	                        "application/json"));
	                entity.setContentType("application/json");
	                request.setEntity(entity);
	                // Send request to WCF service
	                DefaultHttpClient httpClient = new DefaultHttpClient();
	                HttpResponse response = httpClient.execute(request);
	                HttpEntity httpEntity = response.getEntity();
	                String xml = EntityUtils.toString(httpEntity);
	                // Log.i("JSON REsponse length ", "" + xml);
	                JSONObject jsnobj = new JSONObject(xml);
	                String response_12 = jsnobj.getString("cip");
	                // Log.i("JSON REsponse ", "" + response_12);
	                JSONArray jsonarray = new JSONArray(response_12);
	                Log.e("Execute Asyn after execute =","yes call");
	                int arraylength_new12 = jsonarray.length();
	                for (int i = 0; i < arraylength_new12; i++) {

	                    JSONObject jsonobjarray = jsonarray.getJSONObject(i);
	                    ip = jsonobjarray.getString("ip");
	                    countryname = jsonobjarray.getString("country_code");
	                    Constants.IP_address = ip;
	                    Constants.COUNTRY_NAME = countryname;
	                    share_pref.setIP_Share_pref(Constants.IP_address);
	                    share_pref.setCountry_Share_pref(Constants.COUNTRY_NAME);
	                    
	                    Log.e("IP=",String.valueOf(ip));
	                    Log.e("countryname=",String.valueOf(countryname));
	                    
	                }
	            } catch (Exception e) {

	            		Log.e("Error message in MainActivity=",String.valueOf(e));
	            		Log.e("Error Msg=",String.valueOf(e.getMessage()));
	            		
	            }

	            return null;
	        }

	        @Override
	        protected void onPostExecute(BufferedReader result) {
	            try {
	            	 Log.e("call doPost","yes call");
	              if(share_pref.getIP_Share_pref()!="")
	              {
	            	  doGCMRegister(MainActivity.this);
	              }
	            	
	            } catch (UnsupportedOperationException e) {
	            }
	            super.onPostExecute(result);
	        }
	    }
	 
	 void doGCMRegister(Context con) {

		 
	        checkNotNull(Constants.SENDER_ID, "SENDER_ID");
	        GCMRegistrar.checkDevice(getApplicationContext());
	        GCMRegistrar.checkManifest(getApplicationContext());
	        String regId = GCMRegistrar.getRegistrationId(getApplicationContext());

	        
	        SharedPreferences myPrefs = getSharedPreferences("fvdTube_prefs", MODE_PRIVATE);

			String is_registered = myPrefs.getString(
					"is_registered_on_application_server","");
			
				
			
		

	        
	        if (regId.equals("") || share_pref.getIP_Share_pref()=="" || is_registered== null || is_registered=="" ) {
	        	
	            GCMRegistrar.register(MainActivity.this,Constants.SENDER_ID);
	            // Toast.makeText(getApplicationContext(),"not now register",Toast.LENGTH_SHORT).show();
	        } else {
	            // Toast.makeText(getApplicationContext(),"Allready register",Toast.LENGTH_SHORT).show();
	        }
	    }

	    private void checkNotNull(Object reference, String name) {
	        if (reference == null) {
	            throw new NullPointerException("error occured!!");
	        }
	    }
	 
	   /* //get file downloding status by me
	    
	    public void status()
	    {
	    	Toast.makeText(getApplicationContext(), "status call",Toast.LENGTH_LONG).show();
	    	List<DownloadTask> mDownloadinglist=null;
	    	Log.e("call status function=","yes call ");
    		 
	    	mDownloadinglist = DownloadTaskManager.getInstance(MainActivity.this).getDownloadingTask();
	    	 for (final DownloadTask task : mDownloadinglist) {
	    		 Toast.makeText(getApplicationContext(), "status call loop",Toast.LENGTH_LONG).show();  
	    		Log.e("call status loop=","yes call ");
	    		
	    		 if (!task.getDownloadState().equals(DownloadState.FINISHED)) {
		               // Log.d(TAG, "add listener");
		                	Log.e("Running file name=File ",task.getFileName() );
		                	Toast.makeText(getApplicationContext(), "call looper="+task.getFileName(),Toast.LENGTH_LONG).show();
		                	
		                	Log.e("call status inner for each=","yes call ");		
		                //addListener(task);
		            }
		        }
	    }
	    //end
	     * 
	     * 
*/
	    //add add
	  
}