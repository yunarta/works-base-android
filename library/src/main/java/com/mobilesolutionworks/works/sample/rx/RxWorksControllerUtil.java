package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.SimpleWorksController;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created with IntelliJ
 * Created by lucas
 * Date 26/03/15
 */

public class RxWorksControllerUtil {

    private RxWorksControllerUtil() {
        // No instance
    }

    public static Runnable wrap(SimpleWorksController<?> controller, Action action) {
        return () -> controller.runOnUIWhenIsReady(() -> {
            try {
                action.run();
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        });
    }

    public static <T> void safeConsume(SimpleWorksController<?> controller, Consumer<T> consumer, T t) {
        controller.runOnUIWhenIsReady(() -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        });
    }

}
