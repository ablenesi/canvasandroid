package edu.ubbcluj.canvasAndroid.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;

public class CustomArrayAdapterMessage extends ArrayAdapter<MessageSequence> {
	private final Context context;
	private final List<MessageSequence> values;

	public CustomArrayAdapterMessage(Context context,
			List<MessageSequence> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.message_row_layout,
				parent, false);
		TextView authorTextView = (TextView) rowView.findViewById(R.id.author);
		TextView bodyTextView = (TextView) rowView.findViewById(R.id.body);
		TextView dateTextView = (TextView) rowView.findViewById(R.id.date);		
		
		// author
		authorTextView.setText(values.get(position).getName()+":");
		
		// set the authorTextView width
		double rowWidth = (double) parent.getWidth();
		int labelWidth = (int) (rowWidth * 65 / 100);
		authorTextView.getLayoutParams().width = labelWidth;

		// message body
		bodyTextView.setText(values.get(position).getBody());

		// message created date
		dateTextView.setText(values.get(position).getCreatedAt());
		return rowView;
	}
}
