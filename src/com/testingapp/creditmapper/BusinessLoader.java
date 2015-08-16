package com.testingapp.creditmapper;

import android.content.Context;

import com.testingapp.creditmapper.SearchResultDatabaseHelper.BusinessCursor;

public class BusinessLoader extends DataLoader<BusinessResult>{
	
	private String mSearchString;
	private int mRow;
	
	public BusinessLoader(Context context, String searchString, int position){
		super(context);
		mSearchString = searchString;
		mRow = position;
	}

	@Override
	public BusinessResult loadInBackground() {
		// TODO Auto-generated method stub
		BusinessResultListManager manager = BusinessResultListManager.getInstance(getContext()); 
		BusinessCursor cursor = manager.getBusinessCursor(mSearchString);
		BusinessResult br = cursor.getBusiness(mRow);
		br.setLocation(manager.getLocationCursor(br.getId()).getLocation());
		return br;
	}

}
