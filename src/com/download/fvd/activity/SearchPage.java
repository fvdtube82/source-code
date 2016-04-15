package com.download.fvd.activity;

import com.download.fvd.activity.YoutubePageFragment.WebViewClientYoutube;
import com.download.fvd.networkchecker.NetworkChecker;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SearchPage extends Activity {

	String url = "https://www.google.com/search?q=songs";
	public static WebView google_webview;
	ProgressBar progressBar;
	ImageView back;
	NetworkChecker checker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);
		checker=new NetworkChecker(SearchPage.this);
		if(!checker.isConnectingNetwork())
		{
			//if internet r not connected
			checker.showInternetDialog();
		}
		
			Bundle bundle= getIntent().getExtras();
			
			url=bundle.getString("search_keyword");
		
		
			url="https://www.google.com/search?q="+url;
			inisilizeComponent();
		
	}
	
	public void inisilizeComponent() {
		google_webview = (WebView)findViewById(R.id.google_page);
		progressBar=(ProgressBar) findViewById(R.id.progressBar1);
		back=(ImageView) findViewById(R.id.back);
		
		
	
	
		//google_webview.setWebViewClient(new WebViewSearch());
		
		startWebView(url);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
	}

	private void startWebView(String url) {
        
        //Create new webview Client to show progress dialog 
        //When opening a url or click on link 
          
		google_webview.setWebViewClient(new WebViewClient() {      
			
           
            //If you will not use this method url links are opeen in new brower not in webview 
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true; 
            } 
         
            //Show loader on url load 
            public void onLoadResource (WebView view, String url) {
               
            		
            		progressBar.setVisibility(View.VISIBLE);
                
            } 
            public void onPageFinished(WebView view, String url) {
               progressBar.setVisibility(View.GONE);
            } 
              
        });  
           
         // Javascript inabled on webview   
		google_webview.getSettings().setJavaScriptEnabled(false); 
		google_webview.getSettings().setBuiltInZoomControls(true);
        // Other webview options 
        /* 
        webView.getSettings().setLoadWithOverviewMode(true); 
        webView.getSettings().setUseWideViewPort(true); 
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
        webView.setScrollbarFadingEnabled(false); 
        webView.getSettings().setBuiltInZoomControls(true); 
        */ 
          
        /* 
         String summary = "<html><body>You scored <b>192</b> points.</body></html>"; 
         webview.loadData(summary, "text/html", null);  
         */ 
          
        //Load url in webview 
		google_webview.loadUrl(url);
           
           
    } 
	
	 public void onBackPressed() { 
	        if(google_webview.canGoBack()) {
	            google_webview.goBack();
	        } else { 
	            // Let the system handle the back button 
	            super.onBackPressed(); 
	        } 
	    } 
}
