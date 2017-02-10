package com.mobilesolutionworks.works.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.sample.WaitingForResult;
import com.mobilesolutionworks.works.core.WorksController;
import com.mobilesolutionworks.works.core.WorksSupportControllerManager;

/**
 * Created by yunarta on 19/11/15.
 */
@Deprecated
public class WorksActivity extends AppCompatActivity implements Host {

    private SparseArray<FragmentTrackInfo> mTrackInfoMap;

    private WorksSupportControllerManager mController;

    public WorksActivity() {
        mTrackInfoMap = new SparseArray<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksSupportControllerManager.InternalLoader loader = (WorksSupportControllerManager.InternalLoader) getSupportLoaderManager().initLoader(0, null, new WorksSupportControllerManager.LoaderCallbacks(this, null));
        mController = loader.getController();
    }

    public WorksSupportControllerManager getControllerManager() {
        return mController;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.dispatchPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.dispatchResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mController.dispatchOnSaveInstanceState(outState);
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

    public void onActivityResultCompat(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
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

    public void postControllerResult(int id, int requestCode, int resultCode, Object data) {
        WorksController controller = mController.getController(id);
        if (controller != null) {
            controller.onControllerResult(requestCode, resultCode, data);
        }
    }
}
