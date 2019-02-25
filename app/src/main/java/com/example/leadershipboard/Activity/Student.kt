package com.example.leadershipboard.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.GetStudentTotalMarksQuery
import kotlinx.android.synthetic.main.activity_student.*
import android.R
import android.net.Uri


class Student : AppCompatActivity() {
    private var editor: SharedPreferences.Editor? = null
    private var uid: String?=null
    private var totalmarks: Int?=null
    private var name:String?=null
    private var regno:String?=null
    private var sow:Boolean?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.leadershipboard.R.layout.activity_student)
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
        account.setOnClickListener {
            accountToast()
        }
        help.setOnClickListener {
            val builder = AlertDialog.Builder(this@Student)
            builder.setMessage("Are you sure want to open the guidelines of the app ?")
            builder.setPositiveButton("YES"){dialog, which ->
                allowpdfDownload()
            }
            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(applicationContext,"cancelled",Toast.LENGTH_SHORT).show()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    fun getMarks() {
        Apollo_Helper.getApolloClient().query(GetStudentTotalMarksQuery.builder().studentId(uid).build()
            ).enqueue(object : ApolloCall.Callback<GetStudentTotalMarksQuery.Data>(){
            override fun onFailure(e: ApolloException) {

            }

            override fun onResponse(response: Response<GetStudentTotalMarksQuery.Data>) {
                runOnUiThread {
                    Log.e("Full Marks", response.data()?.studentTotalMarks?.get(0)?.totalMarks()?.totalMarks().toString())
                    Log.e("name",response.data()?.studentTotalMarks?.get(0)?.name().toString())
                    totalmarks = response.data()?.studentTotalMarks?.get(0)?.totalMarks()?.totalMarks()
                    regno=response.data()?.studentTotalMarks?.get(0)?.registerno()
                    sow=response.data()?.studentTotalMarks?.get(0)?.isSow
                    Log.e("sow",sow.toString())
                    showSow()
                    name=response.data()?.studentTotalMarks?.get(0)?.name().toString()
                    progressBar2.visibility= View.INVISIBLE
                    textView7.text = totalmarks.toString()
                    if (totalmarks==0 || totalmarks==null){
                        textView9.text=resources.getString(com.example.leadershipboard.R.string.stringfor0)
                    }
                    else if (totalmarks!! > 0) {
                        textView9.text=resources.getString(com.example.leadershipboard.R.string.stringforabove50)

                         if(totalmarks!! > 350) {
                            textView9.text=resources.getString(com.example.leadershipboard.R.string.stringforabove350)
                        }
                    }

                    else {
                        textView9.text=resources.getString(com.example.leadershipboard.R.string.stringforabove500)
                    }
                }
            }

        })

    }

    fun accountToast() {
        Toast.makeText(this,""+name+"\n"+regno,Toast.LENGTH_SHORT).show()
    }

    fun allowpdfDownload() {
        val uri = Uri.parse("https://github.com/roshanrahman/vue-leadership/raw/master/github_assets/cs-entry-manual.pdf")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    fun showSow() {
        if (sow==false || sow==null) {
            a1.visibility=View.INVISIBLE
            a2.visibility=View.INVISIBLE
        }
        else {
            a1.visibility=View.VISIBLE
            a2.visibility=View.VISIBLE
        }
    }

}
