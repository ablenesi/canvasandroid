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
import edu.ubbcluj.canvasAndroid.model.FileTreeElement;
import edu.ubbcluj.canvasAndroid.model.Folder;

public class CustomArrayAdapterFileTreeElements extends
		ArrayAdapter<FileTreeElement> {

	private final Context context;
	private final List<FileTreeElement> values;

	public CustomArrayAdapterFileTreeElements(Context context,
			List<FileTreeElement> values) {
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

		if (position == 0) {
			textView.setText("");
			imageView.setImageResource(R.drawable.up);
		} else if (values.get(position) instanceof Folder) {
			textView.setText(values.get(position).getName());
			imageView.setImageResource(R.drawable.folder);
		} else {
			textView.setText(values.get(position).getName());
			imageView.setImageResource(R.drawable.file);
		}

		return rowView;
	}

}
