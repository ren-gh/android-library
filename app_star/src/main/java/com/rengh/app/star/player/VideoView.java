
package com.rengh.app.star.player;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

import com.r.library.common.util.LogUtils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class VideoView extends SurfaceView {
    private static final String TAG = "VideoView";
    private Context context;
    private boolean isReady = false;
    private int position = 0;
    private Uri uri;
    private MediaPlayer player;

    private int width, height;

    public VideoView(Context context) {
        super(context);
        init(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        if (null == this.player) {
            this.player = new MediaPlayer();
        }
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                LogUtils.d(TAG, "surfaceCreated");
                width = getRootView().getWidth();
                height = getRootView().getHeight();
                isReady = true;
                if (player == null) {
                    return;
                }
                player.setDisplay(getHolder());
                onViewCreated();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                LogUtils.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                isReady = false;
                LogUtils.d(TAG, "surfaceDestroyed");
                if (player == null) {
                    return;
                }
                if (player.isPlaying()) {
                    position = player.getCurrentPosition();
                    LogUtils.d(TAG, "当前播放时间：" + position);
                    player.stop();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        LogUtils.d(TAG, "onConfigurationChanged() msg=" + message);
        changeVideoSize();
    }

    // 改变视频的尺寸自适应。
    public void changeVideoSize() {
        LogUtils.d(TAG, "changeVideoSize()");
        if (player == null) {
            return;
        }
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();

        LogUtils.d(TAG, "vw=" + videoWidth + ", vh=" + videoHeight
                + ", sw=" + width + ", sh=" + height);

        // 根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // 竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) width, (float) videoHeight / (float) height);
        } else {
            // 横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) height), (float) videoHeight / (float) width);
        }

        LogUtils.d(TAG, "max=" + max);

        // 视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        LogUtils.d(TAG, "vw=" + videoWidth + ", vh=" + videoHeight);

        // 无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, getRootView().getId());
        this.setLayoutParams(params);
    }

    public void setVideoPath(String path) {
        setVideoPath(Uri.parse(path));
    }

    public void setVideoPath(Uri uri) {
        if (player == null) {
            return;
        }
        this.uri = uri;
        LogUtils.i(TAG, "ready=" + isReady + ", uri=" + uri);
        if (isReady) {
            try {
                player.reset();
                setDataSource(uri);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataSource(String path) throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(path);
    }

    private void setDataSource(Uri uri) throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(context, uri);
    }

    private void setDataSource(FileDescriptor fileDescriptor) throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(fileDescriptor);
    }

    private void setDataSource(FileDescriptor fileDescriptor, long offset, long length)
            throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(fileDescriptor, offset, length);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDataSource(AssetFileDescriptor fileDescriptor) throws IOException,
            IllegalStateException, IllegalArgumentException {
        if (player == null) {
            return;
        }
        player.setDataSource(fileDescriptor);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDataSource(MediaDataSource mediaDataSource) throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(mediaDataSource);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataSource(Uri uri, Map<String, String> map) throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(context, uri, map);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataSource(Uri uri, Map<String, String> map, List<HttpCookie> cookieList)
            throws IOException {
        if (player == null) {
            return;
        }
        player.setDataSource(context, uri, map, cookieList);
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public boolean isLooping() {
        if (player != null) {
            return player.isLooping();
        }
        return false;
    }

    public int getDuration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    private void onViewCreated() {
        if (null != uri && !player.isPlaying()) {
            try {
                player.reset();
                setDataSource(uri);
                player.prepare();
                player.seekTo(position);
                start();
                LogUtils.d(TAG, "续播时间：" + position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    public void seekTo(int startTime) {
        if (player != null && player.isPlaying()) {
            player.seekTo(startTime);
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        if (player != null) {
            player.setOnPreparedListener(listener);
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (player != null) {
            player.setOnCompletionListener(listener);
        }
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        if (player != null) {
            player.setOnErrorListener(listener);
        }
    }

    public void release() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
