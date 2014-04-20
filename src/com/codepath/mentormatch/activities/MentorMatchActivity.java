package com.codepath.mentormatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.mentormatch.R;
import com.codepath.mentormatch.core.ParseApplication;
import com.codepath.mentormatch.fragments.MentorSearchResultsFragment;

public class MentorMatchActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mentor_match);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flMentorSearchResults, new MentorSearchResultsFragment());
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mentor_match, menu);
		return true;
	}

	public void handleLogout(MenuItem item){
		ParseApplication.logoutUser();
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
	}

}
