package edu.ubbcluj.canvasAndroid.backend.repository;

import edu.ubbcluj.canvasAndroid.LoginActivity;

public interface UserDAO {
	public String loginUser(String host);
	public void setUsername(String username);
	public String getUsername();
	public void setPassword(String password);
	public String getPassword();
	public void setLoginActivity(LoginActivity loginActivity);
	public LoginActivity getLoginActivity();
}
