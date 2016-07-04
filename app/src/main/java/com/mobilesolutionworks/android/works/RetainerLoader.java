package com.mobilesolutionworks.android.works;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by yunarta on 5/7/16.
 */

public class RetainerLoader extends Loader<Retainer> {

    private final Retainer mData;

    public RetainerLoader(Context context) {
        super(context);
        mData = new Retainer();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        deliverResult(mData);
    }

    public Retainer getData() {
        return mData;
    }
}
