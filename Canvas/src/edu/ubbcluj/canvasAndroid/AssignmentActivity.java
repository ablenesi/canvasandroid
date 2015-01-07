package edu.ubbcluj.canvasAndroid;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.backend.repository.AssignmentsDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.model.Submission;
import edu.ubbcluj.canvasAndroid.model.SubmissionAttachment;
import edu.ubbcluj.canvasAndroid.model.SubmissionComment;

public class AssignmentActivity extends BaseActivity {

	private static int courseID;
	private static int assignmentID;
	
	private static String activityTitle = "Assignment";

	private static View progressContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		super.onCreate(savedInstanceState);

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

		private AsyncTask<String, Void, String> asyncTask;
		
		private TextView aName;
		private TextView aDueDate;
		private TextView aPossibleGrade;
		private TextView aDescription;
		private TextView aSubmission;
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
			assignmentID = bundle.getInt("assignment_id");

			SharedPreferences sp = this.getActivity().getSharedPreferences(
					"CanvasAndroid", Context.MODE_PRIVATE);

			rootView = inflater.inflate(R.layout.fragment_anassignment,
					container, false);

			aName = (TextView) rootView
					.findViewById(R.id.anassignment_name);
			aDueDate = (TextView) rootView
					.findViewById(R.id.anassignment_due_date);
			aPossibleGrade = (TextView) rootView
					.findViewById(R.id.anassignment_possible_grade);
			aDescription = (TextView) rootView
					.findViewById(R.id.anassignment_description);
			aSubmission = (TextView) rootView
					.findViewById(R.id.anassignment_submission);
			linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_assignment);

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



			setProgressVisible(rootView);

			return rootView;
		}

		public Assignment getAssignment() {
			return assignment;
		}

		public void setAssignment(Assignment assignment) {
			this.assignment = assignment;

			aName.setText(assignment.getName());
			
			if (assignment.getDueAt() != null) {
				aDueDate.setText(formatDate(assignment.getDueAt()));
			} else {
				aDueDate.setText("No due date");
			}
			
			aPossibleGrade.setText("Maximum grade: "
					+ assignment.getPointsPossible());

			if (assignment.getIsGraded()) {
				aPossibleGrade.append(" (Your grade: "
						+ assignment.getScore() + ")");
			}

			if (assignment.getLockExplanation() != null) {
				aDescription.setText(assignment.getLockExplanation());
			} else {
				if (assignment.getDescription() != null) {
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
				linearLayout.addView(tw);
				
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
					linearLayout.addView(tw);
				} else {
					SubmissionComment[] comments = submission.getSubmissionComments();
					
					for (int i = 0; i < comments.length; i++) {
						TextView twComment = new TextView(getActivity());
						twComment.setTextColor(Color.BLACK);
						twComment.setText(comments[i].getComment());
						linearLayout.addView(twComment);
						
						TextView twAuthor = new TextView(getActivity());
						twAuthor.setTextColor(Color.GRAY);
						twAuthor.setGravity(Gravity.END);
						twAuthor.setText(comments[i].getAuthorName() + "\n");
						linearLayout.addView(twAuthor);
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