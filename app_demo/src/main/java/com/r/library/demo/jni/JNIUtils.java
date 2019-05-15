
package com.r.library.demo.jni;

import android.content.Context;

public class JNIUtils {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native String getUid(Context context);
}
