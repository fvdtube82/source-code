package com.download.fvd.activity;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class Play_Youtube_Video extends ActionBarActivity {

	ScaleGestureDetector scalegesture;

	//use for relative layout.....
	View rootview;
	static final int MIN_WIDTH = 100;
	 VideoView videoView;
	 ProgressDialog progress_dialog_loding;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play__youtube__video);
		rootview=findViewById(R.id.root_view);
		
		  try {
		        Bundle video_url_bundle= getIntent().getExtras();
		       String video_url=video_url_bundle.getString("video_url").trim();
		        
			  
		       // String link="http://r8---sn-ci5gup-qxal.googlevideo.com/videoplayback?mime=video%2Fmp4&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Cmime%2Cmm%2Cms%2Cmv%2Cpl%2Cratebypass%2Csource%2Cupn%2Cexpire&ipbits=0&fexp=916636%2C938524%2C9406983%2C9408142%2C9408710%2C945137%2C948124%2C952612%2C952637%2C952642&initcwndbps=300000&upn=zAlECDdUZ84&id=o-ADnu_S2Zh988Xag_09Oc_ITW1BQmQDpAIowyhzF5vM-9&expire=1432033484&mm=31&signature=59817B9375C065F8F66EEF82C99DC1DB4747E01B.5555E5A5A18E31EDED940061804630FE1BA8867E&ratebypass=yes&ms=au&source=youtube&sver=3&mv=m&dur=2649.211&pl=20&key=yt5&ip=122.177.60.104&mt=1432011854&itag=22&fallback_host=tc.v14.cache6.googlevideo.com";
		        videoView = (VideoView) findViewById(R.id.videoView1);
		        
		        
		        progress_dialog_loding=new ProgressDialog(this);
		        progress_dialog_loding.setMessage("Loading video...");
		        progress_dialog_loding.setCancelable(false);
		        progress_dialog_loding.show();
		        MediaController mediaController = new MediaController(this);
		        mediaController.setAnchorView(videoView);
		        Uri video = Uri.parse(video_url);
		        videoView.setMediaController(mediaController);
		        videoView.setVideoURI(video);
		        videoView.setBackgroundColor(Color.TRANSPARENT);
		       // videoView.start();
		       
		        //inislize scalegasture
		      scalegesture =new ScaleGestureDetector(this,new MyScaleGastureListener());
		     
		       
		    } catch (Exception e) {
		        // TODO: handle exception
		        Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
		    }
		  
		  videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				
				progress_dialog_loding.dismiss();
				videoView.start();
				
			}
		});
		  videoView.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				
				progress_dialog_loding.dismiss();
				return false;
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		//Toast.makeText(getApplicationContext(), "call touch evnt",Toast.LENGTH_LONG).show();
		scalegesture.onTouchEvent(event);
		//soundDetect(event);
		
		return true;
	}
	public void callgasture(MotionEvent event) {
		
	}
	//http://s1133.photobucket.com/user/Anniebabycupcakez/media/1489568335355_1995.mp4.html
	
	class MyScaleGastureListener implements OnScaleGestureListener
	{
		private int mw,mh;
		

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			
			mw*=detector.getScaleFactor();
			mh*=detector.getScaleFactor();
			if(mw<MIN_WIDTH)
			{
				mw=videoView.getWidth();
				mh=videoView.getHeight();
				
			}
			Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + mw + ", h=" + mh);
			setFixedVideoSize(mw, mh); // important
			
		//	rootview.setLayoutDirection(Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			
			
			rootview.getLayoutParams().width = mw;
			rootview.getLayoutParams().height = mh;
			
			//Toast.makeText(getApplicationContext(), "on scale", Toast.LENGTH_LONG).show();
			
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			
			mw = videoView.getWidth();
			mh = videoView.getHeight();
			Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + mw + ", h=" + mh);
			//Toast.makeText(getApplicationContext(), "on scale begin", Toast.LENGTH_LONG).show();
			
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			
		//	Toast.makeText(getApplicationContext(), "on scale ended", Toast.LENGTH_LONG).show();
			
		}
		
		
	}
	
	public void setFixedVideoSize(int width, int height)
    {
		videoView.getHolder().setFixedSize(width, height); 
		
    }
	
	
	
	
	
} 

