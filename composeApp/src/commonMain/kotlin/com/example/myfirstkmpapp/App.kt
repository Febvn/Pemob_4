package com.example.myfirstkmpapp

import androidx.compose.runtime.*
import com.example.myfirstkmpapp.ui.screen.ProfileScreen
import com.example.myfirstkmpapp.ui.theme.SkeuomorphicTheme
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileUiState
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * App — Entry point utama aplikasi
 *
 * Menerapkan SkeuomorphicTheme dan menampilkan ProfileScreen.
 */
@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    SkeuomorphicTheme(
        palette = uiState.currentPalette,
        isDark = uiState.isDarkTheme
    ) {
        ProfileScreen(viewModel = viewModel)
    }
}