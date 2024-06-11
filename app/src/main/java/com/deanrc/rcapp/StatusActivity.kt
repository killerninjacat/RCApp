package com.deanrc.rcapp

import CustomAdapter
import ItemsViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import androidx.activity.enableEdgeToEdge
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class StatusActivity : AppCompatActivity() {

    private val BASE_URL = "https://sheetdb.io/"
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_status)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val staffID = intent.getStringExtra("staffID")
        val submit = findViewById<ImageButton>(R.id.tick)
        val codeTextView = findViewById<TextView>(R.id.editTextText)
        submit.setOnClickListener {
            if (codeTextView.text == "" || codeTextView.text.length < 15 ||  codeTextView.text.toString().substring(0, 4)!= "NITT"){
                runOnUiThread {
                    Toast.makeText(this, "File code invalid, paste the code you get after scanning the FILE QR", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val code = codeTextView.text.toString().substring(7, 11)
                if(code[3] == '/'){
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiService::class.java)
                    val call = apiService.getFileData()

                    call.enqueue(object : retrofit2.Callback<FileData> {
                        override fun onResponse(call: retrofit2.Call<FileData>, response: retrofit2.Response<FileData>) {
                            if (!response.isSuccessful) {
                                Log.e("getreq", "Error: ${response.code()}")
                                return
                            }
                            var fileStatus = ArrayList<KeyValue>()
                            val fileDataList = response.body()
                            val k =codeTextView.text.toString()
                            for (i in fileDataList?.indices!!){
                                if(fileDataList[i].FileID.toString() == k) {
                                    fileStatus.apply {
                                        fileStatus.add(KeyValue("FileID",fileDataList[i].FileID?:""))
                                        fileStatus.add(KeyValue("Description",fileDataList[i].Description?:""))
                                        var flag:String
                                        flag = fileDataList[i].Status1?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status1",fileDataList[i].Status1?:""))
                                        flag = fileDataList[i].Status2?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status2",fileDataList[i].Status2?:""))
                                        flag = fileDataList[i].Status3?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status3",fileDataList[i].Status3?:""))
                                        flag = fileDataList[i].Status4?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status4",fileDataList[i].Status4?:""))
                                        flag = fileDataList[i].Status5?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status5",fileDataList[i].Status5?:""))
                                        flag = fileDataList[i].Status6?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status6",fileDataList[i].Status6?:""))
                                        flag = fileDataList[i].Status7?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status7",fileDataList[i].Status7?:""))
                                        flag = fileDataList[i].Status8?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status8",fileDataList[i].Status8?:""))
                                        flag = fileDataList[i].Status9?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status9",fileDataList[i].Status9?:""))
                                        flag = fileDataList[i].Status10?:""
                                        if(flag != "")fileStatus.add(KeyValue("Status10",fileDataList[i].Status10?:""))
                                    }
                                }
                            }
                            runOnUiThread {
                                val statusList = findViewById<RecyclerView>(R.id.recyclerViewStatus)
                                statusList.layoutManager = LinearLayoutManager(this@StatusActivity)
                                val adapter = CustomAdapter(fileStatus)
                                statusList.adapter = adapter
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<FileData>, t: Throwable) {
                            Log.d("getreq", "Failure: ${t.message}")
                        }
                    })

                }
                else{
                    if (staffID==code){
                        val retrofit = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        val apiService = retrofit.create(ApiService::class.java)
                        val call = apiService.getFileData()

                        call.enqueue(object : retrofit2.Callback<FileData> {
                            override fun onResponse(call: retrofit2.Call<FileData>, response: retrofit2.Response<FileData>) {
                                if (!response.isSuccessful) {
                                    Log.e("getreq", "Error: ${response.code()}")
                                    return
                                }
                                var fileStatus = ArrayList<KeyValue>()
                                val k = codeTextView.text.toString()
                                val fileDataList = response.body()
                                for (i in fileDataList?.indices!!){
                                    if(fileDataList[i].FileID.toString() == k) {
                                        fileStatus.apply {
                                            fileStatus.add(KeyValue("FileID",fileDataList[i].FileID?:""))
                                            fileStatus.add(KeyValue("Description",fileDataList[i].Description?:""))
                                            fileStatus.add(KeyValue("Status1",fileDataList[i].Status1?:""))
                                            fileStatus.add(KeyValue("Status2",fileDataList[i].Status2?:""))
                                            fileStatus.add(KeyValue("Status3",fileDataList[i].Status3?:""))
                                            fileStatus.add(KeyValue("Status4",fileDataList[i].Status4?:""))
                                            fileStatus.add(KeyValue("Status5",fileDataList[i].Status5?:""))
                                            fileStatus.add(KeyValue("Status6",fileDataList[i].Status6?:""))
                                            fileStatus.add(KeyValue("Status7",fileDataList[i].Status7?:""))
                                            fileStatus.add(KeyValue("Status8",fileDataList[i].Status8?:""))
                                            fileStatus.add(KeyValue("Status9",fileDataList[i].Status9?:""))
                                            fileStatus.add(KeyValue("Status10",fileDataList[i].Status10?:""))
                                        }
                                    }
                                }
                                val statusList = findViewById<RecyclerView>(R.id.recyclerViewStatus)
                                statusList.layoutManager = LinearLayoutManager(this@StatusActivity)
                                val adapter = CustomAdapter(fileStatus)
                                statusList.adapter = adapter
                            }

                            override fun onFailure(call: retrofit2.Call<FileData>, t: Throwable) {
                                Log.e("getreq", "Failure: ${t.message}")
                            }
                        })

                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(this, "You dont have permission to access this file", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

    }
//
//
//        statusList.layoutManager = LinearLayoutManager(this)
//        val items = listOf(
//            KeyValue("Date 1", "Description 1"),
//            KeyValue("Date 2", "Description 2"),
//            KeyValue("Date 3", "Description 3"),
//            KeyValue("Date 4", "Description 4"),
//            KeyValue("Date 5", "Description 5"),
//            KeyValue("Date 6", "Description 6"),
//            KeyValue("Date 7", "Description 7"),
//            KeyValue("Date 8", "Description 8"),
//            KeyValue("Date 9", "Description 9"),
//            KeyValue("Date 10", "Description 10")
//        )
//        val adapter = CustomAdapter(items)
//        statusList.adapter = adapter


}