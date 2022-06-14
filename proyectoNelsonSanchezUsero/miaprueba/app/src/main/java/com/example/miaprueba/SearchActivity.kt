package com.example.miaprueba

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.SearchView
import com.example.miaprueba.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding

    val url="https://www.spartasportcenter.com"
    var google = "https://www.google.es/"
    var busqueda = "search?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        web()
    }

    private fun web() {
        binding.webView.webChromeClient = object : WebChromeClient(){

        }
        binding.webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //return super.shouldOverrideUrlLoading(view, request)
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.swipeRefresh.isRefreshing=true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipeRefresh.isRefreshing=false
            }
        }

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true


        binding.webView.loadUrl(url)
    }

    private fun setListeners() {


        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        binding.barrasearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let{
                    if(URLUtil.isValidUrl(it)){
                        binding.webView.loadUrl(it)
                    }
                    else{
                        binding.webView.loadUrl("$google$busqueda$it")
                    }
                }


                val teclado = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                teclado.hideSoftInputFromWindow(binding.principal.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })


    }

}