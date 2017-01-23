package com.example.googlemapjugal;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends FragmentActivity implements LocationListener {

	GoogleMap googlemap;
	
	// The minimum distance to change Updates in meters
		private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters
	
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1; // 5 second
	
	double radius;
    int numPoints;
    double latstr,longstr,phase;
    boolean isGPSEnabled = false;
	Button b1;
	protected LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		try
		{
		
		locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
		 isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		initmap();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			System.out.println("Error che"+e.toString());
		}
	}

	public void initmap() {
		// TODO Auto-generated method stub
		 SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        googlemap = mf.getMap();
	        
	        googlemap.setMyLocationEnabled(true);
	        googlemap.setMapType(GoogleMap.MAP_TYPE_HYBRID); 
	        
	        
	     // Setting a custom info window adapter for the google map		
			googlemap.setInfoWindowAdapter(new InfoWindowAdapter() {
				
				// Use default InfoWindow frame
				@Override
				public View getInfoWindow(Marker arg0) {				
					return null;
				}			
				
				// Defines the contents of the InfoWindow
				@Override
				public View getInfoContents(Marker arg0) {
					
					// Getting view from the layout file info_window_layout
					View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
					
					// Getting the position from the marker
					LatLng latLng = arg0.getPosition();
					
					// Getting reference to the TextView to set latitude
					TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
					
					// Getting reference to the TextView to set longitude
					TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
					/*latstr = latLng.latitude;
					longstr = latLng.longitude;*/
					// Setting the latitude 
					tvLat.setText("Latitude:" + latLng.latitude);
					
					// Setting the longitude
					tvLng.setText("Longitude:"+ latLng.longitude);
					
					// Returning the view containing InfoWindow contents
					return v;
					
				}
			
			});
			
			// Adding and showing marker while touching the GoogleMap
						googlemap.setOnMapClickListener(new OnMapClickListener() {
							
							@Override
							public void onMapClick(LatLng arg0) {
								// Clears any existing markers from the GoogleMap
								googlemap.clear();

								// Creating an instance of MarkerOptions to set position
								MarkerOptions markerOptions = new MarkerOptions();
								
								// Setting position on the MarkerOptions
								markerOptions.position(arg0);				
								
								// Animating to the currently touched position
								googlemap.animateCamera(CameraUpdateFactory.newLatLng(arg0));	
								
								// Adding marker on the GoogleMap
								Marker marker = googlemap.addMarker(markerOptions);
								
								// Showing InfoWindow on the GoogleMap-
								marker.showInfoWindow();

								 PolylineOptions options = new PolylineOptions();
								    radius = 0.0008; //What is that?
								    numPoints = 100;
								    phase = 2 * Math.PI / numPoints;
								    for (int i = 0; i <= numPoints; i++) {
								        options.add(new LatLng(latstr + radius * Math.sin(i * phase),
								                longstr + radius * Math.cos(i * phase))); 
								    }
								    int color = Color.RED;
								    googlemap.addPolyline(options
								            .color(color)
								            .width(2));
								
							
								
							}
						});
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		
		if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                    	latstr = location.getLatitude();
                    	longstr = location.getLongitude();
                    }
                }
            }
        }
		
	}
	

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
