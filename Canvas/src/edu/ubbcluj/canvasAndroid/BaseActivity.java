package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.backend.repository.restApi.RestInformationDAO;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonCookie;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;

public class BaseActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	protected static NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("LifeCycle-base", "onCreate");

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
	protected void onStart() {
		Log.d("LifeCycle-base", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.d("LifeCycle-base", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d("LifeCycle-base", "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d("LifeCycle-base", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d("LifeCycle-base", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d("LifeCycle-base", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d("LifeCycle-base", "onSaveInsatace");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d("LifeCycle-base", "onRestoreInsatace");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.default_menu, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mNavigationDrawerFragment.onOptionsItemSelected(item))
			return true;

		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.refresh:
			// RestInformationDAO.clearData();
			finish();
			startActivity(getIntent());
			return true;

		case R.id.dashboard:
			if (this.getClass() == DashBoardActivity.class) {
				finish();
				startActivity(getIntent());
			} else {
				Intent intent = new Intent(this, DashBoardActivity.class);
				startActivity(intent);
			}
			return true;
		case R.id.messages:
			if (this.getClass() == MessagesActivity.class) {
				finish();
				startActivity(getIntent());
			} else {
				Intent intent = new Intent(this, MessagesActivity.class);
				startActivity(intent);
			}
			return true;
		case R.id.logout:
			SingletonCookie.getInstance().deleteCookieStore();

			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

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
				Toast.makeText(activity, "valami", Toast.LENGTH_SHORT).show();

				((BaseActivity) activity).onSectionAttached(getArguments()
						.getInt(ARG_SECTION_NUMBER));
			} else
				Log.e("Courses", "Number of courses = 0");
		}
	}



}
