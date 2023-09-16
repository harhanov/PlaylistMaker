package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private var settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()

    fun getThemeSettingsLiveData(): LiveData<ThemeSettings> {
        themeSettingsLiveData.postValue(settingsInteractor.getThemeSettings())
        return themeSettingsLiveData
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(settings)
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

}
