package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
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
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;

public class BaseActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	protected static NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("LifeCycle-base", "onCreate");

		// set the shared preferences with the Base activity sharedpreferences
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(BaseActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

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
		// update the main content by replacing fragments
		// FragmentManager fragmentManager = getSupportFragmentManager();

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
		/*
		 * ArrayList<ActiveCourse> activeCourses = (ArrayList<ActiveCourse>)
		 * mNavigationDrawerFragment .getActiveCourses();
		 */

		// mTitle = activeCourses.get(number).getName();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.default_menu, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.refresh:
			RestInformationDAO.clearData();
			finish();
			startActivity(getIntent());
			return true;

		case R.id.dashboard:
			if (this.getClass() == DashBoardActivity.class) {
				RestInformationDAO.clearData();
				finish();
				startActivity(getIntent());
			} else {
				Intent intent = new Intent(this, DashBoardActivity.class);
				startActivity(intent);
			}
			return true;
		case R.id.messages:
			if (this.getClass() == MessagesActivity.class) {
				RestInformationDAO.clearData();
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
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
			}
		}
	}

}
