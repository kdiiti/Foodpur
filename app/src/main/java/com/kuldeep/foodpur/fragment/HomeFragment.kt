package com.kuldeep.foodpur.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.adapter.HomeRecyclerAdapter
import com.kuldeep.foodpur.database.RestraEntity
import com.kuldeep.foodpur.model.Dish
import com.kuldeep.foodpur.utill.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class HomeFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar:ProgressBar
    val dishInfoList= arrayListOf<Dish>()
    val ratingComparator= Comparator<Dish>{
            dish1,dish2 ->
        if (dish1.rating.compareTo(dish2.rating,true)==0){
            dish1.name.compareTo(dish2.name,true)
        }else{
            dish1.rating.compareTo(dish2.rating,true)
        }

    }
    val costComparator= Comparator<Dish>{
            dish1,dish2 ->
        if (dish1.cost_for_one.compareTo(dish2.cost_for_one,true)==0){
            dish1.name.compareTo(dish2.name,true)
        }else{
            dish1.cost_for_one.compareTo(dish2.cost_for_one,true)
        }
    }
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        recyclerHome=view.findViewById(R.id.recyclerHome)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        layoutManager=LinearLayoutManager(activity)
        progressLayout.visibility=View.VISIBLE
        setHasOptionsMenu(true)
        val queue=Volley.newRequestQueue(activity as Context)
        val url=" http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
        val jsonObjectRequest=object :JsonObjectRequest(Request.Method.GET,url,null,Response.Listener{
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    progressLayout.visibility=View.GONE
                    val resArray = data.getJSONArray("data")
                    for (i in 0 until resArray.length()) {
                        val dishJsonObject = resArray.getJSONObject(i)
                        val dishObject = Dish(
                            dishJsonObject.getString("id"),
                            dishJsonObject.getString("name"),
                            dishJsonObject.getString("rating"),
                            dishJsonObject.getString("cost_for_one"),
                            dishJsonObject.getString("image_url")

                        )
                        dishInfoList.add(dishObject)
                        recyclerAdapter = HomeRecyclerAdapter(activity as Context, dishInfoList)
                        recyclerHome.adapter = recyclerAdapter
                        recyclerHome.layoutManager=layoutManager

                }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },Response.ErrorListener {

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
        } else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings"){
                    text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
            dialog.setCancelable(false)

        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.rating) {
            Collections.sort(dishInfoList, ratingComparator)
            dishInfoList.reverse()
        }
        if (id == R.id.costHL) {
            Collections.sort(dishInfoList, costComparator)
            dishInfoList.reverse()
        }
        if (id==R.id.costLH){
            Collections.sort(dishInfoList, costComparator)
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


}
