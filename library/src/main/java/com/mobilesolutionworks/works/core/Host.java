package com.mobilesolutionworks.works.core;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

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
     * @See startActivity(Intent, Bundle);
     */
    void startActivity(Intent intent);

    /**
     * Fragment and activty implements this method.
     */
    void startActivity(Intent intent, @Nullable Bundle options);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options);

    void requestPermissions(@NonNull String[] permissions, int requestCode);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    @Nullable View getView();

    /**
     *
     * For host activity:
     *  @return getResources()
     *
     * For host fragment
     *  @return getResources()
     *
     * This is transparent for both
     *
     * You can also get the resources from the activity
     * But the fragment contains a pointer to the resources
     * This method will avoid doing 1 extra operation
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
