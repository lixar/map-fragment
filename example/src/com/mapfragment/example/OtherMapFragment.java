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

import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.mapfragment.library.MapFragment;

public class OtherMapFragment extends MapFragment {

	private GeoPoint mapCenter;
	private int zoomLevel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mapCenter = new GeoPoint((int) (45.417 * 1E6), (int) (-75.7 * 1E6));
		zoomLevel = 8;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Other Map Fragment");
		mapView.setBuiltInZoomControls(true);
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
}
