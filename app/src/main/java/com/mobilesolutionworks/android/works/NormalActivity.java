package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yunarta on 5/7/16.
 */

public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle args;
            NormalFragment fragment;

            args = new Bundle();
            args.putString("name", "Fragment#1");

            fragment = new NormalFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            args = new Bundle();
            args.putString("name", "Fragment#2");

            fragment = new NormalFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            args = new Bundle();
            args.putString("name", "Fragment#3");

            fragment = new NormalFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
