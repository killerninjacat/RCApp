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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

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
        val staffIdBox = findViewById<EditText>(R.id.editTextStaffID)
        val passwordBox = findViewById<EditText>(R.id.editTextPassword)
        val submitButton= findViewById<Button>(R.id.submitButton)
        val url = URL("http://10.0.2.2:3000/api/mobile/getStaffProjects")
        submitButton.setOnClickListener {
            val staffId = staffIdBox.text.toString()
            val password = passwordBox.text.toString()
            Thread {
                val jsonInputString = JSONObject()
                    .put("staffID", staffId)
                    .put("password", password)
                    .toString()
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; utf-8")
                conn.doOutput = true

                OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
                    writer.write(jsonInputString)
                }

                val responseCode = conn.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = conn.inputStream.bufferedReader().readText()
                    val responseJson = JSONObject(response)
                    val content = responseJson.getString("content")
                    runOnUiThread {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(this, DataActivity::class.java)
                    intent.putExtra("content", content)
                    intent.putExtra("staffID", staffId)
                    startActivity(intent)
                }
                    else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                        runOnUiThread {
                            Toast.makeText(this, "Invalid staff ID or password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                 else {
                    runOnUiThread {
                        Toast.makeText(this, "Connection error!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}