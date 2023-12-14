package com.practicum.playlistmaker.utils

import android.app.Activity
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R

object BottomNavigationUtils {

    fun hideBottomNavigationView(activity: Activity?) {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
    }

    fun showBottomNavigationView(activity: Activity?) {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
    }
}