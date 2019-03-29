
package com.rengh.library.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ServiceUtils {
    public static ComponentName startService(Context context, Intent intent) {
        ComponentName componentName;
        if (Build.VERSION.SDK_INT >= 26) {
            componentName = context.startForegroundService(intent);
        } else {
            componentName = context.startService(intent);
        }
        return componentName;
    }
}
