package com.example.kotlinlab12

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class MyService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()

        // 使用 Handler 執行延遲任務，避免直接操作 Thread
        handler.postDelayed({
            try {
                // 建立 Intent 並啟動 SecActivity
                val intent = Intent(this, SecActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 3000) // 延遲 3 秒
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 指定返回值為 START_NOT_STICKY，服務結束後不會重啟
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // 此服務不支援綁定，因此返回 null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除所有未完成的延遲任務，避免記憶體洩漏
        handler.removeCallbacksAndMessages(null)
    }
}