package edu.ubbcluj.canvasAndroid.view.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;

//CustomArrayAdapter to display icons in list elements
public class CustomArrayAdapterActivityStream extends ArrayAdapter<ActivityStream> {
	private final Context context;
	private final List<ActivityStream> values;

	public CustomArrayAdapterActivityStream(Context context, List<ActivityStream> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.dashboard_row_layout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(values.get(position).getTitle());
		if (!values.get(position).getRead_state())
			textView.setTypeface(null, Typeface.BOLD);
		
		// Change the icon for Conversation, Message, Submission, Announcement etc.
		String s = values.get(position).getType();

		if ((s.equals("Conversation")) || (s.equals("DiscussionTopic"))) {
			imageView.setImageResource(R.drawable.message);
		} else if (s.equals("Message")) {
			imageView.setImageResource(R.drawable.calendar);
		} else if (s.equals("Submission")) {
			imageView.setImageResource(R.drawable.submission);
		} else if (s.equals("Announcement")) {
			imageView.setImageResource(R.drawable.announcement);
		} else {
			imageView.setImageResource(R.drawable.default_icon);
		}

		return rowView;
	}
}
