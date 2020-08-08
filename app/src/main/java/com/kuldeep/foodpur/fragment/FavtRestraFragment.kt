package com.kuldeep.foodpur.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kuldeep.foodpur.R
import com.kuldeep.foodpur.adapter.FavouriteRecyclerAdapter
import com.kuldeep.foodpur.adapter.HomeRecyclerAdapter
import com.kuldeep.foodpur.database.FoodDatabase
import com.kuldeep.foodpur.database.RestraEntity


class FavtRestraFragment : Fragment() {
    lateinit var recyclerFavt: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    var dbRestraList= listOf<RestraEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_favt_restra, container, false)
        recyclerFavt=view.findViewById(R.id.recyclerFavt)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        layoutManager= LinearLayoutManager(activity)
        dbRestraList=RetrieveFavourites(activity as Context).execute().get()
        if (activity !=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavouriteRecyclerAdapter(activity as Context,dbRestraList)
            recyclerFavt.adapter=recyclerAdapter
            recyclerFavt.layoutManager=layoutManager
        }
        return view
    }
    class RetrieveFavourites(val context:Context):AsyncTask<Void,Void,List<RestraEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<RestraEntity> {
           val db=Room.databaseBuilder(context,FoodDatabase::class.java,"restra-db").build()
            return db.restraDao().getAllRestra()
        }

    }


}