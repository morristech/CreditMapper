package com.testingapp.creditmapper;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends FragmentActivity {
	
	protected abstract Fragment createFragment();
	
	protected int getLayoutResID(){
		return R.layout.activity_onefragment;
	}
	
	protected int getFragmentContainerID(){
		return R.id.oneFragmentContainer;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getLayoutResID());
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		Fragment fragment = fm.findFragmentById(getFragmentContainerID());
		if (fragment == null) {
			//Defines which fragment to display
			fragment = createFragment();
			ft.add(getFragmentContainerID(), fragment).commit();
			if(getFragmentContainerID() == R.id.bottomFragmentContainer){
				fragment = new MapFragment();
				ft.add(R.id.topFragmentContainer, fragment);
			}
		}
		
		//Adds Back Menu button
		if(NavUtils.getParentActivityName(this) != null){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.menu_new_search_item:
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				return true;
			case android.R.id.home:
				if(NavUtils.getParentActivityName(this) != null){
					NavUtils.navigateUpFromSameTask(this);
				}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
