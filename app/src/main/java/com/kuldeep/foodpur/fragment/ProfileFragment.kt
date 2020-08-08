package com.kuldeep.foodpur.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kuldeep.foodpur.R


class ProfileFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtProfileName:TextView
    lateinit var txtMobiNo:TextView
    lateinit var txtEmail:TextView
    lateinit var txtAdd:TextView

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
       sharedPreferences=getContext()!!.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        txtProfileName= view.findViewById(R.id.txtProfileName)
        txtMobiNo=view.findViewById(R.id.txtProfileMobiNo)
        txtEmail=view.findViewById(R.id.txtProfileEmail)
        txtAdd=view.findViewById(R.id.txtProfileAdd)
        txtProfileName.text=sharedPreferences.getString("user_name","Name")
        txtMobiNo.text=sharedPreferences.getString("user_mobile_number","5844")
        txtEmail.text=sharedPreferences.getString("user_email","Email")
        txtAdd.text=sharedPreferences.getString("user_address","Address")

       
        return view
    }


}