package com.example.myfirstkmpapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * ColorWheel — Pemilih warna Hue (lingkaran)
 * 
 * Memungkinkan pengguna memilih warna dasar untuk aplikasi.
 * 
 * @param onColorChanged Callback saat warna berubah (terpilih)
 */
@Composable
fun ColorWheel(
    modifier: Modifier = Modifier,
    onColorChanged: (Color) -> Unit
) {
    var center by remember { mutableStateOf(Offset.Zero) }
    var radius by remember { mutableStateOf(0f) }
    
    // Fungsi untuk mendapatkan warna dari sudut/angle
    fun getColorAtAngle(angle: Float): Color {
        val normalizedAngle = (angle + 360f) % 360f
        val h = normalizedAngle / 360f
        
        // Helper HSL to Color
        fun hslToColor(h: Float, s: Float, l: Float): Color {
            val q = if (l < 0.5f) l * (1f + s) else l + s - l * s
            val p = 2f * l - q
            fun hue2rgb(p: Float, q: Float, t: Float): Float {
                var varT = t
                if (varT < 0f) varT += 1f
                if (varT > 1f) varT -= 1f
                if (varT < 1f / 6f) return p + (q - p) * 6f * varT
                if (varT < 1f / 2f) return q
                if (varT < 2f / 3f) return p + (q - p) * (2f / 3f - varT) * 6f
                return p
            }
            return Color(
                red = hue2rgb(p, q, h + 1f / 3f),
                green = hue2rgb(p, q, h),
                blue = hue2rgb(p, q, h - 1f / 3f),
                alpha = 1f
            )
        }
        return hslToColor(h, 0.8f, 0.5f)
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val angle = atan2(offset.y - center.y, offset.x - center.x) * (180f / PI.toFloat())
                    onColorChanged(getColorAtAngle(angle))
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val offset = change.position
                    val angle = atan2(offset.y - center.y, offset.x - center.x) * (180f / PI.toFloat())
                    onColorChanged(getColorAtAngle(angle))
                }
            }
    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            center = Offset(size.width / 2, size.height / 2)
            radius = size.minDimension / 2 - 10.dp.toPx()
            
            val sweepGradient = Brush.sweepGradient(
                colors = listOf(
                    Color.Red, Color.Magenta, Color.Blue, Color.Cyan, 
                    Color.Green, Color.Yellow, Color.Red
                ),
                center = center
            )
            
            drawCircle(
                brush = sweepGradient,
                radius = radius,
                style = Stroke(width = 20.dp.toPx())
            )
        }
    }
}
