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

public class CustomArrayAdapterAssignments  extends ArrayAdapter<Assignment> {
	private final Context context;
	private final List<Assignment> values;

	public CustomArrayAdapterAssignments(Context context, List<Assignment> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		double pointsPossible;
		double rowWidth;
		int labelWidth;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.assignment_row_layout, parent, false);
		
		rowWidth = (double) parent.getWidth();
		labelWidth = (int) (rowWidth * 65 / 100);
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textViewName = (TextView) rowView.findViewById(R.id.label);
		textViewName.setText(values.get(position).getName());
		textViewName.getLayoutParams().width = labelWidth; 

		TextView textViewOutOf = (TextView) rowView.findViewById(R.id.outof);
		
		try {
			pointsPossible = values.get(position).getPointsPossible();
		} catch (Exception e) {
			pointsPossible = 0.0d;
		}
		
		if (values.get(position).getIsGraded()) {
			textViewOutOf.setText(values.get(position).getScore() + " of " + pointsPossible);
			imageView.setImageResource(R.drawable.submission);
		} else {
			imageView.setImageResource(R.drawable.submission_gray);
			textViewOutOf.setText(" of " + pointsPossible);
		}
			
		return rowView;
	}

}