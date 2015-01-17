package edu.ubbcluj.canvasAndroid.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.R;
import edu.ubbcluj.canvasAndroid.controller.ControllerFactory;
import edu.ubbcluj.canvasAndroid.controller.ConversationController;
import edu.ubbcluj.canvasAndroid.controller.rest.RestInformation;
import edu.ubbcluj.canvasAndroid.model.Conversation;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.view.adapter.CustomArrayAdapterConversation;

public class MessagesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_messages);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.messages_content_frame, new PlaceholderFragment())
					.commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView list;
		private View viewContainer;

		private ControllerFactory cf;
		private List<Conversation> conversation;

		private AsyncTask<String, Void, String> asyncTask;
		private AsyncTask<String, Void, String> asyncTaskForRefresh;
		
		private CustomArrayAdapterConversation adapter;

		private SwipeRefreshLayout swipeView;
		
		public PlaceholderFragment() {
			cf = ControllerFactory.getInstance();
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

					if (asyncTaskForRefresh != null)
						asyncTaskForRefresh.cancel(true);
					
					if (swipeView != null)
						swipeView.setRefreshing(false);
					
					if(!CookieHandler.checkData(getActivity().getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE), 
							PropertyProvider.getProperty("url")
							+ "/api/v1/conversations/" + conversation.get(position).getId()) && !CheckNetwork.isNetworkOnline(getActivity())) {
						Toast.makeText(getActivity(), "No network connection!",
								Toast.LENGTH_LONG).show();
					} else {
						Intent messageItemIntent = new Intent(view.getContext(),
								MessageItemActivity.class);
	
						Bundle bundle = new Bundle();
						// conversationID
	
						bundle.putInt("id", conversation.get(position).getId());
						messageItemIntent.putExtras(bundle); // Put the id to the
																// Course Intent
						startActivity(messageItemIntent);
					}
				}
			});

			ConversationController conversationController;
			conversationController = cf.getConversationController();
			conversationController
					.setSharedPreferences(this.getActivity()
							.getSharedPreferences("CanvasAndroid",
									Context.MODE_PRIVATE));

			conversation = new ArrayList<Conversation>();

			conversationController.addInformationListener(new InformationListener() {

				@Override
				public void onComplete(InformationEvent e) {
					ConversationController conversation = (ConversationController) e
							.getSource();
					setProgressGone();
					setConversation(conversation.getData());

					adapter = new CustomArrayAdapterConversation(getActivity(),
							PlaceholderFragment.this.conversation);
					list.setAdapter(adapter);
				}
			});

			swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);

	        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
	        	
	        	@Override
	        	public void onRefresh() {
	        		if(!CheckNetwork.isNetworkOnline(getActivity())) {
	        			swipeView.setRefreshing(false);
						Toast.makeText(getActivity(), "No network connection!",
								Toast.LENGTH_LONG).show();
	        		} else {
	        			ConversationController conversationController;
	        			conversationController = cf.getConversationController();
	        			conversationController
	        					.setSharedPreferences(getActivity()
	        							.getSharedPreferences("CanvasAndroid",
	        									Context.MODE_PRIVATE));
	        			RestInformation.clearData();
	        		
		        		conversationController.addInformationListener(new InformationListener() {
		        				
		        			@Override
		        			public void onComplete(InformationEvent e) {
		        				ConversationController conversation = (ConversationController) e
		    							.getSource();
		    					setProgressGone();
		    					setConversation(conversation.getData());

		    					adapter = new CustomArrayAdapterConversation(getActivity(),
		    							PlaceholderFragment.this.conversation);
		    					list.setAdapter(adapter);
		    					
		        				swipeView.setRefreshing(false);
		        			}
		        			
		        		});
		        		
		        		asyncTaskForRefresh = ((AsyncTask<String, Void, String>) conversationController);
		        		asyncTaskForRefresh.execute(new String[] { PropertyProvider.getProperty("url")
		        						+ "/api/v1/conversations/" });
	        		}
	        	} 	
	    });
			
			asyncTask = ((AsyncTask<String, Void, String>) conversationController);
			asyncTask.execute(new String[] { PropertyProvider.getProperty("url")
							+ "/api/v1/conversations" });

			return rootView;
		}

		@Override
		public void onStop() {
			if ( asyncTask != null && asyncTask.getStatus() == Status.RUNNING) {
				asyncTask.cancel(true);
			}
			
			if ( asyncTaskForRefresh != null && asyncTaskForRefresh.getStatus() == Status.RUNNING) {
				asyncTaskForRefresh.cancel(true);
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
