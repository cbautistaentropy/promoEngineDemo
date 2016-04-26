package com.entropy.promoenginedemoapp;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BranchLocationActivity extends BaseActivity implements OnMapReadyCallback {
	
	private LatLng[] latLngDestination;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branch_location);
		
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText(getIntent().getExtras().getString("branchName"));
		tvActionNext.setVisibility(View.INVISIBLE);
		
		com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(BranchLocationActivity.this);
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		try {
			LatLng loc = new LatLng(getIntent().getExtras().getDouble("lat"), 
					getIntent().getExtras().getDouble("longi"));
			map.getUiSettings().setMapToolbarEnabled(false);
	        map.setMyLocationEnabled(false);
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
	        map.getUiSettings().setMyLocationButtonEnabled(false);
	        
			if(getIntent().getExtras().getString("polygon") == null || getIntent().getExtras().getString("polygon").equals("")) {
				map.addCircle(new CircleOptions().center(loc)
	        			.radius(Double.valueOf(getIntent().getExtras().getString("accuracy")))
	        			.fillColor(Color.argb(20, 0, 80, 270))
	        			.strokeWidth(2)
						.strokeColor(Color.MAGENTA));
			} else {
				JSONArray polyArray = new JSONArray(getIntent().getExtras().getString("polygon").toString());
				latLngDestination = new LatLng[polyArray.length()];
				
		        for(int a = 0; a < polyArray.length(); a++) {
		        	latLngDestination[a] = new LatLng(polyArray.getJSONArray(a).getDouble(0), polyArray.getJSONArray(a).getDouble(1));
				}
		        
		        map.addPolygon(new PolygonOptions().geodesic(true)
				   .add(latLngDestination)
				   .strokeWidth(2)
				   .strokeColor(Color.MAGENTA)
				   .fillColor(Color.argb(20, 0, 80, 270))
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
