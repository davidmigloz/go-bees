/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;
import com.davidmiguel.gobees.monitoring.MonitoringFragment;

/**
 * Helper class to manage notification channels introduced in Android O, and create notifications.
 */
public class NotificationsHelper extends ContextWrapper {

    public static final String MONITORING_CHANNEL = "monitoring";

    private NotificationManager manager;

    /**
     * Registers notification channels, which can be used later by individual notifications.
     */
    public NotificationsHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createMonitoringChannel();
        }
    }

    /**
     * Get the notification manager.
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createMonitoringChannel() {
        NotificationChannel channel = new NotificationChannel(MONITORING_CHANNEL,
                getString(R.string.not_channel_monitoring), NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(getString(R.string.not_channel_monitoring_desc));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.setShowBadge(false);
        channel.enableLights(false);
        getManager().createNotificationChannel(channel);
    }

    /**
     * Get monitoring notification.
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     */
    public Notification getMonitoringNotification(long apiaryId, long hiveId) {
        // Intent to the monitoring activity (when the notification is clicked)
        Intent monitoringIntent = new Intent(getApplicationContext(), MonitoringActivity.class);
        monitoringIntent.putExtra(MonitoringFragment.ARGUMENT_APIARY_ID, apiaryId);
        monitoringIntent.putExtra(MonitoringFragment.ARGUMENT_HIVE_ID, hiveId);
        PendingIntent pMonitoringIntent = PendingIntent.getActivity(this, 0,
                monitoringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Create notification
        return new NotificationCompat.Builder(getApplicationContext(), MONITORING_CHANNEL)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(getString(R.string.app_name))
                .setContentText(getString(R.string.monitoring_notification_text))
                .setSmallIcon(R.drawable.ic_recording)
                .setContentIntent(pMonitoringIntent)
                .setOngoing(true).build();
    }
}
