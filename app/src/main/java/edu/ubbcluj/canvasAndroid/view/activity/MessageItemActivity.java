package edu.ubbcluj.canvasAndroid.view.activity;

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
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.MessageSequenceController;
import edu.ubbcluj.canvasAndroid.controller.NewMessageController;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterMessage;

public class MessageItemActivity extends ActionBarActivity{

	private static int messageID;

	private static EditText messageField;
	
	private AsyncTask<String, Void, String> asyncTask;
	
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
		ControllerFactory cf = ControllerFactory.getInstance();
		NewMessageController newMessageController = cf.getNewMessageController();

		messageField = (EditText) findViewById(R.id.message_item_text);
		String body = messageField.getText().toString();

		newMessageController.setMessageItemActivity(this);
		newMessageController.setBody(body);
		newMessageController.addInformationListener(new InformationListener() {
			@Override
			public void onComplete(InformationEvent e) {
				ControllerFactory cf = ControllerFactory.getInstance();
				MessageSequenceController messageSequenceController;
				messageSequenceController = cf.getMessageSequenceController();
				messageSequenceController.setSharedPreferences(
						MessageItemActivity.this.getSharedPreferences(
								"CanvasAndroid",
								Context.MODE_PRIVATE)
						);
					
				messageSequenceController
						.addInformationListener(new InformationListener() {

							@Override
							public void onComplete(InformationEvent e) {
								MessageSequenceController messageSequence = (MessageSequenceController) e
										.getSource();
								placeholderFragment.setMessageSequence(messageSequence.getData());
								placeholderFragment.setAdapter();
								placeholderFragment.setList();
							}
						});

				if (!CheckNetwork.isNetworkOnline(MessageItemActivity.this)) {
					
					Toast.makeText(MessageItemActivity.this, "No network connection!",
							Toast.LENGTH_SHORT).show();
					
				} else {
					
					asyncTask = ((AsyncTask<String, Void, String>) messageSequenceController).
								execute(new String[] { 
										PropertyProvider.getProperty("url")
										+ "/api/v1/conversations/" 
										+ messageID 
								});
				}
			}
		});
		
		if(!CheckNetwork.isNetworkOnline(this)) {
			
			Toast.makeText(this, "No network connection!",
					Toast.LENGTH_SHORT).show();
			
		} else {
			
			// Execute asyncTasc
			asyncTask = ((AsyncTask<String, Void, String>) newMessageController)
					.execute(new String[] { PropertyProvider.getProperty("url")
							+ "/conversations/" + messageID + "/add_message" });
			
			messageField.setText("");
			
			RestInformation.clearData();
		}
	}

	@Override
	protected void onStop() {
		if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
			asyncTask.cancel(true);
		}
		super.onStop();
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

		private ControllerFactory cf;
		private List<MessageSequence> messageSequence;

		private AsyncTask<String, Void, String> asyncTask;
		
		private CustomArrayAdapterMessage adapter;

		public PlaceholderFragment() {
			cf = ControllerFactory.getInstance();
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

			MessageSequenceController messageSequenceController;
			messageSequenceController = cf.getMessageSequenceController();
			messageSequenceController
					.setSharedPreferences(this.getActivity()
							.getSharedPreferences("CanvasAndroid",
									Context.MODE_PRIVATE));

			messageSequence = new ArrayList<MessageSequence>();

			messageSequenceController
					.addInformationListener(new InformationListener() {

						@Override
						public void onComplete(InformationEvent e) {
							MessageSequenceController messageSequence = (MessageSequenceController) e
									.getSource();
							setProgressGone();
							setMessageSequence(messageSequence.getData());

							adapter = new CustomArrayAdapterMessage(
									getActivity(),
									PlaceholderFragment.this.messageSequence);
							list.setAdapter(adapter);
						}
					});

			asyncTask = ((AsyncTask<String, Void, String>) messageSequenceController).
							execute(new String[] { 
									PropertyProvider.getProperty("url")
									+ "/api/v1/conversations/" 
									+ messageID 
							});

			
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
