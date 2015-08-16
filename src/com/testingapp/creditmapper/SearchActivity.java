package com.testingapp.creditmapper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;

public class SearchActivity extends SingleFragmentActivity{
	
	private static final String LOG_TAG = "SearchActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "onCreate method called");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Log.i(LOG_TAG, "onCreateOptionsMenu method called");
		return true;
	}
	
	//Called before onStart, onPause, onDestroy, to save data
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Log.i(LOG_TAG, "onSaveInstanceState method called");
	}
	
	//Placeholder to override onStart method
	@Override
	public void onStart(){
		super.onStart();
		Log.i(LOG_TAG, "onStart method called");
	}
	
	//Placeholder to override onStart method
	@Override
	public void onPause(){
		super.onPause();
		Log.i(LOG_TAG, "onPause method called");
	}
	
	//Placeholder to override onResume method
	@Override
	public void onResume(){
		super.onResume();
		Log.i(LOG_TAG, "onResume method called");
	}
	
	//Placeholder to override onStop method
	@Override
	public void onStop(){
		super.onStop();
		Log.i(LOG_TAG, "onStop method called");
	}
	
	//Placeholder to override onDestroy method
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(LOG_TAG, "onDestroy method called");
	}

	@Override
	protected Fragment createFragment() {
		
		String searchResult = getIntent().getStringExtra(SearchResultListFragment.SEARCH_QUERY);
		int position = getIntent().getIntExtra(SearchResultListFragment.ARG_POSITION, 0);
		
		Bundle b = new Bundle();
		b.putString(SearchResultListFragment.SEARCH_QUERY, searchResult);
		b.putInt(SearchResultListFragment.ARG_POSITION, position);
		
		return SearchFragment.newInstance(b);
	}

}
