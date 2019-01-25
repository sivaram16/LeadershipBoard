package com.example.leadershipboard.Activity

import Constants
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.leadershipboard.FacultyLoginMutation
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlin.math.sign

class SignInActivity : AppCompatActivity() {
    private var userid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setOnClickListeners()
    }
    private fun setOnClickListeners(){
        signin.setOnClickListener{
            Log.e("Clicked","haha")
            progressBar.visibility=View.VISIBLE
            usernameTextInputLayout.error = null
            passwordInputLayout.error = null
            postLoginRequest()
        }
    }
    private fun handleWhenPasswordIncorrect() {
        passwordInputLayout.error = "Password is incorrect"
        passwordEditText.text = null
        passwordEditText.requestFocus()
    }

    private fun handleWhenInvalidAccount() {
        usernameTextInputLayout.error = "Username does not exist"
        passwordEditText.text = null
        usernameEditText.text = null
        usernameTextInputLayout.requestFocus()
    }

    private fun handleLoginSuccess() {
        runOnUiThread {
            startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
            finish()
        }
    }

    private fun postLoginRequest() {
        Apollo_Helper.getApolloClient().mutate(FacultyLoginMutation.builder().username(usernameEditText.text.toString().trim())
            .password(passwordEditText.text.toString().trim()).build()).enqueue(object : ApolloCall.Callback<FacultyLoginMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.e("Failure", e.toString())
            }
            override fun onResponse(response: Response<FacultyLoginMutation.Data>) {
                Log.e("Response", response.data().toString())
                userid = response.data()!!.facultyLogin().id()
                runOnUiThread {
                    progressBar.visibility=View.INVISIBLE
                    val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
                    val editor = pref.edit()
                    editor.putString("UID", userid) // Storing string
                    editor.commit() // commit changes
                    if (response.data()?.facultyLogin()?.errors() == null) {
                        handleLoginSuccess()
                    } else {
                        val receivedError = response.data()?.facultyLogin()?.errors()?.get(0)?.errorCode().toString()
                        when(receivedError) {
                            Constants.PASSWORD_INVALID_ERROR_CODE -> {
                                handleWhenPasswordIncorrect()
                            }
                            Constants.USERNAME_INVALID_ERROR_CODE -> {
                                handleWhenInvalidAccount()
                            }
                        }
                    }
                }
            }
        }
        )
    }
    }
