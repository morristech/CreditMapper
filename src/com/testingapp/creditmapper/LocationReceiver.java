package com.testingapp.creditmapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
	private static final String TAG = "LocationReceiver";
	
	private Location loc;
	private boolean enabled;

	public Location getLoc() {
		return loc;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// If you got a Location extra, use it
		Location loc2 = (Location) intent
				.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
		if (loc2 != null) {
			onLocationReceived(context, loc2);
			return;
		}
		// If you get here, something else has happened
		if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
			enabled = intent.getBooleanExtra(
					LocationManager.KEY_PROVIDER_ENABLED, false);
			onProviderEnabledChanged(enabled);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	protected void onLocationReceived(Context context, Location loc2) {
		loc = loc2;
		Log.d(TAG, this + " Got location from " + loc.getProvider() + ": "
				+ loc.getLatitude() + ", " + loc.getLongitude());
	}

	protected void onProviderEnabledChanged(boolean enabled) {
		Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
	}
}
