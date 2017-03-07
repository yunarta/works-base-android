package com.mobilesolutionworks.works.core;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.mobilesolutionworks.works.sample.activity.WorksCompatActivity;
import com.mobilesolutionworks.works.sample.fragment.WorksFragment;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * Created by yunarta on 28/6/16.
 */
public class SimpleWorksController<H extends Host> extends WorksController {

    private static final Executor UIExecutor = command -> new Handler(Looper.getMainLooper()).post(command);

    private final PublicObservable observable = new PublicObservable();

    private boolean mIsPaused = true;

    private H mHost;

    public void runOnUIWhenIsReady(final Runnable runnable) {
        if (mIsPaused) {
            observable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    observable.deleteObserver(this);
                    executeInUIThread(runnable);
                }
            });
        } else {
            executeInUIThread(runnable);
        }
    }

    private void executeInUIThread(final Runnable runnable) {
        if(isInUIThread()) {
            runnable.run();
        } else {
            UIExecutor.execute(runnable);
        }
    }

    private boolean isInUIThread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /* package */ void setHost(H host) {
        boolean sendUpdate = mHost != host;

        this.mHost = host;
        if (sendUpdate) {
            onHostUpdated();
        }
    }

    protected H getHost() {
        return mHost;
    }

    @Override
    protected void onResume() {
        mIsPaused = false;
        observable.setChanged();
        observable.notifyObservers();
    }

    protected void onHostUpdated() {

    }

    @Override
    public void onPaused() {
        mIsPaused = true;
    }

    public boolean isResumed() {
        return !mIsPaused;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        observable.deleteObservers();
    }

    public static <C extends SimpleWorksController> C  init(Host fragment, int id, Callable<C> controller) {
        return fragment.getControllerManager().initController(id, fragment.getArguments(), new WorksSupportControllerManager.ControllerCallbacks<C>() {
            @Override
            public C onCreateController(int id, Bundle bundle) {
                try {
                    return controller.call();
                } catch (Exception e) {
                    // TODO
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onLoadFinished(int id, Bundle bundle, C controller) {
                controller.setHost(fragment);
            }

            @Override
            public void onLoaderReset(C loader) {

            }
        });
    }

    private static final class PublicObservable extends Observable {

        @Override
        public void setChanged() {
            super.setChanged();
        }

    }

    @Deprecated
    public static abstract class ControllerCallbacks<Controller extends SimpleWorksController<E>, E extends Host> implements WorksSupportControllerManager.ControllerCallbacks<Controller> {

        private E mHost;

        @Deprecated
        public ControllerCallbacks(E host) {
            mHost = host;
        }

        @Override
        @Deprecated
        public void onLoadFinished(int id, Bundle bundle, Controller controller) {
            controller.setHost(mHost);
        }

        @Override
        @Deprecated
        public void onLoaderReset(Controller loader) {

        }
    }
}
