package com.download.fvd.activity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.download.download2.DownloadNotificationListener;
import com.download.download2.DownloadTask;
import com.download.download2.DownloadTaskManager;
import com.download.fdv.youtubesdk.YoutubeFileExtractor.youtubedatalistener;
import com.download.fvd.networkchecker.NetworkChecker;
import com.downoad.fdv.volley_resource.AppController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YoutubePageFragment extends Fragment implements
		youtubedatalistener {
	View view;
	public static WebView youtube_webview;
	ImageView download;
	String youtube_url = "http://m.youtube.com/";
	Animation download_button_animation;
	Hashtable urlParameters;
	String videoId = null;
	ListView download_formate_list;
	public static int popup_current_row_index;
	// this variable use in popup for show downloding formate

	String video_time_duration, video_title, video_image;
	ArrayList<Youtube_Data_Setter_Getter> youtube_data_refrence;
	NetworkImageView video_img;
	YoutubeAdapter youtubeAdapter;
	Dialog youtube_dialog;
	
	
	
	
	
	private static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	// ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	NetworkChecker networkChecker;
	
	
	//show dialog for wait..
	ProgressDialog dialog;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater
				.inflate(R.layout.youtubepage_fragment, container, false);
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("Loading...");
		dialog.show();
		inisilizeComponent();
		
		//this class is use to check internet connection
		networkChecker=new NetworkChecker(getActivity());
		
		download_button_animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.a_shake);
		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			
				
				//Toast.makeText(getActivity(), youtube_webview.getUrl(),
						//Toast.LENGTH_LONG).show();
				Log.e("youtube url=", String.valueOf(youtube_webview.getUrl()));
				String url = youtube_webview.getUrl();

				// call class for fetch youtube data
				new com.download.fdv.youtubesdk.YoutubeFileExtractor(
						getActivity(), url, YoutubePageFragment.this);
				// show dialog box for download video
				// showYoutubedataDialog();
				
			}
		});
		return view;
	}

	public void showYoutubedataDialog() {

		// ImageLoader imageLoader =
		// AppController.getInstance().getImageLoader();
		// Dialog dialog = new Dialog(this, R.style.Theme.CustomDialog);
		 youtube_dialog = new Dialog(getActivity());
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		youtube_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		youtube_dialog.setContentView(R.layout.youtube_video_dialog);
		download_formate_list = (ListView) youtube_dialog
				.findViewById(R.id.download_formate_list);
		TextView video_title = (TextView) youtube_dialog
				.findViewById(R.id.video_title_name);
		TextView video_time = (TextView) youtube_dialog
				.findViewById(R.id.video_time_duration);
		/*Button download = (Button) youtube_dialog.findViewById(R.id.download);
		Button play_video=(Button) youtube_dialog.findViewById(R.id.play);
		*/
		
		View download =  youtube_dialog.findViewById(R.id.download);
		View play_video= youtube_dialog.findViewById(R.id.play);
		
		ImageView close=(ImageView) youtube_dialog.findViewById(R.id.close);
		
		Log.e("Video image url=", this.video_image);

		video_title.setText(this.video_title);
		video_time.setText(this.video_time_duration);
		// video_title.setSelected(true);
		video_img = (NetworkImageView) youtube_dialog
				.findViewById(R.id.video_image);
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// download_formate_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	
		// If you are using NetworkImageView
		Log.e("Video url image:==", this.video_image);

		video_img.setImageUrl(this.video_image, imageLoader);
		popup_current_row_index = 0;
		youtubeAdapter = new YoutubeAdapter(getActivity(),
				youtube_data_refrence);
		download_formate_list.setAdapter(youtubeAdapter);

		youtube_dialog.show();

		// add listenet on listview
		download_formate_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				youtubeAdapter.notifyDataSetChanged();
				popup_current_row_index = position;
				// Toast.makeText(getApplicationContext(),
				// "Popup list="+popup_list_index,Toast.LENGTH_LONG).show();
				
			}
		});

		// implement downloding concept....
		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Toast.makeText(getActivity(),
						String.valueOf(popup_current_row_index),
						Toast.LENGTH_LONG).show();*/
				View view = download_formate_list
						.getChildAt(popup_current_row_index);
				TextView text_link = (TextView) view
						.findViewById(R.id.video_name);
				TextView video_title=(TextView) view.findViewById(R.id.video_title);
				
				/*Toast.makeText(getActivity(),
						String.valueOf(text_link.getTag()), Toast.LENGTH_LONG)
						.show();*/
			//	new Downloader(getActivity(),String.valueOf(text_link.getTag()),String.valueOf(video_title.getTag()));
				//start downloder manager..
				
				//code for get file extension...
				String video_extension = null;
				String extension=(String) text_link.getText();
				if(extension.toLowerCase().contains("WEBM".toLowerCase()))
				{
					video_extension=".webm";
				}
				else if(extension.toLowerCase().contains("MP4".toLowerCase()))
				{
					video_extension=".mp4";
				}
				else if(extension.toLowerCase().contains("FLV".toLowerCase()))
				{
					video_extension=".flv";
				}
				else if(extension.toLowerCase().contains("3GP".toLowerCase()))
				{
					video_extension=".3gp";
				}
				
					
				
				
			
				//end code get file extension...
				String url=String.valueOf(text_link.getTag()).trim();
				String video_name=String.valueOf(video_title.getTag()).trim();
				String video_images=video_image;
				//Toast.makeText(getActivity(), video_images, Toast.LENGTH_LONG).show();
				
				if (!URLUtil.isHttpUrl(url)) {
	                Toast.makeText(getActivity(), "not valid http url", Toast.LENGTH_SHORT).show();
	                return;
	            }
				
				//|
				video_name=video_name.replaceAll("[^a-zA-Z]+", "_").trim();
				//video_name=video_name.replaceAll(" ","_");
			//	Log.e("title my video===",video_name);
				
				DownloadTask downloadTask6 = new DownloadTask(url,SDCARD+"/"+"FvdTube",video_name+video_extension,video_name,null);
				
				Log.e("title my video===",video_images);
				
				downloadTask6.setThumbnail(video_images);
				
				
			/*	DownloadTaskManager.getInstance(getActivity()).registerListener(downloadTask6,
	                    new DownloadNotificationListener(getActivity(), downloadTask6));
	          */
				DownloadTaskManager.getInstance(getActivity()).startDownload(downloadTask6);
	            
				 /*String url1 =urls[i]; 
		            if (!URLUtil.isHttpUrl(url)) {
		                Toast.makeText(getActivity(), "not valid http url", Toast.LENGTH_SHORT).show();
		                return;
		            }
		            
		            DownloadTask downloadTask6 = new DownloadTask(url, SDCARD, url.substring(url
		                    .lastIndexOf("/")), url.substring(url.lastIndexOf("/")), null);
		            DownloadTask downloadTask6 = new DownloadTask(url1, SDCARD, file_name[i], file_name[i], null);
		            
		            DownloadTaskManager.getInstance(getActivity()).registerListener(downloadTask6,
		                    new DownloadNotificationListener(getActivity(), downloadTask6));
		            DownloadTaskManager.getInstance(getActivity()).startDownload(downloadTask6);
				
				i++;
				*/
				//end  downloder manager code
				//Toast.makeText(getActivity(), "Downloding Start...", Toast.LENGTH_LONG).show();
				youtube_dialog.dismiss();
				
			}
		});
		
		
		//close popup 
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				youtube_dialog.dismiss();
				
			}
		});
		
		
		//add listener on button for play video onile 
		play_video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				View view = download_formate_list
						.getChildAt(popup_current_row_index);
				TextView text_link = (TextView) view
						.findViewById(R.id.video_name);
				TextView video_title=(TextView) view.findViewById(R.id.video_title);
				
					
				//Toast.makeText(getActivity(), "Play ="+String.valueOf(text_link.getTag()), Toast.LENGTH_LONG).show();
				//pass the video url by intent for play video
				Intent play_video_intent=new Intent(getActivity(),Play_Youtube_Video.class);
				play_video_intent.putExtra("video_url",String.valueOf(text_link.getTag()));
				
				startActivity(play_video_intent);
				youtube_dialog.dismiss();
				
			}
		});

	}

	public void inisilizeComponent() {
		youtube_webview = (WebView) view.findViewById(R.id.youtube_webview);
		download = (ImageView) view.findViewById(R.id.download);
		// download.setAnimation(animation);
		youtube_webview.getSettings().setJavaScriptEnabled(true);

		youtube_webview.setWebViewClient(new WebViewClientYoutube());
		youtube_webview.loadUrl(youtube_url);
	}

	public void getYoutubeUrl() {

		// Toast.makeText(getActivity(),
		// youtube_webview.getUrl(),Toast.LENGTH_LONG).show();
		if(networkChecker.isConnectingNetwork())
		{
			download.setVisibility(View.VISIBLE);
		}
		else
		{
			networkChecker.showInternetDialog();
		}
		
		// download.startAnimation(download_button_animation);
		// Log.e("youtube get url=", String.valueOf( youtube_webview.getUrl()));
	}

	public class WebViewClientYoutube extends WebViewClient {
		boolean flag;
		int i = 0;

		public WebViewClientYoutube() {
			flag = false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
			
		
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			
			super.onPageFinished(view, url);
			if(dialog.isShowing())
			{
				dialog.dismiss();
			}
			if (flag == false) {
				// flag=true;
				i++;
				if (i == 2) {
					flag = true;
				}

			} else if (flag == true) {
				// Toast.makeText(getActivity(),
				// "call fetch url",Toast.LENGTH_LONG).show();
				// getYoutubeUrl();
			}
			Log.e("page finish=", view.getUrl());

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			// Toast.makeText(getActivity(),
			// "should over riden..",Toast.LENGTH_LONG).show();
			return true;

		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			// TODO Auto-generated method stub
			super.doUpdateVisitedHistory(view, url, isReload);

			// call function for check valid url or not
			checkValid_Video(view);

		}

	}

	public void checkValid_Video(WebView webView) {

		try {
			if ((webView.getUrl() == null)) {
				// Toast.makeText(getBaseContext(),
				// "url length=no URL ="+webView.getUrl(),
				// Toast.LENGTH_SHORT).show();
				// /////////////////////nothing
			}

			else {
				if ((webView.getUrl().equals("http://m.youtube.com/"))) {
					System.out.println("youtube" + webView.getUrl());
					// Toast.makeText(getActivity(),
					// "only youtube url..",Toast.LENGTH_LONG).show();
					download.clearAnimation();
					download.setVisibility(View.GONE);
				} else {

					// Toast.makeText(getActivity(),
					// "video id url="+webView.getUrl(),Toast.LENGTH_LONG).show();

					String url = webView.getUrl();
					checkValidUrl(url);
					videoId = (String) urlParameters.get("v");
					download.startAnimation(download_button_animation);
					if(networkChecker.isConnectingNetwork())
					{
						download.setVisibility(View.VISIBLE);
					}
					else
					{
						networkChecker.showInternetDialog();
					}
					// Toast.makeText(getActivity(), "video id final check ="+
					// videoId,Toast.LENGTH_LONG).show();
					if (videoId == null) {
						System.out.print("showError('VIDEO_NOT_FOUND");
						
						// jsObject.eval("showError('VIDEO_NOT_FOUND');");
						//Toast.makeText(getActivity(), "Video not found",
							//	Toast.LENGTH_LONG).show();
						download.clearAnimation();
						download.setVisibility(View.GONE);
					}
					if (videoId.indexOf("&") > 0) {
						videoId = videoId.substring(0, videoId.indexOf("&"));
					}

					System.out.println("videoId: " + videoId);
					Log.e("Video new Url=", url);
					if (videoId == null) {
						//Toast.makeText(getActivity(), "Video not found",
						//		Toast.LENGTH_LONG).show();
						download.clearAnimation();
						download.setVisibility(View.GONE);
						Log.e("url null=", "url null");
						// videoId = getSubstring(getRedirectedLocation(url),
						// "v=", "&");
					}

				}
			}
		} catch (Exception ex) {

		}

	}

	public void checkValidUrl(String url) {

		// in this method we write the logic to get valid url id....
		int i = url.indexOf("?");
		if (i > -1) {
			String parameters = url.substring(url.indexOf("?") + 1);
			System.out.println("parameters: " + parameters);
			urlParameters = new Hashtable();
			StringTokenizer tokenizer1 = new StringTokenizer(parameters, "&");
			while (tokenizer1.hasMoreTokens()) {
				StringTokenizer tokenizer2 = new StringTokenizer(
						tokenizer1.nextToken(), "=");
				urlParameters.put(tokenizer2.nextToken(),
						tokenizer2.nextToken());
			}
		} else {
			System.out.print("showError('VIDEO_NOT_FOUND");
			// jsObject.eval("showError('VIDEO_NOT_FOUND');");
			//Toast.makeText(getActivity(), "Video not found", Toast.LENGTH_LONG)
				//	.show();

		}
	}

	@Override
	public void youtubeContentInfo(String title, String link, String image,
			String time) {
		video_title = title;
		video_time_duration = time;
		video_image = image;
		Log.e("Image link=== sp", video_image);
		Log.e("Youtube Listener call", "Yes");
		Log.e("Title of video new title=", title);
		Log.e("Link =", link);
		Log.e("Image=", image);
		Log.e("time=", time);
		//Toast.makeText(getActivity(), "call get info", Toast.LENGTH_LONG)
				//.show();

	}

	@Override
	public void youtubeUrlInfo(
			ArrayList<Youtube_Data_Setter_Getter> youtube_data) {

		this.youtube_data_refrence = youtube_data;
		
		//check url is valid or not for downloding...
		
		Youtube_Data_Setter_Getter youtube_ur_check=this.youtube_data_refrence.get(0);
		String url_check=youtube_ur_check.getSongs_link();
		Log.e("Check ur valid or not=",url_check);
		
		
		//index of return -1 if string does not match
		if(url_check.indexOf("decryptSignature")!=-1)
		{ //invalid url 
			
			AlertDialog.Builder invalid_url_dialog=new AlertDialog.Builder(getActivity());
			invalid_url_dialog.setMessage("This video not Permisson for Download");
			invalid_url_dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});invalid_url_dialog.show();
			
			
			
		}
		else
		{
			//valid url (show dialog )
			showYoutubedataDialog();
			
		}
	
	
		
	}

}