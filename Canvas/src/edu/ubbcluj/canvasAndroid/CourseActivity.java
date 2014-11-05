package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.AssignmentsDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.ToDoDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterAnnouncements;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterAssignments;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterToDo;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.model.Assignment;

@SuppressWarnings("deprecation")
public class CourseActivity extends BaseActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static int courseID;
	private String courseName;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the shared prefferences with the Course activity sharedpreferences
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(CourseActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

		// Set up the action bar.
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// get the course id
		Bundle bundle = getIntent().getExtras();
		courseID = bundle.getInt("id");
		courseName = bundle.getString("name");

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			View tabView = this.getLayoutInflater().inflate(
					R.layout.actionbar_tab, null);
			TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
			tabText.setText(mSectionsPagerAdapter.getPageTitle(i));

			actionBar.addTab(actionBar.newTab().setCustomView(tabView)
					.setTabListener(this));
		}
	}

	@Override
	public void restoreActionBar() {
		// TODO Auto-generated method stub
		super.restoreActionBar();
		actionBar.setTitle(courseName);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tab_todo).toUpperCase(l);
			case 1:
				return getString(R.string.tab_assignments).toUpperCase(l);
			case 2:
				return getString(R.string.tab_announcements).toUpperCase(l);
				/*
				 * case 3: return getString(R.string.tab_grades).toUpperCase(l);
				 * case 4: return getString(R.string.tab_files).toUpperCase(l);
				 * case 5: return
				 * getString(R.string.tab_syllabus).toUpperCase(l);
				 */
			}
			return null;
		}
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

		private ListView list;
		private View viewContainer;

		private DAOFactory df;
		private List<Assignment> assignments;
		private List<Announcement> announcements;
		
		private CustomArrayAdapterAssignments assignmentsAdapter;
		private CustomArrayAdapterToDo toDoAdapter;
		private CustomArrayAdapterAnnouncements announcementAdapter;
		
		private AsyncTask<String, Void, String> asyncTask;

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
			// Get the activity stream
			df = DAOFactory.getInstance();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

			View rootView;

			switch (sectionNumber) {
			case 1:
				rootView = inflater.inflate(R.layout.fragment_assignment, null);
				
				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);
				
				ToDoDAO todoDao;
				todoDao = df.getToDoDAO();
				
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Assignment assignment = assignments.get(position);
						
						Intent assignmentIntent = new Intent(getActivity(), InformationActivity.class);
						
						Bundle bundle = new Bundle();
						bundle.putSerializable("activity_type", InformationActivity.AssignmentInformation);
						bundle.putInt("course_id", assignment.getCourseId());
						bundle.putInt("assignment_id", assignment.getId());
						
						assignmentIntent.putExtras(bundle);
						
						startActivity(assignmentIntent);
					}
				});
				
				assignments = new ArrayList<Assignment>();
				
				todoDao.addInformationListener(new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						ToDoDAO ad = (ToDoDAO) e.getSource();

						setProgressGone();
						setAssignments(ad.getData());
						toDoAdapter = new CustomArrayAdapterToDo(getActivity(),
								assignments);
						list.setAdapter(toDoAdapter);
					}
				});

				asyncTask = ((AsyncTask<String, Void, String>) todoDao);
				asyncTask.execute(new String[] { PropertyProvider
								.getProperty("url") + "/api/v1/courses/"
								+ courseID + "/todo" });

				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_assignment, null);

				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);

				AssignmentsDAO assignmentsDao;
				assignmentsDao = df.getAssignmentsDAO();

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Assignment assignment = assignments.get(position);

						Intent assignmentIntent = new Intent(getActivity(),
								InformationActivity.class);

						Bundle bundle = new Bundle();
						bundle.putSerializable("activity_type",
								InformationActivity.AssignmentInformation);
						bundle.putInt("course_id", assignment.getCourseId());
						bundle.putInt("assignment_id", assignment.getId());

						assignmentIntent.putExtras(bundle);

						startActivity(assignmentIntent);
					}
				});

				assignments = new ArrayList<Assignment>();

				// assignmentsDao.setPlaceholderFragment(this);
				assignmentsDao
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AssignmentsDAO ad = (AssignmentsDAO) e
										.getSource();

								setProgressGone();
								setAssignments(ad.getData());
								assignmentsAdapter = new CustomArrayAdapterAssignments(
										getActivity(), assignments);
								list.setAdapter(assignmentsAdapter);
							}
						});

				asyncTask = ((AsyncTask<String, Void, String>) assignmentsDao);
				asyncTask.execute(new String[] { PropertyProvider
								.getProperty("url")
								+ "/api/v1/courses/"
								+ courseID + "/assignments" });
				break;

			case 3:
				rootView = inflater.inflate(R.layout.fragment_assignment, null);

				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Announcement announcement = announcements.get(position);

						Intent announcementIntent = new Intent(getActivity(),
								InformationActivity.class);

						Bundle bundle = new Bundle();
						bundle.putSerializable("activity_type",
								InformationActivity.AnnouncementInformation);
						bundle.putInt("course_id", announcement.getCourseId());
						bundle.putInt("announcement_id",
								announcement.getAnnouncementId());

						announcementIntent.putExtras(bundle);

						startActivity(announcementIntent);
					}
				});

				AnnouncementDAO announcementDao;
				announcementDao = df.getAnnouncementDAO();

				announcementDao
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AnnouncementDAO ad = (AnnouncementDAO) e
										.getSource();

								setProgressGone();
								setAnnouncement(ad.getData());
								announcementAdapter = new CustomArrayAdapterAnnouncements(
										getActivity(), announcements);
								list.setAdapter(announcementAdapter);
							}
						});

				asyncTask = ((AsyncTask<String, Void, String>) announcementDao);
				asyncTask.execute(new String[] { PropertyProvider
								.getProperty("url")
								+ "/api/v1/courses/"
								+ courseID + "/activity_stream" });

				break;

			default:
				rootView = inflater.inflate(R.layout.fragment_course, null);
			}

			return rootView;
		}

		@Override
		public void onStop() {
			if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
				asyncTask.cancel(true);
			}
			super.onStop();
		}
		
		public void setAssignments(List<Assignment> assignment) {
			this.assignments = assignment;
		}

		public void setAnnouncement(List<Announcement> announcement) {
			this.announcements = announcement;
		}

		// Hide progressbar
		public void setProgressGone() {
			viewContainer.setVisibility(View.GONE);
		}
	}

}
