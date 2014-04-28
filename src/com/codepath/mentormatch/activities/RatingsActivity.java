package com.codepath.mentormatch.activities;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.mentormatch.R;
import com.codepath.mentormatch.core.MentorMatchApplication;
import com.codepath.mentormatch.fragments.MatchResultsListFragment;
import com.codepath.mentormatch.helpers.ParseQueries;
import com.codepath.mentormatch.models.parse.MatchRelationship;
import com.codepath.mentormatch.models.parse.Task;
import com.codepath.mentormatch.models.parse.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RatingsActivity extends Activity {
	public static final String RELATIONSHIP_ID_EXTRA = "relationshipId";

	private MatchRelationship relationship;
	private User mentee;
	private User mentor;
//	private String requestId;
	
	private Button btnSave;
	private EditText etReview;
	private RatingBar rbRating;
	private TextView tvName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ratings);
		etReview = (EditText) findViewById(R.id.etReview);
		btnSave = (Button) findViewById(R.id.btnSave);
		rbRating = (RatingBar) findViewById(R.id.rbRating);
		tvName = (TextView) findViewById(R.id.tvName);
		String objId = getIntent().getStringExtra(RELATIONSHIP_ID_EXTRA);
		btnSave.setEnabled(false);
		ParseQueries.getRelationshipById(objId, new RetrieveRelationshipCallback());
//		setupTask();
	}
	
	private void populateExistingData() {
		if(((User) ParseUser.getCurrentUser()).isMentor()) {
			tvName.setText(mentee.getFullName());
			rbRating.setRating((float) relationship.getMenteeRating());
			etReview.setText(relationship.getCommentForMentee());
			
		} else {			
			tvName.setText(mentor.getFullName());
			rbRating.setRating((float) relationship.getMentorRating());
			etReview.setText(relationship.getCommentForMentor());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (((User) ParseUser.getCurrentUser()).isMentor()) {
			getMenuInflater().inflate(R.menu.main_mentor, menu);
		} else {
			getMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}
	
	public void handleLogout(MenuItem item){
		MentorMatchApplication.logoutUser();
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
	}
	
	public void viewConnections(MenuItem item) {
		Intent i = new Intent(this, ConnectionsActivity.class);
		startActivity(i);
	}
	
	public void viewRequests(MenuItem item) {
		//Intent i = new Intent(this, PendingRequestActivity.class);
		Intent i = new Intent(this, ProfileBuilderActivity.class);
		i.putExtra("foo", "details");
		//i.putExtra(ProfileSummaryListFragment.USER_EXTRA, ParseUser.getCurrentUser().getObjectId());
		startActivity(i);
	}
	
	public void viewMyProfile(MenuItem item) {
		Intent i = new Intent(this, ProfileDetailActivity.class);
		i.putExtra(MatchResultsListFragment.USER_EXTRA, ParseUser.getCurrentUser().getObjectId());
		startActivity(i);
	}

	public void saveRating(View view) {
		if(((User)ParseUser.getCurrentUser()).isMentor()) {
			relationship.setMenteeRating(rbRating.getRating());
			relationship.setCommentForMentee(etReview.getText().toString());

		} else {
			relationship.setMentorRating(rbRating.getRating());
			relationship.setCommentForMentor(etReview.getText().toString());

		}
		relationship.saveInBackground();
		setResult(RESULT_OK); // set result code and bundle data for response
		finish();
	}
	
	private class RetrieveRelationshipCallback extends GetCallback<MatchRelationship> {
		@Override
		public void done(MatchRelationship obj, ParseException e) {
			if(e == null) {
				relationship = obj;
				mentee = (User) relationship.getMentee();
				mentor = (User) relationship.getMentor();
				populateExistingData();
				btnSave.setEnabled(true);				
			} else {
				Log.d("DEBUG", "Error getting relationship");
				e.printStackTrace();
			}
		}		
	}
	
	
	// Task Related Objects
	private EditText etTaskDescription;
	
	private void setupTask() {
		etTaskDescription = (EditText) findViewById(R.id.etTaskDescription);
	}
	public void onAddTask(View view) {
		Task task = new Task();
		task.setDescription(etTaskDescription.getText().toString());
		task.setDueDate(new Date());
		task.setMatchRelationshipId(relationship);
		task.saveInBackground();
	}
	
	private void retrieveTasks(){
		ParseQuery<Task> query = ParseQuery.getQuery("Task");
		query.whereEqualTo(Task.RELATIONSHIP_KEY, relationship);
		query.findInBackground(new FindCallback<Task>() {

			@Override
			public void done(List<Task> list, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					Toast.makeText(getBaseContext(), "Tasks found: " + list.size(), Toast.LENGTH_LONG).show();
				} else {
					e.printStackTrace();
				}
				
			}
		}); 
			
	}

}
