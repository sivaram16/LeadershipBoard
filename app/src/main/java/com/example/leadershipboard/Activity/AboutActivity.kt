package com.example.leadershipboard.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.about_activity.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setOnClickListener()
    }
    private fun setOnClickListener() {
        cross.setOnClickListener {
            finish()
        }
    }
}