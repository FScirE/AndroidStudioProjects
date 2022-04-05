package com.example.myfirstandroidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val currentNum = findViewById<TextView>(R.id.textViewFirst)

        //Get old value
        //If intent extra message is empty set number to 0
        val extraMessage = intent.getStringExtra(EXTRA_MESSAGE)
        if (extraMessage != null) {
            currentNum.text = extraMessage
        }
        else {
            currentNum.text = "0"
        }

        //Toast Button
        findViewById<Button>(R.id.toastButton).setOnClickListener {
            val myToast = Toast.makeText(applicationContext, "It works!", Toast.LENGTH_SHORT)
            myToast.show()
        }

        //Count Button
        findViewById<Button>(R.id.countButton).setOnClickListener {
            val newNum = currentNum.text.toString().toInt() + 1
            currentNum.text = newNum.toString()
        }

        //Random Button
        findViewById<Button>(R.id.randomButton).setOnClickListener {
            sendRandomNumber()
        }
    }

    private fun sendRandomNumber() {
        val currentNum = findViewById<TextView>(R.id.textViewFirst).text
        val intent = Intent(this, RandomPage::class.java).apply {
            putExtra(EXTRA_MESSAGE, currentNum)
        }
        startActivity(intent)
    }
}