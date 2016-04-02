package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mobilesolutionworks.android.app.WorksActivity;

public class MainActivity extends WorksActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.addToBackStack(null).replace(R.id.fragment_container, new WCHostFragment()).commit();
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null).replace(R.id.fragment_container, new WCHostFragment()).commit();

//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_container, new BWCHostFragment()).commit();
        }
//        WorksControllerManager manager = getControllerManager();
//        Log.d("/!", "manager.mWho = " + manager.who());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
