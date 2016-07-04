package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.WorksFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController2;

import bolts.Continuation;
import bolts.Task;

/**
 * A placeholder fragment containing a simple view.
 */
public class BoltsActivityFragment extends WorksFragment {

    private BoltsWorksController2 mBwc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksControllerManager manager = getControllerManager();

        mBwc = manager.initController(0, null, new BoltsWorksController2.ControllerCallbacks());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onLogin(View view) {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getFragmentManager(), "dialog");

        mBwc.addTask(Task.forResult(true), new Continuation<Boolean, Boolean>() {
            @Override
            public Boolean then(Task<Boolean> task) {
                DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag("dialog");
                if (dialog != null) {
                    dialog.dismiss();
                }

                return null;
            }
        });
    }
}
