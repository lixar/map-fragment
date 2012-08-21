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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

/**
 * This is a fragment that will be used during transition from activities to fragments.
 */
public abstract class ActivityHostFragment extends LocalActivityManagerFragment {
    
    protected abstract Class<? extends Activity> getActivityClass();
    private final static String ACTIVITY_TAG = "hosted";

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		View _view = view.findViewById(R.id.mf__hosted_view_frame);
		if (_view == null) {
			throw new IllegalStateException("View by id 'mf__hosted_view_frame' needs to be specified by this Fragment's xml layout");
		}

		ViewGroup hostedViewFrame = null;

		try {
			hostedViewFrame = (ViewGroup) _view;
		} catch (ClassCastException e) {
			throw new ClassCastException("View by id 'mf__hosted_view_frame' needs to be a subclass of ViewGroup");
		}

		hostedViewFrame.addView(createHostedView());
		view.requestLayout();
	}

	protected View createHostedView() {
		Intent intent = new Intent(getActivity(), getActivityClass());

		final Window w = getLocalActivityManager().startActivity(ACTIVITY_TAG, intent);
		final View wd = w != null ? w.getDecorView() : null;

		if (wd != null) {
			ViewParent parent = wd.getParent();
			if(parent != null) {
				ViewGroup v = (ViewGroup)parent;
				v.removeView(wd);
			}

			wd.setVisibility(View.VISIBLE);
			// This seems to cause confusion with the Menu Option.  When enabled
			// the keyevents somehow gets lost in the MapActivity within the
			// fragment and therefore cause the menu not to show up.
            //wd.setFocusableInTouchMode(true);
            if(wd instanceof ViewGroup) {
                ((ViewGroup) wd).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            }
        }
        return wd;
    }

    /**
     * For accessing public methods of the hosted activity
     */
    public Activity getHostedActivity() {
		return getLocalActivityManager().getActivity(ACTIVITY_TAG);
}
