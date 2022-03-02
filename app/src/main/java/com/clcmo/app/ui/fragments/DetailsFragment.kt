package com.clcmo.app.ui.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.clcmo.app.objects.RSSConstants.URL


class DetailsFragment : Fragment() {
    private var webview: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        webview = WebView(activity)
        return webview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val link = bundle.getString(URL)
            setText(link)
        }
    }

    fun setText(url: String?) {
        // Set the scale factor
        webview?.setInitialScale(50)
        webview?.settings?.builtInZoomControls = true
        webview?.settings?.setSupportZoom(true)
        url?.let { webview?.loadUrl(it) }
    }
}