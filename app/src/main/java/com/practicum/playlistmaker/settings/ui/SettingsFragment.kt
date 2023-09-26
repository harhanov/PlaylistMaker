package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(), KoinComponent {

    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeSwitcher = binding.themeSwitcher

        viewModel.getThemeSettingsLiveData().observe(viewLifecycleOwner) { themeSettings ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

