package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity(), KoinComponent {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exitSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = binding.themeSwitcher

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

