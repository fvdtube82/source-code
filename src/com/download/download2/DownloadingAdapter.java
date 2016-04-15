
package com.download.download2;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.download.fvd.activity.R;
import com.downoad.fdv.volley_resource.AppController;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadingAdapter extends ArrayAdapter<DownloadTask> {

    private static final String TAG = "DownloadItemAdapter";

    private LayoutInflater mLayoutInflater;

    private List<DownloadTask> mTaskList;
    
    private Context mContext;

    public DownloadingAdapter(Context context, int textViewResourceId, List<DownloadTask> taskList) {
        super(context, textViewResourceId, taskList);
        mTaskList = taskList;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public int getCount() {
        return mTaskList.size();
    }

    public DownloadTask getItem(int position) {
        return mTaskList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        final DownloadTask task = mTaskList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.download_list_item, null);
            holder = new ViewHolder();

           /* holder.mThumbnail = (NetworkImageView) convertView.findViewById(Res.getInstance(mContext).getId("thumbnail"));
            holder.mTitle = (TextView) convertView.findViewById(Res.getInstance(mContext).getId("title"));
            holder.mSize = (TextView) convertView.findViewById(Res.getInstance(mContext).getId("size"));
            holder.mStatusText = (TextView) convertView.findViewById(Res.getInstance(mContext).getId("state"));
            holder.mStateImageView = (ImageView) convertView.findViewById(Res.getInstance(mContext).getId("ic_state"));
            holder.mProgressBar = (ProgressBar) convertView.findViewById(Res.getInstance(mContext).getId("progress"));
          */ 
            
            holder.mThumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mpercent=(TextView) convertView.findViewById(R.id.percent);
            holder.mSize = (TextView) convertView.findViewById(R.id.size);
            holder.mStatusText = (TextView) convertView.findViewById(R.id.state);
            holder.mStateImageView = (ImageView) convertView.findViewById(R.id.ic_state);
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progress);
         
            
            holder.mProgressBar.setMax(100);
         convertView.setTag(holder);
         } else {
         holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(task.getTitle());
        holder.mSize.setText(formatSize(task.getFinishedSize(), task.getTotalSize()));
        //holder.mProgressBar.setProgress(task.getFinishedSize());
       
       // Toast.makeText(getContext(), String.valueOf(progress_bar_value), Toast.LENGTH_LONG).show();
        //Log.e("Progress bar=",String.valueOf(progress_bar_value));
        //finishedSize*100/totalSize
      
        
        
       try
       {
        if(URLUtil.isHttpUrl(task.getThumbnail())){
            
        	/*Bitmap bitmap_image=new getBitMapFromUrl().get();
        	
        	 holder.mThumbnail.setImageBitmap(bitmap_image);
        	 holder.mThumbnail.setImageBitmap(getBitMapFromUrl(task.getThumbnail()));
 			*/
        	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    		// download_formate_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	
    		// If you are using NetworkImageView
    	

        	 holder.mThumbnail.setImageUrl(task.getThumbnail(), imageLoader);
			
        } else if (URLUtil.isFileUrl(task.getThumbnail())){
            holder.mThumbnail.setImageBitmap(BitmapFactory.decodeFile(task.getThumbnail().substring(8)));
        } else if (URLUtil.isAssetUrl(task.getThumbnail())){
            holder.mThumbnail.setImageBitmap(getBitmapFromAsset(task.getThumbnail().substring(22)));
        }
        
       }
       catch(Exception ex){}
        // ImageUtil.loadImage(holder.mIcon, task.getThumbnail());

        
        
        if (task.getPercent() > 0) {
            holder.mProgressBar.setProgress(task.getPercent());
            holder.mpercent.setText(String.valueOf(task.getPercent()+ "%"));
        }
      
       
        switch (mTaskList.get(position).getDownloadState()) {
        	
            
            case PAUSE:
            	
            	Log.e("Pause call by DownlodingAdapter=","call Adapter");
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_paused"));
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_ing")); 
               // Log.e("Progress bar percent=",String.valueOf(task.getPercent()));
                holder.mProgressBar.setProgress(task.getPercent());
                holder.mpercent.setText("Pause");
                //Toast.makeText(mContext,"pause" ,Toast.LENGTH_LONG).show();
                //holder.mProgressBar.setIndeterminate(true);
               // Toast.makeText(mContext, "pause call", Toast.LENGTH_LONG).show();
                if(task.getPercent()==0)
                { //it will be execute when apps has reopen after exit or call downliadlistActivity after back
                	try
                	{
                	// int progress_bar_value=task.getFinishedSize()*100/task.getTotalSize();
                		double number2 = (double)Math.round((double)task.getFinishedSize() * 100)/(double)task.getTotalSize();
                        
                        // Log.e("Total percent downloding file=", String.valueOf(number2));
                         int total_percent_download=(int)number2;
                        
                     holder.mProgressBar.setProgress(total_percent_download);
                    holder.mpercent.setText(String.valueOf(total_percent_download+ "%"));
                    
                     //Log.e("Progress bar find=", String.valueOf(progress_bar_value));
                     
                    // Toast.makeText(mContext, "call"+String.valueOf(progress_bar_value),Toast.LENGTH_LONG).show();
                     
                	}
                	catch(ArithmeticException ex){
                		 
                	}
                }
                holder.mProgressBar.setIndeterminate(false);
                break;
            case FAILED:
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_failed"));
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_retry"));
               // Toast.makeText(mContext,"failed" ,Toast.LENGTH_LONG).show();
                holder.mpercent.setText("Failed");
                holder.mProgressBar.setIndeterminate(true);
                Log.e("Failed call by DownlodingAdapter=","call Adapter");
                break;
            case DOWNLOADING:
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_downloading"));
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_pause"));
              // Toast.makeText(mContext, "Downloding Start", Toast.LENGTH_LONG).show();
                
                holder.mProgressBar.setIndeterminate(false);
              // long size= ((int)(task.getFinishedSize()*100));
             //  long percents=(int)size/task.getTotalSize();
               // Toast.makeText(mContext,"Downloding" ,Toast.LENGTH_LONG).show();
                Log.e("Progress downloging",String.valueOf("Finished size="+String.valueOf(task.getFinishedSize()*100)));
                //Toast.makeText(mContext, "call downloding method",Toast.LENGTH_SHORT).show();
               // Toast.makeText(mContext,"downloding" ,Toast.LENGTH_LONG).show();
               // Toast.makeText(mContext, "value="+percents,20).show();
                Log.e("Downloding call by DownlodingAdapter=","call Adapter");
                break;
            case FINISHED:
                holder.mProgressBar.setProgress(100);
                holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_finished"));
               // Toast.makeText(mContext,"finish" ,Toast.LENGTH_LONG).show();
                holder.mpercent.setText("Finished");
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("download_finished_do"));
                Log.e("Finished call by DownlodingAdapter=","call Adapter");
                break;
            case INITIALIZE:
                holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_initial"));
             // Toast.makeText(mContext,"INITIALIZE" ,Toast.LENGTH_LONG).show();
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_ing"));
              //this code writen by me for show downloding process
               // new DownloadNotificationListener(mContext, task);
                holder.mpercent.setText("Wait...");
              //  task.setDownloadState(downloadState);
                Log.e("Initialize call by DownlodingAdapter=","call Adapter");
                break;
            default:
            
                break;
                
        }

        if (position % 2 == 0) {
            /*convertView.setBackgroundColor(Res.getInstance(mContext).getColor(
                   "listview_even_bg"));*/
        } else {
//            convertView.setBackgroundColor(Res.getInstance(mContext)
//                    .getColor("listview_odd_bg"));
        }
      
        return convertView;
    }

    private String formatSize(int finishedSize, int totalSize) {
        StringBuilder sb = new StringBuilder(50);

        float finished = ((float) finishedSize) / 1024 / 1024;
        if (finished < 1) {
            sb.append(String.format("%1$.2f K / ", ((float) finishedSize) / 1024));
        } else {
            sb.append((String.format("%1$.2f M / ", finished)));
        }

        float total = ((float) totalSize) / 1024 / 1024;
        if (total < 1) {
            sb.append(String.format("%1$.2f K ", ((float) totalSize) / 1024));
        } else {
            sb.append(String.format("%1$.2f M ", total));
        }
        return sb.toString();
    }

    static class ViewHolder {
        public NetworkImageView mThumbnail;

        public TextView mTitle;

        public TextView mStatusText;

        public TextView mSize;
        public TextView mpercent;
        public ProgressBar mProgressBar;

        public ImageView mStateImageView;

    }
    
    
    /*public static Bitmap getBitMapFromUrl(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/

    /*class getBitMapFromUrl extends AsyncTask<String,Void,Bitmap>
    {

		@Override
		protected Bitmap doInBackground(String... url) {
			
			 URL myFileUrl = null;
		        Bitmap bitmap = null;
		        try {
		            myFileUrl = new URL(url[0]);
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        }
		        try {
		            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		            conn.setDoInput(true);
		            conn.connect();
		            InputStream is = conn.getInputStream();
		            bitmap = BitmapFactory.decodeStream(is);
		            is.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        return bitmap;
		        
		}
		
    	
    }
    */
    
    private Bitmap getBitmapFromAsset(String fileName) {
        Bitmap image = null;
        try {
            AssetManager am = mContext.getAssets();
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {

        }
        return image;
    }
}