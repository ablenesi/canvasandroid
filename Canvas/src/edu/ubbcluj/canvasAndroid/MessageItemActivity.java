package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.repository.MessageSequenceDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterMessage;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;

public class MessageItemActivity extends BaseActivity {

	private static int messageID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the shared preferences with the MessageItem activity sharedpreferences
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(MessageItemActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, new PlaceholderFragment())
					.commit();
		}

		Bundle bundle = getIntent().getExtras();
		messageID = bundle.getInt("id");

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
					null);

			// Set the progressbar visibility
			list = (ListView) rootView.findViewById(R.id.list);
			viewContainer = rootView.findViewById(R.id.linProg);
			viewContainer.setVisibility(View.VISIBLE);

			MessageSequenceDAO messageSequenceDao;
			messageSequenceDao = df.getMessageSequenceDAO();

			messageSequence = new ArrayList<MessageSequence>();

			// dashboardDao.setDba(this);

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

			asyncTask = ((AsyncTask<String, Void, String>) messageSequenceDao);
			asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
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

		public void setMessageSequence(List<MessageSequence> messageSequence) {
			this.messageSequence = messageSequence;
		}
	}
}
