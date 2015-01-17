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
import edu.ubbcluj.canvasAndroid.controller.AssignmentsController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.SubmissionCommentController;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.model.Submission;
import edu.ubbcluj.canvasAndroid.model.SubmissionAttachment;
import edu.ubbcluj.canvasAndroid.model.SubmissionComment;
import edu.ubbcluj.canvasAndroid.persistence.CourseProvider;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class AssignmentActivity extends BaseActivity {

	private static int courseID;
	private static int assignmentID;
	
	private static String activityTitle = "Assignment";

	private static View progressContainer;
	private PlaceholderFragment fragment;

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
 
	@Override
	public void restoreActionBar() {
		super.restoreActionBar();
		getSupportActionBar().setTitle(activityTitle);
	}

	public void sendComment(View view) {
		fragment.sendComment();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ControllerFactory cf = ControllerFactory.getInstance();
		private Assignment assignment;

		private AsyncTask<String, Void, String> queryAsyncTask;
		private AsyncTask<String, Void, String> commentAsyncTask;
		
		private TextView cName;
		private TextView aName;
		private TextView aDueDate;
		private TextView aPossibleGrade;
		private TextView aDescription;
		private TextView aSubmission;
		private EditText sComment;
		private Button bSendComment;
		private LinearLayout linearLayoutComments;

		private ProgressDialog dialog;
		
		public PlaceholderFragment() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = null;

			Bundle bundle = getArguments();
			courseID = bundle.getInt("course_id");
			assignmentID = bundle.getInt("assignment_id");

			SharedPreferences sp = this.getActivity().getSharedPreferences(
					"CanvasAndroid", Context.MODE_PRIVATE);

			rootView = inflater.inflate(R.layout.fragment_anassignment,
					container, false);

			aName = (TextView) rootView
					.findViewById(R.id.anassignment_name);
			cName = (TextView) rootView
					.findViewById(R.id.anassignment_course);
			aDueDate = (TextView) rootView
					.findViewById(R.id.anassignment_due_date);
			aPossibleGrade = (TextView) rootView
					.findViewById(R.id.anassignment_possible_grade);
			aDescription = (TextView) rootView
					.findViewById(R.id.anassignment_description);
			aSubmission = (TextView) rootView
					.findViewById(R.id.anassignment_submission);
			sComment = (EditText) rootView
					.findViewById(R.id.comment);
			bSendComment = (Button) rootView
					.findViewById(R.id.buttonSend);
			linearLayoutComments = (LinearLayout) rootView
					.findViewById(R.id.linear_layout_comments);

			AssignmentsController assignmentController = cf.getAssignmentsController();
			assignmentController.setSharedPreferences(sp);

			assignmentController.addInformationListener(new InformationListener() {

				@Override
				public void onComplete(InformationEvent e) {
					AssignmentsController ad = (AssignmentsController) e.getSource();

					if (!ad.getData().isEmpty()) {
						setAssignment(ad.getData().get(0));
						setProgressGone();
					}
				}
			});

			queryAsyncTask = ((AsyncTask<String, Void, String>) assignmentController);
			queryAsyncTask.execute(new String[] { PropertyProvider
							.getProperty("url")
							+ "/api/v1/courses/"
							+ courseID + "/assignments/" + assignmentID });



			setProgressVisible(rootView);

			return rootView;
		}

		@SuppressWarnings("unchecked")
		/**
		 * Send comment for the actual assignment.
		 */
		public void sendComment() {
			
			String comment = sComment.getText().toString();
			
			if (comment.compareTo("") != 0) {
				
				showDialog("Sending comment");
				
				SubmissionCommentController commentController = cf.getSubmissionCommentController();
				
				commentController.setComment(comment);
				
				commentController.addInformationListener(new InformationListener() {
					
					@Override
					public void onComplete(InformationEvent e) {
						SharedPreferences sp = PlaceholderFragment.this.getActivity().getSharedPreferences(
								"CanvasAndroid", Context.MODE_PRIVATE);
						
						AssignmentsController assignmentController = cf.getAssignmentsController();
						assignmentController.setSharedPreferences(sp);

						assignmentController.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								AssignmentsController ad = (AssignmentsController) e.getSource();

								if (!ad.getData().isEmpty()) {
									Assignment newAssignment = ad.getData().get(0);
									linearLayoutComments.removeAllViews();
									setAssignment(newAssignment);
									sComment.setText("");
									closeDialog();
								}
							}
						});

						RestInformation.clearData();
						
						queryAsyncTask = ((AsyncTask<String, Void, String>) assignmentController);
						queryAsyncTask.execute(new String[] { PropertyProvider
										.getProperty("url")
										+ "/api/v1/courses/"
										+ courseID + "/assignments/" + assignmentID });
					}
				});
				
				commentAsyncTask = ((AsyncTask<String, Void, String>) commentController);
				commentAsyncTask.execute(new String[] {
					PropertyProvider.getProperty("url") +
					"/courses/" + courseID + "/assignments/" + assignmentID +
					"/submissions/" + assignment.getSubmission().getUserId()
				});
			}
		}
		
		public Assignment getAssignment() {
			return assignment;
		}

		public void setAssignment(Assignment assignment) {
			this.assignment = assignment;

			aName.setText(assignment.getName());
			cName.setText(CourseProvider.getInstance().getCourseWithID(assignment.getCourseId()).getName());
			
			if (assignment.getDueAt() != null) {
				aDueDate.setText(formatDate(assignment.getDueAt()));
			} else {
				aDueDate.setText("No due date");
			}
			
			if (assignment.getIsGraded()) {
				aPossibleGrade.setText("Your grade: "
						+ assignment.getScore() + "\n");
			} else {
				aPossibleGrade.setText("Not graded\n");
			}
			
			aPossibleGrade.append("(Max grade: "
					+ assignment.getPointsPossible() + ")");

			if (assignment.getLockExplanation() != null) {
				aDescription.setText(assignment.getLockExplanation());
			} else {
				if (assignment.getDescription() != null && assignment.getDescription().compareTo("null") != 0) {
					aDescription.setText(Html.fromHtml(assignment
							.getDescription()));
				} else {
					aDescription.setText("No description");
				}
			}
			
			if (assignment.getSubmission() == null || assignment.getSubmission().getAttempt() == 0) {
				aSubmission.setText("Not yet submitted");
				
				TextView tw = new TextView(getActivity());
				tw.setTextColor(Color.BLACK);
				tw.setText("No comments");
				bSendComment.setVisibility(View.GONE);
				sComment.setVisibility(View.GONE);
				linearLayoutComments.addView(tw);
				
			} else {
				String txt = new String("Turned in!\n");
				Submission submission = assignment.getSubmission();
				
				txt += formatDate(submission.getSubmittedAt());
				
				if (submission.getLate()) {
					txt += " (late)";
				}
				
				txt += "\nFiles:\n";
				
				SubmissionAttachment[] attachments = submission.getAttachments();
				
				for (int i = 0; i < attachments.length; i++) {
					txt += attachments[i].getDispalyName() + "\n";
				}
				
				if (submission.getGrade() != null) {
					txt += "\nGrade: " + submission.getGrade();
				}
				
				txt += "\n";
				
				aSubmission.setText(txt);
				
				if (submission.getSubmissionComments() == null || submission.getSubmissionComments().length == 0) {
					TextView tw = new TextView(getActivity());
					tw.setText("No comments");
					tw.setTextColor(Color.BLACK);
					
					linearLayoutComments.addView(tw);
				} else {
					SubmissionComment[] comments = submission.getSubmissionComments();
					
					for (int i = 0; i < comments.length; i++) {
						TextView twComment = new TextView(getActivity());
						twComment.setTextColor(Color.BLACK);
						twComment.setText(comments[i].getComment());
						linearLayoutComments.addView(twComment);
						
						TextView twAuthor = new TextView(getActivity());
						twAuthor.setTextColor(Color.GRAY);
						twAuthor.setGravity(Gravity.END);
						twAuthor.setText(comments[i].getAuthorName() + "\n");
						linearLayoutComments.addView(twAuthor);
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
			if (queryAsyncTask != null && queryAsyncTask.getStatus() == Status.RUNNING) {
				queryAsyncTask.cancel(true);
			}
			
			if (commentAsyncTask != null && commentAsyncTask.getStatus() == Status.RUNNING) {
				commentAsyncTask.cancel(true);
			}
			super.onStop();
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

	}
	
	public static void setProgressVisible(View rootView) {
		progressContainer = rootView.findViewById(R.id.linProg);
		progressContainer.setVisibility(View.VISIBLE);
	}

	public static void setProgressGone() {
		progressContainer.setVisibility(View.GONE);
	}
}
