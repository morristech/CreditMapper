package com.testingapp.creditmapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SearchResultDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG_DATABASE_HELPER = "SearchResultDatabaseHelper";
	
	private static final String DB_NAME = "businesses.sqlite";
	private static final int VERSION = 1;
	private static final String TABLE_BUSINESS = "business";
	private static final String TABLE_LOCATION = "location";

	private static final String COLUMN_MERCHANTNAME = "merchant_name";
	private static final String COLUMN_MERCHANTCODE = "merchant_code";
	private static final String COLUMN_MERCHANTCATEGORY = "merchant_category";
	private static final String COLUMN_IS_FAVORITE = "is_favorite";
	private static final String COLUMN_BUSINESS_ID = "_id";

	private static final String COLUMN_LOCATION_ID = "location_id";
	private static final String COLUMN_ADDRESSONE = "address_one";
	private static final String COLUMN_ADDRESSTWO = "address_two";
	private static final String COLUMN_CITY = "city";
	private static final String COLUMN_STATE = "state";
	private static final String COLUMN_ZIP = "zip";

	public SearchResultDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG_DATABASE_HELPER, "onCreate method called");
		// Create the "run" table
		db.execSQL("create table business (" + COLUMN_BUSINESS_ID
				+ " integer primary key autoincrement, " + COLUMN_MERCHANTNAME
				+ " varchar(100), " + COLUMN_MERCHANTCODE + " integer, "
				+ COLUMN_MERCHANTCATEGORY + " varchar(100), "
				+ COLUMN_IS_FAVORITE + " integer)");
		// Create the "location" table
		db.execSQL("create table location (" + COLUMN_ADDRESSONE
				+ " varchar(100), " + COLUMN_ADDRESSTWO + " varchar(100), "
				+ COLUMN_CITY + " varchar(100), " + COLUMN_STATE
				+ " varchar(2), " + COLUMN_ZIP + " integer, "
				+ COLUMN_LOCATION_ID
				+ " integer references business("+ COLUMN_BUSINESS_ID + "))");
		
		//TODO: Initial population of all results
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Implement schema changes and data massage here when upgrading
	}

	public long insertBusiness(BusinessResult br) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_MERCHANTNAME, br.getMerchantName());
		cv.put(COLUMN_MERCHANTCODE, br.getMerchantCode());
		cv.put(COLUMN_MERCHANTCATEGORY, br.getMerchantCategory());
		
		if(br.isFavorite()){
			cv.put(COLUMN_IS_FAVORITE, "Y");
		}
		else{
			cv.put(COLUMN_IS_FAVORITE, "N");
		}
		
		long id = getWritableDatabase().insert(TABLE_BUSINESS, null, cv);
		Log.d(TAG_DATABASE_HELPER, "Commited Business w/ ID: " + id);
		Log.d(TAG_DATABASE_HELPER, "Business: " + br);
		insertLocation(br.getLocation(), id);
		return id;
	}

	private long insertLocation(LocationDesc loc, long bus_id) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ADDRESSONE, loc.getAddressLine1());
		cv.put(COLUMN_ADDRESSTWO, loc.getAddressLine2());
		cv.put(COLUMN_CITY, loc.getCity());
		cv.put(COLUMN_STATE, loc.getState());
		cv.put(COLUMN_ZIP, loc.getZipCode());
		cv.put(COLUMN_LOCATION_ID, bus_id);
		long id = getWritableDatabase().insert(TABLE_LOCATION, null, cv);
		Log.d(TAG_DATABASE_HELPER, "Commited Location w/ ID: " + id);
		Log.d(TAG_DATABASE_HELPER, "Location: " + loc);
		return id;
	}
	
	

	public BusinessCursor queryBusinessTable(String query) {
		// Equivalent to "select * from run order by start_date asc"
		
		Cursor wrapped = null;
		if(query == null){
			wrapped = getReadableDatabase().query(TABLE_BUSINESS, null, null,
							null, null, null, null);
		}
		else{
			wrapped = getReadableDatabase().query(TABLE_BUSINESS, null, COLUMN_MERCHANTNAME + " = ?",
					new String[] {query}, null, null, COLUMN_MERCHANTNAME + " asc");
		}
		Log.d(TAG_DATABASE_HELPER, "Number of rows Business rows returned: " + wrapped.getCount());
		
		return new BusinessCursor(wrapped);
	}

	/**
	 * A convenience class to wrap a cursor that returns rows from the "business"
	 * table. The {@link getRun()} method will give you a Run instance
	 * representing the current row.
	 */
	public static class BusinessCursor extends CursorWrapper {
		
		public BusinessCursor(Cursor c) {
			super(c);
		}

		/**
		 * Returns a Run object configured for the current row, or null if the
		 * current row is invalid.
		 */
		public BusinessResult getBusiness(int position) {
			if (isBeforeFirst() || isAfterLast()){
				if(isBeforeFirst()){
					if(!moveToFirst()){
						Log.d(TAG_DATABASE_HELPER, "Returning null: moveToFirst");
						return null;
					}
				}
				else{
					Log.d(TAG_DATABASE_HELPER, "Returning null: isAfterLast");
					return null;
				}
			}
			
			if(!moveToPosition(position)){
				Log.d(TAG_DATABASE_HELPER, "Returning null: moveToPosition");
				return null;
			}
			
			BusinessResult businessResult = new BusinessResult();
			int busID = getInt(getColumnIndex(COLUMN_BUSINESS_ID));
			businessResult.setId(busID);
			String merchantName = getString(getColumnIndex(COLUMN_MERCHANTNAME));
			businessResult.setMerchantName(merchantName);
			int merchantCode = getInt(getColumnIndex(COLUMN_MERCHANTCODE));
			businessResult.setMerchantCode(merchantCode);
			String merchantCategory = getString(getColumnIndex(COLUMN_MERCHANTCATEGORY));
			businessResult.setMerchantCategory(merchantCategory);
			String favorite = getString(getColumnIndex(COLUMN_MERCHANTCATEGORY));
			if(favorite != null && favorite.equals("Y")){
				businessResult.setFavorite(true);
			}
			else{
				businessResult.setFavorite(false);
			}
			
			return businessResult;
		}
	}
	
	public LocationCursor queryLocationTable(int busID) {
		// Equivalent to "select * from run order by start_date asc"
		Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION, null, COLUMN_LOCATION_ID + " = " + busID,
				null, null, null, null);
		Log.d(TAG_DATABASE_HELPER, "Number of Location rows returned: " + wrapped.getCount());
		return new LocationCursor(wrapped);
	}

	/**
	 * A convenience class to wrap a cursor that returns rows from the "business"
	 * table. The {@link getRun()} method will give you a Run instance
	 * representing the current row.
	 */
	public static class LocationCursor extends CursorWrapper {
		public LocationCursor(Cursor c) {
			super(c);
		}

		/**
		 * Returns a Run object configured for the current row, or null if the
		 * current row is invalid.
		 */
		public LocationDesc getLocation() {
			if (isBeforeFirst() || isAfterLast()){
				if(isBeforeFirst()){
					Log.d(TAG_DATABASE_HELPER, "Returning null: isBeforeFirst");
					if(!moveToFirst()){
						return null;
					}
				}
				else{
					return null;
				}
			}
			
			LocationDesc location = new LocationDesc();
			int locID = getInt(getColumnIndex(COLUMN_LOCATION_ID));
			location.setId(locID);
			String address1 = getString(getColumnIndex(COLUMN_ADDRESSONE));
			location.setAddressLine1(address1);
			String address2 = getString(getColumnIndex(COLUMN_ADDRESSTWO));
			location.setAddressLine2(address2);
			String city = getString(getColumnIndex(COLUMN_CITY));
			location.setCity(city);
			String state = getString(getColumnIndex(COLUMN_STATE)); 
			location.setState(state);
			int zip = getInt(getColumnIndex(COLUMN_ZIP));
			location.setZipCode(zip); 
			
			return location;
		}
	}
	
	public void cleanTables(){
		getWritableDatabase().delete(TABLE_BUSINESS, null, null);
		getWritableDatabase().delete(TABLE_LOCATION, null, null);
		getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS);
		getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(getWritableDatabase());
	}
}
