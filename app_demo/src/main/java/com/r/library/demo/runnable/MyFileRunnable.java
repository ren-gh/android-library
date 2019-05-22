
package com.r.library.demo.runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

import com.r.library.common.util.FileUtils;
import com.r.library.common.util.LogUtils;

public class MyFileRunnable implements Runnable {
    private final String TAG = "MyFileRunnable";
    private Context mContext;

    private String mPath_0 = "0.txt";
    private String mPath_1 = "1.txt";

    public MyFileRunnable(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        String allTxt = FileUtils.getContent(FileUtils.getAssetsFileInputStream(mContext, mPath_0));
        String[] arr = allTxt.split("\n");

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets().open(mPath_1)));
            String line;
            int k = 0;
            int i = 0;
            int j = 0;
            StringBuffer buffer = new StringBuffer();
            StringBuffer bufferErr = new StringBuffer();
            while ((line = in.readLine()) != null) {
                k++;
                boolean contains = false;
                int n;
                for (n = 0; n < arr.length; n++) {
                    String tmp = arr[n];
                    if (line.equals(tmp)) {
                        contains = true;
                        break;
                    }
                }
                n++;
                if (contains) {
                    i++;
                    String right = "第 " + k + " 条：" + line + " 存在! " + "行号：" + n;
                    buffer.append(right).append("\n");
                    LogUtils.pl(TAG, right);
                } else {
                    j++;
                    String error = "第 " + k + " 条：" + line + " 不存在! " + "行号：" + n + " Error!!!";
                    buffer.append(error).append("\n");
                    bufferErr.append(error).append("\n");
                    LogUtils.pl(TAG, error);
                }
            }
            String allResult = "比对日志：\n" + buffer.toString();
            FileUtils.write("/sdcard/比对结果.txt", allResult, false);

            String less = "缺少内容：\n" + bufferErr.toString();
            FileUtils.write("/sdcard/比对结果.txt", less, true);

            String result = "比对结束，共比对 " + k + " 条数据！" +
                    "其中 " + i + " 条存在！" + "缺少 " + j + " 条！";
            LogUtils.i(TAG, result);
            FileUtils.write("/sdcard/比对结果.txt", result, true);
        } catch (IOException e) {
            LogUtils.e(TAG, e.toString());
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LogUtils.e(TAG, e.toString());
            } catch (Exception e) {
                LogUtils.e(TAG, e.toString());
            }
        }
    }
}
