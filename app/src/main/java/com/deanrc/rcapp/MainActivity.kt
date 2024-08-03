package com.deanrc.rcapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val forgotButton = findViewById<TextView>(R.id.forgotPassword)
        val baseUrl="https://rcpms.nitt.edu"
        forgotButton.setOnClickListener {
            Toast.makeText(this, "Reset your password in the R&C website", Toast.LENGTH_SHORT)
                .show()
        }
        val url = URL("$baseUrl/api/mobile/getStaffProjects")
        submitButton.setOnClickListener {
            val staffId = staffIdBox.text.toString()
            val password = passwordBox.text.toString()
            if(staffId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Thread {
                try {
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
                        //val content = "[{\"id\":{\"value\":\"9\"},\"14. Amount Received Date\":{\"value\":\"04-12-2017\"},\"01. S.No.\":{\"value\":\"1\"},\"05. Name\":{\"value\":\"HOD\"},\"15. Amount Received till Date\":{\"value\":\"65,00,000\"},\"03. Staff ID\":{\"value\":\"1208\"},\"08. Funding Agency\":{\"value\":\"DST-FIST\"},\"09. Account Type\":{\"value\":\"Project\"},\"19. Equipment Balance\":{\"value\":\"14,37,760\"},\"06. Mail ID\":{\"value\":\"michael@nitt.edu\"},\"11. Duration\":{\"value\":\"5 Years\"},\"13. Overhead %\":{\"value\":\"0%\"},\"16. Equipment Cr\":{\"value\":\"65,00,000\"},\"12. Sanctioned Amount\":{\"value\":\"93,00,000\"},\"07. Title of the Project\":{\"value\":\"To Strengthen the Post Graduate Teaching and Research Facilities in the Department\"},\"10. Sanction Order Number\":{\"value\":\"SR\\/FST\\/ETI-412\\/2016, dt. 22-11-2017\"},\"94. Total Credit\":{\"value\":\"65,00,000\"},\"95. Total Debit\":{\"value\":\"50,62,240\"},\"96. Closing Balance\":{\"value\":\"14,37,760\"},\"17. Equipment Dr\":{\"value\":\"50,62,240\"},\"04. Dep\":{\"value\":\"CA\"},\"18. Equipment Process\":{\"value\":\"0\"},\"staff_ID\":{\"value\":\"1208\"},\"99a. Expenditure Details\":{\"value\":\"387.pdf\",\"path\":\"\\/9\\/64.pdf\"},\"97. Sanction Order\":{\"value\":\"387 - SO.pdf\",\"path\":\"\\/9\\/67.pdf\"},\"99. Utilization Certificate\":{\"value\":\"387 - UC - 1&2.pdf\",\"path\":\"\\/9\\/80.pdf\"}}]"
                        Log.d("content", content)
                        runOnUiThread {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        }
                        val intent = Intent(this, DataActivity::class.java)
                        intent.putExtra("content", content)
                        intent.putExtra("staffID", staffId)
                        startActivity(intent)
                    } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Invalid staff ID or password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Connection error!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Connection error!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
    }
}
