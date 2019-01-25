package com.example.leadershipboard.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()
        DataStore.tempBucket.clear()
        setOnClickListeners()
    }
    private fun setOnClickListeners() {
        ViewRecord.setOnClickListener{
            startActivity(Intent(this@DashboardActivity, ViewRecordActivity::class.java))
        }
        aboutButton.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, AboutActivity::class.java))
        }
        materialButton.setOnClickListener {
            editor?.clear()
            editor?.commit()
            val intent = Intent(this@DashboardActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        addNewEntry.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, NewEntryActivity::class.java))
        }
    }
}
