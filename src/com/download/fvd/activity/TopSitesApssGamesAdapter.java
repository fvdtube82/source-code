package com.download.fvd.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TopSitesApssGamesAdapter extends BaseAdapter {
	ImageLoader imageLoader = ImageLoader.getInstance();
	 
	int mode = 1;
	// private List<Club> clubs;
	private Context activity;
	private String[] Title;
	private static LayoutInflater inflater = null;
	String CountryName, DeviceId;
	String[] imagethumb, bann_pos_id;
	// public ImageLoader imageLoader;
	ImageView imageview_check;
	private String[] flipperImage, add_id, headerurl;
	AdapterViewFlipper ADDViewflipper;
	int mFlipping;
	LinearLayout row_popular_main;
	AnimateFirstDisplayListener animateFirstListener;
	ArrayList<TopSitesAndAppsGames> arrayList;// = new
												// ArrayList<TopSitesAndAppsGames>();

	Animation bottomDown;
	DisplayImageOptions options;

	public TopSitesApssGamesAdapter(Context a,
			ArrayList<TopSitesAndAppsGames> arrlisttopsite) {
		imageLoader.init(ImageLoaderConfiguration.createDefault(a));
		this.arrayList = arrlisttopsite;
		
		//BaseActivity.imageLoader.init(ImageLoaderConfiguration.createDefault(a));
		activity = a;
		animateFirstListener = new AnimateFirstDisplayListener();
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageLoader=new ImageLoader(activity.getApplicationContext());

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		animateFirstListener = new AnimateFirstDisplayListener();
	}

	public int getCount() {
		return arrayList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.row_design_app_popular, null);

		

		TextView text = (TextView) vi.findViewById(R.id.apptitle);
		ImageView image = (ImageView) vi.findViewById(R.id.imageview_appicon);
		text.setText(arrayList.get(position).getTitle());
		/*Typeface custom_font = Typeface.createFromAsset(activity.getAssets(),
				"fonts/julius-sans-one.ttf");
		
		text.setTypeface(custom_font);*/
		row_popular_main = (LinearLayout) vi
				.findViewById(R.id.row_popular_main);
		imageLoader.displayImage(arrayList.get(position).getImageUrl(), image,
				options, animateFirstListener);

		


		return vi;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
