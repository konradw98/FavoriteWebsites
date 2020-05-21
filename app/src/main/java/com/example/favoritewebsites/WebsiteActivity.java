package com.example.favoritewebsites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebsiteActivity extends AppCompatActivity {
    int urlId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website2);

        Intent intent= getIntent();
        urlId=intent.getIntExtra("urlId",-1);

        WebView webView=findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if(urlId!=-1){
            Log.i("URL", MainActivity.urls.get(urlId));
        webView.loadUrl(MainActivity.urls.get(urlId));
        }
        else{
            Toast toast=Toast. makeText(getApplicationContext(),"ERROR (with intent)!",Toast. LENGTH_LONG);
            toast.show();
        }


    }
}
