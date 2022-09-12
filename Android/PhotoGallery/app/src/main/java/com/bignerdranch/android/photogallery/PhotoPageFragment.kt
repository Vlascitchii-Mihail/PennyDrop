package com.bignerdranch.android.photogallery

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

private const val ARG_URI = "photo_page_url"

class PhotoPageFragment: VisibleFragment() {
    private lateinit var uri: Uri
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uri = arguments?.getParcelable(ARG_URI) ?: Uri.EMPTY
    }

    //put off Lint's warning
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_page, container, false)

        webView = view.findViewById(R.id.web_view)

        //creating a progress bar
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.max = 100

        //put on JavaScript
        //settings - WebSettings
        webView.settings.javaScriptEnabled = true

        //using WebView for opening the photo detail view
        //without WebViewClient() URS opens in browser
        webView.webViewClient = WebViewClient()

        //WebChromeClient() - app's chrome listener
        webView.webChromeClient = object : WebChromeClient() {

            //showing information about progressBar status
            /**
             * @param newProgress - progressBar's progress: Int
             */
            override fun onProgressChanged(webView: WebView, newProgress: Int) {
                if (newProgress == 100) progressBar.visibility = View.GONE
                else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }

            //Установите подзаголовок панели действий. Это будет отображаться,
            // только если установлено DISPLAY_SHOW_TITLE. Установите значение null,
            // чтобы полностью отключить субтитры.
            override fun onReceivedTitle(view: WebView, title: String?) {
                (activity as AppCompatActivity).supportActionBar?.subtitle = title
            }
        }

        //loading the page
        webView.loadUrl(uri.toString())
        return view
    }

    companion object {
        fun newInstance(uri: Uri): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
        }
    }
}