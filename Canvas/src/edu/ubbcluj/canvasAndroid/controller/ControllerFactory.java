package edu.ubbcluj.canvasAndroid.backend.repository;

import edu.ubbcluj.canvasAndroid.backend.repository.restApi.RestApiDAOFactory;

public abstract class DAOFactory {
	public static DAOFactory getInstance() {
		return new RestApiDAOFactory();
	}	
	
	public abstract UserDAO getUserDAO();
	public abstract ActivityStreamDAO getDashboardDAO();
	public abstract ActivityStreamSummaryDAO getActivityStreamSummaryDAO();
	public abstract CoursesDAO getCoursesDAO();
	public abstract AssignmentsDAO getAssignmentsDAO();
	public abstract ToDoDAO getToDoDAO();
	public abstract AnnouncementDAO getAnnouncementDAO();
	public abstract AnnouncementCommentDAO getAnnouncementCommentDAO();
	public abstract ConversationDAO getConversationDAO();
	public abstract MessageSequenceDAO getMessageSequenceDAO();
	public abstract FolderDAO getFolderDAO();
	public abstract SubmissionCommentDAO getSubmissionCommentDAO();
	public abstract NewMessageDAO getNewMessageDAO();
	
}