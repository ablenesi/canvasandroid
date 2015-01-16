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
import edu.ubbcluj.canvasAndroid.model.Assignment;

public class CustomArrayAdapterToDo  extends ArrayAdapter<Assignment> {
	private final Context context;
	private final List<Assignment> values;

	public CustomArrayAdapterToDo(Context context, List<Assignment> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		double rowWidth;
		int labelWidth;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.assignment_row_layout, parent, false);
		
		rowWidth = (double) parent.getWidth();
		labelWidth = (int) (rowWidth * 80 / 100); 
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textViewName = (TextView) rowView.findViewById(R.id.label);
		textViewName.setText(values.get(position).getName());
		textViewName.getLayoutParams().width = labelWidth; 

		imageView.setImageResource(R.drawable.submission_gray);
		
		TextView textViewOutOf = (TextView) rowView.findViewById(R.id.outof);
		textViewOutOf.setText("");
		
			
		return rowView;
	}

}