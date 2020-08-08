package com.kuldeep.foodpur.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.activity.RestaDetailActivity
import com.kuldeep.foodpur.database.FoodDatabase
import com.kuldeep.foodpur.database.RestraEntity
import com.kuldeep.foodpur.model.Dish
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_single_row.view.*

class HomeRecyclerAdapter (val context: Context, val itemList: ArrayList<Dish>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtDishName:TextView=view.findViewById(R.id.txtDishName)
        val txtDishCost:TextView=view.findViewById(R.id.txtDishCost)
        val txtDishRating:TextView=view.findViewById(R.id.txtDishRating)
        val imgDishImage:ImageView=view.findViewById(R.id.imgDishImage)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
        val imgAddFavt:ImageView=view.findViewById(R.id.imgAddFavt)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val dish=itemList[position]
        holder.txtDishName.text=dish.name
        holder.txtDishCost.text="${dish.cost_for_one} Rs. "
        holder.txtDishRating.text=dish.rating
        Picasso.get().load(dish.image_url).error(R.drawable.app_main_icon).into(holder.imgDishImage)

        val restraEntity=RestraEntity(
            dish.id?.toInt() as Int,dish.name,dish.rating,dish.cost_for_one,dish.image_url
        )
        val checkFav=DBAsyncTask(context,restraEntity,1 ).execute()
        val isFav=checkFav.get()
        if (isFav){
            val image=R.drawable.ic_favtfill
            holder.imgAddFavt.setImageResource(image)
        }
        else{
            holder.imgAddFavt.setImageResource(R.drawable.ic_favt)
        }

        holder.imgAddFavt.setOnClickListener {
            if (!DBAsyncTask(context,restraEntity,1).execute().get())
            {
                val async=DBAsyncTask(context,restraEntity,2).execute()
                val result=async.get()
                if (result){
                    Toast.makeText(context,"Restaurant Added to favourites",Toast.LENGTH_SHORT).show()
                    holder.imgAddFavt.setImageResource(R.drawable.ic_favtfill)
                }
                else{
                    Toast.makeText(context,"Some Error occurred",Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                val async=DBAsyncTask(context,restraEntity,3).execute()
                val result=async.get()
                if (result){
                    Toast.makeText(context,"Restaurant Removed from favourites",Toast.LENGTH_SHORT).show()
                    holder.imgAddFavt.setImageResource(R.drawable.ic_favt)
                }
                else{
                    Toast.makeText(context,"Some Error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.llContent.setOnClickListener {
            val intent=Intent(context, RestaDetailActivity::class.java)
            intent.putExtra("food_id",dish.id)
            context.startActivity(intent)
        }

    }

    class DBAsyncTask(val context: Context,val restraEntity: RestraEntity,val mode:Int) : AsyncTask<Void,Void,Boolean>(){
        val db=Room.databaseBuilder(context,FoodDatabase::class.java,"restra-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    val restra:RestraEntity?=db.restraDao().getRestraById(restraEntity.id)
                    db.close()
                    return restra != null
                }
                2->{
                    db.restraDao().insertResta(restraEntity)
                    db.close()
                    return true
                }
                3->{
                    db.restraDao().deleteRestra(restraEntity)
                    db.close()
                    return true
                }

            }
           return false
        }

    }


}