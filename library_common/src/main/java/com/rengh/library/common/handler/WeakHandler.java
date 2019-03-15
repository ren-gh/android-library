package com.rengh.library.common.handler;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by rengh on 18-2-6.
 */
public class WeakHandler extends Handler {
    private final WeakReference<WeakHandlerListener> weakReference;

    public WeakHandler() {
        super();
        weakReference = new WeakReference<WeakHandlerListener>(null);
    }

    public WeakHandler(WeakHandlerListener weakHandlerListener) {
        super();
        weakReference = new WeakReference<WeakHandlerListener>(weakHandlerListener);
    }

    public WeakHandler(WeakHandlerListener weakHandlerListener, Looper looper) {
        super(looper);
        weakReference = new WeakReference<WeakHandlerListener>(weakHandlerListener);
    }

    public WeakHandler(WeakHandlerListener weakHandlerListener, Looper looper, Callback callback) {
        super(looper, callback);
        weakReference = new WeakReference<WeakHandlerListener>(weakHandlerListener);
    }

    @Override
    public void handleMessage(Message msg) {
        WeakHandlerListener weakHandlerListener = weakReference.get();
        if (null != weakHandlerListener) {
            weakHandlerListener.process(msg);
        }
    }
}
