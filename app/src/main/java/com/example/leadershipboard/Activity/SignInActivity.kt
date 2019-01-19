package com.example.leadershipboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.LoginMutation
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setOnClickListeners()
    }
    private fun setOnClickListeners(){
        signin.setOnClickListener{
            Log.e("Clicked","haha")
            postLoginRequest()
        }
    }
    private fun postLoginRequest() {
        Apollo_Helper.getApolloClient().mutate(LoginMutation.builder().username(usernameEditText.text.toString().trim())
            .password(passwordEditText.text.toString().trim()).build()).enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.e("Failure",""+e)

            }
            override fun onResponse(response: Response<LoginMutation.Data>) {
                Log.e("Response",""+response.data())
                startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
            }

        }
        )
    }
    }
