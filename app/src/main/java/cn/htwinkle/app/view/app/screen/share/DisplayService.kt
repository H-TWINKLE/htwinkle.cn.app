/*
 * Copyright (C) 2023 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.htwinkle.app.view.app.screen.share

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import cn.htwinkle.app.R
import com.pedro.common.ConnectChecker
import com.pedro.common.VideoCodec
import com.pedro.library.generic.GenericDisplay


/**
 * Basic RTMP/RTSP service streaming implementation with camera2
 */
class DisplayService : Service() {

    private var displayBinder: DisplayBinder = DisplayBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return displayBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "RTP Display service create")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }
        genericDisplay = GenericDisplay(baseContext, true, connectChecker)
        genericDisplay.glInterface?.setForceRender(true)
        INSTANCE = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "RTP Display service started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "RTP Display service destroy")
        stopStream()
        INSTANCE = null
    }

    class DisplayBinder : Binder() {
    }

    private fun keepAliveTrick() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_light_blue_a700_36dp)
            .setSilent(true)
            .setOngoing(false)
            .build()
        startForeground(1, notification)
    }

    companion object COMPANION {
        private const val TAG = "DisplayService"
        private const val channelId = "rtpDisplayStreamChannel"
        const val notifyId = 1303813

        var width = 0
        var height = 0
        var dpi = 0

        lateinit var connectChecker: ConnectChecker
        var INSTANCE: DisplayService? = null
    }

    private var notificationManager: NotificationManager? = null
    private lateinit var genericDisplay: GenericDisplay

    fun setVideoCodec(codec: VideoCodec) {
        genericDisplay.setVideoCodec(codec)
    }

    fun sendIntent(): Intent? {
        return genericDisplay.sendIntent()
    }

    fun isStreaming(): Boolean {
        return genericDisplay.isStreaming
    }

    fun stopStream() {
        if (genericDisplay.isStreaming) {
            genericDisplay.stopStream()
            notificationManager?.cancel(notifyId)
        }
    }

    fun showNotification(text: String) {
        val notification = NotificationCompat.Builder(baseContext, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_light_blue_a700_36dp)
            .setContentTitle("屏幕分享")
            .setContentText(text)
            .setOngoing(false)
            .build()
        notificationManager?.notify(notifyId, notification)
    }

    fun prepareStreamRtp(resultCode: Int, data: Intent) {
        keepAliveTrick()
        genericDisplay.setIntentResult(resultCode, data)
    }

    fun startStreamRtp(endpoint: String) {
        if (!genericDisplay.isStreaming) {
            //genericDisplay.prepareVideo(width, height, 60, width * height, 0, dpi)
            if (genericDisplay.prepareVideo(width, height, 60, width * height, 0, dpi)) {
                genericDisplay.startStream(endpoint)
            }
        } else {
            showNotification("You are already streaming :(")
        }
    }
}