package cn.htwinkle.app.componet;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.loopj.android.image.SmartImage;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NotCacheWebImage implements SmartImage {

    private static final String TAG = "NotCacheWebImage";
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;
    private String url;

    public NotCacheWebImage(String var1) {
        this.url = var1;
    }

    @Override
    public Bitmap getBitmap(Context var1) {
        Bitmap var2 = null;
        if (this.url != null) {
            var2 = this.getBitmapFromUrl(this.url);
        }
        return var2;
    }

    private Bitmap getBitmapFromUrl(String var1) {
        Bitmap var2 = null;
        try {
            URLConnection var3 = (new URL(var1)).openConnection();
            var3.setConnectTimeout(CONNECT_TIMEOUT);
            var3.setReadTimeout(READ_TIMEOUT);
            var2 = BitmapFactory.decodeStream((InputStream) var3.getContent());
        } catch (Exception var4) {
            Log.e(TAG, "getBitmapFromUrl: " + var4.getMessage());
        }
        return var2;
    }
}
