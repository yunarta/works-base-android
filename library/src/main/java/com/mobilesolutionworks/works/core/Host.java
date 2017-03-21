package com.mobilesolutionworks.works.core;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by lucas34990 on 10/2/17.
 */

public interface Host {

    /**
     *
     * For host activity:
     *  @return this()
     *
     * For host fragment
     *  @return getActivity()
     *  This implementation is transparent for the fragment since
     *  it already has this method.
     */
    FragmentActivity getActivity();

    /**
     *
     * For host activity:
     *  @return getResources()
     *
     * For host fragment
     *  @return getResources()
     *
     */
    Resources getResources();

    /**
     *
     * For host activity:
     *  @return getSupportFragmentManager()
     *
     * For host fragment
     *  @return getChildFragmentManager()
     */
    FragmentManager getHostFragmentManager();

    /**
     * This method is public in fragment but protected in activity
     * To have the same visibility we declare the function.
     * So both will have the public method
     * If your host is neither fragment or activity
     * you should throw an exception
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     *
     * For host activity:
     *  @return getIntent().getExtra()
     *
     * For host fragment
     *  @return getArguments()
     */
    Bundle getArguments();

    /**
     *
     * For host activity:
     *  It will call the normal finish()
     *
     * For host fragment
     *  Up to fragment implementation
     *
     * For host Dialog
     *   It will call dismiss()
     */
    void finish();

    /**
     * Experimental
     */
    WorksSupportControllerManager getControllerManager();


}
