package com.cai.travellog;

import com.google.android.gms.maps.*;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Build;

public class MainActivity extends ActionBarActivity  {
	public final static String TAG="message activity";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//inflate activity_main, but everything is in fragment_main
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//If your main class extends ActionBarActivity and has a PlaceholderFragment
		//leave this place blank and move to work in PlaceholderFragment class
		//You can also move type PlaceholderFragment to a new file
			  
	}
	





	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
