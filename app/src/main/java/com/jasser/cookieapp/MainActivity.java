package com.jasser.cookieapp;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText cookieInput = findViewById(R.id.cookieInput);
        Button btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(v -> {
            String cookies = cookieInput.getText().toString();
            injectCookiesAndOpen(cookies);
        });
    }

    private void injectCookiesAndOpen(String cookieString) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        
        // تقسيم الكوكيز وحقنها
        String[] pairs = cookieString.split(";");
        for (String pair : pairs) {
            cookieManager.setCookie("https://www.facebook.com", pair);
        }

        WebView webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.facebook.com");
    }
}
