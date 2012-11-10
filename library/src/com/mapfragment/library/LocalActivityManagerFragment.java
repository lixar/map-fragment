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

package com.mapfragment.library;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.actionbarsherlock.app.SherlockFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * This is a fragment that will be used during transition from activities to fragments.
 */
public class LocalActivityManagerFragment extends SherlockFragment {

    private static final String TAG = LocalActivityManagerFragment.class.getSimpleName();
    private static final String KEY_STATE_BUNDLE = "localActivityManagerState";

    private LocalActivityManager mLocalActivityManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(): " + getClass().getSimpleName());

        Bundle state = null;
        if(savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }

        mLocalActivityManager = new LocalActivityManager(getActivity(), true);
        mLocalActivityManager.dispatchCreate(state);
    }

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume(): " + getClass().getSimpleName());
        mLocalActivityManager.dispatchResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause(): " + getClass().getSimpleName());
        mLocalActivityManager.dispatchPause(getActivity().isFinishing());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(): " + getClass().getSimpleName());
        mLocalActivityManager.dispatchStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy(): " + getClass().getSimpleName());
        mLocalActivityManager.dispatchDestroy(getActivity().isFinishing());
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	protected Window createActivity(String id, Intent intent) {
		return mLocalActivityManager.startActivity(id, intent);
	}

	protected Window destroyActivity(String id, boolean finishing) {
		final Window window = mLocalActivityManager.destroyActivity(id, finishing);

		boolean destroyHackWorked = destroyActivityBug(id, finishing);
		if (!destroyHackWorked) {
			final String logName = LocalActivityManagerFragment.class.getSimpleName();
			Log.d(logName, "Failed to execute destroyActivityBug hack successfully");
		}

		return window;
	}

	/**
	 * This method is here to attempt to correct the bug there is in the
	 * LocalActivityManager class.  Since this class has been deprecated
	 * then I don't see this changing anytime soon.  This hach should be
	 * safe until this class dissapears completely.
	 *
	 * @param id
	 * @param finishing
	 * @return
	 */
	private boolean destroyActivityBug(String id, boolean finishing) {
		// http://code.google.com/p/android/issues/detail?id=12359
		// http://www.netmite.com/android/mydroid/frameworks/base/core/java/android/app/LocalActivityManager.java
		final LocalActivityManager activityManager = mLocalActivityManager;
		if (activityManager != null) {
			activityManager.destroyActivity(id, false);
			try {
				final Field mActivitiesField = LocalActivityManager.class.getDeclaredField("mActivities");
				if (mActivitiesField != null) {
					mActivitiesField.setAccessible(true);
					@SuppressWarnings("unchecked")
					final Map<String, Object> mActivities = (Map<String, Object>) mActivitiesField.get(activityManager);
					if (mActivities != null) {
						mActivities.remove(id);
					}
					final Field mActivityArrayField = LocalActivityManager.class.getDeclaredField("mActivityArray");
					if (mActivityArrayField != null) {
						mActivityArrayField.setAccessible(true);
						@SuppressWarnings("unchecked")
						final ArrayList<Object> mActivityArray = (ArrayList<Object>) mActivityArrayField.get(activityManager);
						if (mActivityArray != null) {
							for (Object record : mActivityArray) {
								final Field idField = record.getClass().getDeclaredField("id");
								if (idField != null) {
									idField.setAccessible(true);
									final String _id = (String) idField.get(record);
									if (id.equals(_id)) {
										mActivityArray.remove(record);
										break;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
