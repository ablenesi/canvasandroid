package edu.ubbcluj.canvasAndroid.controller.rest;

import edu.ubbcluj.canvasAndroid.controller.ActivityStreamSummaryController;
import edu.ubbcluj.canvasAndroid.controller.AnnouncementCommentController;
import edu.ubbcluj.canvasAndroid.controller.AnnouncementController;
import edu.ubbcluj.canvasAndroid.controller.AssignmentsController;
import edu.ubbcluj.canvasAndroid.controller.ConversationController;
import edu.ubbcluj.canvasAndroid.controller.CoursesController;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.FolderController;
import edu.ubbcluj.canvasAndroid.controller.MessageSequenceController;
import edu.ubbcluj.canvasAndroid.controller.NewMessageController;
import edu.ubbcluj.canvasAndroid.controller.SubmissionCommentController;
import edu.ubbcluj.canvasAndroid.controller.ToDoController;

public class RestApiControllerFactory extends ControllerFactory {

	@Override
	public RestUserController getUserController() {
		return new RestUserController();
	}

	@Override
	public RestActivityStreamController getDashboardController() {
		return new RestActivityStreamController();
	}

	@Override
	public CoursesController getCoursesController() {
		return new RestCoursesController();
	}

	@Override
	public AssignmentsController getAssignmentsController() {
		return new RestAssignmentsController();
	}

	@Override
	public ToDoController getToDoController() {
		return new RestToDoController();
	}

	@Override
	public AnnouncementController getAnnouncementController() {
		return new RestAnnouncementController();
	}

	@Override
	public ConversationController getConversationController() {
		return new RestConversationController();
	}

	@Override
	public MessageSequenceController getMessageSequenceController() {
		return new RestMessageSequenceController();
	}

	@Override
	public FolderController getFolderController() {
		return new RestFolderController();
	}

	@Override
	public ActivityStreamSummaryController getActivityStreamSummaryController(){
		return new RestActivityStreamSummaryController();
	}

	@Override
	public AnnouncementCommentController getAnnouncementCommentController() {
		return new RestAnnouncementCommentController();
	}
	
	@Override
	public SubmissionCommentController getSubmissionCommentController() {
		return new RestSubmissionCommentController();
	}

	public NewMessageController getNewMessageController() {
		return new RestNewMessageController();

	}

}
