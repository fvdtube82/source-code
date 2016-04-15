package com.download.download2;




import java.io.File;
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
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.provider.MediaStore.Files;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownlodedAdapter extends ArrayAdapter<DownloadTask> {

    private static final String TAG = "DownloadItemAdapter";

    private LayoutInflater mLayoutInflater;

    private List<DownloadTask> mTaskList;
   
    public int new_position=-1;
    int click_position=-1;
    boolean options_Visible;
    private Context mContext;
    ViewHolder holder = null;
    public DownlodedAdapter(Context context, int textViewResourceId, List<DownloadTask> taskList) {
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
       // ViewHolder holder = null;
        
        final DownloadTask task = mTaskList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.downloaded_list, null);
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
            holder.last_play =(TextView) convertView.findViewById(R.id.last_play);
            holder.mSize = (TextView) convertView.findViewById(R.id.size);
            holder.mStatusText = (TextView) convertView.findViewById(R.id.state);
            holder.mStateImageView = (ImageView) convertView.findViewById(R.id.ic_state);
          //  holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progress);
         //inisilize share and play option
            holder.play_share_layout=convertView.findViewById(R.id.play_share_layout);
            
            holder.share_option=convertView.findViewById(R.id.share_layout);
            holder.play_option=convertView.findViewById(R.id.play_layout);
            
           // holder.mProgressBar.setMax(100);
         convertView.setTag(holder);
         } else {
         holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(task.getTitle());
        
        String Total_size_of_file=formatSize(task.getFinishedSize(), task.getTotalSize());
        String file_size[]= Total_size_of_file.split("/");
        
        holder.mSize.setText(file_size[0]);
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
           // holder.mProgressBar.setProgress(task.getPercent());
            holder.last_play .setText(String.valueOf(task.getPercent()+ "%"));
        }
      
       
        switch (mTaskList.get(position).getDownloadState()) {
        	
            
            case PAUSE:
            	
            	Log.e("Pause call by DownlodingAdapter=","call Adapter");
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_paused"));
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_ing")); 
               // Log.e("Progress bar percent=",String.valueOf(task.getPercent()));
              //  holder.mProgressBar.setProgress(task.getPercent());
                holder.last_play .setText("Pause");
               // Toast.makeText(mContext,"pause" ,Toast.LENGTH_LONG).show();
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
                        
                   //  holder.mProgressBar.setProgress(total_percent_download);
                    holder.last_play .setText(String.valueOf(total_percent_download+ "%"));
                    
                     //Log.e("Progress bar find=", String.valueOf(progress_bar_value));
                     
                    // Toast.makeText(mContext, "call"+String.valueOf(progress_bar_value),Toast.LENGTH_LONG).show();
                     
                	}
                	catch(ArithmeticException ex){
                		 
                	}
                }
              //  holder.mProgressBar.setIndeterminate(false);
                break;
            case FAILED:
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_failed"));
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_retry"));
               // Toast.makeText(mContext,"failed" ,Toast.LENGTH_LONG).show();
                holder.last_play .setText("Failed");
              //  holder.mProgressBar.setIndeterminate(true);
                Log.e("Failed call by DownlodingAdapter=","call Adapter");
                break;
            case DOWNLOADING:
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_downloading"));
              //  holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_pause"));
            
                holder.mStateImageView.setImageResource(R.drawable.arrow_down);
                // Toast.makeText(mContext, "call download", Toast.LENGTH_LONG).show();
                
              //  holder.mProgressBar.setIndeterminate(false);
              // long size= ((int)(task.getFinishedSize()*100));
             //  long percents=(int)size/task.getTotalSize();
              //  Toast.makeText(mContext,"Downloding" ,Toast.LENGTH_LONG).show();
                Log.e("Progress downloging",String.valueOf("Finished size="+String.valueOf(task.getFinishedSize()*100)));
                //Toast.makeText(mContext, "call downloding method",Toast.LENGTH_SHORT).show();
               // Toast.makeText(mContext,"downloding" ,Toast.LENGTH_LONG).show();
               // Toast.makeText(mContext, "value="+percents,20).show();
                Log.e("Downloding call by DownlodingAdapter=","call Adapter");
                break;
            case FINISHED:
              //  holder.mProgressBar.setProgress(100);
               // holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_finished"));
               // Toast.makeText(mContext,"finish" ,Toast.LENGTH_LONG).show();
               
                holder.mStateImageView.setImageResource(R.drawable.arrow_down);
                Log.e("Finished call by DownlodingAdapter=","call Adapter");
              
                File file=new File(task.getFilePath()+"/"+ task.getFileName());
               
                Log.e("File extension of list view=",getFileExtension(file));
                
                holder.last_play .setText("Format: "+getFileExtension(file));
                
                break;
            case INITIALIZE:
               // holder.mProgressBar.setIndeterminate(false);
                holder.mStatusText.setText(Res.getInstance(mContext).getString("download_initial"));
             // Toast.makeText(mContext,"INITIALIZE" ,Toast.LENGTH_LONG).show();
                holder.mStateImageView.setImageResource(Res.getInstance(mContext).getDrawable("ic_download_ing"));
              //this code writen by me for show downloding process
               // new DownloadNotificationListener(mContext, task);
                holder.last_play .setText("Wait...");
              //  task.setDownloadState(downloadState);
                Log.e("Initialize call by DownlodingAdapter=","call Adapter");
                break;
            default:
            
                break;
                
        }
        
        //write logic for show and hide play and share layout
       //	//this is hide and visible logic part
        if (new_position == position && click_position == new_position) {
            if (options_Visible)
            {
                holder.play_share_layout.setVisibility(View.VISIBLE);
            holder.mStateImageView.setImageResource(R.drawable.arrow_up);
            }
            else
                holder.play_share_layout.setVisibility(View.GONE);
            	
            	
            click_position = new_position;
        } else if (new_position == position && click_position != new_position) {
            if (options_Visible)
                holder.play_share_layout.setVisibility(View.VISIBLE);
            holder.mStateImageView.setImageResource(R.drawable.arrow_up);
            click_position = new_position;
        } else {
            holder.play_share_layout.setVisibility(View.GONE);
        }
        //end code of hide or show play and share 
        
        
        
        //add click listener on image view for show play and share option logic
        
        holder.mStateImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//this is hide and visible logic part
				
				 new_position = position;
	                options_Visible = !options_Visible;
	                Log.e("option visible=",String.valueOf(options_Visible));
	                if (click_position != new_position && options_Visible == false)
	                    options_Visible = true;
	                
	                notifyDataSetChanged();
				
			}
		});
        
        //click event on share option for link
        holder.share_option.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Toast.makeText(mContext, "click on share option",Toast.LENGTH_LONG).show();
				 Intent intent = new Intent(Intent.ACTION_SEND);
                 intent.setType("text/plain");
                 intent.putExtra(Intent.EXTRA_TEXT, "http://m.fvdtube.com/FVDTube.apk"+"\n"+task.getFileName() +"| from best powerful video downloder !");
                // intent.putExtra(android.content.Intent.EXTRA_SUBJECT, task.getFileName()+);
                 mContext.startActivity(Intent.createChooser(intent, "Share"));
			
				
				
			}
		});
        
        
        //click event on play option
        holder.play_option.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Toast.makeText(mContext, "click on play option="+String.valueOf(position),Toast.LENGTH_LONG).show();
				
				 onDownloadFinishedClick(mTaskList.get(position));
			
				
			}
		});
        
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
        public TextView last_play;
        public ProgressBar mProgressBar;

        public ImageView mStateImageView;
        private View share_option,play_option,play_share_layout;
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
    /**
     * You can overwrite this method to implement what you want do after download task item is clicked.
     * @param task
     */
    public void onDownloadFinishedClick(DownloadTask task) {
        Log.d(TAG, task.getFilePath() + "/"+ task.getFileName());
       
        
      /*  Intent intent = DownloadOpenFile.openFile(task.getFilePath()
                + "/"+ task.getFileName());*/
        
        try
        {
        	
        
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(task.getFilePath() + "/"+ task.getFileName()));
        intent.setDataAndType(Uri.parse(task.getFilePath() + "/"+ task.getFileName()), "video/mp4");
       // mContext.startActivity(intent);
        if (null == intent) {
            Toast.makeText(mContext, R.string.download_file_not_exist, Toast.LENGTH_LONG).show();
        } else {
            mContext.startActivity(intent);
        }
        }catch(Exception ex){
        	
        	Toast.makeText(mContext, "You have not any Media Player !",Toast.LENGTH_LONG).show();
        	
        }
    }
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
    
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}