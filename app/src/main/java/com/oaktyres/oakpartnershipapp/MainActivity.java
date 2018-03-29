package com.oaktyres.oakpartnershipapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView myWebView;
    private ProgressBar progressbar;

    String rewardspoints;
    String BDFText;
    String DSPText;
    Button rewards;
    Button DSP;
    Button BDF;
    ImageView loadingsplash;
    String login;
    String key;

    String pageurl;

    Boolean loggedin = false;

    String checkbrand =

            "javascript: brand();" +
                "function brand() {" +
                "var LS = document.getElementsByClassName('info-block dealer-support-total hidden-xs landsail')[0];" +
                "var EV = document.getElementsByClassName('info-block dealer-support-total hidden-xs evergreen')[0];" +
                "var DV = document.getElementsByClassName('info-block dealer-support-total hidden-xs davanti')[0];" +
                "if (LS != null){" +
                "console.log('LSAIL');" +
                "document.cookie = 'Brand=LS';" +
                "    } " +
                "if (EV != null){" +
                "console.log('EVER');" +
                "document.cookie = 'Brand=EV';" +
                "    } " +
                "if (DV != null){" +
                "console.log('DAV');" +
                "document.cookie = 'Brand=DV';" +
                "    }" +
                "}";

    String Runjava =

            "javascript: function name() { var node = document.getElementsByClassName('info-block rewards-total hidden-xs')[0]; var node2 = " +
                    "document.getElementsByClassName('info-block bdf-total hidden-xs')[0]; " +
                    "var node3 = document.getElementsByClassName('info-block dealer-support-total hidden-xs evergreen')[0]; " +
                    "textContent = node.textContent; textContent2 = node2.textContent; textContent3 = node3.textContent; " +
                    "var len = textContent.trim(); " +
                    "var len2 = textContent2.trim(); " +
                    "var len3 = textContent3.trim(); " +
                    "document.cookie = 'reward=' + len; " +
                    "document.cookie = 'BDF=' +len2; document.cookie = 'DSP=' + len3;} name();";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();

        login = extras.getString("KEY_USER");
        key = extras.getString("KEY_PASSWORD");

        myWebView = (WebView) findViewById(R.id.siteview);
        myWebView.loadUrl("https://oak-partnership.co.uk/Account/Login?ReturnUrl=%2F&ReturnUrl=%2F");

        progressbar = (ProgressBar) findViewById(R.id.progressBar2);
        loadingsplash = (ImageView) findViewById(R.id.imgSplash);

        CookieManager.getInstance().setAcceptCookie(true);

        rewards = (Button) findViewById(R.id.rewards);
        BDF = findViewById(R.id.BDF);
        DSP = findViewById(R.id.DSP);

        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        myWebView.setWebContentsDebuggingEnabled(true);



        myWebView.setVisibility(View.GONE);
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                myWebView.setVisibility(View.INVISIBLE);
                progressbar.setVisibility(View.VISIBLE);
                loadingsplash.setVisibility(View.VISIBLE);

                Log.d("MyApp", "Moving to page " + url);
                Log.d("User attempt", "User: " + login + " Is attempting to login");

            }

            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                Log.i("URL: ", url);

                if (myWebView.getProgress() == 100 && loggedin == true) {
                    Log.d("Javascripting", "Loading The Javascript now");

                    pageurl = url;
                    myWebView.loadUrl(Runjava);
                    myWebView.loadUrl(checkbrand);

                    String cookies = CookieManager.getInstance().getCookie(url);
                    Log.d("All the Cookies", "All the cookies in a string:" + cookies);

                    rewardspoints = (getCookie(url, "reward"));
                    BDFText = (getCookie(url, "BDF"));
                    DSPText = (getCookie(url, "DSP"));

                    brandcolour();

                    rewards.setText(rewardspoints);
                    BDF.setText(BDFText);
                    DSP.setText(DSPText);

                    myWebView.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    loadingsplash.setVisibility(View.GONE);

                }

                if (login == null || key == null || url == "https://oak-partnership.co.uk/Account/LogOff?Length=0" || url == "https://oak-partnership.co.uk/Account/Login") {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


                if (loggedin == false) {

                    Log.d("Javascripting", "Logging in using JS");
                    myWebView.loadUrl(
                            "javascript:(function() { " +
                                    "var TheTextBox = document.getElementById('Email');" +
                                    "TheTextBox.value = ('" + login + "');" +
                                    "var Passwordbox = document.getElementById('Password');" +
                                    "Passwordbox.value = ('" + key + "');" +
                                    "document.getElementsByClassName('button')[0].click();" +
                                    "})()");
                    loggedin = true;
                }
            }
        });
    }

    public void brandcolour() {

        String brandcookie;
        brandcookie = getCookie(pageurl, "Brand");

        String brand = brandcookie.replaceAll("\\s+","");

        Log.v("Brand", "Brand= "+brand);

        if (brand.length() > 1){

            Log.v("Brand", "ENTERED STATEMENT");

            if (brand.equals("EV")) {
                Log.v("Brand", "Make it Green");
                DSP.setBackgroundColor(getResources().getColor((R.color.Evergreen)));
            }

            if (brand.equals("LS")){
                Log.v("Brand", "Make it Blue");
                DSP.setBackgroundColor(getResources().getColor(R.color.Landsail));
            }

            if (brand.equals("DV")){
                Log.v("Brand", "Make it Orange");
                DSP.setBackgroundColor(getResources().getColor(R.color.Davanti));
            }

            else if (brand == null) {
                return;
            }
        }
    }

    public String getCookie(String siteName,String CookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(CookieName)){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {

    }

    public MainActivity() {
        super();
    }


}
