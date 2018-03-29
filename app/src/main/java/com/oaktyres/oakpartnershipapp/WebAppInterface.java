package com.oaktyres.oakpartnershipapp;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by levinm on 27/02/2018.
 */

class WebAppInterface {

    Activity mContext;

    public WebAppInterface(Activity c) {
            mContext = c;
        }
        @JavascriptInterface
        public void onData(String value){
            //you will get HAI message in here
            Log.v("javascript", value);
        }
    }

