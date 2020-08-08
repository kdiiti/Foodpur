package com.kuldeep.foodpur.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kuldeep.foodpur.R
import org.json.JSONObject
import java.lang.Exception

class RegisterUser : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var etName: EditText
    lateinit var etPassRegister: EditText
    lateinit var etPassConfm:EditText
    lateinit var etAddRegister:EditText
    lateinit var etEmailRegis: EditText
    lateinit var etMobiRegi: EditText
    lateinit var btnRegis: Button
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_user)

        toolbar=findViewById(R.id.toolbar)
        etName=findViewById(R.id.etName)
        etPassRegister=findViewById(R.id.etPassRegi)
        etPassConfm=findViewById(R.id.etCnfPass)
        etAddRegister=findViewById(R.id.etDeliveryAdd)
        etEmailRegis=findViewById(R.id.etEmail)
        etMobiRegi=findViewById(R.id.etMobileRegister)
        btnRegis=findViewById(R.id.btnRegister)
        sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        layoutManager= LinearLayoutManager(this@RegisterUser)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val queue = Volley.newRequestQueue(this@RegisterUser)
        val url = "http://13.235.250.119/v2/register/fetch_result"


        btnRegis.setOnClickListener {
            sendRegisterRequest(etName.text.toString(),etMobiRegi.text.toString(),etAddRegister.text.toString(),etPassConfm.text.toString(),etEmailRegis.text.toString())
        }

    }


    fun sendRegisterRequest(name: String, phone: String, address: String, password: String, email: String) {
        val queue = Volley.newRequestQueue(this)
        val register="http://13.235.250.119/v2/register/fetch_result"
        val jsonParams = JSONObject()
        jsonParams.put("name", name)
        jsonParams.put("mobile_number", phone)
        jsonParams.put("password", password)
        jsonParams.put("address", address)
        jsonParams.put("email", email)
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,register,jsonParams,Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val response = data.getJSONObject("data")
                        sharedPreferences.edit().putString("user_id", response.getString("user_id")).apply()
                        sharedPreferences.edit().putString("user_name", response.getString("name")).apply()
                        sharedPreferences.edit().putString("user_mobile_number",response.getString("mobile_number")).apply()
                        sharedPreferences.edit().putString("user_address", response.getString("address")).apply()
                        sharedPreferences.edit().putString("user_email", response.getString("email")).apply()
                        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                        startActivity(Intent(this@RegisterUser,DashboardActivity::class.java))
                        finish()
                    } else {

                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(
                            this@RegisterUser,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception){

                    e.printStackTrace()
                }
            },
            Response.ErrorListener {Toast.makeText(this@RegisterUser, it.message, Toast.LENGTH_SHORT).show() }
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

            override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }

}