package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by yunarta on 5/7/16.
 */

public class NormalFragment extends Fragment {

    private String mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mName = args.getString("name");

        Log.d("[fragment]", mName + " onCreate");
        super.onCreate(savedInstanceState);

        RetainerLoader loader = (RetainerLoader) getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Retainer>() {
            @Override
            public Loader<Retainer> onCreateLoader(int id, Bundle args) {
                return new RetainerLoader(getActivity());
            }

            @Override
            public void onLoadFinished(Loader<Retainer> loader, Retainer data) {

            }

            @Override
            public void onLoaderReset(Loader<Retainer> loader) {

            }
        });
        Log.d("[fragment]", mName + " loaded " + loader.getData());

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("[fragment]", mName + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("[fragment]", mName + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("[fragment]", mName + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("[fragment]", mName + " onStop " + isRemoving());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("[fragment]", mName + " onDestroy " + isRemoving());
    }
}
