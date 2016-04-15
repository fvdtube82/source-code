
package com.download.download2;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * DownloadNotificationListener implements DownloadListener and finished the job
 * of show download progress and messages in notification bar.You can use it
 * show messages in notification bar, or you can write own DownloadListener.
 * 
 * @author offbye@gmail.com
 */

public class DownloadNotificationListener implements DownloadListener {

    private Context mContext;

    private Notification mNotification;

    private int mId;

    private NotificationManager mNotificationManager;

    private int mProgress = 0;
    int global_percent;
    public DownloadNotificationListener(Context context, DownloadTask task) {
        mContext = context;
        mId = task.getUrl().hashCode();
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = initNotifiction(task.getTitle());
        
        
    }
    
    @Override
    public void onDownloadStop() {
        mNotification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                mContext.getString(Res.getInstance(mContext).getString("download_stopped")));
        
        mNotificationManager.notify(mId, mNotification);
        mNotificationManager.cancel(mId);
        
        
       
    }

    @Override
    public void onDownloadStart() {
        mNotificationManager.notify(mId, mNotification);
        
       
    }

    @Override
    public void onDownloadProgress(int finishedSize, int totalSize, int speed) {
        //int percent = finishedSize * 100 / totalSize;
    	
    	double number2 = (double)Math.round((double)finishedSize * 100)/(double)totalSize;
        
    	
         int percent=(int)number2;
        
        // Toast.makeText(mContext,"call notification onDownloadProgress="+String.valueOf(percent),Toast.LENGTH_LONG).show();
         
         global_percent=percent;
        if (percent - mProgress > 1) { //FVDTube
            mProgress = percent;
            mNotification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                    String.format(mContext.getString(Res.getInstance(mContext).getString(
                            "download_speed")), mProgress, speed));
            mNotification.contentView.setProgressBar(Res.getInstance(mContext).getId("notify_processbar"), 100, percent, false);
            mNotificationManager.notify(mId, mNotification);
        }
        
        
      
    }

    @Override
    public void onDownloadPause() {
        mNotification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                mContext.getString(Res.getInstance(mContext).getString("download_paused")));
        mNotification.contentView.setProgressBar(Res.getInstance(mContext).getId("notify_processbar"), 100, global_percent,false);
        mNotificationManager.notify(mId, mNotification);
    
    
       
    }

    @Override
    public void onDownloadFinish(String filepath) {
        mNotification.icon = android.R.drawable.stat_sys_download_done;
        mNotification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                mContext.getString(Res.getInstance(mContext).getString("download_finished")));
        mNotification.contentView.setProgressBar(Res.getInstance(mContext).getId("notify_processbar"), 100, 100, false);
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_LIGHTS;

        /*Intent intent = new Intent( mContext.getString(Res.getInstance(mContext).getString("download_list_action")));
        intent.putExtra("isDownloaded", true);

        mNotification.contentIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/
        
        //my code 
        
        Intent intents=new Intent(mContext,DownloadListActivity.class);
    	//intent.putExtra("message","hellow");
    	
    	TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(mContext);
    	taskStackBuilder.addParentStack(DownloadListActivity.class);
    	
    	
    	
    	taskStackBuilder.addNextIntent(intents);
    	taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	 mNotification.contentIntent=taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	
        //my code end
        mNotificationManager.notify(mId, mNotification);
        // Toast.makeText(mContext,
        // String.format(mContext.getString(Res.getInstance(mContext).getString("downloaded_file),
        // filepath), Toast.LENGTH_LONG).show();
       
    }

    @Override
    public void onDownloadFail() {
        mNotification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                mContext.getString(Res.getInstance(mContext).getString("download_failed")));
        mNotification.contentView.setProgressBar(Res.getInstance(mContext).getId("notify_processbar"), 100, 0, true);
        mNotificationManager.notify(mId, mNotification);
        mNotificationManager.cancel(mId);
        
       
        
        
        
        
    }

    public Notification initNotifiction(String title) {
       
    	
    	
    	
    	Notification notification = new Notification(android.R.drawable.stat_sys_download,
                mContext.getString(Res.getInstance(mContext).getString("downloading_msg")) + title, System.currentTimeMillis());

        notification.contentView = new RemoteViews(mContext.getPackageName(),
                Res.getInstance(mContext).getLayout("download_notify"));
        notification.contentView.setProgressBar(Res.getInstance(mContext).getId("notify_processbar"), 100, 0, false);
        notification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_state"),
                mContext.getString(Res.getInstance(mContext).getString("downloading_msg")));

        notification.contentView.setTextViewText(Res.getInstance(mContext).getId("notify_text"), title);
       // Intent intent=new Intent(mContext,DownloadListActivity.class);
       // notification.contentIntent = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
      
        //my new code
        
        Intent intents=new Intent(mContext,DownloadListActivity.class);
    	//intent.putExtra("message","hellow");
    	
    	TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(mContext);
    	taskStackBuilder.addParentStack(DownloadListActivity.class);
    	
    	
    	
    	taskStackBuilder.addNextIntent(intents);
    	taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	 notification.contentIntent=taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	
        
        //end code...
        
        
        
        
        return notification;

    }
}
