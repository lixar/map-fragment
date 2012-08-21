package com.mapfragment.library;

import android.app.Activity;
import android.view.View;
import com.google.android.maps.MapView;

/**
 * Created with IntelliJ IDEA.
 * User: mlavigne
 * Date: 12-08-16
 * Time: 9:13 AM
 */
public class MapFragment extends ActivityHostFragment {

	private MapView mapView;

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return DefaultMapActivity.class;
	}

	@Override
	protected View createHostedView() {
		View hostedView = super.createHostedView();

		mapView = (MapView) hostedView.findViewById(R.id.mf__hosted_view);
		if (mapView == null) {
			throw new IllegalStateException("mapView is null please make sure you've given your MapView instance an id of mf__hosted_view");
		}
		return hostedView;
	}

	protected MapView getMapView() {
		return mapView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mapView = null;
	}
}
