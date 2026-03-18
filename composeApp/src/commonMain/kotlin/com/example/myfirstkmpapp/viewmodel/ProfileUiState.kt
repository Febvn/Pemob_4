package com.example.myfirstkmpapp.viewmodel

import com.example.myfirstkmpapp.data.ProfileData
import com.example.myfirstkmpapp.ui.theme.SkeuPalette
import com.example.myfirstkmpapp.ui.theme.SkeuPalettes

/**
 * ProfileUiState — Representasi state untuk UI Profile.
 *
 * @property profileData Data profil pengguna
 * @property isDarkTheme Status tema (dark mode)
 * @property currentPalette Palet warna skeuomorphic yang sedang aktif
 */
data class ProfileUiState(
    val profileData: ProfileData = ProfileData(),
    val isDarkTheme: Boolean = false,
    val currentPalette: SkeuPalette = SkeuPalettes.Leather
)
