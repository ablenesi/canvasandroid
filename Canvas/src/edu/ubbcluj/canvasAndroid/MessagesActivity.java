package edu.ubbcluj.canvasAndroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ubbcluj.canvasAndroid.backend.repository.ConversationDAO;
import edu.ubbcluj.canvasAndroid.backend.repository.DAOFactory;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.adapters.CustomArrayAdapterConversation;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Conversation;

public class MessagesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the shared preferences with the Base activity sharedpreferences
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		sPreferences.init(MessagesActivity.this.getSharedPreferences(
				"CanvasAndroid", Context.MODE_PRIVATE));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, new PlaceholderFragment())
					.commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView list;
		private View viewContainer;

		private DAOFactory df;
		private List<Conversation> conversation;

		private AsyncTask<String, Void, String> asyncTask;
		
		private CustomArrayAdapterConversation adapter;

		public PlaceholderFragment() {
			df = DAOFactory.getInstance();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_messages, null);

			// Set the progressbar visibility
			list = (ListView) rootView.findViewById(R.id.list);
			viewContainer = rootView.findViewById(R.id.linProg);
			viewContainer.setVisibility(View.VISIBLE);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent messageItemIntent = new Intent(view.getContext(),
							MessageItemActivity.class);

					Bundle bundle = new Bundle();
					// conversationID

					bundle.putInt("id", conversation.get(position).getId());
					messageItemIntent.putExtras(bundle); // Put the id to the
															// Course Intent
					startActivity(messageItemIntent);
				}
			});

			ConversationDAO conversationDao;
			conversationDao = df.getConversationDAO();

			conversation = new ArrayList<Conversation>();

			// dashboardDao.setDba(this);

			conversationDao.addInformationListener(new InformationListener() {

				@Override
				public void onComplete(InformationEvent e) {
					ConversationDAO conversation = (ConversationDAO) e
							.getSource();
					setProgressGone();
					setConversation(conversation.getData());

					adapter = new CustomArrayAdapterConversation(getActivity(),
							PlaceholderFragment.this.conversation);
					list.setAdapter(adapter);
				}
			});

			asyncTask = ((AsyncTask<String, Void, String>) conversationDao);
			asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
							+ "/api/v1/conversations" });

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
		}

		public void setConversation(List<Conversation> conversation) {
			this.conversation = conversation;
		}
	}

}
