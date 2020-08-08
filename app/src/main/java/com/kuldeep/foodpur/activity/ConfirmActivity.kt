package com.kuldeep.foodpur.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kuldeep.foodpur.R

class ConfirmActivity : AppCompatActivity() {
    lateinit var btnOk:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        btnOk=findViewById(R.id.btnGoToHome)
        btnOk.setOnClickListener {
            val intent= Intent(this,DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent= Intent(this,DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}