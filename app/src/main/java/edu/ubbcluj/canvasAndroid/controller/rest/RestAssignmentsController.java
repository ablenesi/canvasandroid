package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.AssignmentsController;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.model.Submission;
import edu.ubbcluj.canvasAndroid.model.SubmissionAttachment;
import edu.ubbcluj.canvasAndroid.model.SubmissionComment;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestAssignmentsController extends AsyncTask<String, Void, String>
		implements AssignmentsController {

	private List<Assignment> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestAssignmentsController() {
		super();
		actionList = new ArrayList<InformationListener>();
	}

	@Override
	public synchronized void addInformationListener(InformationListener il) {
		actionList.add(il);
	}

	@Override
	public synchronized void removeInformationListener(InformationListener il) {
		actionList.remove(il);
	}

	/**
	 * Notifies listeners that the data has been retrieved from the server, and it's conversion is finished.
	 */
	public synchronized void notifyListeners() {
		for (InformationListener il : actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	public List<Assignment> getData() {
		return data;
	}

	@Override
	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	}

	@Override
	public void clearData() {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sp);
		
		persistentCookieStore.clear();		
	}
	
	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {
		String response = "";

		for (String url : urls) {
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}

		data = new ArrayList<Assignment>();

		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		if (jsonSource.startsWith("[")) {
			try {
				JSONArray jArr = new JSONArray(jsonSource);

				for (int i = 0; i < jArr.length(); i++) {
					if ( isCancelled() ) break;
					JSONObject jObj = jArr.getJSONObject(i);
					data.add(convertJSONtoAssignment(jObj));
				}

			} catch (JSONException e) {
				Log.e("Json Assignments", e.getMessage(), new Throwable());
			}
		} else {
			try {
				JSONObject jObj = new JSONObject(jsonSource);
				data.add(convertJSONtoAssignment(jObj));
			} catch (JSONException e) {
				Log.e("Json Assignment", e.getMessage(), new Throwable());
			}
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "Assignment asynctask cancelled");
		}
		
		return response;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Assignment} object.
	 */
	private Assignment convertJSONtoAssignment(JSONObject jObj) {
		Assignment assignment = new Assignment();
		int courseId;
		int assignmentId;

		try {
			courseId = jObj.getInt("course_id");
			assignmentId = jObj.getInt("id");

			assignment.setCourseId(courseId);
			assignment.setId(assignmentId);
			assignment.setName(jObj.getString("name"));
			if (!jObj.isNull("points_possible")) {
				assignment.setPointsPossible(jObj.getDouble("points_possible"));
			} else {
				assignment.setPointsPossible(0);
			}
			assignment.setDescription(jObj.getString("description"));

			if (jObj.isNull("due_at")) {
				assignment.setDueAt("No due date");
			} else {
				assignment.setDueAt(jObj.getString("due_at"));
			}

			if (jObj.isNull("lock_explanation")) {
				assignment.setLockExplanation(null);
			} else {
				assignment.setLockExplanation(jObj
						.getString("lock_explanation"));
			}

			String url = PropertyProvider.getProperty("url")
					+ "/api/v1/courses/" + courseId + "/assignments/"
					+ assignmentId + "/submissions/self?include=submission_comments";
			String response = RestInformation.getData(url).replace(
					"while(1);", "");

			JSONObject submissionObj = new JSONObject(response);

			if (submissionObj.getString("score").equals("null"))
				assignment.setIsGraded(false);
			else {
				assignment.setIsGraded(true);
				assignment.setScore(submissionObj.getDouble("score"));
			}
			
			assignment.setSubmission(convertJSONtoSubmission(submissionObj));

		} catch (JSONException e) {
			Log.e("JSON Courses", e.getMessage());
		}

		return assignment;
	}
	
	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Submission} object.
	 */
	private Submission convertJSONtoSubmission(JSONObject jObj) {
		Submission submission = new Submission();
		
		try {
			submission.setId(jObj.getInt("id"));
			
			submission.setAssignmentId(jObj.getInt("assignment_id"));
			if (!jObj.isNull("attempt")) {
				submission.setAttempt(jObj.getInt("attempt"));
			}
			submission.setBody(jObj.getString("body"));
			submission.setGrade(jObj.getString("grade"));
			if (!jObj.isNull("grade_matches_current_submission")) {
				submission.setGradeMatchesCurrentSubmission(jObj.getBoolean("grade_matches_current_submission"));
			}
			if (!jObj.isNull("grader_id")) {
				submission.setGraderId(jObj.getInt("grader_id"));
			}
			if (!jObj.isNull("score")) {
				submission.setScore(jObj.getDouble("score"));
			}
			submission.setSubmissionType(jObj.getString("submission_type"));
			if (!jObj.isNull("submitted_at")) {
				submission.setSubmittedAt(jObj.getString("submitted_at"));
			}
			submission.setUrl(jObj.getString("url"));
			if (!jObj.isNull("user_id")) {
				submission.setUserId(jObj.getInt("user_id"));
			}
			submission.setWorkflowState(jObj.getString("workflow_state"));
			if (!jObj.isNull("late")) {
				submission.setLate(jObj.getBoolean("late"));
			}
			submission.setPreviewUrl("preview_url");
			
			if (!jObj.isNull("attachments")) {
				JSONArray attachmentsJSON = jObj.getJSONArray("attachments");
				SubmissionAttachment[] attachments = new SubmissionAttachment[attachmentsJSON.length()];
				
				for (int i = 0; i < attachmentsJSON.length(); i++) {
					JSONObject obj = attachmentsJSON.getJSONObject(i);
					attachments[i] = convertJSONtoAttachment(obj);
				}
				
				submission.setAttachments(attachments);
			}
			
			if (!jObj.isNull("submission_comments")) {
				JSONArray commentsJSON = jObj.getJSONArray("submission_comments");
				SubmissionComment[] comments = new SubmissionComment[commentsJSON.length()];
				
				for (int i = 0; i < commentsJSON.length(); i++) {
					JSONObject obj = commentsJSON.getJSONObject(i);
					comments[i] = convertJSONtoComment(obj);
				}
				
				submission.setSubmissionComments(comments);
			}
			
		} catch (JSONException e) {
			Log.e("Json Submission", e.getMessage(), new Throwable());
		}
		
		
		return submission;
	}
	
	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Attachment} object.
	 */
	private SubmissionAttachment convertJSONtoAttachment(JSONObject jObj) {
		SubmissionAttachment attachment = new SubmissionAttachment();
		
		try {
			attachment.setId(jObj.getInt("id"));
			attachment.setContentType("content_type");
			attachment.setDispalyName(jObj.getString("display_name"));
			attachment.setFileName(jObj.getString("filename"));
			attachment.setUrl(jObj.getString("url"));
			if (!jObj.isNull("size")) {
				attachment.setSize(jObj.getInt("size"));
			}
			attachment.setCreatedAt(jObj.getString("created_at"));
			attachment.setUpdatedAt(jObj.getString("updated_at"));
			attachment.setUnlockAt(jObj.getString("unlock_at"));
			attachment.setLocked(jObj.getBoolean("locked"));
			attachment.setHidden(jObj.getBoolean("hidden"));
			attachment.setHiddenForUser(jObj.getBoolean("hidden_for_user"));
			attachment.setThumbnailUrl(jObj.getString("thumbnail_url"));
			attachment.setLockedForUser(jObj.getBoolean("locked_for_user"));
			attachment.setPreviewUrl(jObj.getString("preview_url"));
		} catch (JSONException e) {
			Log.e("Json Attachment", e.getMessage(), new Throwable());
		}
		
		return attachment;
	}
	
	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Comment} object.
	 */
	private SubmissionComment convertJSONtoComment(JSONObject jObj) {
		SubmissionComment comment = new SubmissionComment();
		
		try {
			comment.setAuthorId(jObj.getInt("author_id"));
			comment.setAuthorName(jObj.getString("author_name"));
			comment.setComment(jObj.getString("comment"));
			comment.setCreatedAt(jObj.getString("created_at"));
			comment.setId(jObj.getInt("id"));
		} catch (JSONException e) {
			Log.e("Json SubmissionComment", e.getMessage(), new Throwable());
		}
		
		return comment;
	}
}
