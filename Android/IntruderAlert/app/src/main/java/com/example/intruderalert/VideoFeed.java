package com.example.intruderalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;




public class VideoFeed extends AppCompatActivity {


    //private PlayerView playerView;

    private static final String SHARED_PREFS_NAME = "MySharedPrefs";
    private SharedPreferences sp;

    public WebView mWebView;



   // private String urlToload="http://192.168.43.27:5556/video_feed";



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feed);

        sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbaro);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setTitle("Camera Feed");





        mWebView = findViewById(R.id.webview);


        String urlToload = sp.getString("url", "http://192.168.43.27:5556/video_feed");

        mWebView.loadUrl(urlToload);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);




        mWebView.setWebChromeClient(new WebChromeClient() {



        });




        }

    public void logout(MenuItem item) {

        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.destroy();

        // Finish the current activity and return to MainActivity
        finish();
    }







}
