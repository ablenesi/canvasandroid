package edu.ubbcluj.canvasAndroid.controller;

import edu.ubbcluj.canvasAndroid.controller.rest.RestApiControllerFactory;

public abstract class ControllerFactory {
	public static ControllerFactory getInstance() {
		return new RestApiControllerFactory();
	}	
	
	public abstract UserController getUserController();
	public abstract ActivityStreamController getDashboardController();
	public abstract ActivityStreamSummaryController getActivityStreamSummaryController();
	public abstract CoursesController getCoursesController();
	public abstract AssignmentsController getAssignmentsController();
	public abstract ToDoController getToDoController();
	public abstract AnnouncementController getAnnouncementController();
	public abstract AnnouncementCommentController getAnnouncementCommentController();
	public abstract ConversationController getConversationController();
	public abstract MessageSequenceController getMessageSequenceController();
	public abstract FolderController getFolderController();
	public abstract SubmissionCommentController getSubmissionCommentController();
	public abstract NewMessageController getNewMessageController();
	
}
