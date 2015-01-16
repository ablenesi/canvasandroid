package edu.ubbcluj.canvasAndroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonCookie;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterCourseSelection;

public class CourseSelectionActivity extends ActionBarActivity {

	private CustomArrayAdapterCourseSelection adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_selection);
		
		adapter = new CustomArrayAdapterCourseSelection(this);
		
		ListView list = (ListView) findViewById(R.id.courselist);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.itemSelected(position);
				adapter.notifyDataSetChanged();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		Intent intent = null;
		
		switch (id) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.messages:
			if(!CookieHandler.checkData(this.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
					PropertyProvider.getProperty("url")
					+ "/api/v1/conversations") && !CheckNetwork.isNetworkOnline(this)) {
				Toast.makeText(this, "No network connection!",
						Toast.LENGTH_LONG).show();
			} else {
				intent = new Intent(this, MessagesActivity.class);
				startActivity(intent);
				return true;
			}
		case R.id.settings:
			return true;
		case R.id.logout:
			SingletonCookie.getInstance().deleteCookieStore();
			intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
