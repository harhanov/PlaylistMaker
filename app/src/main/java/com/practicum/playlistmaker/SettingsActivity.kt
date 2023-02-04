package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backFromSettings = findViewById<View>(R.id.exitSettings)
        backFromSettings.setOnClickListener{
            val backSettingsIntent = Intent(this, MainActivity::class.java)
            startActivity(backSettingsIntent)
        }
    }
}