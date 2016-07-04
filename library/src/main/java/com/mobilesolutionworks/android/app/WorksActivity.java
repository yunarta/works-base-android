package com.mobilesolutionworks.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksActivity extends AppCompatActivity {

    private SparseArray<FragmentTrackInfo> mTrackInfoMap;

    private WorksControllerManager mController;

    public WorksActivity() {
        mTrackInfoMap = new SparseArray<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksControllerManager.Loader loader = (WorksControllerManager.Loader) getSupportLoaderManager().initLoader(0, null, new WorksControllerManager.LoaderCallbacks(this));
        mController = loader.getController();
    }

    public WorksControllerManager getControllerManager() {
        return mController;
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (fragment instanceof WaitingForResult) {
            FragmentManager fm = fragment.getFragmentManager();
            int id = fm.getFragments().indexOf(fragment);

            FragmentTrackInfo info = new FragmentTrackInfo(id);

            Fragment parent = fragment;
            while ((parent = parent.getParentFragment()) != null) {
                fm = parent.getFragmentManager();
                id = fm.getFragments().indexOf(parent);
                info = new FragmentTrackInfo(id, info);
            }

            mTrackInfoMap.put(requestCode, info);
            super.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityFromFragment(fragment, intent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int key = requestCode & 0xffff;

        FragmentTrackInfo trackInfo = mTrackInfoMap.get(key);
        mTrackInfoMap.remove(key);

        if (trackInfo != null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.getFragments().get(trackInfo.mId);

            if (trackInfo.mChild != null) {
                FragmentTrackInfo childInfo = trackInfo;
                Fragment child = fragment;

                while ((childInfo = childInfo.mChild) != null) {
                    fm = child.getChildFragmentManager();
                    child = fm.getFragments().get(childInfo.mId);
                }

                fragment = child;
            }

            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class FragmentTrackInfo {

        FragmentTrackInfo mChild;

        int mId;

        FragmentTrackInfo(int id) {
            mId = id;
        }

        FragmentTrackInfo(int id, FragmentTrackInfo info) {
            mId = id;
            mChild = info;
        }

        public int getId() {
            int requestId = 0;
            if (mChild != null) {
                requestId |= mChild.getId() << 8;
            }

            requestId |= mId;
            return requestId;
        }
    }
}
