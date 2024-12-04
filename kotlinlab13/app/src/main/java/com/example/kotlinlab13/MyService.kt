package com.example.kotlinlab13

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    private var channel: String? = null
    private var workerThread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 取得傳入的頻道資訊
        channel = intent?.getStringExtra("channel")

        // 發送歡迎廣播訊息
        channel?.let { sendChannelMessage(it, isWelcome = true) } ?: sendErrorMessage()

        // 如果先前的執行緒仍在運行，則中斷它
        workerThread?.takeIf { it.isAlive }?.interrupt()

        // 建立並啟動新執行緒
        workerThread = Thread {
            try {
                Thread.sleep(3000) // 延遲三秒後發送後續訊息
                channel?.let { sendChannelMessage(it, isWelcome = false) } ?: sendErrorMessage()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.apply { start() }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // 發送指定頻道的訊息
    private fun sendChannelMessage(channel: String, isWelcome: Boolean) {
        val message = when (channel) {
            "music" -> if (isWelcome) "歡迎來到音樂頻道" else "即將播放本月TOP10音樂"
            "new" -> if (isWelcome) "歡迎來到新聞頻道" else "即將為您提供獨家新聞"
            "sport" -> if (isWelcome) "歡迎來到體育頻道" else "即將播報本週NBA賽事"
            else -> "頻道錯誤"
        }
        sendBroadcast(Intent(channel).putExtra("msg", message))
    }

    // 發送錯誤訊息
    private fun sendErrorMessage() {
        sendBroadcast(Intent("error").putExtra("msg", "頻道錯誤"))
    }
}
