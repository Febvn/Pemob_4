package com.example.myfirstkmpapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette

import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton

/**
 * ProfileHeader — Composable Function #1
 *
 * Menampilkan foto profil (circular) dengan border "leather stitching",
 * nama pengguna, dan title/role.
 *
 * @param name Nama pengguna
 * @param title Role / titel pengguna
 * @param image Foto profil kustom
 * @param onImageClick Callback saat foto diklik
 * @param onRemoveImageClick Callback saat tombol hapus foto diklik
 * @param modifier Modifier opsional
 */
@Composable
fun ProfileHeader(
    name: String,
    title: String,
    image: ImageBitmap? = null,
    onImageClick: (() -> Unit)? = null,
    onRemoveImageClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val palette = LocalSkeuPalette.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // === Circular Profile Image with Skeuomorphic Border ===
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(130.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = palette.shadowDark,
                        spotColor = palette.shadowDark
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.secondary,
                                palette.primary,
                                palette.primaryVariant,
                            )
                        )
                    )
                    .border(
                        width = 4.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                palette.secondaryVariant,
                                palette.stitching,
                                palette.primaryVariant,
                            )
                        ),
                        shape = CircleShape
                    )
                    .let { 
                        if (onImageClick != null) it.clickable(onClick = onImageClick) else it
                    }
            ) {
                // Inner ring untuk efek "sunken" / embossed
                Box(
                    modifier = Modifier
                        .size(118.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    palette.surface,
                                    palette.surfaceVariant,
                                )
                            )
                        )
                        .border(
                            width = 2.dp,
                            color = palette.shadowLight,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (image != null) {
                        Image(
                            bitmap = image,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.size(64.dp),
                            tint = palette.primary
                        )
                    }
                }
            }

            // Tombol "X" untuk hapus foto (overlap di kanan atas)
            if (image != null && onRemoveImageClick != null) {
                IconButton(
                    onClick = onRemoveImageClick,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = 4.dp, y = (-4).dp)
                        .shadow(4.dp, CircleShape)
                        .background(palette.surface, CircleShape)
                        .border(1.dp, palette.shadowLight, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Photo",
                        tint = palette.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Name ===
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            ),
            textAlign = TextAlign.Center,
            color = palette.onBackground,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // === Title / Role ===
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = palette.shadowDark,
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            palette.primary,
                            palette.primaryVariant,
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = palette.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                ),
            )
        }
    }
}
