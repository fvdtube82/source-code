package com.download.fvd.activity;


import com.download.fvd.networkchecker.NetworkChecker;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;


public class Top_Apps extends Activity {

	private ListView popular_listview_bg;
	private ProgressBar progressBar1;
	LinearLayout linearlayout_main;
	NetworkChecker checker;
	View back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "101757112", "206273549", true);
		setContentView(R.layout.activity_top__apps);
		checker=new NetworkChecker(Top_Apps.this);
		//check network connection
		if(!checker.isConnectingNetwork())
		{//if network not connected
			checker.showInternetDialog();
		}
		
		
		
		popular_listview_bg = (ListView)findViewById(R.id.popular_gridview);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		back= findViewById(R.id.back);
		
		//linearlayout_main = (LinearLayout) findViewById(R.id.linearlayout_main_app_frag);
	
		new TopSitesAppsGamesAsynckTask(Top_Apps.this, popular_listview_bg,
				progressBar1).execute();
		
		
		popular_listview_bg.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				
				/*Activity activity = getActivity();

				if (activity instanceof MainActivity) {
					MainActivity myactivity = (MainActivity) activity;
					myactivity
							.navigateToUrl(TopSitesAppsGamesAsynckTask.arrlisttopsite
									.get(position).getLink());*/
				String urls=	TopSitesAppsGamesAsynckTask.arrlisttopsite.get(position).getLink();

					Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urls));
					startActivity(intent);
					
			}
		});
		
		
		//back to prev screen
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
	}

	
}
