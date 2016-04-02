package com.mobilesolutionworks.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksActivity extends AppCompatActivity {
    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    ActivityControllerHost mHost;

    private SparseArray<FragmentTrackInfo> mTrackInfoMap;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHost = new ActivityControllerHost(this);
        mTrackInfoMap = new SparseArray<>();

        NonConfiguration nc = (NonConfiguration) getLastCustomNonConfigurationInstance();
        if (nc != null) {
            mHost.restoreLoaderNonConfig(nc.allControllerManagers);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mHost.dispatchPostCreate();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        mHost.dispatchStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mHost.dispatchStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public Object getLastPersistedObject() {
        NonConfiguration nc = (NonConfiguration) super.getLastCustomNonConfigurationInstance();
        return nc != null ? nc.persisted : null;
    }

    public Object onRetainPersistedObject() {
        return null;
    }

    @Override
    public final Object onRetainCustomNonConfigurationInstance() {
        SimpleArrayMap<String, WorksControllerManager> allControllerManagers = mHost.retainLoaderNonConfig();

        NonConfiguration nc = new NonConfiguration();
        nc.persisted = onRetainPersistedObject();
        nc.allControllerManagers = allControllerManagers;

        return nc;
    }

    @Override
    @Deprecated
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    class NonConfiguration {
        public Object persisted;

        public SimpleArrayMap<String, WorksControllerManager> allControllerManagers;
    }

    ActivityControllerHost getControllerHost() {
        return mHost;
    }

    public WorksControllerManager getControllerManager() {
        return mHost.getControllerManager();
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

    class FragmentTrackInfo {

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
