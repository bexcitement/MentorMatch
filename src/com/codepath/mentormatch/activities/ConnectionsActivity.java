package com.codepath.mentormatch.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.mentormatch.R;
import com.codepath.mentormatch.adapters.ConnectionsAdapter;
import com.codepath.mentormatch.helpers.ParseQueries;
import com.codepath.mentormatch.models.parse.MatchRelationship;
import com.codepath.mentormatch.models.parse.MentorRequest;
import com.parse.FindCallback;
import com.parse.ParseException;

public class ConnectionsActivity extends Activity {
	public static final int RATING_REQUEST_CODE = 100;
	
	ListView lvConnections;
	ArrayList<MentorRequest> requestList;
	ConnectionsAdapter requestAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connections);
		lvConnections = (ListView) findViewById(R.id.lvConnections);
		requestList = new ArrayList<MentorRequest>();
		requestAdapter = new ConnectionsAdapter(this, requestList);
		lvConnections.setAdapter(requestAdapter);
		setupListeners();
		retrieveConnections();
	}
	
	private void setupListeners() {
		lvConnections.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
    			MentorRequest requestId = requestAdapter.getItem(pos);
				Intent i = new Intent(getBaseContext(), RatingsActivity.class);
				i.putExtra(RatingsActivity.RELATIONSHIP_ID_EXTRA, requestId.getObjectId());
				startActivityForResult(i, RATING_REQUEST_CODE);
    		}
    	});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == RATING_REQUEST_CODE) {
		  Toast.makeText(this, "Rating has been saved.", Toast.LENGTH_SHORT).show();
	  }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void retrieveConnections() {
		ParseQueries.retrieveConnections(new FindCallbackClass());

	}
	
	private class FindCallbackClass extends FindCallback<MatchRelationship> {

		@Override
		public void done(List<MatchRelationship> relationsList, ParseException e) {
			// TODO Auto-generated method stub
	        if (e == null) {
	            Log.d("DEBUG", "Retrieved " + relationsList.size() + " REQUESTS");
	            for(MatchRelationship obj : relationsList) {
	            	requestAdapter.add(obj.getMentorRequestId());
	            }
	            requestAdapter.notifyDataSetChanged();
	        } else {
	            Log.d("DEBUG", "Error: " + e.getMessage());
	        }
			
		}
		
	}

}
