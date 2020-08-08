package com.kuldeep.foodpur.activity

import android.content.Intent
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

class Forget_Password : AppCompatActivity() {
    lateinit var etMobiNoForget:EditText
    lateinit var etEmailForget:EditText
    lateinit var btnForgetNext:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget__password)

        etMobiNoForget=findViewById(R.id.etMobiNoForget)
        etEmailForget=findViewById(R.id.etEmailForget)
        btnForgetNext=findViewById(R.id.btnNextForget)

        btnForgetNext.setOnClickListener {
            sendOtpRequest(etMobiNoForget.text.toString(),etEmailForget.text.toString())
        }
    }
    fun sendOtpRequest(phone: String,email:String){
        val queue = Volley.newRequestQueue(this)
        val forget="http://13.235.250.119/v2/forgot_password/fetch_result"
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", phone)
        jsonParams.put("email",email)
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,forget,jsonParams, Response.Listener {
                print("Response is $it")
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    val firstTry=data.getBoolean("first_try")
                    if (success ) {

                            Toast.makeText(this@Forget_Password,"OTP has been sent to Registered Email ",Toast.LENGTH_SHORT).show()
                            val startAct=Intent(Intent(this@Forget_Password, ResetActivity::class.java))
                            val mobiNo=phone
                            startAct.putExtra("mobile_number",mobiNo)
                            startActivity(startAct)
                            finish()

                    } else {

                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(
                            this@Forget_Password,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception){

                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(this@Forget_Password, it.message, Toast.LENGTH_SHORT).show() }
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