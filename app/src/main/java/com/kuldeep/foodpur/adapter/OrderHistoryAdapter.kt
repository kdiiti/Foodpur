package com.kuldeep.foodpur.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.model.Food
import com.kuldeep.foodpur.model.History

class OrderHistoryAdapter (val context: Context,val itemList:ArrayList<History>):RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>(){
    class OrderHistoryViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtRestraName: TextView =view.findViewById(R.id.txtRestraNameHistory)
        val txtDateHistory: TextView =view.findViewById(R.id.txtDateHistory)
        val txtFoodNameHistory: TextView =view.findViewById(R.id.txtFoodNameHistory)
        val txtPriceOfFood: TextView =view.findViewById(R.id.txtPriceHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history,parent,false)
        return OrderHistoryAdapter.OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
       val order=itemList[position]
        holder.txtRestraName.text=order.restraName
        holder.txtDateHistory.text=order.date
        holder.txtFoodNameHistory.text=order.food_items.name
        holder.txtPriceOfFood.text=order.food_items.cost


    }
}