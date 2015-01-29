package edu.ubbcluj.canvasAndroid.view.activity;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.ActivityStreamController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterActivityStream;

public class DashBoardActivity extends BaseActivity {

	private ControllerFactory cf;
	private List<ActivityStream> activityStream;

	private CustomArrayAdapterActivityStream adapter;
	
	private ListView list;
	private View viewContainer;

	private AsyncTask<String, Void, String> asyncTask;
	private AsyncTask<String, Void, String> asynTaskForRefresh;
	
	private SwipeRefreshLayout swipeView = null;
	
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the progressbar visibility 
		list = (ListView) findViewById(R.id.list);
		viewContainer = findViewById(R.id.linProg);
		viewContainer.setVisibility(View.VISIBLE);

		// Get the activity stream
		cf = ControllerFactory.getInstance();
		
		ActivityStreamController dashboardController;
		dashboardController = cf.getDashboardController();
		dashboardController.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));
		
		activityStream = new ArrayList<ActivityStream>();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActivityStream as = activityStream.get(position);

				if (asynTaskForRefresh != null)
					asynTaskForRefresh.cancel(true);
				
				if (swipeView != null)
					swipeView.setRefreshing(false);
				
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
						as.setRead_state(true);
						activityStream.set(position, as);
						Intent informationIntent = new Intent(
								DashBoardActivity.this, AnnouncementActivity.class);
	
						Bundle bundle = new Bundle();

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
						as.setRead_state(true);
						activityStream.set(position, as);
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
					as.setRead_state(true);
					activityStream.set(position, as);
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
					as.setRead_state(true);
					activityStream.set(position, as);
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
					as.setRead_state(true);
					activityStream.set(position, as);
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
								DashBoardActivity.this, AnnouncementActivity.class);
	
						Bundle bundle = new Bundle();
						bundle.putInt("course_id", as.getCourseId());
						bundle.putInt("announcement_id", as.getSecondaryId());
						informationIntent.putExtras(bundle);
						startActivity(informationIntent);
					}
				}
			}
		});


		dashboardController.addInformationListener(new InformationListener() {

			@Override
			public void onComplete(InformationEvent e) {
				ActivityStreamController asd = (ActivityStreamController) e.getSource();
				setProgressGone();
				setActivityStream(asd.getData());
			}
		});

		asyncTask = ((AsyncTask<String, Void, String>) dashboardController);
		asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
						+ "/api/v1/users/self/activity_stream" });

		// Initialize the dashboard list
		setList();
		
		swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
        	@Override
        	public void onRefresh() {
        		if(!CheckNetwork.isNetworkOnline(DashBoardActivity.this)) {
        			swipeView.setRefreshing(false);
					Toast.makeText(DashBoardActivity.this, "No network connection!",
							Toast.LENGTH_LONG).show();
        		} else {
	        		ActivityStreamController dashboardController;
	        		dashboardController = cf.getDashboardController();
	        		dashboardController.setSharedPreferences(DashBoardActivity.this.getSharedPreferences(
	        				"CanvasAndroid", Context.MODE_PRIVATE));
	        		RestInformation.clearData();
	        		
	        		dashboardController.addInformationListener(new InformationListener() {	
	        			
	        			@Override
	        			public void onComplete(InformationEvent e) {
	        				ActivityStreamController asd = (ActivityStreamController) e.getSource();
	        				setProgressGone();
	        				setActivityStream(asd.getData());
	        				swipeView.setRefreshing(false);
	        				setList();
	        			}
	        			
	        		});
	        		
	
	        		asynTaskForRefresh = ((AsyncTask<String, Void, String>) dashboardController);
	        		asynTaskForRefresh.execute(new String[] { PropertyProvider.getProperty("url")
	        						+ "/api/v1/users/self/activity_stream" });
        		}
        	} 	
    });
	}

	@Override
	protected void onResume() {
		Log.d("LifeCycle-dash", "onResume");
		super.onResume();
		if(adapter!=null)
			setList();
	}

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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
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
