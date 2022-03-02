package com.clcmo.app.applications

import android.app.Application
import android.util.Log
import com.clcmo.app.objects.RSSConstants.RSS_FILE
import com.clcmo.app.objects.RSSConstants.bus
import com.clcmo.app.objects.RSSConstants.list
import com.clcmo.lib.RSSItem
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.otto.ThreadEnforcer
import com.squareup.otto.Bus
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.reflect.Type

class RSSApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        bus = Bus(ThreadEnforcer.ANY)
        list = ArrayList()
        val buffer = StringBuffer()
        try {
            BufferedReader(InputStreamReader(openFileInput(RSS_FILE))).use { input ->
                var line: String?
                while (input.readLine().also { line = it } != null) buffer.append(line)
            }
        } catch (ex: Exception) {
            Log.ERROR
        }
        if (buffer.length > 0) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<RSSItem?>?>() {}.type
            val fromJson: List<RSSItem> = gson.fromJson(buffer.toString(), type)
            list?.addAll(fromJson)
        }
    }
}

