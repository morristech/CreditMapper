package com.testingapp.creditmapper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class MapManager {
	
	public static final String ACTION_LOCATION =
			"com.testingapp.creditmapper.ACTION_LOCATION";

	private static MapManager sMapManager;
	private Context mAppContext;
	private LocationManager mLocationManager;

	// The private constructor forces users to use RunManager.get(Context)
	private MapManager(Context appContext) {
		mAppContext = appContext;
		mLocationManager = (LocationManager) mAppContext
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public static MapManager get(Context c) {
		if (sMapManager == null) {
			// Use the application context to avoid leaking activities
			sMapManager = new MapManager(c.getApplicationContext());
		}
		return sMapManager;
	}

	private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
		return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
	}

	public void startLocationUpdates() {
		String provider = LocationManager.GPS_PROVIDER;
		
		Location lastKnown = mLocationManager.getLastKnownLocation(provider);
		if(lastKnown != null){
			lastKnown.setTime(System.currentTimeMillis());
			broadcastLocation(lastKnown);
		}
		
		// Start updates from the location manager
		PendingIntent pi = getLocationPendingIntent(true);
		mLocationManager.requestLocationUpdates(provider, 1000, 0, pi);
	}

	private void broadcastLocation(Location location) {
		// TODO Auto-generated method stub
		Intent broadcast = new Intent(ACTION_LOCATION);
		broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
		mAppContext.sendBroadcast(broadcast);
	}

	public void stopLocationUpdates() {
		PendingIntent pi = getLocationPendingIntent(false);
		if (pi != null) {
			mLocationManager.removeUpdates(pi);
			pi.cancel();
		}
	}

	public boolean isTrackingLocation() {
		return getLocationPendingIntent(false) != null;
	}

}
