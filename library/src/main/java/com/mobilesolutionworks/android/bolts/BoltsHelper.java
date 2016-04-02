package com.mobilesolutionworks.android.bolts;

import java.util.Arrays;

import bolts.Task;

/**
 * Created by yunarta on 31/1/16.
 */
public class BoltsHelper {

    public static Task<Void> whenAll(Task<?>... tasks) {
        return Task.whenAll(Arrays.asList(tasks));
    }
}
