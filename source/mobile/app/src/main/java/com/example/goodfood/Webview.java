package com.example.goodfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webview = findViewById(R.id.webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }
}