package com.deanrc.rcapp

import DataAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class DataActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
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
        val staffId = intent.getStringExtra("staffID")
        staffIDTextView.text = "Staff ID: $staffId"
        val welcomeTextView = findViewById<TextView>(R.id.WelcomeTextView)
        val content = intent.getStringExtra("content")
        val contentJsonArray = JSONArray(content)
        Log.d("DataActivity", contentJsonArray.length().toString())
        welcomeTextView.text = "Welcome, " + contentJsonArray.getJSONObject(0).getJSONObject("05. Name").getString("value")
        val tallyCodeSpinner = findViewById<Spinner>(R.id.tallyCodeSpinner)
        val tallyCodes = mutableListOf<String>()
        val projectsList = findViewById<RecyclerView>(R.id.recyclerViewData)
        projectsList.layoutManager = LinearLayoutManager(this)
        val items = mutableListOf<JSONObject>()
        for(i in 0 until contentJsonArray.length()) {
            val project = contentJsonArray.getJSONObject(i)
            items.add(project)
            tallyCodes.add(project.getJSONObject("Tallycode").getString("value"))
        }
        val qrIcon = findViewById<View>(R.id.qrImageView)
        qrIcon.setOnClickListener {
            val intent = Intent(this, QRscanner::class.java)
            intent.putExtra("staffID", staffId)
            startActivity(intent)
        }
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, tallyCodes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tallyCodeSpinner.adapter = spinnerAdapter
        tallyCodeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTallyCode = parent.getItemAtPosition(position).toString()
                val filteredItems = items.filter { it.getJSONObject("Tallycode").getString("value") == selectedTallyCode }
                val listKeyValue = transformData(filteredItems)
                val sortedList = listKeyValue.sortedWith { o1, o2 ->
                    when {
                        !o1.key.any { it.isDigit() } && o2.key.any { it.isDigit() } -> -1
                        o1.key.any { it.isDigit() } && !o2.key.any { it.isDigit() } -> 1
                        else -> o1.key.compareTo(o2.key)
                    }
                }
                val adapter = DataAdapter(sortedList)
                projectsList.adapter = adapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
}
}
data class KeyValue(val key: String, val value: String)

fun transformData(projectList: List<JSONObject>): List<KeyValue> {
    val keyValueList = mutableListOf<KeyValue>()
    for (project in projectList) {
        val keysIterator: Iterator<String> = project.keys()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            if (!key.contains("97. Sanction Order") &&
                !key.contains("98. Office Order") &&
                !key.contains("99. Utilization Certificate") &&
                !key.contains("99a. Expenditure Details")) {
                val value = project.getJSONObject(key).getString("value")
                keyValueList.add(KeyValue(key, value))
            }
        }
    }
    return keyValueList
}