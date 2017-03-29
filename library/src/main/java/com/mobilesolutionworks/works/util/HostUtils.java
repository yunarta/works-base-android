package com.mobilesolutionworks.works.util;

import android.support.v4.app.Fragment;

import com.mobilesolutionworks.works.core.Host;

/**
 * Created by lucas34990 on 29/3/17.
 */

public final class HostUtils {

    private HostUtils() {
        // Helper class
    }

    /**
     *
     * @param fragment
     * @return Retrieve the fragment host
     *
     * If the fragment has a parent fragment. Mostly fragment inside fragment we return
     *
     */
    public static Host getParentHost(Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if(parentFragment == null) {
            // For phone
            return (Host) fragment.getHost(); // Activity
        } else {
            // For tablet
            return (Host) parentFragment;
        }
    }

}
