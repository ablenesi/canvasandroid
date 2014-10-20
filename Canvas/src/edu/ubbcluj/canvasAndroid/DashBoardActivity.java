package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ubbcluj.canvasAndroid.backend.repository.ActivityStreamDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterActivityStream;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;

public class DashBoardActivity extends BaseActivity {

	private DAOFactory df;
	private List<ActivityStream> activityStream;

	private CustomArrayAdapterActivityStream adapter;
	
	private ListView list;
	private View viewContainer;

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
		df = DAOFactory.getInstance();

		ActivityStreamDAO dashboardDao;
		dashboardDao = df.getDashboardDAO();

		activityStream = new ArrayList<ActivityStream>();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ActivityStream as = activityStream.get(position);
				
				if (as.getType().equals("Announcement")) {
					Intent informationIntent = new Intent(DashBoardActivity.this, InformationActivity.class);
					
					Bundle bundle = new Bundle();
					
					bundle.putSerializable("activity_type", InformationActivity.AnnouncementInformation);
					bundle.putInt("course_id", as.getCourseId());						
					bundle.putInt("announcement_id", as.getSecondaryId());
					informationIntent.putExtras(bundle);
					startActivity(informationIntent);
				}
				
				if (as.getType().equals("Submission")) {
					Intent informationIntent = new Intent(DashBoardActivity.this, InformationActivity.class);
					
					Bundle bundle = new Bundle();
					
					bundle.putSerializable("activity_type", InformationActivity.AssignmentInformation);
					bundle.putInt("course_id", as.getCourseId());						
					bundle.putInt("assignment_id", as.getSecondaryId());
					informationIntent.putExtras(bundle);
					startActivity(informationIntent);
				}
				
				if (as.getType().equals("Message")) {
					Intent informationIntent = new Intent(DashBoardActivity.this, InformationActivity.class);
					
					Bundle bundle = new Bundle();
					
					bundle.putSerializable("activity_type", InformationActivity.AssignmentInformation);
					bundle.putInt("course_id", as.getCourseId());						
					bundle.putInt("assignment_id", as.getSecondaryId());
					informationIntent.putExtras(bundle);
					startActivity(informationIntent);
				}
				
				if (as.getType().equals("Conversation")) {
					Intent messagesItemIntent = new Intent(DashBoardActivity.this, MessageItemActivity.class);
					
					Bundle bundle = new Bundle();					
					bundle.putInt("id", as.getSecondaryId());
					messagesItemIntent.putExtras(bundle);
					startActivity(messagesItemIntent);
				}
			
			}
		});
		
		//dashboardDao.setDba(this);
		
		dashboardDao.addInformationListener(new InformationListener() {
			
			@Override
			public void onComplete(InformationEvent e) {
				ActivityStreamDAO asd = (ActivityStreamDAO) e.getSource();
				setProgressGone();
				setActivityStream(asd.getData());
			}
		});
		
		((AsyncTask<String, Void, String>) dashboardDao)
				.execute(new String[] { PropertyProvider.getProperty("url") + "/api/v1/users/self/activity_stream" });
		

		// Initialize the dashboard list
		setList();
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
