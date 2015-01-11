package edu.ubbcluj.canvasAndroid;

import java.util.Random;

import edu.ubbcluj.canvasAndroid.backend.repository.ActivityStreamSummaryDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service{
	
	public static boolean service_started = false;
	public static AlarmManager alarm;
	public NotificationManager nm;
	private AsyncTask<String, Void, String> asyncTask;
	private DAOFactory df;
	public static int c=1;
	public static int announcementUnreadCount = 0;
	public static int newAnnouncementUnreadCount = 0;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

    
	@SuppressWarnings("unchecked")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		df = DAOFactory.getInstance();
		ActivityStreamSummaryDAO assDao;
		assDao = df.getActivityStreamSummaryDAO();
		
		asyncTask = ((AsyncTask<String, Void, String>) assDao);
		asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
						+ "/api/v1/users/self/activity_stream/summary" });
		
		if (newAnnouncementUnreadCount > announcementUnreadCount){
		    long when = System.currentTimeMillis();
		    Random rng = new Random();
		    nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE) ;
		    Intent intent2=new Intent(getApplicationContext(),DashBoardActivity.class);
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
		    announcementUnreadCount = newAnnouncementUnreadCount;
		}
		announcementUnreadCount = newAnnouncementUnreadCount;
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service_started){
	        alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
	        alarm.set(
	            AlarmManager.RTC_WAKEUP,
	            System.currentTimeMillis() + (1000*180),
	            PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
	        );
        }
    }
	
}
