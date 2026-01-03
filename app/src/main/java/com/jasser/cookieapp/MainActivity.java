package com.jasser.cookieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText cookieInput;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        cookieInput = findViewById(R.id.cookieInput);
        Button btnJoin = findViewById(R.id.btnJoin);

        // استرجاع الكوكيز المحفوظة سابقاً إن وجدت
        String savedCookies = sharedPref.getString("last_cookies", "");
        cookieInput.setText(savedCookies);

        btnJoin.setOnClickListener(v -> {
            String cookies = cookieInput.getText().toString();
            // حفظ الكوكيز الجديدة
            sharedPref.edit().putString("last_cookies", cookies).apply();
            injectCookiesAndOpen(cookies);
        });
    }

    private void injectCookiesAndOpen(String cookieString) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookies(null);
        
        String[] pairs = cookieString.split(";");
        for (String pair : pairs) {
            cookieManager.setCookie("https://www.facebook.com", pair.trim());
        }

        WebView webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.facebook.com");
    }
}
