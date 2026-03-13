package com.example.myfirstkmpapp.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * Membuka dialog pemilihan file gambar dan mengembalikan bytes-nya.
 */
fun pickImageFile(onImagePicked: (ByteArray?) -> Unit) {
    val dialog = FileDialog(null as Frame?, "Pilih Foto Profil", FileDialog.LOAD).apply {
        file = "*.jpg;*.png;*.jpeg;*.bmp"
        isVisible = true
    }
    val dir = dialog.directory
    val file = dialog.file
    if (dir != null && file != null) {
        onImagePicked(File(dir, file).readBytes())
    } else {
        onImagePicked(null)
    }
}

/**
 * Konversi ByteArray ke ImageBitmap menggunakan Skia.
 */
fun ByteArray.toImageBitmap(): ImageBitmap {
    return org.jetbrains.skia.Image.makeFromEncoded(this).toComposeImageBitmap()
}
