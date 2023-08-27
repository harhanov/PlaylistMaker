package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backFromSettings = findViewById<View>(R.id.exitSettings)
        backFromSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val settingsRepository = (application as App).getSettingsRepository()

        val externalNavigator = ExternalNavigator(this)

        val sharingInteractor = SharingInteractorImpl(externalNavigator)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(sharingInteractor)
        )[SettingsViewModel::class.java]

        viewModel.setSettingsRepository(settingsRepository)

        viewModel.getThemeSettingsLiveData().observe(this, Observer { themeSettings ->
            themeSwitcher.isChecked = themeSettings.isNightModeEnabled
        })

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            val newThemeSettings = ThemeSettings(checked)
            viewModel.updateThemeSetting(newThemeSettings)
        }

        val shareApp = findViewById<View>(R.id.shareArea)
        shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        val supportWrite = findViewById<View>(R.id.writeToSupport)
        supportWrite.setOnClickListener {
            viewModel.writeToSupport()
        }

        val termsOfService = findViewById<View>(R.id.serviceTerms)
        termsOfService.setOnClickListener {
            viewModel.openTermsOfService()
        }
    }
}
