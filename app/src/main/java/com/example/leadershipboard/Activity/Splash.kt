package com.example.leadershipboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.leadershipboard.MainActivity
import com.example.leadershipboard.R

class Splash : AppCompatActivity() {
    private val mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mHandler.postDelayed({ intentFunction() }, 2000)
    }

    private fun intentFunction() {
        val intentToMainactivity = Intent(this, SignIn::class.java)
        startActivity(intentToMainactivity)
    }
}
