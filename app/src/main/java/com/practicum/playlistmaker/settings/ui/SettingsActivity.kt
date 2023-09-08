package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.data.ExternalNavigator

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exitSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = binding.themeSwitcher

        val sharingInteractor = Creator.createSharingInteractor(ExternalNavigator(this))
        val settingsInteractor = Creator.createSettingsInteractor(this)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(settingsInteractor, sharingInteractor)
        )[SettingsViewModel::class.java]

        viewModel.setSettingsInteractor(settingsInteractor)

        viewModel.getThemeSettingsLiveData().observe(this) { themeSettings ->
            themeSwitcher.isChecked = themeSettings.isNightModeEnabled
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            val newThemeSettings = ThemeSettings(checked)
            viewModel.updateThemeSetting(newThemeSettings)
        }

        binding.shareArea.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.serviceTerms.setOnClickListener {
            viewModel.openTermsOfService()
        }
    }
}

