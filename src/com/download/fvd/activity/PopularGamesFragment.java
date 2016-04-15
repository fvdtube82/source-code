package com.download.fvd.activity;

import com.appia.sdk.Appia;
import com.appia.sdk.WallResult;
import com.download.fvd.networkchecker.NetworkChecker;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopularGamesFragment extends Fragment{

	LinearLayout add_view;
	Context context;
	NetworkChecker checker;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	
		View view=inflater.inflate(R.layout.popular_games_fragment,container,false);
		context=getActivity();
		checker=new NetworkChecker(getActivity());
		
		add_view=(LinearLayout)view.findViewById(R.id.view_add);
		if(checker.isConnectingNetwork())
		{
			
		
		Appia appia = Appia.getAppia(context);
		
		appia.setSiteId(7262);
		appia.startSession();
		//appia.displayWall(this, WallDisplayType.FULL_SCREEN);
		WallResult wr = appia.getWall(getActivity());
		if (wr.hasError())
		{
		    TextView textView = new TextView(context);
		    textView.setGravity(Gravity.CENTER);
		    textView.setText("Unable to display more applications.");
		    add_view.addView(textView);         //Display message to end-user
		}
		else
			add_view.addView(wr.getView()); 
		}
		else
		{
			checker.showInternetDialog();
		}
		return view;
	}
}
