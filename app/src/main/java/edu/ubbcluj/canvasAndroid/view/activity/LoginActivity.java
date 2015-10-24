package edu.ubbcluj.canvasAndroid.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.CoursesController;
import edu.ubbcluj.canvasAndroid.controller.UserController;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.persistence.CourseProvider;
import edu.ubbcluj.canvasAndroid.persistence.ServiceProvider;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.util.network.CheckSavedSession;

public class LoginActivity extends Activity {

	private ProgressDialog dialog;
	private ControllerFactory cf;
	private CheckSavedSession savedSession;
	private UserController userController;
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

		cf = ControllerFactory.getInstance();
		UserController userController = cf.getUserController();
		userController.setLoginActivity(this);

		userController.setSharedPreferences(this.getSharedPreferences(
				"CanvasAndroid-users", Context.MODE_PRIVATE));
		username =  userController.getLastUsername();
		
		savedSession = new CheckSavedSession();
		((AsyncTask<LoginActivity, Void, Void>) savedSession)
				.execute(new LoginActivity[] { this });

		AutoCompleteTextView userNameTextView = (AutoCompleteTextView) findViewById(R.id.username);
		userNameTextView.setAdapter(userController.getSavedUsersAdapter());
	}
	
	// Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

	/**
	 *  Login after button is pressed.
	 */
	@SuppressWarnings("unchecked")
	public void sendMessage(View vire) {
		if (!CheckNetwork.isNetworkOnline(this)) {
			Toast.makeText(this, "No network connection!",
					Toast.LENGTH_LONG).show();
		} else {
			RestInformation.clearData();
			
			userController = cf.getUserController();
	
			// Set user data to login asyncTasc
			userController.setLoginActivity(this);
			userController.setSharedPreferences(this.getSharedPreferences(
					"CanvasAndroid-users", Context.MODE_PRIVATE));
			userController.setUsername(userField.getText().toString());
			username = userController.getUsername();
			userController.setPassword(passField.getText().toString());
	
			// Execute asyncTasc
			((AsyncTask<String, Void, String>) userController)
					.execute(new String[] { PropertyProvider.getProperty("url") });
		}
	}

	public void loginCompleted() {
		final CoursesController coursesController = cf.getCoursesController();
		coursesController.setSharedPreferences(LoginActivity.this
				.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));

		coursesController.addInformationListener(new InformationListener() {
			
			@Override
			public void onComplete(InformationEvent e) {
				ServiceProvider sp = ServiceProvider.getInstance();
				CourseProvider cp = CourseProvider.getInstance();
				String username = LoginActivity.this.username.replace('@', '.');
				cp.initalize(LoginActivity.this, username);
				sp.initalize(getApplicationContext());
				cp.updateWith(coursesController.getData());
				redirect();
			}
		});
		
		@SuppressWarnings("unchecked")
		AsyncTask<String, Void, String> asyncTask = ((AsyncTask<String, Void, String>) coursesController);
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
