package com.testingapp.creditmapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFragment extends Fragment {
	
	private static final String DIALOG_STATE = "State";
	private static final int REQUEST_CODE = 0;
	
	private Button mSearchButton, mCancelButton, mCalcButton;
	private EditText mQuery, mState;
	private String str;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mainpage, parent, false);
		
		mQuery = (EditText)v.findViewById(R.id.search_text);
		mState = (EditText)v.findViewById(R.id.searchState_text);
		
		mState.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getSupportFragmentManager();
				StatePickerFragment stateDialog = new StatePickerFragment();
				stateDialog.setTargetFragment(MainFragment.this, REQUEST_CODE);
				stateDialog.show(fm, DIALOG_STATE);
				
			}
		});
		
		
		mSearchButton = (Button)v.findViewById(R.id.search_button);
		mCancelButton = (Button)v.findViewById(R.id.cancel_button);
		mCalcButton = (Button) v.findViewById(R.id.openCalculator_button);
		
		mSearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(getActivity() != null){
					
					Intent i = new Intent(getActivity(), SearchResultListActivity.class);
					str = mQuery.getText().toString();
					i.putExtra(SearchResultListFragment.SEARCH_QUERY, str);
					startActivity(i);
				}
			}
		});
		
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getActivity() != null){
					Toast.makeText(getActivity(), R.string.cancel_button, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mCalcButton.setOnClickListener(new View.OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
			@Override
			public void onClick(View arg0) {
				Intent i = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_CALCULATOR);
				startActivity(i);
				// TODO Auto-generated method stub
				
			}
		});
		
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == REQUEST_CODE){
				String str = (String)data.getStringExtra(StatePickerFragment.EXTRA_STATE);
				mState.setText(str.substring(str.length()-2));
			}
		}
	}
	
}
