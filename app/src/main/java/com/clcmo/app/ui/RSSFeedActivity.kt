package com.clcmo.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.clcmo.app.R
import com.clcmo.app.interfaces.OnItemSelectedListener
import com.clcmo.app.objects.RSSConstants.EXTRA_URL
import com.clcmo.app.services.RSSDownloadServices
import com.clcmo.app.ui.fragments.DetailsFragment
import com.clcmo.app.ui.fragments.MyListFragment


class RSSFeedActivity : Activity(), OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tb = findViewById<View>(R.id.toolbar) as Toolbar
        setActionBar(tb)
        if (resources.getBoolean(R.bool.twoPaneMode)) {
            return
        }
        if (savedInstanceState != null) {
            fragmentManager.executePendingTransactions()
            val fragmentById = fragmentManager.findFragmentById(R.id.fragment_container)
            if (fragmentById != null) {
                fragmentManager.beginTransaction()
                    .remove(fragmentById).commit()
            }
        }
        val listFragment = MyListFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, listFragment).commit()
    }

    override fun onRSSItemSelected(link: String?) {
        when {
            resources.getBoolean(R.bool.twoPaneMode) -> {
                val fragment: DetailsFragment = fragmentManager
                    .findFragmentById(R.id.detailFragment) as DetailsFragment
                fragment.setText(link)
            }
            else -> {
                val newFragment = DetailsFragment()
                val args = Bundle()
                args.putString(EXTRA_URL, link)
                newFragment.arguments = args
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, newFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val tb = findViewById<View>(R.id.toolbar) as Toolbar
        tb.inflateMenu(R.menu.mainmenu)
        tb.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                val i = Intent(this, RSSDownloadServices::class.java)
                i.putExtra("url", "http://www.vogella.com/article.rss")
                startService(i)
                return true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_network -> {
                val wirelessIntent = Intent("android.settings.WIRELESS_SETTINGS")
                startActivity(wirelessIntent)
                return true
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}
