package com.practicum.playlistmaker.utils

import android.app.Activity
import android.view.View
import androidx.constraintlayout.widget.Group
import com.practicum.playlistmaker.R

object BottomNavigationUtils {

    fun hideBottomNavigationView(activity: Activity?) {

        val groupBottomNavigation = activity?.findViewById<Group>(R.id.groupBottomNavigation)
        groupBottomNavigation?.visibility = View.GONE

    }

    fun showBottomNavigationView(activity: Activity?) {
        val groupBottomNavigation = activity?.findViewById<Group>(R.id.groupBottomNavigation)
        groupBottomNavigation?.visibility = View.VISIBLE
    }
}