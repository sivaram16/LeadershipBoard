package com.example.leadershipboard.Activity

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.CalculateStarOfWeekQuery
import com.example.leadershipboard.GetConsolidateMarksQuery
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_get_date_details.*
import java.io.File
import java.io.FileOutputStream

class Consolidate : AppCompatActivity() {
    var regulationArrayStarOfTheWeek: MutableList<Int> = mutableListOf<Int>()
    lateinit var starRegulationSpinnerAdapter: ArrayAdapter<Int>
    var sectionArrayStarOfTheWeek: MutableList<Char> = mutableListOf()
    lateinit var starSectionSpinnerAdapter: ArrayAdapter<Char>
    var startDate: String = ""
    var CSVString:String=""
    var endDate:String=""
    var selectedRegulationStar: Int = 0
    var SelectedSectionStar: String = "A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consolidate)
        setonclicklistener()

        regulation_spinner_star?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegulationStar = Integer.parseInt(regulation_spinner_star.selectedItem.toString())
                Log.e("Selected_Regulation", selectedRegulationStar.toString())
            }
        }
        for (i in 2015..2020)
            regulationArrayStarOfTheWeek.add(i)
        starRegulationSpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, regulationArrayStarOfTheWeek)
        starRegulationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regulation_spinner_star.adapter = starRegulationSpinnerAdapter

        section_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SelectedSectionStar = section_spinner.selectedItem.toString()
                Log.e("Selected_Regulation", SelectedSectionStar.toString())
            }
        }
        for (i in "ABC")
            sectionArrayStarOfTheWeek.add(i)
        starSectionSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionArrayStarOfTheWeek)
        starSectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        section_spinner.adapter = starSectionSpinnerAdapter
    }

    fun setonclicklistener() {
        download.setOnClickListener {
            startDate = date.text.toString()
            getCSV()
        }
        date.setOnFocusChangeListener { v, hasFocus -> if(hasFocus) getStartDate()  }
        enddate.setOnFocusChangeListener { v, hasFocus -> if(hasFocus) getEndDate() }
    }
    fun getCSV() {
        Apollo_Helper.getApolloClient().query(
            GetConsolidateMarksQuery.builder().startDate(startDate)
                .endDate(endDate).year(selectedRegulationStar).section(SelectedSectionStar)
                .build()
        ).enqueue(object : ApolloCall.Callback<GetConsolidateMarksQuery.Data>() {
            override fun onResponse(response: Response<GetConsolidateMarksQuery.Data>) {
                runOnUiThread{
                    Log.e("Consolidate String",response.data()?.consolidateMarks.toString())
                    CSVString=response.data()?.consolidateMarks.toString()
                    Toast.makeText(this@Consolidate,"Downloaded", Toast.LENGTH_SHORT).show()
                    convertToCSV(CSVString)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.e("error",e.toString())
            }
        })
    }
    fun convertToCSV(responseString : String) {
        try {
            val file = File(Environment.getExternalStorageDirectory().toString() + File.separator +"Download"+ File.separator+ startDate+" to "+endDate+"(CSE-"+SelectedSectionStar+")")
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


    fun returnDateAsString(year: Int, month: Int, day: Int): String {
        var dateStr = ""
        dateStr += ("$year-")
        if(month < 10) {
            dateStr += ("0$month-")
        } else {
            dateStr += ("$month-")
        }
        if(day < 10) {
            dateStr += ("0$day")
        } else {
            dateStr += ("$day")
        }
        return dateStr
    }

    fun getStartDate() {
        val datePicker = DatePicker(this)
        AlertDialog.Builder(this)
            .setTitle("Select Start Date")
            .setView(datePicker)
            .setCancelable(true)
            .setPositiveButton("Select") { dialog, _ -> startDate = returnDateAsString(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth); Log.i("START DATE: ", startDate);  date.setText(startDate); dialog.cancel(); }
            .create().show()
    }
    fun getEndDate() {
        val datePicker = DatePicker(this)
        AlertDialog.Builder(this)
            .setTitle("Select End Date")
            .setView(datePicker)
            .setCancelable(true)
            .setPositiveButton("Select") { dialog, _ -> endDate = returnDateAsString(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth); Log.i("END DATE: ", startDate); enddate.setText(endDate); dialog.cancel(); }
            .create().show()
    }
}
