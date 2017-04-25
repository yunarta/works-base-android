package com.mobilesolutionworks.works.sample.rx;

import com.mobilesolutionworks.works.core.Controller;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class RxWorksControllerUtil {

    private RxWorksControllerUtil() {
        // No instance
    }

    public static Runnable wrap(Controller<?> controller, Action action) {
        return () -> controller.runOnUIWhenIsReady(() -> {
            try {
                action.run();
            } catch (Exception e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        });
    }

    public static <T> void safeConsume(Controller<?> controller, Consumer<T> consumer, T t) {
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
