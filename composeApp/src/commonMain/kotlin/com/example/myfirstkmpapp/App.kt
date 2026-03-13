package com.example.myfirstkmpapp

import androidx.compose.runtime.*
import com.example.myfirstkmpapp.ui.screen.ProfileScreen
import com.example.myfirstkmpapp.ui.theme.SkeuomorphicTheme
import com.example.myfirstkmpapp.ui.theme.SkeuPalettes

import com.example.myfirstkmpapp.model.ProfileData

/**
 * App — Entry point utama aplikasi
 *
 * Menerapkan SkeuomorphicTheme dan menampilkan ProfileScreen.
 */
@Composable
fun App() {
    var currentPalette by remember { mutableStateOf(SkeuPalettes.Leather) }
    var profileData by remember { mutableStateOf(ProfileData()) }

    SkeuomorphicTheme(palette = currentPalette) {
        ProfileScreen(
            profileData = profileData,
            onProfileDataChange = { profileData = it },
            onPaletteChange = { currentPalette = it }
        )
    }
}