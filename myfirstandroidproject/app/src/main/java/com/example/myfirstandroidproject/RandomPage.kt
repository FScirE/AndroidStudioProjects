package com.example.myfirstandroidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class RandomPage : AppCompatActivity() {
    private var countNum: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.randomnumber_page)

        // Get the Intent that started this activity and extract the string
        countNum = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        findViewById<TextView>(R.id.randomNumberText).apply {
            val r = Random(System.nanoTime())
            val newNum = r.nextInt(0, 1000).toString()
            text = newNum
        }

        //Return Button
        findViewById<Button>(R.id.returnButton).setOnClickListener {
            returnToMain()
        }
    }

    private fun returnToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, countNum)
        }
        startActivity(intent)
    }
}