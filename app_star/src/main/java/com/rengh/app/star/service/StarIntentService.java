
package com.rengh.app.star.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.r.library.common.application.CrashHandler;
import com.r.library.common.util.LogUtils;

public class StarIntentService extends IntentService {
    private static final String TAG = "StarIntentService";
    private static final String KEY_OF_INTENT_PARAMS = "params";

    public static final String ACTION_INIT = "init";

    public static void startIntentService(Context context, String action, String params) {
        if (TextUtils.isEmpty(action)) {
            return;
        }
        Intent intent = new Intent(action);
        if (!TextUtils.isEmpty(params)) {
            intent.putExtra(KEY_OF_INTENT_PARAMS, params);
        }
        intent.setClass(context, StarIntentService.class);
        context.startService(intent);
    }

    public StarIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String params = intent.getStringExtra(KEY_OF_INTENT_PARAMS);
            if (ACTION_INIT.equals(action)) {
                CrashHandler.getInstance().init(this.getApplicationContext());
            }
            LogUtils.i(TAG, "Action: " + action + ", params: " + params);
        }
    }

}
