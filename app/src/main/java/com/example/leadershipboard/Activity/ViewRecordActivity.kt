package com.example.leadershipboard.Activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.BuildConfig
import com.example.leadershipboard.ViewRecordsQuery
import kotlinx.android.synthetic.main.activity_view_records.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ViewRecordActivity : AppCompatActivity() {
    private var uid: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.leadershipboard.R.layout.activity_view_records)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        uid = pref.getString("UID", null) // getting String
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 23) {
            askPermission()
        } else {
            Log.i("Checking else","working fine")
            askPermission()
        }
        setOnClickListener()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setOnClickListener() {
        downloadRecords.setOnClickListener{
            Toast.makeText(this@ViewRecordActivity,"Downloaded",Toast.LENGTH_SHORT).show()
            fetchingAllRecords()
        }
        viewDate.setOnClickListener {
            Toast.makeText(this@ViewRecordActivity,"Downloaded",Toast.LENGTH_SHORT).show()
            fetchingDateRecords()
        }
        viewStaroftheweek.setOnClickListener {
            startActivity(Intent(this@ViewRecordActivity, GetDateDetails::class.java))
        }
    }
    fun fetchingAllRecords(){
        Apollo_Helper.getApolloClient().query(ViewRecordsQuery.builder().csv(true).facultyId(uid).build())
            .enqueue(object : ApolloCall.Callback<ViewRecordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<ViewRecordsQuery.Data>) {
                    var csvString :String?=""
                    var AllRecordCSVName:String ="allRecordLeadershipRecord"
                    csvString=response.data()?.viewRecords()?.csv()
                    Log.e("csvString",""+csvString)
                    convertToCSV(csvString.toString(),AllRecordCSVName)
                    Log.e("csvString",""+csvString)
                }

            })
    }
    fun convertToCSV(responseString : String,storeString : String) {
        try {
    val file = File(Environment.getExternalStorageDirectory().toString() + File.separator +"Download"+ File.separator+ storeString)
    Log.e("dir",file.toString())
    file.createNewFile()
    if (file.exists()) {
        val fo = FileOutputStream(file)
        fo.write(responseString?.toByteArray())
        fo.close()
        println("file created: $file")
    }
    val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    downloadManager.addCompletedDownload(
        file.getName(),
        file.getName(),
        true,
        "text/csv",
        file.getAbsolutePath(),
        file.length(),
        true
    )
}
catch (e:Exception){
    Log.e("WriteFile",e.toString())
}
    }
    fun fetchingDateRecords() {
        var formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-DD")
        Apollo_Helper.getApolloClient().query(ViewRecordsQuery.builder().csv(true)
            .date(formatter.format(Date())).facultyId(uid).build()).enqueue(object : ApolloCall.Callback<ViewRecordsQuery.Data>() {
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<ViewRecordsQuery.Data>) {
                var dateString :String?=""
                var DateCSVName:String ="LeadershipRecord"+formatter.format(Date())
                Log.e("date",""+formatter.format(Date()))
                Log.e("responce",""+response.data()?.viewRecords().toString())
                dateString=response.data()?.viewRecords()?.csv()
                convertToCSV(dateString.toString(),DateCSVName)
            }

        })
    }
    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        } else {
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
            } else {
                Toast.makeText(this@ViewRecordActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

}