package com.example.leadershipboard.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.R
import com.example.leadershipboard.ViewCoursesQuery
import kotlinx.android.synthetic.main.activity_new_entry.*


class NewEntryActivity : AppCompatActivity() {
    var constant = 1
    var selectedRegulation:Int = 0
    var regulationArray: MutableList<Int> = mutableListOf<Int>()
    var courseArray: MutableList<String> = mutableListOf<String>()
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
                getting_Course.visibility = View.GONE
                course_spinner.visibility = View.VISIBLE
                selectedRegulation = Integer.parseInt(regulation_spinner.selectedItem.toString())
                Log.i("Selected Regulation", selectedRegulation.toString())
                fetchingtheCourseDetails()
                //Log.e("other regulation", regulation_spinner.selectedItem.toString())
            }
        }

        for(i in 2015..2025)
        regulationArray.add(i)
        // Create an ArrayAdapter using a simple spinner layout and languages array
        regulationSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regulationArray)
        // Set layout to use when the list of choices appear
        regulationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        regulation_spinner!!.adapter = regulationSpinnerAdapter

        course_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        courseNameAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, courseArray)
        courseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        course_spinner!!.adapter = courseNameAdapter
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
        Apollo_Helper.getApolloClient().query(ViewCoursesQuery.builder().facultyId(uid)
            .build()).enqueue(object : ApolloCall.Callback<ViewCoursesQuery.Data>() {
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<ViewCoursesQuery.Data>) {
                for(items in response.data()?.viewCourses()!!)run {
                    //Log.e("Courses",""+items)
                    //Log.e("Studemts",""+items.students())
                    //Log.e("regulation",items.regulation().toString())
                    courseArray.clear()
                    runOnUiThread {
                        //regulationArray.add(items.regulation())
                        //regulationSpinnerAdapter.notifyDataSetChanged()
                        if(selectedRegulation == items.regulation()){
                            Log.e("checking Regulation", selectedRegulation.toString())
                            Log.e("original Regulation", items.regulation().toString())
                            courseArray.add(items.coursename())
                        }
                        courseNameAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}


