package com.cpasnoor.weatherfeed.http;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class HttpApi {
    private static HttpApi instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Context ctx;

    private HttpApi(Context ctx) {
        this.ctx = ctx;
        requestQueue = Volley.newRequestQueue(this.ctx);
    }

    public static synchronized HttpApi getInstance(Context ctx) {
        if(instance == null) {
            instance = new HttpApi(ctx);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
