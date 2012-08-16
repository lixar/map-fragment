/*
 * Copyright (C) 2011 Ievgenii Nazaruk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inazaruk.example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.*;

import java.util.ArrayList;
import java.util.List;

public class MyMapFragment extends ActivityHostFragment {

	private static final String SAVE_STATE_LAT = "lat";
	private static final String SAVE_STATE_LON = "lon";
	private static final String SAVE_STATE_ZOOM = "zoom";

	private MapView mapView;

	private GeoPoint mapCenter;
	private int zoomLevel;
	private MyItemizedOverlay itemizedOverlay;
    
    @Override
    protected Class<? extends Activity> getActivityClass() {
        return MyMapActivity.class;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		Drawable marker = getResources().getDrawable(R.drawable.ic_launcher);
		itemizedOverlay = new MyItemizedOverlay(marker);

		if (savedInstanceState == null) {
			mapCenter = new GeoPoint((int) (43.716589 * 1E6), (int) (-79.340686 * 1E6));
			zoomLevel = 10;
		} else {
			Integer lat = savedInstanceState.getInt(SAVE_STATE_LAT, (int) (43.716589 * 1E6));
			Integer lon = savedInstanceState.getInt(SAVE_STATE_LON, (int) (-79.340686 * 1E6));
			Integer zoom = savedInstanceState.getInt(SAVE_STATE_ZOOM, 10);
			mapCenter = new GeoPoint(lat, lon);
			zoomLevel = zoom;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(SAVE_STATE_LAT, mapCenter.getLatitudeE6());
		outState.putInt(SAVE_STATE_LON, mapCenter.getLongitudeE6());
		outState.putInt(SAVE_STATE_ZOOM, zoomLevel);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.otherActivity:
				Intent intent = new Intent(getActivity(), OtherFragmentActivity.class);
				startActivity(intent);
				break;
			case R.id.otherFragment:
				addOtherFragment();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mapView = (MapView) view.findViewById(R.id.mapview);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mapView.setBuiltInZoomControls(false);

		List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
		overlays.add(itemizedOverlay);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Main Map Fragment");
	}

	@Override
	public void onPause() {
		super.onPause();

		mapCenter = mapView.getMapCenter();
		zoomLevel = mapView.getZoomLevel();
	}

	@Override
	public void onResume() {
		super.onResume();

		mapView.getController().setCenter(mapCenter);
		mapView.getController().setZoom(zoomLevel);
	}

	private void addOtherFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment fragment = new MyOtherMapFragment();
		fm.beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
	}

	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		private List<Destination> destinationList = new ArrayList<Destination>();

		public MyItemizedOverlay(Drawable drawable) {
			super(boundCenterBottom(drawable));
			// Toronto
			destinationList.add(new Destination("Ottawa", "Hometown", 43.716589, -79.340686));
			// Ottawa
			destinationList.add(new Destination("Toronto", "Eaton Centre", 45.417, -75.7));
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			Destination destination = destinationList.get(i);
			final GeoPoint geoPoint =
					new GeoPoint((int) (destination.latitude * 1E6),
							(int) (destination.longitude * 1E6));
			final OverlayItem item =
					new OverlayItem(geoPoint, destination.title, destination.snippet);
			return item;
		}

		@Override
		public int size() {
			return destinationList.size();
		}
	}

	public static class Destination {
		public double latitude;
		public double longitude;
		public String title;
		public String snippet;

		public Destination(String title, String snippet, double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.title = title;
			this.snippet = snippet;
		}
	}
}
