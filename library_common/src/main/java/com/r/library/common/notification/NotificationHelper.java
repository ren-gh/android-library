
package com.r.library.common.notification;

import static android.content.Context.NOTIFICATION_SERVICE;

import java.util.List;

import com.r.library.common.util.BitmapUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {
    private static NotificationHelper instanse;
    private Context mContext = null;
    private NotificationManager mManager = null;

    public synchronized static NotificationHelper getInstance() {
        if (null == instanse) {
            instanse = new NotificationHelper();
        }
        return instanse;
    }

    public NotificationHelper init(Context context) {
        mContext = context.getApplicationContext();
        mManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        return instanse;
    }

    public NotificationHelper initChannels(List<NotificationChannel> channelList) {
        for (NotificationChannel channel : channelList) {
            initChannel(channel);
        }
        return instanse;
    }

    public NotificationHelper initChannel(NotificationChannel channel) {
        if (Build.VERSION.SDK_INT >= 26) {
            mManager.createNotificationChannel(channel);
        }
        return instanse;
    }

    public NotificationHelper initChannelIds(List<String> channelIdList) {
        for (String channelId : channelIdList) {
            initChannelId(channelId);
        }
        return instanse;
    }

    public NotificationHelper initChannelId(String channelId) {
        if (Build.VERSION.SDK_INT >= 26) {
            // 用户可以看到的通知渠道的名字.
            CharSequence name = channelId;
            // 用户可以看到的通知渠道的描述
            String description = channelId;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            // 配置通知渠道的属性
            channel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {
                    0, 200, 100, 200
            });
            mManager.createNotificationChannel(channel);
        }
        return instanse;
    }

    /**
     * 显示通知。
     *
     * @param title String 通知标题
     * @param content String 通知内容
     * @param intent PendingIntent 点击后的意图
     * @param smallIcon Int 状态栏图片资源ID（Alpha层Icon，即白色图标）
     * @param largeIcon Int 通知栏图标资源ID
     * @param color Color Int 通知栏图标右下角小图标颜色，色值，不是资源ID
     */
    public NotificationCompat.Builder getDefBuilder(String channelId,
            String title,
            String content,
            PendingIntent intent,
            int smallIcon,
            int largeIcon,
            int color) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapUtils.readBitMap(mContext, largeIcon))
                .setColor(color);
        if (null != intent) {
            builder = builder.setContentIntent(intent);
        }
        if (Build.VERSION.SDK_INT < 26) {
            builder = builder.setDefaults(Notification.DEFAULT_ALL);
        }
        return builder;
    }

    /**
     * 显示通知。
     *
     * @param id 通知ID
     * @param builder NotificationCompat.Builder getDefBuilder()
     */
    public NotificationHelper showNotification(int id, NotificationCompat.Builder builder) {
        mManager.notify(id, builder.build());
        return instanse;
    }
}
