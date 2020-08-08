package com.kuldeep.foodpur.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.utill.ConnectionManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var registerUser: TextView
    lateinit var forgetPassword: TextView
    lateinit var etMobileNo:EditText
    lateinit var etPass:EditText
    lateinit var btnLogin:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        setContentView(R.layout.activity_login)
        if (isLoggedIn){
            val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        registerUser=findViewById(R.id.txtRegister)
        forgetPassword=findViewById(R.id.txtForget)
        etMobileNo=findViewById(R.id.etMobileNo_)
        etPass=findViewById(R.id.etPass)
        btnLogin=findViewById(R.id.btnLogin)
        registerUser.paintFlags=registerUser.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        forgetPassword.paintFlags=forgetPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        //sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        registerUser.setOnClickListener{
            startActivity(Intent(this@LoginActivity,
                RegisterUser::class.java))
        }
        forgetPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                Forget_Password::class.java))
        }
        btnLogin.setOnClickListener {
            if(ConnectionManager().checkConnectivity(this@LoginActivity)){
                sendLoginRequest(etMobileNo.text.toString(),etPass.text.toString())

            }else{
                val dialog=AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found ")
                dialog.setPositiveButton("Open Settings"){
                    text,listener ->
                    val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@LoginActivity.finish()
                }
                dialog.setNegativeButton("Exit from App"){text,listener->
                onBackPressed()
                }
                dialog.create()
                dialog.show()

            }

        }

    }

   fun savePreferences(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }
    fun sendLoginRequest(phone: String, password: String){
        val queue = Volley.newRequestQueue(this)
        val login="http://13.235.250.119/v2/login/fetch_result"
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", phone)
        jsonParams.put("password", password)
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,login,jsonParams,Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val response = data.getJSONObject("data")
                        sharedPreferences.edit().putString("user_id", response.getString("user_id")).apply()
                        sharedPreferences.edit().putString("user_name", response.getString("name")).apply()
                        sharedPreferences.edit().putString("user_email", response.getString("email")).apply()
                        sharedPreferences.edit().putString("user_mobile_number",response.getString("mobile_number")).apply()
                        sharedPreferences.edit().putString("user_address", response.getString("address")).apply()
                        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                        startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
                        finish()
                    } else {

                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(
                            this@LoginActivity,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception){

                    e.printStackTrace()
                }
            },
            Response.ErrorListener {Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "7dfdc86f820a48"
                return headers
            }
        }
        queue.add(jsonObjectRequest)


    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@LoginActivity) }
}