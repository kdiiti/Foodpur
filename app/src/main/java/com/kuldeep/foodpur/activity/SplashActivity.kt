package com.kuldeep.foodpur.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.kuldeep.foodpur.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val startAct=Intent(this@SplashActivity,
                LoginActivity::class.java)
            startActivity(startAct)
        },2000)
    }
}