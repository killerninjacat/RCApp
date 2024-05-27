package com.deanrc.rcapp

import CustomAdapter
import ItemsViewModel
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tallyCodeSpinner = findViewById<Spinner>(R.id.tallyCodeSpinner)
        val options = listOf("Option 1", "Option 2", "Option 3")
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, options)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tallyCodeSpinner.adapter = spinnerAdapter
        val projectsList = findViewById<RecyclerView>(R.id.recyclerView)
        projectsList.layoutManager = LinearLayoutManager(this)
        val items = listOf(
            ItemsViewModel("Project 1", "Description 1"),
            ItemsViewModel("Project 2", "Description 2"),
            ItemsViewModel("Project 3", "Description 3"),
            ItemsViewModel("Project 4", "Description 4"),
            ItemsViewModel("Project 5", "Description 5"),
            ItemsViewModel("Project 6", "Description 6"),
            ItemsViewModel("Project 7", "Description 7"),
            ItemsViewModel("Project 8", "Description 8"),
            ItemsViewModel("Project 9", "Description 9"),
            ItemsViewModel("Project 10", "Description 10")
        )
        val adapter = CustomAdapter(items)
        projectsList.adapter = adapter
        GlobalScope.launch(Dispatchers.Main) {
            //val data = fetchDataFromEndpoint("https://your_endpoint_url")
            //val jsonObject = JSONObject(data)
            //findViewById<TextView>(R.id.WelcomeTextView).text = jsonObject.getString("welcomeMessage")
            //findViewById<TextView>(R.id.staffIDTextView).text = "Staff ID: ${jsonObject.getString("staffId")}"
        }
}

    private suspend fun fetchDataFromEndpoint(urlString: String): String {
    return withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val inputStream: InputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            response.toString()
        } finally {
            connection.disconnect()
        }
    }
}
}