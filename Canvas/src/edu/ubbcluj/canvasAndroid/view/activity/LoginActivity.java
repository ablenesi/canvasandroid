package edu.ubbcluj.canvasAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.backend.repository.CoursesDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.UserDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.restApi.RestInformationDAO;
import edu.ubbcluj.canvasAndroid.backend.util.CourseProvider;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.ServiceProvider;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckSavedSession;

public class LoginActivity extends Activity {

	private ProgressDialog dialog;
	private DAOFactory df;
	private CheckSavedSession savedSession;
	private UserDAO userDAO;
	private String username;
	
	private TextView userTw;
	private TextView passTw;
	private EditText userField;
	private EditText passField;
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		dialog = null;
		
		userTw = (TextView) findViewById(R.id.textView2);
		passTw = (TextView) findViewById(R.id.textView3);
		userField = (EditText) findViewById(R.id.username);
		passField = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login_button);
		
		setVisibility(View.INVISIBLE);
		
		// Set the singleton context to make a global context
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(LoginActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

		df = DAOFactory.getInstance();
		UserDAO userDAO = df.getUserDAO();
		userDAO.setLoginActivity(this);

		userDAO.setSharedPreferences(this.getSharedPreferences(
				"CanvasAndroid-users", Context.MODE_PRIVATE));
		username =  userDAO.getLastUsername();
		
		savedSession = new CheckSavedSession();
		((AsyncTask<LoginActivity, Void, Void>) savedSession)
				.execute(new LoginActivity[] { this });

		AutoCompleteTextView userNameTextView = (AutoCompleteTextView) findViewById(R.id.username);
		userNameTextView.setAdapter(userDAO.getSavedUsersAdapter());
	}
	
	// Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// Login after button is pressed
	@SuppressWarnings("unchecked")
	public void sendMessage(View vire) {
		if (!CheckNetwork.isNetworkOnline(this)) {
			Toast.makeText(this, "No network connection!",
					Toast.LENGTH_LONG).show();
		} else {
			RestInformationDAO.clearData();
			
			userDAO = df.getUserDAO();
	
			// Set user data to login asyncTasc
			userDAO.setLoginActivity(this);
			userDAO.setSharedPreferences(this.getSharedPreferences(
					"CanvasAndroid-users", Context.MODE_PRIVATE));
			userDAO.setUsername(userField.getText().toString());
			username = userDAO.getUsername();
			userDAO.setPassword(passField.getText().toString());
	
			// Execute asyncTasc
			((AsyncTask<String, Void, String>) userDAO)
					.execute(new String[] { PropertyProvider.getProperty("url") });
		}
	}

	public void loginCompleted() {
		//showDialog("Connecting... Please wait!");
		final CoursesDAO coursesDao = df.getCoursesDAO();
		coursesDao.setSharedPreferences(LoginActivity.this
				.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));

		coursesDao.addInformationListener(new InformationListener() {
			
			@Override
			public void onComplete(InformationEvent e) {
				ServiceProvider sp = ServiceProvider.getInstance();
				CourseProvider cp = CourseProvider.getInstance();
				String username = LoginActivity.this.username.replace('@', '.');
				cp.initalize(LoginActivity.this, username);
				sp.initalize(getApplicationContext());
				cp.updateWith(coursesDao.getData());
				redirect();
			}
		});
		
		@SuppressWarnings("unchecked")
		AsyncTask<String, Void, String> asyncTask = ((AsyncTask<String, Void, String>) coursesDao);
		asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
				+ "/api/v1/courses?per_page=25" });
	}
	
	// Redirect to dashboard
	public void redirect() {
		closeDialog();
		Intent dashBoardIntent = new Intent(this, DashBoardActivity.class);
		startActivity(dashBoardIntent);
	}

	// Show dialog
	public void showDialog(String message) {
		if (dialog == null) {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage(message);
			dialog.show();
		} else {
			dialog.show();
		}
	}

	// Close dialog
	public void closeDialog() {
		if (dialog != null)
			dialog.dismiss();
	}
	
	public void setVisibility(int visibility) {
		userTw.setVisibility(visibility);
		passTw.setVisibility(visibility);
		userField.setVisibility(visibility);
		passField.setVisibility(visibility);
		loginButton.setVisibility(visibility);
	}
}