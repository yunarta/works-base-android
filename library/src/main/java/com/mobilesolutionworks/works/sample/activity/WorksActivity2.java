package com.mobilesolutionworks.works.sample.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.sample.WaitingForResult;
import com.mobilesolutionworks.works.core.WorksController;
import com.mobilesolutionworks.works.core.WorksControllerManager;

/**
 * Created by lucas34990 on 10/2/17.
 */

public class WorksActivity2 extends Activity implements Host {

    private final SparseArray<FragmentTrackInfo> mTrackInfoMap = new SparseArray<>();

    private WorksControllerManager mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksControllerManager.InternalLoader loader = (WorksControllerManager.InternalLoader) getLoaderManager().initLoader(0, null, new WorksControllerManager.LoaderCallbacks(this, null));
        mController = loader.getController();
    }

    public WorksControllerManager getControllerManager() {
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
//            TODO
//            int id = fm.getFragments().indexOf(fragment);
//
//            FragmentTrackInfo info = new FragmentTrackInfo(id);
//
//            Fragment parent = fragment;
//            while ((parent = parent.getParentFragment()) != null) {
//                fm = parent.getFragmentManager();
//                id = fm.getFragments().indexOf(parent);
//                info = new FragmentTrackInfo(id, info);
//            }
//
//            mTrackInfoMap.put(requestCode, info);
            super.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityFromFragment(fragment, intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int key = requestCode & 0xffff;

        FragmentTrackInfo trackInfo = mTrackInfoMap.get(key);
        mTrackInfoMap.remove(key);

        if (trackInfo != null) {
//            TODO
//            FragmentManager fm = getFragmentManager();
//            Fragment fragment = fm.getFragments().get(trackInfo.mId);
//
//            if (trackInfo.mChild != null) {
//                FragmentTrackInfo childInfo = trackInfo;
//                Fragment child = fragment;
//
//                while ((childInfo = childInfo.mChild) != null) {
//                    fm = child.getChildFragmentManager();
//                    child = fm.getFragments().get(childInfo.mId);
//                }
//
//                fragment = child;
//            }
//
//            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onActivityResultCompat(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public android.support.v4.app.FragmentManager getHostFragmentManager() {
        throw new UnsupportedOperationException();
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
