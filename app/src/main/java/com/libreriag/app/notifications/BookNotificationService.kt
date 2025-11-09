package com.libreriag.app.notifications

import android.content.Context

object BookNotificationService {

    fun notifyBookAdded(context: Context, title: String) {
        NotificationHelper.showBookAddedNotification(context, title)
    }
}
