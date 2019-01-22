package com.example.leadershipboard.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.R
import com.example.leadershipboard.ViewCoursesQuery
import kotlinx.android.synthetic.main.activity_new_entry.*


class NewEntryActivity : AppCompatActivity() {
    var constant = 1
    var regulationArray: MutableList<Int> = mutableListOf<Int>()
    var courseArray: MutableList<ViewCoursesQuery.ViewCourse> = mutableListOf<ViewCoursesQuery.ViewCourse>()
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
    }
    private fun setOnClickListeners() {
        continue_button.setOnClickListener{
            try {
                val num = Integer.parseInt(total_students_editText.text.toString())
                val intent = Intent(this@NewEntryActivity, PageIndexActivity::class.java)
                intent.putExtra("current", num)
                intent.putExtra("currentValue", constant)
                startActivity(intent)
                finish()
            }catch (e: NumberFormatException){
                total_students_editText.error = "Enter an Integer"
            }
        }
    }
    fun fetchingtheCourseDetails() {
        Apollo_Helper.getApolloClient().query(ViewCoursesQuery.builder()
            .build()).enqueue(object : ApolloCall.Callback<ViewCoursesQuery.Data>() {
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<ViewCoursesQuery.Data>) {
                for(items in response.data()?.viewCourses()!!)run {
                    //Log.e("Regulations",""+items.coursename())
                    if (uid.equals(items.faculty().id())) {
                        courseArray.add(items)
                    }
                }

                Log.e("regulation",""+regulationArray)
                Log.e("coursename",""+courseArray)
                Log.e("UID",""+uid)
            }
        })
    }

}