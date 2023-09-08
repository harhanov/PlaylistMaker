package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private var settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()

    fun setSettingsInteractor(interactor: SettingsInteractor) {
        settingsInteractor = interactor
    }

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

    companion object {
        fun getViewModelFactory(settingsInteractor: SettingsInteractor, sharingInteractor: SharingInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    settingsInteractor,
                    sharingInteractor,
                )
            }
        }
    }
}


