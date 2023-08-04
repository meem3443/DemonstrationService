package com.police.demonstrationservice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.police.demonstrationservice.R;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");

            }
        });
        //최초 웹뷰르 로드합니다.
        webView.loadUrl("https://dapi-a60ca.web.app");
    }

    private class BridgeInterface {
        @JavascriptInterface

        public void processDATA(String data){
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK,intent);
            finish();

        }
    }
}