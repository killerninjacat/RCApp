package com.deanrc.rcapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.security.Permission
import java.net.URL

class QRscanner : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST_CODE = 69
    private lateinit var scannerView:CodeScannerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrscanner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        askCameraPermissions()
        scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Log.d("QR link",it.text )
                FetchUrlContentTask().execute(it.text)
                val staffID = intent.getStringExtra("staffID")
                val intent = Intent(this, StatusActivity::class.java)
                intent.putExtra("staffID",staffID)
                startActivity(intent)
            }
        }
//
//        submitButton.setOnClickListener {
//            if (codeTextView.text=="" &&  codeTextView.text.toString().substring(0, 4)!= "NITT"){
//                runOnUiThread {
//                    Toast.makeText(this, "File code invalid, paste the code you get after scanning the FILE QR", Toast.LENGTH_SHORT).show()
//                }
//            }
//            else{
//                val staffID = intent.getStringExtra("staffID")
//                val intent = Intent(this, StatusActivity::class.java)
//                intent.putExtra("fileID",codeTextView.text)
//                intent.putExtra("staffID",staffID)
//                startActivity(intent)
//            }
//        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun askCameraPermissions(){
        val perm = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if (perm !=PackageManager.PERMISSION_GRANTED){
            makeCameraPermRequest()
        }
    }
    private fun makeCameraPermRequest(){
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0]!=PackageManager.PERMISSION_GRANTED ) {
                    runOnUiThread {
                        Toast.makeText(this, "You need Camera permissions to use this feature", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
    private inner class FetchUrlContentTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            val urlString = params[0]
            return try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                startActivity(intent)
                "Opening URL for File-Code in your default browser"
            }
            catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Toast.makeText(this@QRscanner, result, Toast.LENGTH_SHORT).show()
        }
    }
}






