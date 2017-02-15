package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesolutionworks.android.app.WorksFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;

/**
 * A placeholder fragment containing a simple view.
 */
public class WCHostFragment extends WorksFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyBoltsWorksController3 controller = getControllerManager().initController(0, new MyBoltsWorksController3.ControllerCallbacks<MyBoltsWorksController3, WCHostFragment>(this) {
            @Override
            public MyBoltsWorksController3 onCreateController(int id) {
                return new MyBoltsWorksController3();
            }
        });
        Log.d("[fragment]", "controller = " + controller + " this = " + this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class MyBoltsWorksController3 extends BoltsWorksController3<WCHostFragment> {

        @Override
        public void onCreate() {
            super.onCreate();
            Log.d("[fragment]", "onCreate controller = " + getHost());
        }

        @Override
        protected void onResume() {
            super.onResume();
            Log.d("[fragment]", "onResume controller = " + getHost());
        }
    }

}
