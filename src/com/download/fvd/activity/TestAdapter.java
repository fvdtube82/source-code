package com.download.fvd.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestAdapter extends BaseAdapter{

	Context context;
	ArrayList<String> data;
	public TestAdapter(Context context,ArrayList<String> data) {
		
		this.context=context;
		this.data=data;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		 convertView= inflater.inflate(R.layout.download_list_item,null);
		 //TextView textView= (TextView) convertView.findViewById(R.id.textView1);
		 //textView.setText(data.get(position));
		 
		return convertView;
	}

}
