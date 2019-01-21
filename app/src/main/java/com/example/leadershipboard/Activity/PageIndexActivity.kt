package com.example.leadershipboard.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_page_index.*
import java.lang.NumberFormatException



class PageIndexActivity : AppCompatActivity() {

    var number: Int? = 0
    var current: Int? = 0

    var disabledBackButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_index)
        pageIndex_next.isEnabled = false
        number = intent?.getIntExtra("current", 0)
        current = intent?.getIntExtra("currentValue", 0)
        if(current == number){
            pageIndex_next.text = "Finish"
        }
        student_total_count.text = current.toString()+" / "+number
        student_count.text = "Entry for Student "+current
        student_marks.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pageIndex_next.isEnabled = true
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        setOnClickListener()
    }

    override fun onBackPressed() {
        if(disabledBackButton) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "You cannot go back now", Toast.LENGTH_LONG).show()
    }

    private fun setOnClickListener() {
        pageIndex_next.setOnClickListener{
            try {
                val num = Integer.parseInt(student_marks.text.toString())
                if (current!!.toInt() < number!!.toInt()) {
                    val intent = Intent(this@PageIndexActivity, PageIndexActivity::class.java)
                    intent.putExtra("current", number!!)
                    intent.putExtra("currentValue", current!! + 1)
                    startActivity(intent)
                    finish()
                }
            } catch (e: NumberFormatException){
                student_marks.error = "Marks cannot be strings"
            }
        }
        pageIndex_cancel.setOnClickListener{
            startActivity(Intent(this@PageIndexActivity, DashboardActivity::class.java))
        }
    }
}