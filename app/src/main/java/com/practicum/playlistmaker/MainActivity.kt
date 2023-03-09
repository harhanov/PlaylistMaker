package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSearch = findViewById<Button>(R.id.button_search)
        val imageMedia = findViewById<Button>(R.id.button_media)
        val imageSettings = findViewById<Button>(R.id.button_settings)

        imageMedia.setOnClickListener{
            val mediaIntent = Intent(this, MainActivity::class.java)
            startActivity(mediaIntent)

        }

        imageSettings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)

        }
        imageSearch.setOnClickListener{
          val searchActivityIntent = Intent(this, SearchActivity::class.java)
           startActivity(searchActivityIntent)

        }
    }

}