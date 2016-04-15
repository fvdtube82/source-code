package com.download.fvd.activity;

import java.util.ArrayList;





import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class YoutubeAdapter extends BaseAdapter{

	 ArrayList<Youtube_Data_Setter_Getter> youtube_data_refrence;
	 Context context;
	 LayoutInflater inflater;
	public YoutubeAdapter(Context context, ArrayList<Youtube_Data_Setter_Getter> youtube_data_refrence) {
		this.youtube_data_refrence=youtube_data_refrence;
		this.context=context;
		
	}
	@Override
	public int getCount() {
		
		return youtube_data_refrence.size();
	}

	@Override
	public Object getItem(int position) {
		
		return youtube_data_refrence.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		ViewhHolder viewhHolder;
		Youtube_Data_Setter_Getter data=this.youtube_data_refrence.get(position);
		
		if(convertView==null)
		{
			inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.youtube_listview,null);
			viewhHolder=new ViewhHolder();
			viewhHolder.video_name=(TextView) convertView.findViewById(R.id.video_name);
			viewhHolder.video_title=(TextView) convertView.findViewById(R.id.video_title);
			
			convertView.setTag(viewhHolder);
		}
		else
		{
			viewhHolder=(ViewhHolder) convertView.getTag();
			
		}
		if(YoutubePageFragment.popup_current_row_index==position)
		{
			//Toast.makeText(context, "Popup list="+MainActivity.popup_list_index,Toast.LENGTH_LONG).show();
			convertView.setBackgroundColor(Color.LTGRAY);
			
				
			viewhHolder.video_name.setTag(data.getSongs_link());
			viewhHolder.video_title.setTag(data.getTitle());
			convertView.setSelected(true);
		}
		else
		{
			convertView.setBackgroundColor(Color.WHITE);
			
			convertView.setSelected(false);
		}
		viewhHolder.video_name.setText(data.getFormate());
		//Toast.makeText(context,"songs link="+data.getSongs_link() ,Toast.LENGTH_LONG).show();
		//viewhHolder.video_name.setTag(data.getSongs_link());
		Log.e("Percent= list view=",String.valueOf(data.getPercent())+",formate="+data.getFormate()+","+data.getTitle());
		Log.e("list view size=",String.valueOf(data.getRounded_size())+",formate="+data.getFormate()+","+data.getTitle() );
		
		//viewhHolder.video_name.setTag(data.getSongs_link());
		return convertView;
		
	}

	public static class ViewhHolder
	{
		TextView video_name;
		TextView video_title;
	}
}
