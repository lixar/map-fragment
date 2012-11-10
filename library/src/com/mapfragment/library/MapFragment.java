package com.mapfragment.library;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.google.android.maps.MapView;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;


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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		View hostedView = getHostedView();
		mapView = (MapView) hostedView.findViewById(R.id.mf__hosted_view);
		if (mapView == null) {
			throw new IllegalStateException("mapView is null please make sure you've given your MapView an id of 'mf__hosted_view'");
		}
	}

	protected MapView getMapView() {
		return mapView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		cleanUpMapView();
		releaseMapView();
	}

	private void cleanUpMapView() {
		cleanUpMapViewField();
		cleanUpMapViewController();
		cleanUpMapViewConverter();
		cleanUpMapViewZoomHelper();
		cleanUpMapViewOverlays();
	}

	private void cleanUpMapViewField() {
		try {
			releaseMapViewMapField();
		} catch (final NoSuchFieldException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewField() failed", e);
		} catch (final IllegalAccessException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewField() failed", e);
		}
	}

	private void cleanUpMapViewController() {
		try {
			releaseMapViewController();
		} catch (final NoSuchFieldException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewController() failed", e);
		} catch (final IllegalAccessException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewController() failed", e);
		}
	}

	private void cleanUpMapViewConverter() {
		try {
			releaseMapViewConverter();
		} catch (final NoSuchFieldException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewConverter() failed", e);
		} catch (final IllegalAccessException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewConverter() failed", e);
		}
	}

	private void cleanUpMapViewZoomHelper() {
		try {
			releaseMapViewZoomHelper();
		} catch (final NoSuchFieldException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewZoomHelper() failed", e);
		} catch (final IllegalAccessException e) {
			Log.e(MapFragment.class.getSimpleName(), "cleanUpMapViewZoomHelper() failed", e);
		}
	}

	private void cleanUpMapViewOverlays() {
		releaseMapViewOverlays();
	}

	private void releaseMapViewMapField() throws NoSuchFieldException, IllegalAccessException {
		Class<?> MapClass = mapView.getClass();
		Field fMapInView = MapClass.getDeclaredField("mMap");
		AccessibleObject.setAccessible(new AccessibleObject[]{fMapInView}, true);
		fMapInView.set(mapView, null);
	}

	private void releaseMapViewConverter() throws NoSuchFieldException, IllegalAccessException {
		Field fConverterInView = MapView.class.getDeclaredField("mConverter");
		AccessibleObject.setAccessible(new AccessibleObject[]{fConverterInView}, true);
		fConverterInView.set(mapView, null);
	}

	private void releaseMapViewController() throws NoSuchFieldException, IllegalAccessException {
		Field fControllerInView = MapView.class.getDeclaredField("mController");
		AccessibleObject.setAccessible(new AccessibleObject[]{fControllerInView}, true);
		fControllerInView.set(mapView, null);
	}

	private void releaseMapViewZoomHelper() throws NoSuchFieldException, IllegalAccessException {
		Field fZoomHelperInView = MapView.class.getDeclaredField("mZoomHelper");
		AccessibleObject.setAccessible(new AccessibleObject[]{fZoomHelperInView}, true);
		fZoomHelperInView.set(mapView, null);
	}

	private void releaseMapViewOverlays() {
		int size = mapView.getOverlays().size();
		for (int i = size; i > 0; i--) {
			mapView.getOverlays().remove(i - 1);
		}
	}

	private void releaseMapView() {
		ViewParent viewParent = mapView.getParent();
		if (viewParent != null) {
			ViewGroup viewGroup = (ViewGroup) viewParent;
			viewGroup.removeView(mapView);
		}
		mapView = null;
	}
}
