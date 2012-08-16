package com.mapfragment.library;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.maps.MapView;

/**
 * Created with IntelliJ IDEA.
 * User: mlavigne
 * Date: 12-08-16
 * Time: 9:13 AM
 */
public class MapFragment extends ActivityHostFragment {

	protected MapView mapView;

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return DefaultMapActivity.class;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mapView = (MapView) view.findViewById(R.id.mapview);
		return view;
	}
}
