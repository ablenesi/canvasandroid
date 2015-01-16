package edu.ubbcluj.canvasAndroid.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;
import edu.ubbcluj.canvasAndroid.persistence.CourseProvider;

public class CustomArrayAdapterCourseSelection extends ArrayAdapter<ActiveCourse> {
	private final Context context;
	private List<ActiveCourse> courses;
	private CourseProvider provider;
	
	public CustomArrayAdapterCourseSelection(Context context) {
		super(context, android.R.layout.activity_list_item, CourseProvider.getInstance().getAllCourses());
		this.context = context;
		provider = CourseProvider.getInstance();
		courses = provider.getAllCourses();
	}
	
	public void itemSelected(int position) {
		ActiveCourse course = courses.get(position);
		provider.setSelected(course, !course.isSelected());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ActiveCourse course = courses.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.course_selection_row_layout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.coursename);
		ImageView image = (ImageView) rowView.findViewById(R.id.courseicon);
		
		textView.setText(course.getName());
		
		if (course.isSelected()) {
			image.setImageResource(R.drawable.starred);
		} else {
			image.setImageResource(R.drawable.notstarred);
		}
		
		return rowView;
	}
	
	
}
