package com.download.download2;

import com.download.fvd.activity.MainActivity;
import com.download.fvd.activity.R;
import com.download.fvd.activity.R.id;
import com.download.fvd.activity.R.layout;
import com.download.fvd.activity.R.menu;
import com.iinmobi.adsdk.AdSdk;
import com.iinmobi.adsdk.AdSize;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class DownloadListActivity extends Activity {

	public static final String DOWNLOADED = "isDownloaded";

    private static final String TAG = "DownloadListActivity";

    private ListView mDownloadingListView;

    private ListView mDownloadedListView;

    private Context mContext;

    private Button mDownloadedBtn;
    
	private StartAppAd startAppAd = new StartAppAd(this);
    private Button mDownloadingBtn;

    private ImageView mdownloding_indicator;
    private ImageView mdownloded_indicator;
    
    private ImageView back_image;
    List<DownloadTask> mDownloadinglist;

    List<DownloadTask> mDownloadedlist;

    DownloadingAdapter mDownloadingAdapter;

   // DownloadingAdapter mDownloadedAdapter;
    	DownlodedAdapter mDownloadedAdapter;
    	
    	//add integrate
    	LinearLayout NineappAppBanner1,header_banner;
    	
        private final String BANNER_PUB = "tarandeep2@BlueTickBanner";
        
        @Override
        protected void onStart() {
        	// TODO Auto-generated method stub
        	super.onStart();
        	 AdSdk.getInstance().activityStart(this);
        }
        @Override
        protected void onStop() {
        	// TODO Auto-generated method stub
        	super.onStop();
        	AdSdk.getInstance().activityStop(this);
        }
        
  	  @Override
  			public void onResume(){
  				super.onResume();
  				startAppAd.onResume();
  				show_Add();
  				
  				AdSdk.getInstance().activityResume(this);
  			}
  			
  			/**
  			 * Part of the activity's life cycle, StartAppAd should be integrated here
  			 * for the home button exit ad integration.
  			 */
  			@Override
  			public void onPause(){
  				super.onPause();
  				startAppAd.onPause();
  				
  			  AdSdk.getInstance().activityPause(this);
  			}

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
  			
  			@Override
  			protected void onDestroy() {
  				// TODO Auto-generated method stub
  				super.onDestroy();
  			    AdSdk.getInstance().activityDestory(this);
  			}
  	//end add code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StartAppSDK.init(this, "101757112", "206273549", true);
		setContentView(R.layout.download_list);
		
		//integration of 9app
		
		  AdSdk.getInstance().setAppPub(BANNER_PUB); //Need to initialize channel interface 
	        AdSdk.getInstance().start(DownloadListActivity.this);//Initialize monitoring interface 
	        AdSdk.getInstance().activityStart(DownloadListActivity.this);
	       // AdSdk.getInstance().customAppWallEntrance(DownloadListActivity.this);
	        
		//end code of 9app
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	 
	        StrictMode.setThreadPolicy(policy); 
	        } 
		 mContext = this;
		

		 //nine app integration
			
		 	NineappAppBanner1=(LinearLayout) findViewById(R.id.NineappAppBanner1);
		 	header_banner=(LinearLayout) findViewById(R.id.nine_app_header);
		 	 AdSdk.getInstance().setBannerAd(BANNER_PUB, AdSize.BANNER, NineappAppBanner1);
		 	AdSdk.getInstance().setBannerAd(BANNER_PUB, AdSize.BANNER, header_banner);
		 	 
		 	AdSdk.getInstance().notWhetherStartSilentDownload();
	        AdSdk.getInstance().noInstallWindow();


	        AdSdk.getInstance().activityStart(this);
	     //   AdSdk.getInstance().setInterstitialAd(INTERISITIAL_PUB);

	        AdSdk.getInstance().setShowAppList(false);
		 	//end nine app
		 	
		 	
	        mDownloadingBtn = (Button) findViewById(R.id.buttonDownloading);
	        mDownloadedBtn = (Button) findViewById(R.id.buttonDownloaded);
	        mdownloding_indicator=(ImageView) findViewById(R.id.downloding_indicator);
	        mdownloded_indicator=(ImageView) findViewById(R.id.downloded_indicator);
	        back_image=(ImageView) findViewById(R.id.back);
	        
	        
	        mDownloadedBtn.setOnClickListener(new OnClickListener(){

	            @Override
	            public void onClick(View v) {
	                toggleView(true);
	            }});
	        mDownloadingBtn.setOnClickListener(new OnClickListener(){

	            @Override
	            public void onClick(View v) {
	                toggleView(false);
	            }});
	        
	        
	        back_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					
					finish();
					
				} 
			});
	        
	        mDownloadingListView = (ListView) findViewById(R.id.downloadingListView);
	        
	        mDownloadedListView = (ListView) findViewById(R.id.downloadedListView);

	        toggleView(getIntent().getBooleanExtra(DOWNLOADED, false));
	        
	        mDownloadedlist = DownloadTaskManager.getInstance(mContext).getFinishedDownloadTask();
	       mDownloadedAdapter = new DownlodedAdapter(DownloadListActivity.this, 0, mDownloadedlist);

	        mDownloadedListView.setAdapter(mDownloadedAdapter);
	        /*mDownloadedListView.setOnItemClickListener(new OnItemClickListener() {
	        	
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                Log.d(TAG, "arg2" + arg2 + " mDownloadedlist" + mDownloadedlist.size());
	                onDownloadFinishedClick(mDownloadedlist.get(arg2));
	            }
	        });
	        */
	        
	        
	        mDownloadedListView.setOnItemLongClickListener(new OnItemLongClickListener() {

	            @Override
	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
	                    final long arg3) {
	                new AlertDialog.Builder(mContext)
	                        .setItems(
	                                new String[] {
	                                        mContext.getString(R.string.download_delete_task),
	                                        mContext.getString(R.string.download_delete_task_file)
	                                }, new DialogInterface.OnClickListener() {
	                                    public void onClick(DialogInterface dialog, int which) {
	                                        if (which == 0) {
	                                            Toast.makeText(mContext,
	                                                    R.string.download_deleted_task_ok,
	                                                    Toast.LENGTH_LONG).show();
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTask(mDownloadedlist.get(arg2));
	                                            mDownloadedlist.remove(arg2);
	                                            mDownloadedAdapter.notifyDataSetChanged();
	                                        } else if (which == 1) {
	                                            Toast.makeText(mContext,
	                                                   R.string.download_deleted_task_file_ok,
	                                                    Toast.LENGTH_LONG).show();
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTask(mDownloadedlist.get(arg2));
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTaskFile(mDownloadedlist.get(arg2));
	                                            mDownloadedlist.remove(arg2);
	                                            mDownloadedAdapter.notifyDataSetChanged();
	                                        }

	                                    }
	                                }).create().show();
	                return false;
	            }
	        });
	        
	        // downloading list
	        mDownloadinglist = DownloadTaskManager.getInstance(mContext).getDownloadingTask();
	        mDownloadingAdapter = new DownloadingAdapter(DownloadListActivity.this, 0, mDownloadinglist);

	        mDownloadingListView.setAdapter(mDownloadingAdapter);
	       
	        mDownloadingListView.setOnItemClickListener(new OnItemClickListener() {
	        	
	        	
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	            	
	                DownloadTask task = mDownloadinglist.get(arg2);
	                switch (task.getDownloadState()) {
	                   
	                	case PAUSE:
	                        Log.i(TAG, "PAUSE continue yes call" + task.getFileName());
	                        Log.e("Pause call by click=","Yes call");
	                        DownloadTaskManager.getInstance(mContext).continueDownload(task);
	                        //addListener(task);
	                        break;
	                    case FAILED:
	                        Log.i(TAG, "FAILED continue " + task.getFileName());
	                        DownloadTaskManager.getInstance(mContext).continueDownload(task);
	                       Log.e("Downloading Failed","yes call failed");
	                        //addListener(task);
	                        break;
	                    case DOWNLOADING:
	                        Log.i(TAG, "DOWNLOADING pause " + task.getFileName());
	                        DownloadTaskManager.getInstance(mContext).pauseDownload(task);
	                        Log.i(TAG, "PAUSE with downloading yes call" + task.getFileName());
	                        Log.e("Downloding call by click=","Yes call");
	                        
	                        
	                      /*  //by me
	                        DownloadTaskManager.getInstance(mContext).registerListener(task,
	                                new DownloadNotificationListener(mContext,task));
	                        //end
	                      */  break;
	                    case FINISHED:
	                        onDownloadFinishedClick(task);
	                        break;
	                    case INITIALIZE:
	                    		
	                    	/* //by me
	                        DownloadTaskManager.getInstance(mContext).registerListener(task,
	                                new DownloadNotificationListener(mContext,task));
	                   */
	                        break;
	                    default:
	                    	
	                        break;
	                }

	            }
	        });
	        
	        
	        
	        mDownloadingListView.setOnItemLongClickListener(new OnItemLongClickListener() {

	            @Override
	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
	                    final long arg3) {
	                new AlertDialog.Builder(mContext)
	                        .setItems(
	                                new String[] {
	                                        mContext.getString(R.string.download_delete_task),
	                                        mContext.getString(R.string.download_delete_task_file)
	                                }, new DialogInterface.OnClickListener() {
	                                    public void onClick(DialogInterface dialog, int which) {
	                                        if (which == 0) {
	                                            Toast.makeText(mContext,
	                                                    R.string.download_deleted_task_ok,
	                                                    Toast.LENGTH_LONG).show();
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTask(mDownloadinglist.get(arg2));
	                                            mDownloadinglist.remove(arg2);
	                                            mDownloadingAdapter.notifyDataSetChanged();
	                                        } else if (which == 1) {
	                                            Toast.makeText(mContext,
	                                                    R.string.download_deleted_task_file_ok,
	                                                    Toast.LENGTH_LONG).show();
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTask(mDownloadinglist.get(arg2));
	                                            DownloadTaskManager.getInstance(mContext)
	                                                    .deleteDownloadTaskFile(mDownloadinglist.get(arg2));
	                                            mDownloadinglist.remove(arg2);
	                                            mDownloadingAdapter.notifyDataSetChanged();
	                                        }

	                                    }
	                                }).create().show();
	                return false;
	            }
	        });
	        
	        for (final DownloadTask task : mDownloadinglist) {
	            if (!task.getDownloadState().equals(DownloadState.FINISHED)) {
	                Log.d(TAG, "add listener");
	                
	                addListener(task);
	            }
	        }
	        
	        DownloadOperator.check(mContext);
	        
	}
	 private void toggleView(boolean isShowDownloaded) {
	        if (isShowDownloaded) {
	        	
	        	// mDownloadedBtn.setBackgroundResource(R.drawable.download_right_tab_selected);
	           // mDownloadedBtn.setBackgroundResource(R.drawable.download_right_tab_selected);
	           // mDownloadingBtn.setBackgroundResource(R.drawable.download_left_tab);
	        	
	        	mdownloding_indicator.setVisibility(View.GONE);
	        	mdownloded_indicator.setVisibility(View.VISIBLE);
	            mDownloadedListView.setVisibility(View.VISIBLE);
	            mDownloadingListView.setVisibility(View.GONE);
	        } else {
	           // mDownloadedBtn.setBackgroundResource(R.drawable.download_right_tab);
	          //  mDownloadingBtn.setBackgroundResource(R.drawable.download_left_tab_selected);
	           
	        	mdownloding_indicator.setVisibility(View.VISIBLE);
	        	mdownloded_indicator.setVisibility(View.GONE);
	        	mDownloadedListView.setVisibility(View.GONE);
	            mDownloadingListView.setVisibility(View.VISIBLE);
	        }
	    }
	 
	 //addd integrate
	 
		
		/**
		 * Part of the activity's life cycle, StartAppAd should be integrated here
		 * for the back button exit ad integration.
		 */
	
	 class MyDownloadListener implements DownloadListener {
	        private DownloadTask task;

	        public MyDownloadListener(DownloadTask downloadTask) {
	            task = downloadTask;
	        }

	        @Override
	        public void onDownloadFinish(String filepath) {
	            Log.d(TAG, "onDownloadFinish");
	            task.setDownloadState(DownloadState.FINISHED);
	            task.setFinishedSize(task.getFinishedSize());
	            task.setPercent(100);
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                    mDownloadedAdapter.add(task);
	                    mDownloadingAdapter.remove(task);
	                    // toggleView(true);
	                }
	            });

	        }
	        
	        @Override
	        public void onDownloadStart() {
	            task.setDownloadState(DownloadState.INITIALIZE);
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                }
	            });
	        }

	        @Override
	        public void onDownloadPause() {
	            Log.d(TAG, "onDownloadPause");
	            task.setDownloadState(DownloadState.PAUSE);
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                }
	            });
	        }

	        @Override
	        public void onDownloadStop() {
	            Log.d(TAG, "onDownloadStop");
	            task.setDownloadState(DownloadState.PAUSE);
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                }
	            });
	        }

	        @Override
	        public void onDownloadFail() {
	            Log.d(TAG, "onDownloadFail");
	            task.setDownloadState(DownloadState.FAILED);
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                }
	            });
	        }

	        @Override
	        public void onDownloadProgress(final int finishedSize, final int totalSize,
	                int speed) {
	            // Log.d(TAG, "download " + finishedSize);
	            task.setDownloadState(DownloadState.DOWNLOADING);
	            task.setFinishedSize(finishedSize);
	            task.setTotalSize(totalSize);
	            
	            double number2 = (double)Math.round((double)finishedSize * 100)/(double)totalSize;
                
               // Log.e("Total percent downloding file=", String.valueOf(number2));
                int total_percent_download=(int)number2;
                //Log.e("Total percent integer=",String.valueOf(total_percent_download));
              task.setPercent(total_percent_download);
	           // task.setPercent(finishedSize*100/totalSize);
	            task.setSpeed(speed);
	            
	            DownloadListActivity.this.runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    mDownloadingAdapter.notifyDataSetChanged();
	                }
	            });
	        }
	    }

	    public void addListener(DownloadTask task) {
	        DownloadTaskManager.getInstance(mContext).registerListener(task, new MyDownloadListener(task));
	    }

	    @Override
	    protected void onNewIntent(Intent intent) {
	        toggleView(intent.getBooleanExtra(DOWNLOADED, false));

	        super.onNewIntent(intent);
	    }

	 
	 
	 
	 /**
	     * You can overwrite this method to implement what you want do after download task item is clicked.
	     * @param task
	     */
	    public void onDownloadFinishedClick(DownloadTask task) {
	        Log.d(TAG, task.getFilePath() + "/"+ task.getFileName());
	        Intent intent = DownloadOpenFile.openFile(task.getFilePath()
	                + "/"+ task.getFileName());
	        if (null == intent) {
	            Toast.makeText(mContext, R.string.download_file_not_exist, Toast.LENGTH_LONG).show();
	        } else {
	            mContext.startActivity(intent);
	        }
	    }
}
