package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSearch = findViewById<Button>(R.id.button_search)
        val imageMedia = findViewById<Button>(R.id.button_media)
        val imageSettings = findViewById<Button>(R.id.button_settings)

        imageSearch.setOnClickListener{
            val searchIntent = Intent(this, MainActivity::class.java)
            startActivity(searchIntent)
        }

        imageMedia.setOnClickListener{
            val mediaIntent = Intent(this, MainActivity::class.java)
            startActivity(mediaIntent)
        }

        imageSettings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

}