package edu.ubbcluj.canvasAndroid.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.AnnouncementCommentController;
import edu.ubbcluj.canvasAndroid.controller.AnnouncementController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;
import edu.ubbcluj.canvasAndroid.model.AnnouncementCommentReplies;
import edu.ubbcluj.canvasAndroid.persistence.CourseProvider;
import edu.ubbcluj.canvasAndroid.persistence.ServiceProvider;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class AnnouncementActivity extends BaseActivity {

	private static int courseID;
	private static int announcementID;

	private static String activityTitle = "Announcement";

	private PlaceholderFragment fragment;
	private static View progressContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();

		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			fragment = new PlaceholderFragment();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.content_frame, fragment).commit();
		}
	}

	public void sendComment(View view) {
		fragment.sendComment();
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

		private AsyncTask<String, Void, String> queryAsyncTask;
		private AsyncTask<String, Void, String> commentAsyncTask;

		private EditText sComment;
		private Button bSendComment;
		private LinearLayout linearLayoutComments;
		private ProgressDialog dialog;

		private ControllerFactory cf = ControllerFactory.getInstance();
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
			courseID = bundle.getInt("course_id");
			announcementID = bundle.getInt("announcement_id");


			SharedPreferences sp = this.getActivity().getSharedPreferences(
					"CanvasAndroid", Context.MODE_PRIVATE);

			rootView = inflater.inflate(R.layout.fragment_anannouncement,
					container, false);
			linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_announcement);

			textViews = new TextView[5];

			textViews[0] = (TextView) rootView
					.findViewById(R.id.anannouncement_title);
			textViews[1] = (TextView) rootView
					.findViewById(R.id.anannouncement_course);
			textViews[2] = (TextView) rootView
					.findViewById(R.id.anannouncement_date);
			textViews[3] = (TextView) rootView
					.findViewById(R.id.anannouncement_author_name);
			textViews[4] = (TextView) rootView
					.findViewById(R.id.anannouncement_message);
			sComment = (EditText) rootView
					.findViewById(R.id.comment);
			bSendComment = (Button) rootView
					.findViewById(R.id.buttonSend);
			linearLayoutComments = (LinearLayout) rootView
					.findViewById(R.id.linear_layout_comments);

			AnnouncementController announcementController = cf.getAnnouncementController();
			announcementController.setSharedPreferences(sp);

			announcementController
			.addInformationListener(new InformationListener() {

				@Override
				public void onComplete(InformationEvent e) {
					AnnouncementController ad = (AnnouncementController) e
							.getSource();

					if (!ad.getData().isEmpty()) {
						setAnnouncement(ad.getData().get(0));
						setProgressGone();
					}

				}
			});

			asyncTask = ((AsyncTask<String, Void, String>) announcementController);
			asyncTask.execute(new String[] { PropertyProvider
					.getProperty("url")
					+ "/api/v1/courses/"
					+ courseID
					+ "/discussion_topics/"
					+ announcementID });

			setProgressVisible(rootView);

			return rootView;
		}


		@SuppressWarnings("unchecked")
		/**
		 * Send comment for the actual announcement.
		 */
		public void sendComment() {

			String comment = sComment.getText().toString();

			if (comment.compareTo("") != 0) {

				showDialog("Sending comment");

				AnnouncementCommentController commentController = cf.getAnnouncementCommentController();

				commentController.setComment(comment);

				commentController.addInformationListener(new InformationListener() {

					@Override
					public void onComplete(InformationEvent e) {
						SharedPreferences sp = PlaceholderFragment.this.getActivity().getSharedPreferences(
								"CanvasAndroid", Context.MODE_PRIVATE);

						AnnouncementController announcementController = cf.getAnnouncementController();
						announcementController.setSharedPreferences(sp);

						announcementController.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AnnouncementController ad = (AnnouncementController) e.getSource();

								if (!ad.getData().isEmpty()) {
									Announcement newAnnouncement = ad.getData().get(0);
									linearLayoutComments.removeAllViews();
									setAnnouncement(newAnnouncement);
									sComment.setText("");
									closeDialog();
								}
							}
						});

						RestInformation.clearData();

						queryAsyncTask = ((AsyncTask<String, Void, String>) announcementController);
						queryAsyncTask.execute(new String[] { PropertyProvider
								.getProperty("url")
								+ "/api/v1/courses/"
								+ courseID + "/discussion_topics/" + announcementID });
					}
				});
				commentAsyncTask = ((AsyncTask<String, Void, String>) commentController);
				commentAsyncTask.execute(new String[] {
						PropertyProvider.getProperty("url") +
						"/courses/" + courseID + "/discussion_topics/" + announcementID +"/entries"
				});
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
					new RestInformation().execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/courses/"
							+ courseID
							+ "/announcements/"
							+ announcementID});
				}
				textViews[0].setText(announcement.getTitle());
				textViews[1].setText(CourseProvider.getInstance().getCourseWithID(announcement.getCourseId()).getName());
				textViews[2].setText(formatDate(announcement.getPostedAt()));
				textViews[3].setText(announcement.getAuthorName());	
				textViews[4].setText(Html.fromHtml(announcement.getMessage()));

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
						if (replies != null){
							for(int j=0;j<replies.length;j++){
								TextView twCommentReplie = new TextView(getActivity());
								twCommentReplie.setTextColor(Color.BLACK);
								twCommentReplie.setText(replies[j].getMessage());
								twCommentReplie.setPadding(30, 0, 0, 0);
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

		}

		// Show dialog
		public void showDialog(String message) {
			if (dialog == null) {
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage(message);
				dialog.show();
			}
		}

		// Close dialog
		public void closeDialog() {
			if (dialog != null)
				dialog.dismiss();
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
