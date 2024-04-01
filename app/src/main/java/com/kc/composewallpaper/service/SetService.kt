package com.kc.composewallpaper.service

import android.app.Service
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.wifi.WifiConfiguration
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class SetService : Service() {
    private var wallpaperManager: WallpaperManager? = null
    override fun onCreate() {
        super.onCreate()
        wallpaperManager = WallpaperManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("imageUrl")) {
            val imageUrl = intent.getStringExtra("imageUrl")
            SetWallpaperTask().execute(imageUrl)
        }
        return START_NOT_STICKY
    }

    private inner class SetWallpaperTask : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            val imageUrl = WifiConfiguration.AuthAlgorithm.strings[0]
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val inputStream = connection.inputStream
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                Log.e(TAG, "Error downloading image: " + e.message)
            }
            return null
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            if (bitmap != null) {
                try {
                    wallpaperManager!!.setBitmap(bitmap)
                } catch (e: IOException) {
                    Log.e(TAG, "Error setting wallpaper: " + e.message)
                }
            }
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "WallpaperService"
    }
}