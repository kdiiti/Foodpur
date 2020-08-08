package com.kuldeep.foodpur.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.activity.RestaDetailActivity
import com.kuldeep.foodpur.database.RestraEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context:Context,val restraList:List<RestraEntity>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtDishName: TextView =view.findViewById(R.id.txtDishName)
        val txtDishCost: TextView =view.findViewById(R.id.txtDishCost)
        val txtDishRating: TextView =view.findViewById(R.id.txtDishRating)
        val imgDishImage: ImageView =view.findViewById(R.id.imgDishImage)
        val llContent: LinearLayout =view.findViewById(R.id.llContent)
        val imgAddFavt: ImageView =view.findViewById(R.id.imgAddFavt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restraList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val dish=restraList[position]
        holder.txtDishName.text=dish.name
        holder.txtDishCost.text=dish.cost_for_one
        holder.txtDishRating.text=dish.rating
        Picasso.get().load(dish.image_url).error(R.drawable.app_main_icon).into(holder.imgDishImage)
        holder.imgAddFavt.setImageResource(R.drawable.ic_favtfill)
        holder.llContent.setOnClickListener {
            val intent= Intent(context, RestaDetailActivity::class.java)
            intent.putExtra("food_id",dish.id.toString())
            context.startActivity(intent)
        }
    }
}