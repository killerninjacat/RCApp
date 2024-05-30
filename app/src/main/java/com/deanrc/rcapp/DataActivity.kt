package com.deanrc.rcapp

import CustomAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val staffIDTextView = findViewById<TextView>(R.id.staffIDTextView)
        staffIDTextView.text = "Staff ID: " + intent.getStringExtra("staffID")
        val welcomeTextView = findViewById<TextView>(R.id.WelcomeTextView)
        val content = intent.getStringExtra("content")
        val contentJsonArray = JSONArray(content)
        Log.d("DataActivity", contentJsonArray.length().toString())
        welcomeTextView.text = "Welcome, " + contentJsonArray.getJSONObject(0).getJSONObject("05. Name").getString("value")
        val tallyCodeSpinner = findViewById<Spinner>(R.id.tallyCodeSpinner)
        val options = listOf("Option 1", "Option 2", "Option 3")
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, options)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tallyCodeSpinner.adapter = spinnerAdapter
        val projectsList = findViewById<RecyclerView>(R.id.recyclerViewData)
        projectsList.layoutManager = LinearLayoutManager(this)
        val items = mutableListOf<JSONObject>()
        for(i in 0 until contentJsonArray.length()) {
            val project = contentJsonArray.getJSONObject(i)
            items.add(project)
        }
        val listKeyValue = transformData(items)
        val sortedList = listKeyValue.sortedWith { o1, o2 ->
            when {
                !o1.key.any { it.isDigit() } && o2.key.any { it.isDigit() } -> -1
                o1.key.any { it.isDigit() } && !o2.key.any { it.isDigit() } -> 1
                else -> o1.key.compareTo(o2.key)
            }
        }
        val adapter = CustomAdapter(sortedList)
        projectsList.adapter = adapter
}
}
data class KeyValue(val key: String, val value: String)

fun transformData(projectList: List<JSONObject>): List<KeyValue> {
    val keyValueList = mutableListOf<KeyValue>()
    for (project in projectList) {
        val keysIterator: Iterator<String> = project.keys()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            val value = project.getJSONObject(key).getString("value")
            keyValueList.add(KeyValue(key, value))
        }
    }
    return keyValueList
}