package com.example.leadershipboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
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
            startActivity(Intent(this@DashboardActivity, HelpActivity::class.java))
        }
        addNewEntry.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, NewEntryActivity::class.java))
        }
    }

}
