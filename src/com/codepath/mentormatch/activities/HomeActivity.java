package com.codepath.mentormatch.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.mentormatch.R;
import com.codepath.mentormatch.core.ParseApplication;
import com.codepath.mentormatch.helpers.LinkedInClient;
import com.codepath.mentormatch.models.LinkedInUser;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		LinkedInClient linkedInClient = ParseApplication.getRestClient();
				
		linkedInClient.getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonUser) {
				LinkedInUser u = LinkedInUser.fromJson(jsonUser);
				Log.d("DEBUG", "Got back: " + u.toString());
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.d("DEBUG", "FAILED: " + e.getMessage());
				e.printStackTrace();
			}
			
			@Override
			protected void handleFailureMessage(Throwable e, String responseBody) {
				Log.d("DEBUG", "FAILED: " + responseBody);
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}