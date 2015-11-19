package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bolts.Continuation;
import bolts.Task;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.WorksFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorkController;
import com.mobilesolutionworks.android.exe.WorksExecutor;

import java.util.concurrent.Callable;

/**
 * A placeholder fragment containing a simple view.
 */
public class BoltsActivityFragment extends WorksFragment
{
    private BoltsWorkController mBwc;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WorksControllerManager manager = getControllerManager();

        mBwc = manager.initController(0, null, new BoltsWorkController.ControllerCallbacks());
        mBwc.setContinuation("login", new BoltsWorkController.ContinuationFactory<Boolean>()
        {
            @Override
            public Task<Boolean> continueWith(Task<Boolean> task)
            {
                DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag("dialog");
                if (dialog != null)
                {
                    dialog.dismiss();
                }

                return null;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onLogin(View view)
    {
        Task<Boolean> task;

        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getFragmentManager(), "dialog");

        task = Task.call(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return true;
            }
        }).continueWith(new Continuation<Boolean, Boolean>()
        {
            @Override
            public Boolean then(Task<Boolean> task) throws Exception
            {
                return task.getResult();
            }
        }, WorksExecutor.MAIN);
        mBwc.addTask("login", task);
    }
}
