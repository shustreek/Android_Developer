package ru.otus.activitydemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.open_dial_btn).setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+79031234567")))
        }
        findViewById<Button>(R.id.send_msg_btn).setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, "textMessage")
                    .setType("text/plain")
            )
        }
    }
}