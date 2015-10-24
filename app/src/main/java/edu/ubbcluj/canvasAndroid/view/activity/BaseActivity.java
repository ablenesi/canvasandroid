package edu.ubbcluj.canvasAndroid.view.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.CourseProvider;
import edu.ubbcluj.canvasAndroid.persistence.ServiceProvider;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonCookie;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;

public class BaseActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	protected static NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_base);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setBaseActivity(this);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		mNavigationDrawerFragment.setMenu();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		CourseProvider.getInstance().initialize(this);
		
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(BaseActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Intent courseIntent = new Intent(this, CourseActivity.class);

		Bundle bundle = new Bundle();
		// courseID
		bundle.putInt("id",
				mNavigationDrawerFragment.getActiveCourses().get(position)
						.getId());
		// course name
		bundle.putString("name", mNavigationDrawerFragment.getActiveCourses()
				.get(position).getName());

		courseIntent.putExtras(bundle); // Put the id to the Course Intent
		startActivity(courseIntent);
	}

	public void onSectionAttached(int number) {
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		menu.removeItem(Menu.FIRST+6);
		menu.removeItem(Menu.FIRST+7);
        if (ServiceProvider.getInstance().getService_started())
        	menu.add(Menu.NONE,Menu.FIRST+6,Menu.NONE,R.string.stop_service);
		else
			menu.add(Menu.NONE,Menu.FIRST+7,Menu.NONE,R.string.start_service);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.default_menu, menu);
		restoreActionBar();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mNavigationDrawerFragment.onOptionsItemSelected(item))
			return true;

		int id = item.getItemId();
		Intent intent = null;
		
		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.messages:
			if(!CookieHandler.checkData(this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider.getProperty("url")
					+ "/api/v1/conversations") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			} else {
				intent = new Intent(this, MessagesActivity.class);
				startActivity(intent);
			}
			return true;
		case R.id.settings:
			intent = new Intent(this, CourseSelectionActivity.class);
			startActivity(intent);
			return true;
		case R.id.logout:
			if (ServiceProvider.getInstance().getService_started()){
				Intent intent4 = new Intent(this,MyService.class);
				ServiceProvider.getInstance().setService_started(false);
				ServiceProvider.getInstance().setNewAnnouncementUnreadCount(0);
				ServiceProvider.getInstance().setAnnouncementUnreadCount(0);
				Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
				MyService.alarm.cancel(PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0));
				stopService(intent4);
			}
			SingletonCookie.getInstance().deleteCookieStore();
			
			intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			return true;
		case Menu.FIRST+6:
			Intent intent2 = new Intent(this,MyService.class);
			ServiceProvider.getInstance().setService_started(false);
			Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
			MyService.alarm.cancel(PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0));
			stopService(intent2);
			return true;
		case Menu.FIRST+7:
			Intent intent3 = new Intent(this,MyService.class);
			ServiceProvider.getInstance().setService_started(true);
			Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
			startService(intent3);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_base, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

			ArrayList<ActiveCourse> activeCourses = (ArrayList<ActiveCourse>) mNavigationDrawerFragment
					.getActiveCourses();

			if (activeCourses.size() != 0) {
				((BaseActivity) activity).onSectionAttached(getArguments()
						.getInt(ARG_SECTION_NUMBER));
			}
		}
	}



}
