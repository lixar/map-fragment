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

package com.mapfragment.example;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mapfragment.library.MapFragment;

import java.util.ArrayList;
import java.util.List;

public class MainMapFragment extends MapFragment {

	private static final String SAVE_STATE_LAT = "lat";
	private static final String SAVE_STATE_LON = "lon";
	private static final String SAVE_STATE_ZOOM = "zoom";

	private GeoPoint mapCenter;
	private int zoomLevel;
	private MyItemizedOverlay itemizedOverlay;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


		getMapView().setBuiltInZoomControls(false);

		List<Overlay> overlays = getMapView().getOverlays();
		overlays.clear();
		overlays.add(itemizedOverlay);

		getActivity().setTitle("Main Map Fragment");
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
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu, menu);
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
	public void onPause() {
		super.onPause();

		mapCenter = getMapView().getMapCenter();
		zoomLevel = getMapView().getZoomLevel();
	}

	@Override
	public void onResume() {
		super.onResume();

		getMapView().getController().setCenter(mapCenter);
		getMapView().getController().setZoom(zoomLevel);
	}

	private void addOtherFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment fragment = new OtherMapFragment();
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
