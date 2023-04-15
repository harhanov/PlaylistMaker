package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backFromSettings = findViewById<View>(R.id.exitSettings)
        backFromSettings.setOnClickListener {
            finish()
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            // Сохраняем состояние переключателя в SharedPreferences
            val sharedPrefs = App.sharedPrefs
            sharedPrefs.edit().putBoolean("dark_theme", checked).apply()
        }

        val shareApp = findViewById<View>(R.id.shareArea)

        shareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_the_course))
            val chooser = Intent.createChooser(shareIntent, R.string.sharing_text.toString())
            startActivity(chooser)
        }

        val supportWrite = findViewById<View>(R.id.writeToSupport)
        supportWrite.setOnClickListener {
            val email = arrayOf(getString(R.string.mail_address))
            val subject = getString(R.string.mail_subject)
            val message = getString(R.string.mail_message)
            val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(Intent.createChooser(sendIntent, R.string.send_message_title.toString()))
        }
        val termsOfService = findViewById<View>(R.id.serviceTerms)
        termsOfService.setOnClickListener {
            val url = R.string.offer_link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
            startActivity(intent)
        }

    }
}