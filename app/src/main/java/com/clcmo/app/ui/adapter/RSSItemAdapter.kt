package com.clcmo.app.ui.adapter

import com.bumptech.glide.Glide

import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.clcmo.app.R
import com.clcmo.app.ui.fragments.MyListFragment
import com.clcmo.lib.RSSItem
import java.util.*


class RSSItemAdapter(private val rssItems: List<RSSItem>,
                     private val myListFragment: MyListFragment
) : RecyclerView.Adapter<RSSItemAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View? = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rowlayout, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        var mainLayout: View? = v
        var txtHeader: TextView = v?.findViewById(R.id.rsstitle)!!
        var txtFooter: TextView = v?.findViewById(R.id.rssurl)!!
        var imageView: ImageView = v?.findViewById(R.id.icon) as ImageView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rssItem: RSSItem = rssItems[position]
        holder.txtHeader.text = rssItem.title
        holder.txtFooter.text = rssItem.link
        holder.mainLayout?.setOnClickListener { myListFragment.updateDetail(rssItem.link) }


        val r = Random()
        val i: Int = r.nextInt(10)
        Glide
            .with(myListFragment)
            .load("http://lorempixel.com/400/200/sports/$i/")
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = rssItems.size

}