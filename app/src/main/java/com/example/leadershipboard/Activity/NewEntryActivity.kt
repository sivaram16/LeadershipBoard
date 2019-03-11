package com.example.leadershipboard.Activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.R
import com.example.leadershipboard.TestQuery
import com.example.leadershipboard.ViewCoursesQuery
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_get_date_details.*
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class NewEntryActivity : AppCompatActivity() {
    var constant = 1
    var selectedRegulation:Int = 0
    var selectedCourseName: String =""
    var startDate: String = ""
    var selectedCourseId: String = ""
    var courseNameArray: MutableList<String> = mutableListOf()
    var regulationArray: MutableList<Int> = mutableListOf<Int>()
    var studentList:List<ViewCoursesQuery.Student>?= ArrayList<ViewCoursesQuery.Student>()
    lateinit var regulationSpinnerAdapter: ArrayAdapter<Int>
    lateinit var courseNameAdapter: ArrayAdapter<String>
    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)
        continue_button.isEnabled = false
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        uid = pref.getString("UID", null) // getting String
        total_students_editText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                continue_button.isEnabled = true
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        setOnClickListeners()
        fetchingtheCourseDetails()
        regulation_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegulation = Integer.parseInt(regulation_spinner.selectedItem.toString())
                Log.i("Selected Regulation", selectedRegulation.toString())
                fetchingtheCourseDetails()
            }
        }
        for(i in 2015..2020)
        regulationArray.add(i)
        regulationSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regulationArray)
        regulationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regulation_spinner!!.adapter = regulationSpinnerAdapter
        course_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCourseName = course_spinner.selectedItem.toString()
                fetchingtheCourseDetails()
                Log.i("dropDownSelected",courseNameArray.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        courseNameAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, courseNameArray)
        courseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        course_spinner!!.adapter = courseNameAdapter
        }
    private fun setOnClickListeners() {
        entryDate.setOnFocusChangeListener { v, hasFocus -> if(hasFocus) getStartDate()  }

        continue_button.setOnClickListener{
            Log.i("checking studentList", Integer.parseInt(studentList?.count().toString()).toString())
            Log.i("checking edittext", Integer.parseInt(total_students_editText.text.toString()).toString())
            if (Integer.parseInt(studentList?.count().toString())<Integer.parseInt(total_students_editText.text.toString())){
                total_students_editText.error = "You cannot have more than "+studentList?.count().toString()+" students"
            }
            if(startDate.toString().trim().equals("") || startDate == null) {
                Toast.makeText(this, "You forgot the date", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                try {
                    val num = Integer.parseInt(total_students_editText.text.toString())
                    val intent = Intent(this@NewEntryActivity, PageIndexActivity::class.java)
                    intent.putExtra("current", num)
                    intent.putExtra("currentValue", constant)
                    intent.putExtra("courseName", selectedCourseName)
                    intent.putExtra("courseId", selectedCourseId)
                    intent.putExtra("entryDate", startDate)
                    startActivity(intent)
                    finish()
                } catch (e: NumberFormatException) {
                    total_students_editText.error = "you cannot have strings"
                }
            }
        }
    }
    fun fetchingtheCourseDetails() {
        Apollo_Helper.getApolloClient().query(ViewCoursesQuery.builder().facultyId(uid)
            .build()).enqueue(object : ApolloCall.Callback<ViewCoursesQuery.Data>() {
            override fun onFailure(e: ApolloException) {
            }

            override fun onResponse(response: Response<ViewCoursesQuery.Data>) {
                runOnUiThread {
                    courseNameArray.clear()
                    courseNameAdapter.notifyDataSetChanged()
                    for (items in response.data()?.viewCourses()!!) run {
                        runOnUiThread {
                            if (selectedRegulation == items.regulation()) {
                                if (selectedCourseName == items.coursename()) {
                                    studentList = items.students()
                                    studentList?.let { DataStore.setStudentListFromExternal(it) }
                                    selectedCourseId = items.id()
                                }
                                courseNameArray.add(items.coursename())
                                courseNameAdapter.notifyDataSetChanged()
                            }
                            courseNameAdapter.notifyDataSetChanged()
                        }
                    }
                    progressbar.visibility=View.INVISIBLE
                }
            }
        })
    }
    fun getStartDate() {
        val datePicker = DatePicker(this)
        AlertDialog.Builder(this)
            .setTitle("Select Start Date")
            .setView(datePicker)
            .setCancelable(true)
            .setPositiveButton("Select") { dialog, _ -> startDate = returnDateAsString(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth); Log.i("START DATE: ", startDate);  entryDate.setText(startDate); dialog.cancel(); }
            .create().show()
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
}

