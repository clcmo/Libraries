package com.clcmo.app.ui.fragments

import android.app.Fragment
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clcmo.app.R
import com.clcmo.app.interfaces.OnItemSelectedListener
import com.clcmo.app.objects.RSSConstants.bus
import com.clcmo.app.objects.RSSConstants.list
import com.clcmo.app.ui.RSSFeedActivity
import com.clcmo.app.ui.adapter.RSSItemAdapter
import com.clcmo.lib.RSSFeedProvider
import com.clcmo.lib.RSSItem
import com.squareup.otto.Subscribe


class MyListFragment : Fragment() {
    private var listener: OnItemSelectedListener? = null
    var adapter: RSSItemAdapter? = null
    var rssItems: MutableList<RSSItem>? = null
    var parseTask: ParseTask? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_rsslist_overview, container, false)
        val mRecyclerView: RecyclerView = view.findViewById(R.id.my_recycler_view)
        val mLayoutManager = GridLayoutManager(context, 2)

        mRecyclerView.layoutManager = mLayoutManager
        rssItems = list
        adapter = rssItems?.let { RSSItemAdapter(it, this) }
        mRecyclerView.adapter = adapter
        if (rssItems?.isEmpty() == true) {
            updateListContent()
        }
        bus?.register(this)
        return view
    }

    override fun onDestroyView() {
        bus?.unregister(this)
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when (context) {
            is OnItemSelectedListener -> context
            else -> throw ClassCastException("$context must implement MyListFragment.OnItemSelectedListener")
        }
    }

    fun updateDetail(uri: String?) {
        listener?.onRSSItemSelected(uri)
    }

    private fun updateListContent() {
        if (parseTask == null) {
            parseTask = ParseTask()
            parseTask?.setFragment(this)
            parseTask?.execute("http://www.vogella.com/article.rss")
        }
    }

    fun setListContent(result: MutableList<RSSItem>) {
        list = result
        rssItems?.clear()
        rssItems?.addAll(result)
        adapter?.notifyDataSetChanged()
        if (parseTask != null) {
            parseTask?.setFragment(null)
            parseTask = null
        }
    }

    class ParseTask : AsyncTask<String?, Void?, MutableList<RSSItem>>() {
        private var fragment: MyListFragment? = null

        fun setFragment(fragment: MyListFragment?) {
            this.fragment = fragment
        }

        override fun onPostExecute(result: MutableList<RSSItem>) {
            fragment?.setListContent(result)
        }

        override fun doInBackground(vararg p0: String?): MutableList<RSSItem>? {
            return RSSFeedProvider.parse(p0[0])
        }
    }

    @Subscribe
    fun update(`object`: Any?) {
        activity.runOnUiThread {
            list?.let { setListContent(it) }
            createNotification()
        }
    }

    private fun createNotification() {
        val notification = Notification.Builder(activity)
            .setContentTitle("Update").setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(
                PendingIntent.getActivity(
                    context, 0, Intent(
                        context,
                        RSSFeedActivity::class.java
                    ), PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}