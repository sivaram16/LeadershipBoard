package com.example.leadershipboard.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.leadershipboard.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()
        DataStore.tempBucket.clear()
        setOnClickListeners()
    }
    private fun setOnClickListeners() {
        helpmanual.setOnClickListener {
            val builder = AlertDialog.Builder(this@DashboardActivity)
            builder.setMessage("Are you sure want to open the guidelines of the app ?")
            builder.setPositiveButton("YES"){dialog, which ->
                allowpdfDownload()
            }
            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(applicationContext,"cancelled", Toast.LENGTH_SHORT).show()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        ViewRecord.setOnClickListener{
            startActivity(Intent(this@DashboardActivity, ViewRecordActivity::class.java))
        }
        aboutButton.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, AboutActivity::class.java))
        }
        materialButton.setOnClickListener {
            editor?.clear()
            editor?.commit()
            val intent = Intent(this@DashboardActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        addNewEntry.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, NewEntryActivity::class.java))
        }
    }

    fun allowpdfDownload() {
        val uri = Uri.parse("https://github.com/roshanrahman/vue-leadership/raw/master/github_assets/cs-entry-manual.pdf")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}
