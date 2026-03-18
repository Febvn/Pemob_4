package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myfirstkmpapp.data.ProfileData
import com.example.myfirstkmpapp.ui.theme.SkeuPalette
import com.example.myfirstkmpapp.ui.theme.generateSkeuPalette

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ProfileViewModel — ViewModel untuk mengelola state Profile.
 *
 * Menggunakan StateFlow untuk reaktivitas UI.
 */
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Update data profil secara keseluruhan.
     */
    fun updateProfile(newData: ProfileData) {
        _uiState.update { it.copy(profileData = newData) }
    }

    /**
     * Simpan perubahan profil dari form edit.
     */
    fun onSaveProfile(newProfile: ProfileData) {
        _uiState.update { it.copy(profileData = newProfile) }
    }

    /**
     * Toggle Dark/Light Mode.
     */
    fun toggleTheme(isDark: Boolean) {
        _uiState.update { 
            val newPalette = generateSkeuPalette(it.currentPalette.primary, isDark)
            it.copy(isDarkTheme = isDark, currentPalette = newPalette)
        }
    }


    /**
     * Update palet warna berdasarkan warna dasar yang dipilih.
     */
    fun updatePalette(palette: SkeuPalette) {
        _uiState.update { it.copy(currentPalette = palette) }
    }
}
