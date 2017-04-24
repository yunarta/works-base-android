package com.mobilesolutionworks.works.core;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class Controller<H extends Host> extends Lifecycle {

    private static final Executor UIExecutor = command -> new Handler(Looper.getMainLooper()).post(command);

    /* package */ final PublicObservable observable = new PublicObservable();

    private boolean mIsPaused = true;

    private WeakReference<H> mHost;

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

    /* package */ void executeInUIThread(final Runnable runnable) {
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

        this.mHost = new WeakReference<H>(host);
        if (sendUpdate) {
            onHostUpdated();
        }
    }

    /**
     *
     */
    protected @Nullable H getHost() {
        return mHost.get();
    }

    protected Resources getResources() {
        return getHost().getResources();
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
        return getHost() != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        observable.deleteObservers();
    }

    public static Controller empty(Host host, int id) {
        return init(host, id, (Callable<EmptyController>) EmptyController::new);
    }

    private static final class EmptyController<H extends Host> extends Controller<H> {
        public EmptyController() {
            // Empty
        }
    }

    public static <C extends Controller> C  init(Host host, int id, Callable<C> controller) {
        return host.getControllerManager().initController(id, host.getArguments(), new Manager.ControllerCallbacks<C>() {
            @Override
            public C onCreateController(int id, Bundle bundle) {
                try {
                    return controller.call();
                } catch (Exception e) {
                    throw new RuntimeException(e); // Re-trow
                }
            }

            @Override
            public void onLoadFinished(int id, Bundle bundle, C controller) {
                controller.setHost(host);
            }

            @Override
            public void onLoaderReset(C loader) {

            }
        });
    }

    /* package */ static final class PublicObservable extends Observable {

        @Override
        public void setChanged() {
            super.setChanged();
        }

    }

}
