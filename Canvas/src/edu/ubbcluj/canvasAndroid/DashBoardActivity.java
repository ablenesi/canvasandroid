package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ubbcluj.canvasAndroid.backend.repository.ActivityStreamDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.restApi.RestInformationDAO;
import edu.ubbcluj.canvasAndroid.backend.util.CookieHandler;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterActivityStream;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;

public class DashBoardActivity extends BaseActivity {

	private DAOFactory df;
	private List<ActivityStream> activityStream;

	private CustomArrayAdapterActivityStream adapter;
	
	private ListView list;
	private View viewContainer;

	private AsyncTask<String, Void, String> asyncTask;
	private AsyncTask<String, Void, String> asynTaskForRefresh;
	
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("LifeCycle-dash", "onCreate");
		
		// Set the progressbar visibility 
		list = (ListView) findViewById(R.id.list);
		/*TextView tv = (TextView) findViewById(R.id.label); 
        tv.setTypeface(null, Typeface.BOLD); */
		viewContainer = findViewById(R.id.linProg);
		viewContainer.setVisibility(View.VISIBLE);

		// Get the activity stream
		df = DAOFactory.getInstance();
		
		
		ActivityStreamDAO dashboardDao;
		dashboardDao = df.getDashboardDAO();
		dashboardDao.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));
		
		activityStream = new ArrayList<ActivityStream>();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActivityStream as = activityStream.get(position);

				if (as.getType().equals("Announcement")) {
					if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider.getProperty("url")
								+ "/api/v1/courses/"
								+ as.getCourseId()
								+ "/discussion_topics/"
								+ as.getSecondaryId()) && !CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
						Toast.makeText(DashBoardActivity.this, "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent informationIntent = new Intent(
								DashBoardActivity.this, InformationActivity.class);
	
						Bundle bundle = new Bundle();
	
						bundle.putSerializable("activity_type",
								InformationActivity.AnnouncementInformation);
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("announcement_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}

				if (as.getType().equals("Submission")) {
					if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ as.getCourseId() + "/assignments/" + as.getSecondaryId()) && !CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
						Toast.makeText(DashBoardActivity.this, "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent informationIntent = new Intent(
								DashBoardActivity.this, AssignmentActivity.class);
	
						Bundle bundle = new Bundle();
	
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("assignment_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}

				if (as.getType().equals("Message")) {
					if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ as.getCourseId() + "/assignments/" + as.getSecondaryId()) && !CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
						Toast.makeText(DashBoardActivity.this, "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent informationIntent = new Intent(
								DashBoardActivity.this, AssignmentActivity.class);
	
						Bundle bundle = new Bundle();
	
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("assignment_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}

				if (as.getType().equals("Conversation")) {
					if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ as.getCourseId() + "/assignments/" + as.getSecondaryId()) && !CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
						Toast.makeText(DashBoardActivity.this, "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent messagesItemIntent = new Intent(
								DashBoardActivity.this, MessageItemActivity.class);
	
						Bundle bundle = new Bundle();
						bundle.putInt("id", as.getSecondaryId());
						messagesItemIntent.putExtras(bundle);
						startActivity(messagesItemIntent);
					}
				}

				if (as.getType().equals("DiscussionTopic")) {
					if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider.getProperty("url")
								+ "/api/v1/courses/"
								+ as.getCourseId()
								+ "/discussion_topics/"
								+ as.getSecondaryId()) && !CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
						Toast.makeText(DashBoardActivity.this, "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent informationIntent = new Intent(
								DashBoardActivity.this, InformationActivity.class);
	
						Bundle bundle = new Bundle();
	
						bundle.putSerializable("activity_type",
								InformationActivity.AnnouncementInformation);
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("announcement_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}
			}
		});

		// dashboardDao.setDba(this);

		dashboardDao.addInformationListener(new InformationListener() {

			@Override
			public void onComplete(InformationEvent e) {
				ActivityStreamDAO asd = (ActivityStreamDAO) e.getSource();
				setProgressGone();
				setActivityStream(asd.getData());
			}
		});

		asyncTask = ((AsyncTask<String, Void, String>) dashboardDao);
		asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
						+ "/api/v1/users/self/activity_stream" });

		// Initialize the dashboard list
		setList();
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
        	@Override
        	public void onRefresh() {
        		if(!CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
        			swipeView.setRefreshing(false);
					Toast.makeText(DashBoardActivity.this, "No network connection!",
							Toast.LENGTH_LONG).show();
        		} else {
	        		ActivityStreamDAO dashboardDao;
	        		dashboardDao = df.getDashboardDAO();
	        		dashboardDao.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
	        				"CanvasAndroid", Context.MODE_PRIVATE));
	        		RestInformationDAO.clearData();
	        		
	        		
	        		activityStream = new ArrayList<ActivityStream>();
	        		dashboardDao.addInformationListener(new InformationListener() {
	        			
	        			
	        			@Override
	        			public void onComplete(InformationEvent e) {
	        				ActivityStreamDAO asd = (ActivityStreamDAO) e.getSource();
	        				setProgressGone();
	        				setActivityStream(asd.getData());
	        				swipeView.setRefreshing(false);
	        				setList();
	        			}
	        			
	        		});
	        		
	
	        		asynTaskForRefresh = ((AsyncTask<String, Void, String>) dashboardDao);
	        		asynTaskForRefresh.execute(new String[] { PropertyProvider.getProperty("url")
	        						+ "/api/v1/users/self/activity_stream" });
        		}
        	} 	
    });
	}

	@Override
	protected void onStart() {
		Log.d("LifeCycle-dash", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.d("LifeCycle-dash", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d("LifeCycle-dash", "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d("LifeCycle-dash", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d("LifeCycle-dash", "onStop");
		if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
			asyncTask.cancel(true);
		}
		if(asynTaskForRefresh != null && asynTaskForRefresh.getStatus() == Status.RUNNING)
		{
			asynTaskForRefresh.cancel(true);
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d("LifeCycle-dash", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d("LifeCycle-dash", "onSaveInsatace");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d("LifeCycle-dash", "onRestoreInsatace");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	public void setList() {

		if (adapter == null) {
			// Initialize the empty list
			adapter = new CustomArrayAdapterActivityStream(this, activityStream);
			list.setAdapter(adapter);
		} else {
			// Clear list and add new items
			adapter.clear();
			adapter.addAll(activityStream);
			adapter.notifyDataSetChanged();
		}
	}

	public List<ActivityStream> getActivityStream() {
		return activityStream;
	}

	public void setActivityStream(List<ActivityStream> activityStream) {
		this.activityStream = activityStream;
		setList();
	}

	// Hide progressbar
	public void setProgressGone() {
		viewContainer.setVisibility(View.GONE);
	}

}
