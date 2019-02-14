package com.example.leadershipboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.CalculateStarOfWeekQuery
import com.example.leadershipboard.R
import com.example.leadershipboard.TestQuery
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private val mHandler = Handler()
    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        uid = pref.getString("UID", null) // getting String
        Apollo_Helper.getApolloClient().query(
            TestQuery.builder()
                .build()).enqueue(object : ApolloCall.Callback<TestQuery.Data>(){
            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    connectionStatus.text = "Could not connect to server. Are you sure you are online?"
                    retryBtn.visibility = View.VISIBLE
                }

            }

            override fun onResponse(response: Response<TestQuery.Data>) {
                runOnUiThread {
                    intentFunction()
                }
            }

        })

    }

    private fun intentFunction() {
        if (uid != null) {
            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    fun retryConnection(view: View) {
        recreate()
    }
}
