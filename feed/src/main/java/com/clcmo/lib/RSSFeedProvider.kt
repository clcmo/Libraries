package com.clcmo.lib

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.net.URL

object RSSFeedProvider {

    private const val PUB_DATE = "pubDate"
    private const val DESCRIPTION = "description"
    private const val CHANNEL = "channel"
    private const val LINK = "link"
    private const val TITLE = "title"
    private const val ITEM = "item"

    fun parse(rssFeed: String?): MutableList<RSSItem>? {
        val list: MutableList<RSSItem>? = ArrayList()
        val parser = Xml.newPullParser()
        var stream: InputStream? = null

        try {
            stream = URL(rssFeed).openConnection().getInputStream()
            parser.setInput(stream, null)
            var eventType = parser.eventType
            var done = false
            var item: RSSItem? = null
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                var name: String?
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {}
                    XmlPullParser.START_TAG -> {
                        name = parser.name
                        when {
                            name.equals(ITEM, ignoreCase = true) -> {
                                Log.i("new item", "Create new item")
                                item = RSSItem()
                            }
                            item != null -> {
                                when {
                                    name.equals(LINK, ignoreCase = true) -> {
                                        Log.i("Attribute", "setLink")
                                        item.link = parser.nextText()
                                    }
                                    name.equals(DESCRIPTION, ignoreCase = true) -> {
                                        Log.i("Attribute", "description")
                                        item.description = parser.nextText().trim { it <= ' ' }
                                    }
                                    name.equals(PUB_DATE, ignoreCase = true) -> {
                                        Log.i("Attribute", "date")
                                        item.pubDate = parser.nextText()
                                    }
                                    name.equals(TITLE, ignoreCase = true) -> {
                                        Log.i("Attribute", "title")
                                        item.title = parser.nextText().trim { it <= ' ' }
                                    }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        name = parser.name
                        Log.i("End tag", name)
                        when {
                            name.equals(ITEM, ignoreCase = true) && item != null -> {
                                Log.i("Added", item.toString())
                                list?.add(item)
                            }
                            name.equals(CHANNEL, ignoreCase = true) -> done = true
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return list
    }
}