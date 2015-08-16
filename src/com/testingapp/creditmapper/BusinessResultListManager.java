package com.testingapp.creditmapper;

import java.util.ArrayList;

import android.content.Context;

import com.testingapp.creditmapper.SearchResultDatabaseHelper.BusinessCursor;
import com.testingapp.creditmapper.SearchResultDatabaseHelper.LocationCursor;

public class BusinessResultListManager {
	
	private static BusinessResultListManager sManager;
	private SearchResultDatabaseHelper mHelper; 
	private ArrayList<BusinessResult> mResultList;
	
	private BusinessResultListManager(Context c){
		mResultList = new ArrayList<BusinessResult>();
		mHelper = new SearchResultDatabaseHelper(c);
	}
	
	public static BusinessResultListManager getInstance(Context c){
		if(sManager == null){
			sManager = new BusinessResultListManager(c.getApplicationContext());
		}
		return sManager;
	}
	
	public ArrayList<BusinessResult> getAllResults(){
		return mResultList;
	}
	
	public void addResult(BusinessResult result){
		mResultList.add(result);
		commitDatabase(result);
	}
	
	public BusinessCursor getBusinessCursor(String searchString){
		return mHelper.queryBusinessTable(searchString);
	}
	
	public LocationCursor getLocationCursor(int id){
		return mHelper.queryLocationTable(id);
	}
	
	private void commitDatabase(BusinessResult result){
		mHelper.insertBusiness(result);
	}
	
	public void clearResults(){
		mHelper.cleanTables();
		mResultList = new ArrayList<BusinessResult>();
	}

}
