package com.example.leadershipboard.Activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.ViewRecordsQuery
import kotlinx.android.synthetic.main.activity_view_records.*
import java.io.BufferedReader
import java.io.FileWriter
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import androidx.core.content.ContextCompat.getSystemService
import android.app.DownloadManager
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


class ViewRecordActivity : AppCompatActivity() {
    var csvString :String?=""
    var fileReader: BufferedReader? = null
    var fileWriter: FileWriter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.leadershipboard.R.layout.activity_view_records)
        setOnClickListener()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setOnClickListener() {
        downloadRecords.setOnClickListener{
            fetchingAllRecords()
            convertToCSV()
        }
    }
    fun fetchingAllRecords(){
        Apollo_Helper.getApolloClient().query(ViewRecordsQuery.builder().csv(true).build())
            .enqueue(object : ApolloCall.Callback<ViewRecordsQuery.Data>() {
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<ViewRecordsQuery.Data>) {
                    csvString=response.data()?.viewRecords()?.csv()
                    Log.e("csvString",""+csvString)
                }

            })
    }
    fun convertToCSV() {
try {

    val file = File(Environment.getExternalStorageDirectory().toString() + File.separator +"Download"+ File.separator+ "details.csv")
    Log.e("dir",file.toString())
    file.createNewFile()
//write the bytes in file
    if (file.exists()) {
        val fo = FileOutputStream(file)
        fo.write(csvString?.toByteArray())
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
    val a = getFilesDir()
    Log.e("file",a.absolutePath)
}
catch (e:Exception){
    Log.e("WriteFile",e.toString())
}
    }
}