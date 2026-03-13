package com.example.myfirstkmpapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette

/**
 * InfoItem — Composable Function #2
 *
 * Row reusable untuk menampilkan informasi (Email, Phone, Location, dll.)
 * dengan icon di kiri dan label/value di kanan.
 * Menggunakan gaya skeuomorphic dengan efek "inset" pada icon container.
 *
 * @param icon ImageVector dari Material Icons (Lucid Style)
 * @param label Label informasi (contoh: "Email")
 * @param value Nilai informasi (contoh: "user@email.com")
 * @param modifier Modifier opsional
 */
@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val palette = LocalSkeuPalette.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = palette.shadowDark,
                spotColor = palette.shadowDark,
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.surface,
                        palette.surfaceVariant,
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.shadowLight,
                        palette.shadowDark.copy(alpha = 0.1f),
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // === Icon Container (inset / embossed circle) ===
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = CircleShape,
                    ambientColor = palette.shadowDark,
                )
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            palette.secondaryVariant.copy(alpha = 0.3f),
                            palette.background,
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            palette.shadowDark.copy(alpha = 0.15f),
                            palette.shadowLight.copy(alpha = 0.5f),
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp),
                tint = palette.primary,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // === Label & Value ===
        Column {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = palette.onSurfaceLight,
                ),
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = palette.onBackground,
                ),
            )
        }
    }
}
