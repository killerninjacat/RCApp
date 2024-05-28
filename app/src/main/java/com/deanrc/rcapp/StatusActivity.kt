package com.deanrc.rcapp

import CustomAdapter
import ItemsViewModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_status)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val statusList = findViewById<RecyclerView>(R.id.recyclerViewStatus)
        statusList.layoutManager = LinearLayoutManager(this)
        val items = listOf(
            ItemsViewModel("Date 1", "Description 1"),
            ItemsViewModel("Date 2", "Description 2"),
            ItemsViewModel("Date 3", "Description 3"),
            ItemsViewModel("Date 4", "Description 4"),
            ItemsViewModel("Date 5", "Description 5"),
            ItemsViewModel("Date 6", "Description 6"),
            ItemsViewModel("Date 7", "Description 7"),
            ItemsViewModel("Date 8", "Description 8"),
            ItemsViewModel("Date 9", "Description 9"),
            ItemsViewModel("Date 10", "Description 10")
        )
        val adapter = CustomAdapter(items)
        statusList.adapter = adapter
    }
}