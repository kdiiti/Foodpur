package com.kuldeep.foodpur.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.adapter.HomeRecyclerAdapter
import com.kuldeep.foodpur.adapter.OrderHistoryAdapter
import com.kuldeep.foodpur.model.Dish
import com.kuldeep.foodpur.model.Food
import com.kuldeep.foodpur.model.FoodItem
import com.kuldeep.foodpur.model.History
import org.json.JSONException


class OHistoryFragment : Fragment() {
    lateinit var recyclerHistory: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryAdapter
    val orderHistory= arrayListOf<History>()
    lateinit var sharedPreferences: SharedPreferences
   lateinit var foodObject:FoodItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_o_history, container, false)
        recyclerHistory=view.findViewById(R.id.recyclerOhistory)
        layoutManager=LinearLayoutManager(activity as Context)
        sharedPreferences=getContext()!!.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        var restraId:String=""
        val id=sharedPreferences.getString("user_id","user_id")
        val queue= Volley.newRequestQueue(activity as Context)
        val url=" http://13.235.250.119/v2/orders/fetch_result/$id"
        val jsonObjectRequest=object : JsonObjectRequest(Request.Method.GET,url,null, Response.Listener{
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {

                    val historyArray = data.getJSONArray("data")
                    for (i in 0 until historyArray.length()) {
                        val dishJsonObject = historyArray.getJSONObject(i)
                        val foodItem=dishJsonObject.getJSONArray("food_items")
                        for (i in 0 until foodItem.length()){
                            val foodJsonObject=foodItem.getJSONObject(i)
                            foodObject=FoodItem(
                                foodJsonObject.getString("food_item_id"),
                                foodJsonObject.getString("name"),
                                foodJsonObject.getString("cost")
                            )
                        }
                        val dishObject = History(
                            dishJsonObject.getString("order_id"),
                            dishJsonObject.getString("restaurant_name"),
                            dishJsonObject.getString("total_cost"),
                            dishJsonObject.getString("order_placed_at"),
                            foodObject
                        )
                        orderHistory.add(dishObject)
                        recyclerAdapter = OrderHistoryAdapter(activity as Context, orderHistory)
                        recyclerHistory.adapter = recyclerAdapter
                        recyclerHistory.layoutManager = layoutManager
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener {

            Toast.makeText(activity as Context,"Some Volley Error Occurred", Toast.LENGTH_SHORT).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "7dfdc86f820a48"
                return headers
            }

        }
        queue.add(jsonObjectRequest)
        return view
    }


}