package com.example.leadershipboard.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setOnClickListener()
    }
    private fun setOnClickListener() {
        cross.setOnClickListener {
            finish()
        }

        copyright.setOnClickListener {
            val uri = Uri.parse("https://github.com/ThalapathySiva/LeadershipBoard/blob/add-license-1/LICENSE")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}