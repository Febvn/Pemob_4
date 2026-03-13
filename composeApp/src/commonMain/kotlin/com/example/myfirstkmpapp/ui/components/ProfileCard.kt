package com.example.myfirstkmpapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette

/**
 * ProfileCard — Composable Function #3
 *
 * Container utama dengan gaya skeuomorphic.
 * Memberikan efek "raised" / terangkat dari background
 * dengan border yang menyerupai jahitan kulit (stitching).
 *
 * @param title Judul section (opsional, ditampilkan di atas konten)
 * @param elevation Tingkat bayangan / kedalaman
 * @param content Konten composable yang ditampilkan di dalam card
 */
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    elevation: Dp = 10.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val palette = LocalSkeuPalette.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Outer shadow — efek "floating"
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(20.dp),
                ambientColor = palette.shadowDark,
                spotColor = palette.shadowDark,
            )
            .clip(RoundedCornerShape(20.dp))
            // Background gradient — efek depth / kulit premium
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.surface,
                        palette.surfaceVariant.copy(alpha = 0.6f),
                        palette.surface,
                    )
                )
            )
            // Border stitching — efek jahitan kulit
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        palette.shadowLight.copy(alpha = 0.7f),
                        palette.stitching.copy(alpha = 0.4f),
                        palette.shadowDark.copy(alpha = 0.2f),
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp),
    ) {
        // === Section Title ===
        if (title != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Decorative line (kiri)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    palette.stitching.copy(alpha = 0f),
                                    palette.stitching.copy(alpha = 0.6f),
                                )
                            )
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = palette.onSurfaceLight,
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Decorative line (kanan)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    palette.stitching.copy(alpha = 0.6f),
                                    palette.stitching.copy(alpha = 0f),
                                )
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // === Card Content ===
        content()
    }
}
