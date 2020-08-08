package com.kuldeep.foodpur.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.adapter.CartRecyclerAdapter
import com.kuldeep.foodpur.database.FoodDatabase
import com.kuldeep.foodpur.database.FoodEntity
import com.kuldeep.foodpur.model.Food
import com.kuldeep.foodpur.utill.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerCart: RecyclerView
    lateinit var CartRecyclerAdapter: CartRecyclerAdapter
    lateinit var txtNameOfRestaurant: TextView
    lateinit var progressLayout: RelativeLayout
    lateinit var btnPlaceOrder: Button
    lateinit var layoutManager: RecyclerView.LayoutManager
    val orderList= arrayListOf<Food>()
    lateinit var sharedPreferences: SharedPreferences
    var restaurantName: String? = null
    var restaurantId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        recyclerCart=findViewById(R.id.recyclerCart)
        txtNameOfRestaurant=findViewById(R.id.txtNameOfRestaurant)
        progressLayout=findViewById(R.id.progressLayout)
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder)
        layoutManager= LinearLayoutManager(this@CartActivity)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="My Cart"
        sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        progressLayout.visibility=View.GONE
        if(intent!=null)
        {
            val bundle=intent.getBundleExtra("data")
            restaurantName=bundle?.getString("resName")
            restaurantId=bundle?.getString("resId")
        }else {
            finish()
            Toast.makeText(
                this@CartActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (restaurantId == null || restaurantName == null) {
            finish()
            Toast.makeText(
                this@CartActivity,
                "Some error occur!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        txtNameOfRestaurant.text=restaurantName
        val dbList=GetOrders(this,restaurantId.toString()).execute().get()
        for(data in dbList)
        {
            orderList.addAll(
                Gson().fromJson(data.foodItems,Array<Food>::class.java).asList()
            )
        }
        CartRecyclerAdapter=CartRecyclerAdapter(this,orderList )
        recyclerCart.adapter=CartRecyclerAdapter(this,orderList)
        recyclerCart.layoutManager=layoutManager
        var totalCost=0
        for(i in 0 until orderList.size)
        {
            totalCost+=orderList[i].cost_for_one.toInt()
        }
        btnPlaceOrder.text="Place Order(Total: Rs. $totalCost )"

        btnPlaceOrder.setOnClickListener {
            progressLayout.visibility=View.VISIBLE
            sendRequest(totalCost)
        }
    }
    fun sendRequest(totalCost:Int){
        val queue= Volley.newRequestQueue(this@CartActivity)
        val url=" http://13.235.250.119/v2/place_order/fetch_result/"
        val jsonObject= JSONObject()
        jsonObject.put("user_id",sharedPreferences.getString("user_id",null))
        jsonObject.put("restaurant_id",restaurantId)
        jsonObject.put("total_cost",totalCost.toString())
        val food= JSONArray()

        for(i in 0 until orderList.size)
        {
            val foodId=JSONObject()
            foodId.put("food_item_id",orderList[i].id)
            food.put(i,foodId)
        }

        jsonObject.put("food",food)

        if (ConnectionManager().checkConnectivity(this@CartActivity))
        {
            val jsonObjectRequest=object : JsonObjectRequest(Method.POST,url,jsonObject, Response.Listener {
                try {

                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    if(success)
                    {
                        ClearCart(this@CartActivity,restaurantId.toString()).execute().get()

                        val intent= Intent(this@CartActivity,ConfirmActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    else
                    {
                        Toast.makeText(
                            this@CartActivity,
                            "Some error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                catch (e: JSONException){
                    Toast.makeText(
                        this@CartActivity,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
                if (this@CartActivity != null) {
                    Toast.makeText(
                        this@CartActivity,
                        "Volley error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "7dfdc86f820a48"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
        else
        {
            Toast.makeText(this@CartActivity,"No Internet Connection",Toast.LENGTH_SHORT).show()
        }
    }
    class GetOrders(val context: Context,val restaurantId:String):
        AsyncTask<Void, Void, List<FoodEntity>>()
    {
        val db= Room.databaseBuilder(context,FoodDatabase::class.java,"restra-db").build()
        override fun doInBackground(vararg params: Void?): List<FoodEntity> {
            return db.foodDao().getAllOrders()
        }
    }
    class ClearCart(val context:Context,val restaurantId: String):AsyncTask<Void,Void,Boolean>()
    {
        val db= Room.databaseBuilder(context,FoodDatabase::class.java,"restra-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.foodDao().deleteOrders(restaurantId)
            db.close()
            return true
        }

    }
    override fun onBackPressed() {

        ClearCart(this@CartActivity,restaurantId.toString()).execute().get()
        super.onBackPressed()
    }
}