package edu.ubbcluj.canvasAndroid.backend.util.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;

public class CustomArrayAdapterCourses extends ArrayAdapter<ActiveCourse> {
	private final Context context;
	private List<ActiveCourse> values;

	public CustomArrayAdapterCourses(Context context, List<ActiveCourse> values) {
		super(context, android.R.layout.simple_list_item_1, android.R.id.text1,
				values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1,
				parent, false);
		TextView textView = (TextView) rowView.findViewById(android.R.id.text1);

		textView.setText(values.get(position).getName());

		return rowView;
	}

	
	public void setValues(List<ActiveCourse> values) {
		this.values = values;
	}
}
