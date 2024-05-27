package com.deanrc.rcapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button1= findViewById<Button>(R.id.submitButton)
        val registerText = SpannableString("New User? Register Here")
        val registerTextView = findViewById<TextView>(R.id.registerTextView)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        registerText.setSpan(clickableSpan, 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerText.setSpan(ForegroundColorSpan(Color.parseColor("#0000ee")), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerTextView.text = registerText
        registerTextView.movementMethod = LinkMovementMethod.getInstance()
        button1.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }
    }
}