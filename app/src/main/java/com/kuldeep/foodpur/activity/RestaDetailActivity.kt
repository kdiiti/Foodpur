package com.kuldeep.foodpur.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.adapter.DetailRecyclerAdapter
import com.kuldeep.foodpur.adapter.FavouriteRecyclerAdapter
import com.kuldeep.foodpur.adapter.HomeRecyclerAdapter
import com.kuldeep.foodpur.database.FoodDatabase
import com.kuldeep.foodpur.database.FoodEntity
import com.kuldeep.foodpur.database.RestraEntity
import com.kuldeep.foodpur.fragment.FavtRestraFragment
import com.kuldeep.foodpur.model.Dish
import com.kuldeep.foodpur.model.DishDetail
import org.json.JSONException
import org.json.JSONObject

class RestaDetailActivity : AppCompatActivity() {
    lateinit var recyclerDetail: RecyclerView
    lateinit var layoutManagerDetail: RecyclerView.LayoutManager
    lateinit var recyclerAdapterDetail: DetailRecyclerAdapter
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val dishDetailList= arrayListOf<DishDetail>()
    val orderList= arrayListOf<DishDetail>()
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var btnproccedToCart:Button
        var restraId:String?=""
        var restraName:String?=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resta_detail)
        recyclerDetail=findViewById(R.id.recyclerCart)
        toolbar=findViewById(R.id.toolbar)
        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)
        layoutManagerDetail=LinearLayoutManager(this)
        btnproccedToCart=findViewById(R.id.btnProccedtoCart)
        val intent: Intent =getIntent()
        restraId = intent.getStringExtra("food_id").toString()
        progressLayout.visibility=View.VISIBLE
        btnproccedToCart.visibility=View.GONE
        setSupportActionBar(toolbar)
        supportActionBar?.title="Add to Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnproccedToCart.setOnClickListener {
            proceedToCart()
        }
        val queue= Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/$restraId"
        val jsonObjectRequest=object :
            JsonObjectRequest(Request.Method.GET,url,null, Response.Listener<JSONObject> {
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    progressLayout.visibility=View.GONE
                    val resArray = data.getJSONArray("data")
                    for (i in 0 until resArray.length()) {
                        val dishJsonObject = resArray.getJSONObject(i)
                        val dishObject = DishDetail(
                            dishJsonObject.getString("id"),
                            dishJsonObject.getString("name"),
                            dishJsonObject.getString("cost_for_one"),
                            dishJsonObject.getString("restaurant_id"),
                            (i+1).toString()
                        )
                        dishDetailList.add(dishObject)
                        recyclerAdapterDetail = DetailRecyclerAdapter(this, dishDetailList ,object :DetailRecyclerAdapter.OnItemClickListener{
                            override fun onAddItemClick(fooditem: DishDetail) {
                            orderList.add(fooditem)
                                if (orderList.size>0){
                                    btnproccedToCart.visibility=View.VISIBLE
                                    DetailRecyclerAdapter.isCartEmpty=false
                                }
                            }

                            override fun onRemoveItemClick(fooditem: DishDetail) {
                                orderList.remove(fooditem)
                                if (orderList.size == 0) {
                                    btnproccedToCart.visibility = View.GONE
                                    DetailRecyclerAdapter.isCartEmpty = true
                                }
                            }
                        })
                        recyclerDetail.adapter = recyclerAdapterDetail
                        recyclerDetail.layoutManager=layoutManagerDetail


                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener {
                error: VolleyError? ->
            Toast.makeText(this, error?.message, Toast.LENGTH_SHORT).show()

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
    fun proceedToCart(){
        val gson= Gson()
        val foodItems=gson.toJson(orderList)
        val async=ItemsOfCart(this, restraId.toString(),foodItems,1).execute()
        val result=async.get()
        if (result) {
            val data = Bundle()
            data.putString("resId", restraId)
            data.putString("resName", restraName)
            val intent = Intent(this@RestaDetailActivity, CartActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Some unexpected error", Toast.LENGTH_SHORT)
                .show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }
    class ItemsOfCart(
        context: Context,
        val restaurantId: String,
        val foodItems: String,
        val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "restra-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.foodDao().insertOrder(FoodEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }

                2 -> {
                    db.foodDao().deleteOrder(FoodEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }
            }

            return false
        }

    }


}