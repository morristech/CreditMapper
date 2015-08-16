package com.testingapp.creditmapper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchFragment extends Fragment{
	
	private TextView mSearchString, mResultLocation, mResultDistance, mResultMercInfo;
	private BusinessResult mResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		LoaderManager lm = getLoaderManager();
		lm.initLoader(0, getArguments(), new BusinessLoaderCallbacks());
	}
	
	private class BusinessLoaderCallbacks implements LoaderCallbacks<BusinessResult>{

		@Override
		public Loader<BusinessResult> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			return new BusinessLoader(getActivity().getApplicationContext(), args.getString(SearchResultListFragment.SEARCH_QUERY), 
		           args.getInt(SearchResultListFragment.ARG_POSITION));
		}

		@Override
		public void onLoadFinished(Loader<BusinessResult> loader,
				BusinessResult result) {
			// TODO Auto-generated method stub
			Log.d("SearchFragment", result.toString());
			mResult = result;
			
			mSearchString.setText("Displaying result for " + getArguments().getString(SearchResultListFragment.SEARCH_QUERY));
			mResultLocation.setText(mResult.getLocation().toString());
			mResultDistance.setText(mResult.getDistance() + "mi");
			mResultMercInfo.setText(mResult.toString());
			
		}

		@Override
		public void onLoaderReset(Loader<BusinessResult> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search, parent, false);
		
		mSearchString = (TextView)v.findViewById(R.id.ind_search_result_greeting);
		mResultLocation = (TextView)v.findViewById(R.id.ind_search_result_location);
		mResultDistance = (TextView)v.findViewById(R.id.ind_search_result_distance);
		mResultMercInfo = (TextView)v.findViewById(R.id.ind_search_result_merchantInfo);
		
		return v;
	}
	
	public static SearchFragment newInstance(Bundle b){
		
		SearchFragment fragment = new SearchFragment();
		fragment.setArguments(b);
		
		return fragment;
	}
	
	// Menu option to start completely new search
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.new_search_menu, menu);
	}

}
