package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.FileTreeElement;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface FolderController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<FileTreeElement> getData();

	void setSharedPreferences(SharedPreferences sp);

	void clearData();

}
