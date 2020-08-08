package com.kuldeep.foodpur.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.database.FoodDatabase
import com.kuldeep.foodpur.database.FoodEntity
import com.kuldeep.foodpur.model.DishDetail
import java.util.ArrayList

class DetailRecyclerAdapter(val context: Context,val itemList:ArrayList<DishDetail>,val listener:OnItemClickListener): RecyclerView.Adapter<DetailRecyclerAdapter.DetailViewHolder>() {
    companion object{
        var isCartEmpty=true
    }
    class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtSrDetail: TextView = view.findViewById(R.id.txtSrNo)
        val txtDishNameDetail: TextView = view.findViewById(R.id.txtFoodName)
        val txtCostDetail: TextView = view.findViewById(R.id.txtFoodPrice)
        val btnAddCart: Button = view.findViewById(R.id.btnAdd)
        val btnRemoveCart:Button=view.findViewById(R.id.btnRemove)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restadetail, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    interface OnItemClickListener{
        fun onAddItemClick(fooditem:DishDetail)
        fun onRemoveItemClick(fooditem: DishDetail)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val dishDetail = itemList[position]
        holder.txtDishNameDetail.text = dishDetail.name
        holder.txtCostDetail.text = "Rs. ${dishDetail.cost_for_one}"
        holder.txtSrDetail.text = dishDetail.srNo

        holder.btnAddCart.setOnClickListener {
            holder.btnAddCart.visibility = View.GONE
            holder.btnRemoveCart.visibility = View.VISIBLE
            listener.onAddItemClick(dishDetail)
        }
        holder.btnRemoveCart.setOnClickListener {
            holder.btnRemoveCart.visibility = View.GONE
            holder.btnAddCart.visibility = View.VISIBLE
            listener.onRemoveItemClick(dishDetail)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

