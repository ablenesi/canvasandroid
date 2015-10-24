package edu.ubbcluj.canvasAndroid.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.AnnouncementController;
import edu.ubbcluj.canvasAndroid.controller.AssignmentsController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.FolderController;
import edu.ubbcluj.canvasAndroid.controller.ToDoController;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.model.File;
import edu.ubbcluj.canvasAndroid.model.FileTreeElement;
import edu.ubbcluj.canvasAndroid.model.Folder;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.FolderStack;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.util.network.RestDownloadManager;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterAnnouncements;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterAssignments;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterFileTreeElements;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterToDo;

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
		int actualSection = tab.getPosition() + 1;
		
		switch (actualSection) {
		case 1: {
			if(!CookieHandler.checkData(getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider
					.getProperty("url")
					+ "/api/v1/courses/"
					+ courseID
					+ "/todo") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		case 2: {
			if(!CookieHandler.checkData(this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider
					.getProperty("url")
					+ "/api/v1/courses/"
					+ courseID
					+ "/assignments") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		case 3: {
			if(!CookieHandler.checkData(this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider
					.getProperty("url")
					+ "/api/v1/courses/"
					+ courseID
					+ "/activity_stream") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		case 4: {
			if(!CookieHandler.checkData(this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider
					.getProperty("url")
					+ "/api/v1/courses/"
					+ courseID
					+ "/folders/by_path") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		}
		
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
			return 4;
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
			case 3:
				return getString(R.string.tab_files).toUpperCase(l);
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

		private ControllerFactory cf;
		private List<Assignment> assignments;
		private List<Announcement> announcements;
		private List<FileTreeElement> fileTreeElements;

		private CustomArrayAdapterAssignments assignmentsAdapter;
		private CustomArrayAdapterToDo toDoAdapter;
		private CustomArrayAdapterAnnouncements announcementAdapter;
		private CustomArrayAdapterFileTreeElements fileTreeElementsAdapter;

		final FolderStack folderStack = new FolderStack();
		
		private AsyncTask<String, Void, String> asyncTaskAssignment;
		private AsyncTask<String, Void, String> asyncTaskForRefreshAssignment;
		
		private AsyncTask<String, Void, String> asyncTaskAnnouncement;
		private AsyncTask<String, Void, String> asyncTaskForRefreshAnnouncement;
		
		private AsyncTask<String, Void, String> asyncTaskFolder;
		private AsyncTask<String, Void, String> asyncTaskForRefreshFolder;
		
		private AsyncTask<String, Void, String> asyncTaskComingUp;
		private AsyncTask<String, Void, String> asyncTaskForRefreshComingUp;

		private SwipeRefreshLayout swipeView;
		
		private RestDownloadManager downloadManager;
		
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
			cf = ControllerFactory.getInstance();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

			View rootView;

			final SharedPreferences sp = this
					.getActivity()
					.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE);

			switch (sectionNumber) {
			case 1: {
				ToDoController todoController;

				rootView = inflater.inflate(R.layout.fragment_assignment, null);

				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Assignment assignment = assignments.get(position);
						
						if (asyncTaskComingUp != null)
							asyncTaskComingUp.cancel(true);
						if (swipeView != null)
							swipeView.setRefreshing(false);
						
						if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
								PropertyProvider.getProperty("url")
									+ "/api/v1/courses/"
									+ assignment.getCourseId()
									+ "/assignments/"
									+ assignment.getId()) && !CheckNetwork.isNetworkOnline(getActivity())) {
							Toast.makeText(getActivity(), "No network connection!",
									Toast.LENGTH_LONG).show();
						} else {
							Intent assignmentIntent = new Intent(
									getActivity(), AssignmentActivity.class);
	
							Bundle bundle = new Bundle();
							bundle.putInt("course_id", assignment.getCourseId());
							bundle.putInt("assignment_id", assignment.getId());
	
							assignmentIntent.putExtras(bundle);
	
							startActivity(assignmentIntent);
						}
					}
				});

				assignments = new ArrayList<Assignment>();
				todoController = cf.getToDoController();
				todoController.setSharedPreferences(sp);

				todoController.addInformationListener(new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						ToDoController ad = (ToDoController) e.getSource();

						setProgressGone();
						setAssignments(ad.getData());
						toDoAdapter = new CustomArrayAdapterToDo(getActivity(),
								assignments);
						list.setAdapter(toDoAdapter);
					}
				});

				if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
						PropertyProvider
						.getProperty("url")
						+ "/api/v1/courses/"
						+ courseID
						+ "/todo") && !CheckNetwork.isNetworkOnline(getActivity())) {
					setProgressGone();
				} else {
					asyncTaskComingUp = ((AsyncTask<String, Void, String>) todoController);
					asyncTaskComingUp.execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ courseID
							+ "/todo" });
				}

				swipeView = (SwipeRefreshLayout) rootView
						.findViewById(R.id.swipe);

				swipeView
						.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
							@Override
							public void onRefresh() {
				        		if(!CheckNetwork.isNetworkOnline(getActivity())) {
				        			swipeView.setRefreshing(false);
									Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_LONG).show();
				        		} else {
									ToDoController todoController;
									todoController = cf.getToDoController();
									todoController.setSharedPreferences(sp);
									RestInformation.clearData();
	
									todoController.addInformationListener(new InformationListener() {
	
										@Override
										public void onComplete(InformationEvent e) {
											ToDoController ad = (ToDoController) e.getSource();
	
											setProgressGone();
											setAssignments(ad.getData());
											toDoAdapter = new CustomArrayAdapterToDo(
													getActivity(), assignments);
											list.setAdapter(toDoAdapter);
											swipeView.setRefreshing(false);
										}
									});
	
									asyncTaskForRefreshComingUp = ((AsyncTask<String, Void, String>) todoController);
									asyncTaskForRefreshComingUp.execute(new String[] { PropertyProvider
											.getProperty("url")
											+ "/api/v1/courses/"
											+ courseID
											+ "/todo" });
				        		}
							}
						});

				break;
			}
			case 2: {
				rootView = inflater.inflate(R.layout.fragment_assignment, null);

				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);

				AssignmentsController assignmentsController;
				assignmentsController = cf.getAssignmentsController();
				assignmentsController.setSharedPreferences(sp);

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Assignment assignment = assignments.get(position);

						if (asyncTaskAssignment != null)
							asyncTaskAssignment.cancel(true);
						if (swipeView != null)
							swipeView.setRefreshing(false);
						
						if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
								PropertyProvider.getProperty("url")
									+ "/api/v1/courses/"
									+ assignment.getCourseId()
									+ "/assignments/"
									+ assignment.getId()) && !CheckNetwork.isNetworkOnline(getActivity())) {
							Toast.makeText(getActivity(), "No network connection!",
									Toast.LENGTH_LONG).show();
						} else {
						Intent assignmentIntent = new Intent(getActivity(),
								AssignmentActivity.class);

						Bundle bundle = new Bundle();
						bundle.putInt("course_id", assignment.getCourseId());
						bundle.putInt("assignment_id", assignment.getId());

						assignmentIntent.putExtras(bundle);

						startActivity(assignmentIntent);
						}
					}
				});

				assignments = new ArrayList<Assignment>();

				// assignmentsController.setPlaceholderFragment(this);
				assignmentsController
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AssignmentsController ad = (AssignmentsController) e
										.getSource();

								setProgressGone();
								setAssignments(ad.getData());
								assignmentsAdapter = new CustomArrayAdapterAssignments(
										getActivity(), assignments);
								list.setAdapter(assignmentsAdapter);
							}
						});

				if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
						PropertyProvider
						.getProperty("url")
						+ "/api/v1/courses/"
						+ courseID
						+ "/assignments") && !CheckNetwork.isNetworkOnline(getActivity())) {
						setProgressGone();
				} else {
					asyncTaskAssignment = ((AsyncTask<String, Void, String>) assignmentsController);
					asyncTaskAssignment.execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ courseID
							+ "/assignments" });
				}

				final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView
						.findViewById(R.id.swipe);

				swipeView
						.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
							@Override
							public void onRefresh() {
				        		if(!CheckNetwork.isNetworkOnline(getActivity())) {
				        			swipeView.setRefreshing(false);
									Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_LONG).show();
				        		} else {
									AssignmentsController assignmentsController;
									assignmentsController = cf.getAssignmentsController();
									assignmentsController.setSharedPreferences(sp);
									RestInformation.clearData();
	
									// assignmentsController.setPlaceholderFragment(this);
									assignmentsController
											.addInformationListener(new InformationListener() {
	
												@Override
												public void onComplete(
														InformationEvent e) {
													AssignmentsController ad = (AssignmentsController) e
															.getSource();
	
													setProgressGone();
													setAssignments(ad.getData());
													assignmentsAdapter = new CustomArrayAdapterAssignments(
															getActivity(),
															assignments);
													list.setAdapter(assignmentsAdapter);
													swipeView.setRefreshing(false);
												}
											});
	
									asyncTaskForRefreshAssignment = ((AsyncTask<String, Void, String>) assignmentsController);
									asyncTaskForRefreshAssignment.execute(new String[] { PropertyProvider
											.getProperty("url")
											+ "/api/v1/courses/"
											+ courseID
											+ "/assignments" });
				        		}
							}
						});
				break;
			}
			case 3: {
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
						
						if (asyncTaskAssignment != null)
							asyncTaskAssignment.cancel(true);
						if (swipeView != null)
							swipeView.setRefreshing(false);
						
						if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
								PropertyProvider.getProperty("url")
									+ "/api/v1/courses/"
									+ announcement.getCourseId()
									+ "/discussion_topics/"
									+ announcement.getAnnouncementId()) && !CheckNetwork.isNetworkOnline(getActivity())) {
							Toast.makeText(getActivity(), "No network connection!",
									Toast.LENGTH_LONG).show();
						} else {
							announcement.setRead_state(true);
							announcements.set(position,announcement);
							announcementAdapter = new CustomArrayAdapterAnnouncements(
									getActivity(), announcements);
							list.setAdapter(announcementAdapter);
							Intent announcementIntent = new Intent(getActivity(),
									AnnouncementActivity.class);
	
							Bundle bundle = new Bundle();
							bundle.putInt("course_id", announcement.getCourseId());
							bundle.putInt("announcement_id",
									announcement.getAnnouncementId());
	
							announcementIntent.putExtras(bundle);
	
							startActivity(announcementIntent);
						}
					}
				});

				AnnouncementController announcementController;
				announcementController = cf.getAnnouncementController();
				announcementController.setSharedPreferences(sp);

				announcementController
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AnnouncementController ad = (AnnouncementController) e
										.getSource();

								setProgressGone();
								setAnnouncement(ad.getData());
								announcementAdapter = new CustomArrayAdapterAnnouncements(
										getActivity(), announcements);
								list.setAdapter(announcementAdapter);
							}
						});

				if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
						PropertyProvider
						.getProperty("url")
						+ "/api/v1/courses/"
						+ courseID
						+ "/activity_stream") && !CheckNetwork.isNetworkOnline(getActivity())) {
					setProgressGone();
				} else {
					asyncTaskAnnouncement = ((AsyncTask<String, Void, String>) announcementController);
					asyncTaskAnnouncement.execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ courseID
							+ "/activity_stream" });
				}

				final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView
						.findViewById(R.id.swipe);

				swipeView
						.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
							@Override
							public void onRefresh() {
				        		if(!CheckNetwork.isNetworkOnline(getActivity())) {
				        			swipeView.setRefreshing(false);
									Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_LONG).show();
				        		} else {
									AnnouncementController announcementController;
									announcementController = cf.getAnnouncementController();
									announcementController.setSharedPreferences(sp);
									RestInformation.clearData();
	
									announcementController
											.addInformationListener(new InformationListener() {
	
												@Override
												public void onComplete(
														InformationEvent e) {
													AnnouncementController ad = (AnnouncementController) e
															.getSource();
	
													setProgressGone();
													setAnnouncement(ad.getData());
													announcementAdapter = new CustomArrayAdapterAnnouncements(
															getActivity(),
															announcements);
													list.setAdapter(announcementAdapter);
													swipeView.setRefreshing(false);
												}
											});
	
									asyncTaskForRefreshAnnouncement = ((AsyncTask<String, Void, String>) announcementController);
									asyncTaskForRefreshAnnouncement.execute(new String[] { PropertyProvider
											.getProperty("url")
											+ "/api/v1/courses/"
											+ courseID
											+ "/activity_stream" });
				        		}
							}
						});

				break;
			}
			case 4: {
				rootView = inflater.inflate(R.layout.fragment_assignment, null);

				final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView
						.findViewById(R.id.swipe);

				// Set the progressbar visibility
				list = (ListView) rootView.findViewById(R.id.list);
				viewContainer = rootView.findViewById(R.id.linProg);
				viewContainer.setVisibility(View.VISIBLE);

				final InformationListener folderInformationListener = new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						FolderController fd = (FolderController) e.getSource();

						setProgressGone();
						setFileTreeElements(fd.getData());
						fileTreeElementsAdapter = new CustomArrayAdapterFileTreeElements(
								getActivity(), fileTreeElements);
						list.setAdapter(fileTreeElementsAdapter);
						swipeView.setRefreshing(false);
					}
				};
				
				swipeView
						.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
							@Override
							public void onRefresh() {
				        		if(!CheckNetwork.isNetworkOnline(getActivity())) {
				        			swipeView.setRefreshing(false);
									Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_LONG).show();
				        		} else {
									FolderController folderController;
									folderController = cf.getFolderController();
									folderController.setSharedPreferences(sp);
									RestInformation.clearData();
	
									folderController
											.addInformationListener(folderInformationListener);
	
									Folder currentFolder = folderStack.getHead();
	
									asyncTaskForRefreshFolder = ((AsyncTask<String, Void, String>) folderController);
									asyncTaskForRefreshFolder.execute(new String[] {
											currentFolder.getFoldersUrl(),
											currentFolder.getFilesUrl() });
				        		}
							}
						});

				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FileTreeElement fileTreeElement = fileTreeElements
								.get(position);

						// if we are in the root folder
						if (fileTreeElement != null) {
							if (fileTreeElement instanceof File) {
								File file = (File) fileTreeElement;
								
								if (!CheckNetwork.isNetworkOnline(getActivity())) {
									
									Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_SHORT).show();
									
								} else {
									downloadManager = new RestDownloadManager(getActivity());
									downloadManager.registerActionDownloadCompleteReceiver();
									downloadManager.registerActionNotificationClickedReceiver();
									downloadManager.downloadFile(file);
									
									Toast.makeText(getActivity(), file.getName() + " downloading...",
											Toast.LENGTH_LONG).show();
								}
								
							} else {
								Folder folder = (Folder) fileTreeElement;

								if((!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
										folder.getFoldersUrl()) || 
									!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
										folder.getFilesUrl())) && 
									!CheckNetwork.isNetworkOnline(getActivity())) {
										Toast.makeText(getActivity(), "No network connection!",
											Toast.LENGTH_LONG).show();
								} else {
									if (folder != null) {
										swipeView.setRefreshing(true);
	
										if (position == 0) {
											folderStack.removeHead();
										} else
											folderStack.push(folder);
	
										FolderController folderController;
										folderController = cf.getFolderController();
										folderController.setSharedPreferences(sp);
	
										folderController
												.addInformationListener(folderInformationListener);
	
										asyncTaskFolder = ((AsyncTask<String, Void, String>) folderController);
										asyncTaskFolder.execute(new String[] {
												folder.getFoldersUrl(),
												folder.getFilesUrl() });
									}
								}
							}
						}
					}
				});

				FolderController folderController;
				folderController = cf.getFolderController();
				folderController.setSharedPreferences(sp);

				folderController.addInformationListener(new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						FolderController fd = (FolderController) e.getSource();

						List<FileTreeElement> fte = fd.getData();
						Folder rootfolder = (Folder) fte.get(1);

						folderStack.push(rootfolder);

						FolderController folderControllerforRootElements = cf.getFolderController();
						folderControllerforRootElements.setSharedPreferences(sp);
						folderControllerforRootElements
								.addInformationListener(folderInformationListener);

						asyncTaskFolder = ((AsyncTask<String, Void, String>) folderControllerforRootElements);
						asyncTaskFolder.execute(new String[] {
								rootfolder.getFoldersUrl(),
								rootfolder.getFilesUrl() });
					}
				});

				
				if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
						PropertyProvider
						.getProperty("url")
						+ "/api/v1/courses/"
						+ courseID
						+ "/folders/by_path") && !CheckNetwork.isNetworkOnline(getActivity())) {
					setProgressGone();
				} else {
					asyncTaskFolder = ((AsyncTask<String, Void, String>) folderController);
					asyncTaskFolder.execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ courseID
							+ "/folders/by_path" });
				}

				break;
			}
			default:
				rootView = inflater.inflate(R.layout.fragment_course, null);
			}

			return rootView;
		}

		@Override
		public void onStop() {
			if (asyncTaskComingUp != null && asyncTaskComingUp.getStatus() == Status.RUNNING) {
				asyncTaskComingUp.cancel(true);
			}
			if (asyncTaskForRefreshComingUp != null
					&& asyncTaskForRefreshComingUp.getStatus() == Status.RUNNING) {
				asyncTaskForRefreshComingUp.cancel(true);
			}
			
			if (asyncTaskAnnouncement != null && asyncTaskAnnouncement.getStatus() == Status.RUNNING) {
				asyncTaskAnnouncement.cancel(true);
			}
			if (asyncTaskForRefreshAnnouncement != null
					&& asyncTaskForRefreshAnnouncement.getStatus() == Status.RUNNING) {
				asyncTaskForRefreshAnnouncement.cancel(true);
			}
			
			if (asyncTaskAssignment != null && asyncTaskAssignment.getStatus() == Status.RUNNING) {
				asyncTaskAssignment.cancel(true);
			}
			if (asyncTaskForRefreshAssignment != null
					&& asyncTaskForRefreshAssignment.getStatus() == Status.RUNNING) {
				asyncTaskForRefreshAssignment.cancel(true);
			}
			
			if (asyncTaskFolder != null && asyncTaskFolder.getStatus() == Status.RUNNING) {
				asyncTaskFolder.cancel(true);
			}
			if (asyncTaskForRefreshFolder != null
					&& asyncTaskForRefreshFolder.getStatus() == Status.RUNNING) {
				asyncTaskForRefreshFolder.cancel(true);
			}
			
			if (downloadManager != null)
				downloadManager.unRegisterReceiver();

			super.onStop();
		}

		public void setAssignments(List<Assignment> assignment) {
			this.assignments = assignment;
		}

		public void setAnnouncement(List<Announcement> announcement) {
			this.announcements = announcement;
		}

		public void setFileTreeElements(List<FileTreeElement> fileTreeElements) {
			this.fileTreeElements = fileTreeElements;
			fileTreeElements.set(0, folderStack.getHeadParent());
		}

		// Hide progressbar
		public void setProgressGone() {
			viewContainer.setVisibility(View.GONE);
		}
	}

}
