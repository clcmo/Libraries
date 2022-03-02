package com.clcmo.app.services

import android.app.IntentService
import android.content.Intent
import com.clcmo.app.objects.RSSConstants.RSS_UPDATE
import com.clcmo.app.objects.RSSConstants.bus
import com.clcmo.app.objects.RSSConstants.list
import com.clcmo.lib.RSSFeedProvider


class RSSDownloadServices : IntentService("RssDownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        val extras = intent?.extras
        val string = extras?.getString("url")
        list = RSSFeedProvider.parse(string)
        bus?.post(RSS_UPDATE)
    }

    companion object {
        var NOTIFICATION = "rssfeedupdated"
    }
}