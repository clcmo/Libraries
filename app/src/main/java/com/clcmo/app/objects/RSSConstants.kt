package com.clcmo.app.objects

import com.clcmo.lib.RSSItem
import com.squareup.otto.Bus

object RSSConstants {
    const val URL = "url"
    const val RSS_UPDATE = "neuedaten"
    const val RSS_FILE = "rssitems.json"

    var list: MutableList<RSSItem>? = null
    var bus: Bus? = null

    const val EXTRA_URL = "url"
}