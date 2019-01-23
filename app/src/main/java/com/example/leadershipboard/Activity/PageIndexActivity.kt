package com.example.leadershipboard.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.R
import com.example.leadershipboard.ViewCoursesQuery
import kotlinx.android.synthetic.main.activity_new_entry.*
import kotlinx.android.synthetic.main.activity_page_index.*
import java.lang.NumberFormatException



class PageIndexActivity : AppCompatActivity() {

    var number: Int? = 0
    var current: Int? = 0
    var courseName: String? = ""
    var studentList: List<ViewCoursesQuery.Student> = ArrayList<ViewCoursesQuery.Student>()
    var studentRegisterNumber: MutableList<String> = mutableListOf<String>()
    var selectedStudentArray: MutableList<String> = mutableListOf()
    lateinit var studentRegisterAdapter: ArrayAdapter<String>
    var disabledBackButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_index)
        pageIndex_next.isEnabled = false
        number = intent?.getIntExtra("current", 0)
        current = intent?.getIntExtra("currentValue", 0)

        studentList = DataStore.studentList
        if(current == number){
            pageIndex_next.text = "Finish"
        }
        student_total_count.text = current.toString()+" / "+number
        student_count.text = "Entry for Student "+current
        student_marks.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pageIndex_next.isEnabled = true
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        setOnClickListener()
        registerspinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }


        studentRegisterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, selectedStudentArray)
        // Set layout to use when the list of choices appear
        studentRegisterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        getStudentsList()
        registerspinner!!.adapter = studentRegisterAdapter


    }

    fun getStudentsList(){
        for (student in studentList){
            Log.i("REGISTER NUMBER", student.registerno())
            selectedStudentArray.add(student.registerno())
            studentRegisterAdapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        if(disabledBackButton) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "Click cancel to go back now", Toast.LENGTH_LONG).show()
    }

    private fun setOnClickListener() {
        pageIndex_next.setOnClickListener{
            try {
                val num = Integer.parseInt(student_marks.text.toString())
                if (current!!.toInt() < number!!.toInt()) {
                    val intent = Intent(this@PageIndexActivity, PageIndexActivity::class.java)
                    intent.putExtra("current", number!!)
                    intent.putExtra("currentValue", current!! + 1)
                    startActivity(intent)
                    finish()
                }
            } catch (e: NumberFormatException){
                student_marks.error = "Marks cannot be strings"
            }
        }
        pageIndex_cancel.setOnClickListener{
            startActivity(Intent(this@PageIndexActivity, DashboardActivity::class.java))
            finish()
        }
    }
    /*fun fetchingthestudentDetails() {
        Apollo_Helper.getApolloClient().query(
            ViewCoursesQuery.builder()
                .build()).enqueue(object : ApolloCall.Callback<ViewCoursesQuery.Data>() {
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<ViewCoursesQuery.Data>) {
                for(items in response.data()?.viewCourses()!!)run {

                    runOnUiThread {
                        if (courseName.equals(items.coursename())) {
                            studentRegisterNumber.add(items)
                        }
                        studentRegisterAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }*/
}