package com.testingapp.creditmapper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MapFragment extends SupportMapFragment {
	
	private static final String ARG_BUS_ID = "BUS_ID";
	
	private LocationReceiver mLocationReceiver = new LocationReceiver();
	private GoogleMap mGoogleMap;
	
	/*{
		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
		}
	};*/
	
	private MapManager mMapManager;
	private Location mCurrLocation;
	private TextView mLocationText;
	
	public static MapFragment newInstance(int busId) {
		Bundle args = new Bundle();
		args.putInt(ARG_BUS_ID, busId);
		MapFragment rf = new MapFragment();
		rf.setArguments(args);
		return rf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mMapManager = MapManager.get(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map, parent, false);
		
		mLocationText = (TextView) v.findViewById(R.id.messageMapFragment);
		
		mMapManager.startLocationUpdates();
		updateUI();
		
		mGoogleMap = getMap();
		if(mGoogleMap == null){
			Log.d("MapFragment", "No map retrieved");
		}else{
			mGoogleMap.setMyLocationEnabled(true);
		}
		

		return v;
	}
	
	private void updateUI(){
		mCurrLocation = mLocationReceiver.getLoc();
		if(mCurrLocation != null){
			mLocationText.setText("Current Location is: " + mCurrLocation.getLatitude() 
					+ " " + mCurrLocation.getLongitude());
		}
		else{
			mLocationText.setText("No current location found");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(mLocationReceiver, new IntentFilter(MapManager.ACTION_LOCATION));
	}
	
	// Placeholder to override onStart method
	@Override
	public void onPause() {
		super.onPause();
		mMapManager.stopLocationUpdates();
	}

	// Placeholder to override onResume method
	@Override
	public void onResume() {
		super.onResume();
		mMapManager.startLocationUpdates();
		updateUI();
	}

	// Placeholder to override onStop method
	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(mLocationReceiver);
		mMapManager.stopLocationUpdates();
	}

	// Placeholder to override onDestroy method
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapManager.stopLocationUpdates();
	}

}
