package prism.mediscan

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_document.*


class DocumentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        init()
    }

    fun init() {
        val code = intent.extras["code"]
        val type = intent.extras["type"]

        val longtype = if (type == 'N') "notice" else "rcp";
        val url = "http://agence-prd.ansm.sante.fr/php/ecodex/$longtype/$type$code.htm";

        webView.getSettings().setJavaScriptEnabled(true);

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectCSS()
                super.onPageFinished(view, url)
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP) override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, request.url)
                view.context.startActivity(intent)

                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
                return true
            }
        }

        webView.loadUrl(url);
    }

    private fun injectCSS() {
        try {
            Log.d("Document", "Injecting CSS");
            val CSS = "'body { overflow-wrap: break-word; }'";
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    "style.innerHTML = $CSS;" +
                    "parent.appendChild(style);" +
//                    "\$('a[name=Ann3bNotice]').text = 'OK'" +
                    "})()")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
