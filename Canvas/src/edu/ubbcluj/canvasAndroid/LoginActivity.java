package edu.ubbcluj.canvasAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.UserDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckSavedSession;

public class LoginActivity extends Activity {

	private ProgressDialog dialog;
	private DAOFactory df;
	private CheckSavedSession savedSession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set the singleton context to make a global context
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(LoginActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

		savedSession = new CheckSavedSession();
		((AsyncTask<LoginActivity, Void, Void>) savedSession)
				.execute(new LoginActivity[] { this });

		if (!CheckNetwork.isNetworkOnline(this)) {
			Toast.makeText(this, "Warning! No network connection",
					Toast.LENGTH_LONG).show();
			;
		}

		df = DAOFactory.getInstance();
		UserDAO userDAO = df.getUserDAO();
		userDAO.setLoginActivity(this);

		userDAO.setSharedPreferences(this.getSharedPreferences(
				"CanvasAndroid-users", Context.MODE_PRIVATE));

		AutoCompleteTextView userNameTextView = (AutoCompleteTextView) findViewById(R.id.username);
		userNameTextView.setAdapter(userDAO.getSavedUsersAdapter());
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
		UserDAO userDAO = df.getUserDAO();

		// Get user info
		EditText userField = (EditText) findViewById(R.id.username);
		EditText passField = (EditText) findViewById(R.id.password);

		// Set user data to login asyncTasc
		userDAO.setLoginActivity(this);
		userDAO.setSharedPreferences(this.getSharedPreferences(
				"CanvasAndroid-users", Context.MODE_PRIVATE));
		userDAO.setUsername(userField.getText().toString());
		userDAO.setPassword(passField.getText().toString());

		// Execute asyncTasc
		((AsyncTask<String, Void, String>) userDAO)
				.execute(new String[] { PropertyProvider.getProperty("url") });
	}

	// Redirect to dashboard
	public void redirect() {
		Intent dashBoardIntent = new Intent(this, DashBoardActivity.class);
		startActivity(dashBoardIntent);
	}

	// Show dialog
	public void showDialog(String message) {
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage(message);
		dialog.show();
	}

	// Close dialog
	public void closeDialog() {
		dialog.dismiss();
	}
}
