package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private lateinit var settingsRepository: SettingsRepository

    fun setSettingsRepository(repository: SettingsRepository) {
        settingsRepository = repository
    }

    fun getThemeSettingsLiveData(): LiveData<ThemeSettings> {
        return settingsRepository.getThemeSettingsLiveData()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.writeToSupport()
    }

    fun openTermsOfService() {
        sharingInteractor.openTermsOfService()
    }

    companion object {
        fun getViewModelFactory(sharingInteractor: SharingInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    sharingInteractor,
                                    )
            }
        }
    }

}