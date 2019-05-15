
package com.r.library.common.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;

public class ServiceHelper {
    public static ComponentName startService(Context context, Intent intent) {
        ComponentName componentName;
        if (VERSION.SDK_INT >= 26) {
            try {
                componentName = context.startService(intent);
            } catch (IllegalStateException var4) {
                componentName = context.startForegroundService(intent);
            }
        } else {
            componentName = context.startService(intent);
        }

        return componentName;
    }

    public static void setForegroundService(Service service, int iconResId, String channelName,
            String channelId, int id) {
        setForegroundService(service, service.getPackageName() + "." + channelName,
                channelId, iconResId, "", "", "",
                false, true, id);
    }

    public static void setForegroundService(Service service, String channelName, String channelId,
            int smallIcon, String title, String content, String desc, boolean autoCancel,
            boolean onGoing, int notificationId) {
        Notification notification;
        if (VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(desc);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(service, channelId);
            builder.setSmallIcon(smallIcon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(autoCancel)
                    .setOngoing(onGoing);
            NotificationManager notificationManager = service.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notification = builder.build();
        } else {
            notification = new Notification();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        }
        service.startForeground(notificationId, notification);
    }
}
