package com.mobilesolutionworks.android.app;

/**
 * Created by yunarta on 20/11/15.
 */
interface HostState
{
    int CREATED   = 1;
    int START     = 2;
    int RESUME    = 3;
    int PAUSED    = 4;
    int STOP      = 5;
    int DESTROYED = 6;
}
