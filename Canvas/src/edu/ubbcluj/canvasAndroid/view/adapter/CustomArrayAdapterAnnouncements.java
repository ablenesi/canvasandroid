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
import edu.ubbcluj.canvasAndroid.model.Announcement;

//CustomArrayAdapter to display icons in list elements
public class CustomArrayAdapterAnnouncements extends ArrayAdapter<Announcement> {
	private final Context context;
	private final List<Announcement> values;

	public CustomArrayAdapterAnnouncements(Context context,
			List<Announcement> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.announcement_row_layout,
				parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(values.get(position).getTitle());
		if (!values.get(position).getRead_state())
			textView.setTypeface(null, Typeface.BOLD);
		imageView.setImageResource(R.drawable.announcement);

		return rowView;
	}
}
