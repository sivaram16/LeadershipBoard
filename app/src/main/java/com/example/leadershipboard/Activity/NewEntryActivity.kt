package com.example.leadershipboard.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_new_entry.*
import android.text.Editable


class NewEntryActivity : AppCompatActivity() {

    var constant = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)
        continue_button.isEnabled = false
        total_students_editText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                continue_button.isEnabled = true
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        setOnClickListeners()
    }
    private fun setOnClickListeners() {
        continue_button.setOnClickListener{
            try {
                val num = Integer.parseInt(total_students_editText.text.toString())
                val intent = Intent(this@NewEntryActivity, PageIndexActivity::class.java)
                intent.putExtra("current", num)
                intent.putExtra("currentValue", constant)
                startActivity(intent)
            }catch (e: NumberFormatException){
                total_students_editText.error = "Enter an Integer"
            }
        }
    }
}