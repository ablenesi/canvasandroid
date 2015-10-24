package edu.ubbcluj.canvasAndroid.view.activity;

import java.util.Random;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.ActivityStreamSummaryController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.persistence.ServiceProvider;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;

public class MyService extends Service{
	public static AlarmManager alarm;
	public NotificationManager nm;
	private AsyncTask<String, Void, String> asyncTask;
	private ControllerFactory cf;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

    
	@SuppressWarnings("unchecked")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences.getInstance();
		sPreferences.init(getApplicationContext().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));
		ServiceProvider sp = ServiceProvider.getInstance();
		sp.initalize(getApplicationContext());
		cf = ControllerFactory.getInstance();
		ActivityStreamSummaryController assController;
		assController = cf.getActivityStreamSummaryController();
		asyncTask = ((AsyncTask<String, Void, String>) assController);
		asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
						+ "/api/v1/users/self/activity_stream/summary" });
		int newAnnouncementUnreadCount = ServiceProvider.getInstance().getNewAnnouncementUnreadCount();
		int announcementUnreadCount = ServiceProvider.getInstance().getAnnouncementUnreadCount();
		if (newAnnouncementUnreadCount > announcementUnreadCount){
		    long when = System.currentTimeMillis();
		    Random rng = new Random();
		    nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE) ;
		    Intent intent2=new Intent(getApplicationContext(),LoginActivity.class);
		    int x = rng.nextInt();
		    PendingIntent  pending=PendingIntent.getActivity(getApplicationContext(), x, intent2, 0);
		    Notification notification;
	        notification = new NotificationCompat.Builder(this)
	                .setContentTitle("New Announcement")
	                .setContentText("You have "+(newAnnouncementUnreadCount-announcementUnreadCount)+" new Announcements."
	                		).setSmallIcon(R.drawable.announcement)
	                .setContentIntent(pending).setWhen(when).setAutoCancel(true)
	                .build();
		    notification.flags |= Notification.FLAG_AUTO_CANCEL;
		    notification.defaults |= Notification.DEFAULT_SOUND;
		    
		    nm.notify(1, notification);
		    ServiceProvider.getInstance().setAnnouncementUnreadCount(newAnnouncementUnreadCount);
		}
		ServiceProvider.getInstance().setAnnouncementUnreadCount(newAnnouncementUnreadCount);
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ServiceProvider.getInstance().getService_started()){
	        alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
	        alarm.set(
	            AlarmManager.RTC_WAKEUP,
	            System.currentTimeMillis() + (1000*30),
	            PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
	        );
        }
    }
	
}
