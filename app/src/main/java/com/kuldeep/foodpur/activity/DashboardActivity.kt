package com.kuldeep.foodpur.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kuldeep.foodpur.*
import com.kuldeep.foodpur.fragment.*
import androidx.core.content.edit as edit1

class DashboardActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences:SharedPreferences
    var previousMenuItem:MenuItem? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        drawerLayout=findViewById(R.id.drawerll)
        coordinatorLayout=findViewById(R.id.clayout)
        toolbar=findViewById(R.id.naviToolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationBar)
        sharedPreferences=getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        setUpToolbar()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@DashboardActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        openHome()
        navigationView.setNavigationItemSelectedListener {
           if (previousMenuItem!=null){
               previousMenuItem?.isChecked=false
           }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when (it.itemId){
                R.id.home ->{
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.myProfile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )
                        .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title="My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.fvtRestra ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavtRestraFragment()
                        )
                        .addToBackStack("Favourites")
                        .commit()
                    supportActionBar?.title="Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory ->{ supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        OHistoryFragment()
                    )
                    .addToBackStack("History")
                    .commit()
                    supportActionBar?.title="Order History"
                    drawerLayout.closeDrawers()}
                R.id.faq ->{ supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        FAQFragment()
                    )
                    .addToBackStack("FAQ")
                    .commit()
                    supportActionBar?.title="Frequently Asked Questions"
                    drawerLayout.closeDrawers()}
                R.id.logOut ->{
                    val dialog= AlertDialog.Builder(this@DashboardActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out? ")
                    dialog.setPositiveButton("Yes"){
                            text,listener ->
                        var intent=Intent(this@DashboardActivity,LoginActivity::class.java)
                        sharedPreferences.edit().clear().apply()
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No"){text,listener->
                        drawerLayout.closeDrawer(Gravity.LEFT)
                    }
                    dialog.create()
                    dialog.show()


                }
            }
            return@setNavigationItemSelectedListener true
        }
         val header:View = navigationView.getHeaderView(0)
        val name: TextView = header.findViewById(R.id.txtNameDrawer)
        val mobi:TextView=header.findViewById(R.id.txtMobidrawer)
        name.text=sharedPreferences.getString("user_name","Name")
        mobi.text=sharedPreferences.getString("user_mobile_number","Mobile No.")
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       val id=item.itemId
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openHome(){
        val fragment= HomeFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.home)

    }

    override fun onBackPressed() {
        val fragment=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (fragment){
            !is HomeFragment -> openHome()

            else ->ActivityCompat.finishAffinity(this@DashboardActivity)
        }
    }

}