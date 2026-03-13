package com.example.myfirstkmpapp.model

/**
 * ProfileData — Data class untuk menyimpan informasi profil pengguna.
 */
data class ProfileData(
    val name: String = "John Doe",
    val title: String = "Software Engineer",
    val bio: String = "Write your biography here. Tell the world about your passion, experience, and what you are currently working on. Make it interesting!",
    val email: String = "john.doe@example.com",
    val phone: String = "-",
    val location: String = "-",
    val university: String = "-",
    val major: String = "-",
    val skills: String = "-",
    val profileImage: ByteArray? = null
)
