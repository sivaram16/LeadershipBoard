package com.example.leadershipboard.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.GetStudentTotalMarksQuery
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_student.*

class Student : AppCompatActivity() {
    private var editor: SharedPreferences.Editor? = null
    private var uid: String?=null
    private var totalmarks: Int?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()
        uid=pref.getString("UID",null)
        setOnClickListener()
        getMarks()
    }
    fun setOnClickListener() {
        aboutButton.setOnClickListener {
            startActivity(Intent(this@Student, AboutActivity::class.java))
        }
        materialButton.setOnClickListener { editor?.clear()
            editor?.commit()
            val intent = Intent(this@Student, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun getMarks() {
        Apollo_Helper.getApolloClient().query(GetStudentTotalMarksQuery.builder().studentId(uid).build()
            ).enqueue(object : ApolloCall.Callback<GetStudentTotalMarksQuery.Data>(){
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<GetStudentTotalMarksQuery.Data>) {
                runOnUiThread {
                    Log.e(
                        "Full Marks",
                        response.data()?.studentTotalMarks?.get(0)?.totalMarks()?.totalMarks().toString()
                    )
                    totalmarks = response.data()?.studentTotalMarks?.get(0)?.totalMarks()?.totalMarks()
                    progressBar2.visibility= View.INVISIBLE
                    textView7.text = totalmarks.toString()
                    if (totalmarks==0 || totalmarks?.equals(null)!!){
                        textView9.text=resources.getString(R.string.stringfor0)
                    }
                    else if (totalmarks!! > 0) {
                        textView9.text=resources.getString(R.string.stringforabove50)

                         if(totalmarks!! > 350) {
                            textView9.text=resources.getString(R.string.stringforabove350)
                        }
                    }

                    else {
                        textView9.text=resources.getString(R.string.stringforabove500)
                    }
                }
            }

        })

    }

}
