package com.example.leadershipboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.leadershipboard.R

class SplashActivity : AppCompatActivity() {
    private val mHandler = Handler()
    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        uid = pref.getString("UID", null) // getting String
        mHandler.postDelayed({
            intentFunction()
        }, 2000)
    }

    private fun intentFunction() {
        if (uid != null) {
            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        //SayMyName
    }
}
