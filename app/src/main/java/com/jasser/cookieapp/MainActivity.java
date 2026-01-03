package com.jasser.cookieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        EditText cookieInput = findViewById(R.id.cookieInput);
        Button btnJoin = findViewById(R.id.btnJoin);

        cookieInput.setText(sharedPref.getString("last_cookies", ""));

        btnJoin.setOnClickListener(v -> {
            String cookies = cookieInput.getText().toString();
            sharedPref.edit().putString("last_cookies", cookies).apply();
            startFacebookSession(cookies);
        });
    }

    private void startFacebookSession(String cookieString) {
        WebView webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        // إيهام فيسبوك أنك تستخدم متصفح كمبيوتر أو هاتف حديث
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36");

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        cookieManager.removeAllCookies(null);

        // تنظيف وحقن الكوكيز بشكل صحيح
        String[] pairs = cookieString.split(";");
        for (String pair : pairs) {
            if (pair.contains("=")) {
                cookieManager.setCookie("https://.facebook.com", pair.trim() + "; Domain=.facebook.com; Path=/; Secure; HttpOnly");
            }
        }

        cookieManager.flush();
        
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.facebook.com");
    }
}
