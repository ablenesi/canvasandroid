package edu.ubbcluj.canvasAndroid.util.network;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.model.File;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RestDownloadManager {

	private DownloadManager downloadmanager;
	private Activity activity;

	private File file;

	private String state;
	
	private BroadcastReceiver receiver;
	
	public RestDownloadManager(final Activity activity) {
		this.activity = activity;

		state = Environment.getExternalStorageState();
		
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

					Toast.makeText(activity, file.getName() + " downloaded!",
							Toast.LENGTH_LONG).show();

				} else if (DownloadManager.ACTION_NOTIFICATION_CLICKED
						.equals(action)) {

					java.io.File ioFile = null;
					
					if (Environment.MEDIA_MOUNTED.equals(state)) {
						
						ioFile = new java.io.File(Environment.getExternalStorageDirectory()
								+ "/download/"
			                    + file.getName());
	
					    Intent newIntent = new Intent();
					    newIntent.setAction(android.content.Intent.ACTION_VIEW);
					   
					    newIntent.setData(Uri.fromFile(ioFile));
					   
					    activity.startActivity(newIntent);
					
					}
					
				}

			}
		};

	
	}

	public void registerActionNotificationClickedReceiver(){

		activity.registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_NOTIFICATION_CLICKED));	
	}
	
	public void registerActionDownloadCompleteReceiver(){
		activity.registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
	
	
	public void unRegisterReceiver(){
		activity.unregisterReceiver(receiver);
	}
	
	
	public void downloadFile(File file) {

		this.file = file;

		downloadmanager = (DownloadManager) activity
				.getSystemService(Context.DOWNLOAD_SERVICE);

		Uri uri = Uri.parse(file.getUrl());
		
		DownloadManager.Request request = new Request(uri);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		
		//if external storage exists, save the file there
		
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getName());
	    } 
	    			
		downloadmanager.enqueue(request);
	}
}
