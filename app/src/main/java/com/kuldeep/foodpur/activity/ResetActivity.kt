package com.kuldeep.foodpur.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kuldeep.foodpur.R
import org.json.JSONObject

class ResetActivity : AppCompatActivity() {
    lateinit var etOtp:EditText
    lateinit var etNewPass:EditText
    lateinit var etNewPassCnf:EditText
    lateinit var btnNextForget: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        etOtp=findViewById(R.id.etOtp)
        etNewPass=findViewById(R.id.etNewPass)
        etNewPassCnf=findViewById(R.id.etNewPassConf)
        btnNextForget=findViewById(R.id.btnNextForget)
        sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        val intent: Intent =getIntent()
        val mobiNo:String= intent.getStringExtra("mobile_number").toString()
        btnNextForget.setOnClickListener {
            sendOtpRequest(mobiNo,etNewPass.text.toString(),etOtp.text.toString())

        }
    }
    fun sendOtpRequest(phone: String,pass:String,otp:String){
        val queue = Volley.newRequestQueue(this)
        val forget="http://13.235.250.119/v2/reset_password/fetch_result/"
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", phone)
        jsonParams.put("password",pass)
        jsonParams.put("otp",otp)
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,forget,jsonParams, Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success ) {
                            Toast.makeText(this@ResetActivity,"Password is successfully changed ",
                                Toast.LENGTH_SHORT).show()
                            val startAct=Intent(Intent(this@ResetActivity, LoginActivity::class.java))
                        sharedPreferences.edit().clear().apply()
                            startActivity(startAct)
                            finish()
                    } else {
                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(
                            this@ResetActivity,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception){

                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(this@ResetActivity, it.message, Toast.LENGTH_SHORT).show() }
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
}