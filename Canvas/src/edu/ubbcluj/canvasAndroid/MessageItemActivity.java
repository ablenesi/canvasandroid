package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.MessageSequenceDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.NewMessageDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterMessage;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;

public class MessageItemActivity extends ActionBarActivity{

	private static int messageID;

	private static EditText messageField;
	
	private PlaceholderFragment placeholderFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		messageID = bundle.getInt("id");
		
		placeholderFragment = new PlaceholderFragment();
		
		setContentView(R.layout.activity_message_item);		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.message_item_frame, placeholderFragment)
					.commit();
		}
	}

	@SuppressWarnings("unchecked")
	public void sendChatMessage(View view) {
		DAOFactory df = DAOFactory.getInstance();
		NewMessageDAO newMessageDAO = df.getNewMessageDAO();

		messageField = (EditText) findViewById(R.id.message_item_text);
		String body = messageField.getText().toString();

		newMessageDAO.setMessageItemActivity(this);
		newMessageDAO.setBody(body);
		newMessageDAO.addInformationListener(new InformationListener() {
			@Override
			public void onComplete(InformationEvent e) {
				NewMessageDAO newMessageDAO = (NewMessageDAO) e
						.getSource();
				placeholderFragment.addMessageSequence(newMessageDAO.getData());

				placeholderFragment.setAdapter();
				placeholderFragment.setList();
			}
		});
		
		if(!CheckNetwork.isNetworkOnline(this)) {
			Toast.makeText(this, "No network connection!",
					Toast.LENGTH_SHORT).show();
		} else {
			// Execute asyncTasc
			((AsyncTask<String, Void, String>) newMessageDAO)
					.execute(new String[] { PropertyProvider.getProperty("url")
							+ "/conversations/" + messageID + "/add_message" });
			
			messageField.setText("");
		}
	}

	public void setToastMessageSending(){
		Toast.makeText(this, "Sending message!",
				Toast.LENGTH_SHORT).show();
	}
	
	public void setToastMessageError(){
		Toast.makeText(this, "Sorry, your message was not sent!",
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView list;
		private View viewContainer;

		private DAOFactory df;
		private List<MessageSequence> messageSequence;

		private AsyncTask<String, Void, String> asyncTask;
		
		private CustomArrayAdapterMessage adapter;

		public PlaceholderFragment() {
			df = DAOFactory.getInstance();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_message_item,
					container, false);

			// Set the progressbar visibility
			list = (ListView) rootView.findViewById(R.id.message_item_list);
			viewContainer = rootView.findViewById(R.id.linProg);
			setProgressVisible();

			MessageSequenceDAO messageSequenceDao;
			messageSequenceDao = df.getMessageSequenceDAO();
			messageSequenceDao
					.setSharedPreferences(this.getActivity()
							.getSharedPreferences("CanvasAndroid",
									Context.MODE_PRIVATE));

			messageSequence = new ArrayList<MessageSequence>();

			messageSequenceDao
					.addInformationListener(new InformationListener() {

						@Override
						public void onComplete(InformationEvent e) {
							MessageSequenceDAO messageSequence = (MessageSequenceDAO) e
									.getSource();
							setProgressGone();
							setMessageSequence(messageSequence.getData());

							adapter = new CustomArrayAdapterMessage(
									getActivity(),
									PlaceholderFragment.this.messageSequence);
							list.setAdapter(adapter);
						}
					});

			asyncTask = ((AsyncTask<String, Void, String>) messageSequenceDao).execute(new String[] { PropertyProvider.getProperty("url")
							+ "/api/v1/conversations/" + messageID });

			return rootView;
		}

		@Override
		public void onStop() {
			if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
				asyncTask.cancel(true);
			}
			super.onStop();
		}

		// Hide progressbar
		public void setProgressGone() {
			viewContainer.setVisibility(View.GONE);
			list.setSelection(list.getCount() - 1);
		}
		
		//show progressbar
		public void setProgressVisible() {
			viewContainer.setVisibility(View.VISIBLE);
		}

		public void setMessageSequence(List<MessageSequence> messageSequence) {
			this.messageSequence = messageSequence;
		}
		
		public void addMessageSequence(MessageSequence ms){
			this.messageSequence.add(ms);
		}
		
		public void setAdapter(){
			this.adapter = new CustomArrayAdapterMessage(
					getActivity(),
					PlaceholderFragment.this.messageSequence);
		}
		
		public void setList(){
			list.setAdapter(adapter);
		}
	}
}
