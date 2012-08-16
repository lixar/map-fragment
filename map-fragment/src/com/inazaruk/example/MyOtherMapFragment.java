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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.maps.*;

import java.util.ArrayList;
import java.util.List;

public class MyOtherMapFragment extends ActivityHostFragment {

	private MapView mapView;

	private GeoPoint mapCenter;
	private int zoomLevel;

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return MyMapActivity.class;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mapCenter = new GeoPoint((int) (45.417 * 1E6), (int) (-75.7 * 1E6));
		zoomLevel = 8;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Other Map Fragment");
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
