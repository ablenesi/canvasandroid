package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import edu.ubbcluj.canvasAndroid.backend.repository.ActivityStreamSummaryDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementCommentDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.AssignmentsDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.ConversationDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.CoursesDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.FolderDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.MessageSequenceDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.ToDoDAO;

public class RestApiDAOFactory extends DAOFactory {

	@Override
	public RestUserDAO getUserDAO() {
		return new RestUserDAO();
	}

	@Override
	public RestActivityStreamDAO getDashboardDAO() {
		return new RestActivityStreamDAO();
	}

	@Override
	public CoursesDAO getCoursesDAO() {
		return new RestCoursesDAO();
	}

	@Override
	public AssignmentsDAO getAssignmentsDAO() {
		return new RestAssignmentsDAO();
	}

	@Override
	public ToDoDAO getToDoDAO() {
		return new RestToDoDAO();
	}

	@Override
	public AnnouncementDAO getAnnouncementDAO() {
		return new RestAnnouncementDAO();
	}

	@Override
	public ConversationDAO getConversationDAO() {
		return new RestConversationDAO();
	}

	@Override
	public MessageSequenceDAO getMessageSequenceDAO() {
		return new RestMessageSequence();
	}

	@Override
	public FolderDAO getFolderDAO() {
		return new RestFolderDAO();
	}

	@Override
	public ActivityStreamSummaryDAO getActivityStreamSummaryDAO(){
		return new RestActivityStreamSummaryDAO();
	}

	@Override
	public AnnouncementCommentDAO getAnnouncementCommentDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
