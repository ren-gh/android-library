
package com.rengh.library.common.application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.rengh.library.common.net.LocalNetHelper;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.ThreadUtils;

import android.content.Context;
import android.os.Build;

/**
 * Created by rengh on 17-5-29.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public final String TAG = "CrashHandler";
    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    @SuppressWarnings("unused")
    private Context mContext = null;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            ThreadUtils.sleep(1000 * 3);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        LogUtils.e(TAG, getCrashInfo(ex));
        // 自动上传错误日志
        // 上报
        return true;
    }

    /**
     * 返回错误信息
     */
    private String getCrashInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("Base Info: ");
        sb.append("product=").append(Build.PRODUCT);
        sb.append("&brand=").append(Build.BRAND);
        sb.append("&board=").append(Build.BOARD);
        sb.append("&model=").append(Build.MODEL);
        sb.append("&user=").append(Build.USER);
        sb.append("&type=").append(Build.TYPE);
        sb.append("&id=").append(Build.ID);
        sb.append("&sdk=").append(Build.VERSION.SDK_INT);
        sb.append("&eth=").append(LocalNetHelper.getMac(LocalNetHelper.TYPE_ETH_0));
        sb.append("&wlan=").append(LocalNetHelper.getMac(LocalNetHelper.TYPE_WLAN_0));
        sb.append("&netDev=").append(LocalNetHelper.getIpName());
        sb.append("&netMac=").append(LocalNetHelper.getMac(LocalNetHelper.getIpName()));
        sb.append("$ip=").append(LocalNetHelper.getIp());
        sb.append("&imei=").append(LocalNetHelper.getIMEI(mContext));
        sb.append("&meid=").append(LocalNetHelper.getMEID(mContext));
        sb.append(" ");

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append("Crash Info: ").append(writer.toString());

        return sb.toString();
    }
}
