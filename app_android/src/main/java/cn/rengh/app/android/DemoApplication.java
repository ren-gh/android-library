package cn.rengh.app.android;

import android.app.Application;
import android.content.Intent;

//import com.tencent.tinker.anno.DefaultLifeCycle;
//import com.tencent.tinker.loader.app.DefaultApplicationLike;
//import com.tencent.tinker.loader.shareutil.ShareConstants;
//
//@DefaultLifeCycle(
//        application = "cn.rengh.app.android.DemoApplication",
//        flags = ShareConstants.TINKER_ENABLE_ALL)
public class DemoApplication extends Application {

//    public DemoApplication() {
//        super(
//                //tinkerFlags, which types is supported
//                //dex only, library only, all support
//                ShareConstants.TINKER_ENABLE_ALL,
//                // This is passed as a string so the shell application does not
//                // have a binary dependency on your ApplicationLifeCycle class.
//                "cn.rengh.app.android.DemoApplication");
//    }
//
//    public DemoApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
