package edu.ubbcluj.canvasAndroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.AssignmentsDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.restApi.RestInformationDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.ServiceProvider;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;
import edu.ubbcluj.canvasAndroid.model.AnnouncementCommentReplies;
import edu.ubbcluj.canvasAndroid.model.Assignment;

public class InformationActivity extends BaseActivity {

	private enum ActivityType {
		Assignment, Announcement
	};

	public static final ActivityType AssignmentInformation = ActivityType.Assignment;
	public static final ActivityType AnnouncementInformation = ActivityType.Announcement;

	private static ActivityType activityType;
	private static int courseID;
	private static int assignmentID;
	private static int announcementID;
	
	private static String activityTitle = "Information";

	private static View progressContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();

		activityType = (ActivityType) bundle.getSerializable("activity_type");

		super.onCreate(savedInstanceState);

		if (activityType == AssignmentInformation) {
			activityTitle = "Assignment";
		} else {
			activityTitle = "Announcement";
			
		}

		if (savedInstanceState == null) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, fragment).commit();
		}
	}

	@Override
	public void restoreActionBar() {
		super.restoreActionBar();
		getSupportActionBar().setTitle(activityTitle);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private DAOFactory df = DAOFactory.getInstance();
		private Assignment assignment;
		private Announcement announcement;

		private AsyncTask<String, Void, String> asyncTask;
		
		TextView textViews[] = null;
		private LinearLayout linearLayout;

		public PlaceholderFragment() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = null;

			Bundle bundle = getArguments();
			activityType = (ActivityType) bundle
					.getSerializable("activity_type");
			courseID = bundle.getInt("course_id");
			if (activityType == AssignmentInformation) {
				assignmentID = bundle.getInt("assignment_id");
			} else {
				announcementID = bundle.getInt("announcement_id");
			}

			SharedPreferences sp = this.getActivity().getSharedPreferences(
					"CanvasAndroid", Context.MODE_PRIVATE);

			switch (activityType) {
			case Assignment:
				rootView = inflater.inflate(R.layout.fragment_anassignment,
						container, false);

				textViews = new TextView[4];

				textViews[0] = (TextView) rootView
						.findViewById(R.id.anassignment_name);
				textViews[1] = (TextView) rootView
						.findViewById(R.id.anassignment_due_date);
				textViews[2] = (TextView) rootView
						.findViewById(R.id.anassignment_possible_grade);
				textViews[3] = (TextView) rootView
						.findViewById(R.id.anassignment_description);

				AssignmentsDAO assignmentDAO = df.getAssignmentsDAO();
				assignmentDAO.setSharedPreferences(sp);

				assignmentDAO.addInformationListener(new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						AssignmentsDAO ad = (AssignmentsDAO) e.getSource();

						if (!ad.getData().isEmpty()) {
							setAssignment(ad.getData().get(0));
							setProgressGone();
						}
					}
				});

				asyncTask = ((AsyncTask<String, Void, String>) assignmentDAO);
				asyncTask.execute(new String[] { PropertyProvider
								.getProperty("url")
								+ "/api/v1/courses/"
								+ courseID + "/assignments/" + assignmentID });

				break;

			case Announcement:
				rootView = inflater.inflate(R.layout.fragment_anannouncement,
						container, false);
				linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_announcement);

				textViews = new TextView[4];

				textViews[0] = (TextView) rootView
						.findViewById(R.id.anannouncement_title);
				textViews[1] = (TextView) rootView
						.findViewById(R.id.anannouncement_date);
				textViews[2] = (TextView) rootView
						.findViewById(R.id.anannouncement_author_name);
				textViews[3] = (TextView) rootView
						.findViewById(R.id.anannouncement_message);

				AnnouncementDAO announcementDAO = df.getAnnouncementDAO();
				announcementDAO.setSharedPreferences(sp);

				announcementDAO
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AnnouncementDAO ad = (AnnouncementDAO) e
										.getSource();

								if (!ad.getData().isEmpty()) {
									setAnnouncement(ad.getData().get(0));
									setProgressGone();
								}
								
							}
						});

				asyncTask = ((AsyncTask<String, Void, String>) announcementDAO);
				asyncTask.execute(new String[] { PropertyProvider
								.getProperty("url")
								+ "/api/v1/courses/"
								+ courseID
								+ "/discussion_topics/"
								+ announcementID });

				break;
			}

			setProgressVisible(rootView);

			return rootView;
		}

		public Assignment getAssignment() {
			return assignment;
		}

		public void setAssignment(Assignment assignment) {
			this.assignment = assignment;

			if (textViews != null) {
				textViews[0].setText(assignment.getName());
				textViews[1].setText(formatDate(assignment.getDueAt()));
				textViews[2].setText("Maximum grade: "
						+ assignment.getPointsPossible());

				if (assignment.getIsGraded()) {
					textViews[2].append(" (Your grade: "
							+ assignment.getScore() + ")");
				}

				if (assignment.getLockExplanation() != null) {
					textViews[3].setText(assignment.getLockExplanation());
				} else {
					if (assignment.getDescription() != null) {
						textViews[3].setText(Html.fromHtml(assignment
								.getDescription()));
					} else {
						textViews[3].setText("No description");
					}
				}
			}
		}

		public Announcement getAnnouncement() {
			return announcement;
		}

		public void setAnnouncement(Announcement announcement) {
			this.announcement = announcement;

			if (textViews != null) {
				if (!announcement.getRead_state()){
					ServiceProvider.getInstance().setAnnouncementUnreadCount(
							ServiceProvider.getInstance().getAnnouncementUnreadCount()-1);
					announcement.setRead_state(true);
					new RestInformationDAO().execute(new String[] { PropertyProvider
									.getProperty("url")
									+ "/courses/"
									+ courseID
									+ "/announcements/"
									+ announcementID});
				}
				textViews[0].setText(announcement.getTitle());
				textViews[1].setText(formatDate(announcement.getPostedAt()));
				textViews[2].setText(announcement.getAuthorName());
				textViews[3].setText(Html.fromHtml(announcement.getMessage()));
				
				if (announcement.getAc() == null || announcement.getAc().length == 0) {
					TextView tw = new TextView(getActivity());
					tw.setText("No comments");
					tw.setTextColor(Color.BLACK);
					linearLayout.addView(tw);
				} else {
					AnnouncementComment[] comments = announcement.getAc();
					
					for (int i = 0; i < comments.length; i++) {
						TextView twComment = new TextView(getActivity());
						twComment.setTextColor(Color.BLACK);
						twComment.setText(comments[i].getMessage());
						linearLayout.addView(twComment);
						
						TextView twAuthor = new TextView(getActivity());
						twAuthor.setTextColor(Color.GRAY);
						twAuthor.setGravity(Gravity.END);
						twAuthor.setText(comments[i].getUserName() + "\n");
						linearLayout.addView(twAuthor);
						AnnouncementCommentReplies[] replies = comments[i].getAcr();
						for(int j=0;j<replies.length;j++){
							TextView twCommentReplie = new TextView(getActivity());
							twCommentReplie.setTextColor(Color.BLACK);
							twCommentReplie.setText(replies[j].getMessage());
							linearLayout.addView(twCommentReplie);
							
							TextView twAuthorReplie = new TextView(getActivity());
							twAuthorReplie.setTextColor(Color.GRAY);
							twAuthorReplie.setGravity(Gravity.END);
							twAuthorReplie.setText(replies[j].getUserName() + "\n");
							linearLayout.addView(twAuthorReplie);
						}
					}
				}
			}
			
		}

		private String formatDate(String date) {
			if (!date.startsWith("No")) {
				String newDate = date.substring(0, date.indexOf('Z'));
				newDate = newDate.replace("T", "\n");

				return newDate;
			}

			return date;
		}
		
		@Override
		public void onStop() {
			if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
				asyncTask.cancel(true);
			}
			super.onStop();
		}

	}

	public static void setProgressVisible(View rootView) {
		progressContainer = rootView.findViewById(R.id.linProg);
		progressContainer.setVisibility(View.VISIBLE);
	}

	public static void setProgressGone() {
		progressContainer.setVisibility(View.GONE);
	}

}
