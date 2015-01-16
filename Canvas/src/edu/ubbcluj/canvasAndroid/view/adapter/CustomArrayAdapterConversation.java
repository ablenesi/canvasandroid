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
import edu.ubbcluj.canvasAndroid.model.Conversation;
import edu.ubbcluj.canvasAndroid.model.Person;

public class CustomArrayAdapterConversation extends ArrayAdapter<Conversation> {
	private final Context context;
	private final List<Conversation> values;

	public CustomArrayAdapterConversation(Context context,
			List<Conversation> values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.conversation_row_layout, parent, false);
		TextView labelTextView = (TextView) rowView.findViewById(R.id.label);
		TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
		TextView clockTextView = (TextView) rowView.findViewById(R.id.clock);
		TextView lastMessageView = (TextView) rowView.findViewById(R.id.last_message);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

		//participants in the conversation
		String text = "";
		List<Person> participants = values.get(position).getParticipants();

		if (participants.size() > 3) {
			for (int i = 0; i < 5; i++) {
				text += participants.get(i).getName() + " and ";
			}
			text += participants.size() - 3 + "";
		} else {
			for (int i = 0; i < participants.size(); i++) {
				text += values.get(position).getParticipants().get(i).getName();
				if (i < participants.size() - 1)
					text += " and ";
			}
		}

		//set the labelTextView width
		double rowWidth = (double) parent.getWidth();
		int labelWidth = (int) (rowWidth * 65 / 100);
		labelTextView.getLayoutParams().width = labelWidth;
		
		
		labelTextView.setText(text);
		imageView.setImageResource(R.drawable.message);
		dateTextView.setText(values.get(position).getDate());
		clockTextView.setText(values.get(position).getClock());
		lastMessageView.setText(values.get(position).getLastMessage());
		
		
		return rowView;
	}
}
