package com.example.kotlinlab13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvMsg: TextView

    // 建立 BroadcastReceiver 作為匿名內部類別
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra("msg")
            tvMsg.text = message ?: "未收到訊息"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMsg = findViewById(R.id.tvMsg)

        // 設定 WindowInsets 處理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 設定按鈕監聽事件
        findViewById<Button>(R.id.btnMusic).setOnClickListener { registerChannel("music") }
        findViewById<Button>(R.id.btnNew).setOnClickListener { registerChannel("new") }
        findViewById<Button>(R.id.btnSport).setOnClickListener { registerChannel("sport") }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除廣播接收器註冊
        unregisterReceiver(receiver)
    }

    private fun registerChannel(channel: String) {
        // 創建 IntentFilter 以監聽指定的廣播頻道
        val intentFilter = IntentFilter(channel)

        // 註冊廣播接收器，兼容 Android 版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }

        // 啟動服務並傳遞頻道資訊
        Intent(this, MyService::class.java).apply {
            putExtra("channel", channel)
            startService(this)
        }
    }
}
