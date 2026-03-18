package com.example.myfirstkmpapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Skeuomorphic Color Palette Data Class
 */
data class SkeuPalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val secondaryVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceLight: Color,
    val onPrimary: Color,
    val shadowDark: Color,
    val shadowLight: Color,
    val stitching: Color,
    val success: Color,
    val error: Color,
    val info: Color,
    val iconTint: Color
)

/**
 * Dynamic Palette Generator
 * Menghasil palet skeuomorphic lengkap dari satu warna dasar.
 */
fun generateSkeuPalette(baseColor: Color, isDark: Boolean = false): SkeuPalette {
    val hsv = FloatArray(3)
    
    val r = baseColor.red
    val g = baseColor.green
    val b = baseColor.blue
    
    val max = maxOf(r, maxOf(g, b))
    val min = minOf(r, minOf(g, b))
    var h: Float
    var s: Float
    val l_orig = (max + min) / 2f

    if (max == min) {
        h = 0f
        s = 0f
    } else {
        val d = max - min
        s = if (l_orig > 0.5f) d / (2f - max - min) else d / (max + min)
        h = when (max) {
            r -> (g - b) / d + (if (g < b) 6f else 0f)
            g -> (b - r) / d + 2f
            else -> (r - g) / d + 4f
        }
        h /= 6f
    }

    // Fungsi helper untuk membuat warna dari HSL
    fun fromHsl(h: Float, s: Float, l: Float, a: Float = 1f): Color {
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
            alpha = a
        )
    }

    // Adjustment for Dark Mode
    // If isDark is true, we force saturation to 0 for a "MONOCHROME" look
    val s_final = if (isDark) 0.05f else s
    val l_final = if (isDark) l_orig.coerceAtMost(0.2f) else l_orig

    // Generate colors based on the Hue (or Grayscale if isDark)
    val bg = if (isDark) fromHsl(h, 0f, 0.08f) else fromHsl(h, s.coerceAtMost(0.3f), 0.9f)
    val surf = if (isDark) fromHsl(h, 0f, 0.12f) else fromHsl(h, s.coerceAtMost(0.2f), 0.95f)
    val surfVar = if (isDark) fromHsl(h, 0f, 0.05f) else fromHsl(h, s.coerceAtMost(0.25f), 0.85f)
    
    val primary = if (isDark) Color(0xFFE0E0E0) else fromHsl(h, s.coerceAtLeast(0.5f), 0.4f)
    val primaryVar = if (isDark) Color(0xFFA0A0A0) else fromHsl(h, s.coerceAtLeast(0.6f), 0.3f)
    
    val secondary = if (isDark) Color(0xFF757575) else fromHsl(h, s.coerceAtMost(0.4f), 0.6f)

    return SkeuPalette(
        background = bg,
        surface = surf,
        surfaceVariant = surfVar,
        primary = primary,
        primaryVariant = primaryVar,
        secondary = secondary,
        secondaryVariant = if (isDark) Color(0xFF424242) else fromHsl(h, s.coerceAtMost(0.4f), 0.7f),
        onBackground = if (isDark) Color.White.copy(alpha = 0.95f) else fromHsl(h, s.coerceAtMost(0.2f), 0.15f),
        onSurface = if (isDark) Color.White.copy(alpha = 0.9f) else fromHsl(h, s.coerceAtMost(0.2f), 0.25f),
        onSurfaceLight = if (isDark) Color.White.copy(alpha = 0.6f) else fromHsl(h, s.coerceAtMost(0.15f), 0.5f),
        onPrimary = Color.Black,
        shadowDark = if (isDark) Color(0, 0, 0, 180) else Color(0, 0, 0, 60),
        shadowLight = if (isDark) Color(255, 255, 255, 40) else Color(255, 255, 255, 180),
        stitching = if (isDark) fromHsl(h, s.coerceAtMost(0.3f), 0.3f) else fromHsl(h, s.coerceAtMost(0.3f), 0.7f),
        success = Color(0xFF43A047),
        error = Color(0xFFE53935),
        info = fromHsl((h + 0.5f) % 1f, 0.4f, 0.5f),
        iconTint = primary
    )
}

/**
 * CompositionLocal for SkeuPalette
 */
val LocalSkeuPalette = staticCompositionLocalOf { SkeuPalettes.Leather }

/**
 * Predefined Skeuomorphic Palettes
 */
object SkeuPalettes {
    val Leather = generateSkeuPalette(Color(0xFF8B5E3C))
    val Ocean = generateSkeuPalette(Color(0xFF3498DB))
    val Slate = generateSkeuPalette(Color(0xFF7F8C8D))
}

/**
 * Skeuomorphic MaterialTheme wrapper
 */
@Composable
fun SkeuomorphicTheme(
    palette: SkeuPalette = SkeuPalettes.Leather,
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onPrimary,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            error = palette.error,
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onPrimary,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            error = palette.error,
        )
    }

    CompositionLocalProvider(LocalSkeuPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(
                headlineLarge = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.onBackground,
                    letterSpacing = 0.5.sp
                ),
                headlineMedium = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.onBackground,
                    letterSpacing = 0.3.sp
                ),
                titleMedium = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.onSurface,
                    letterSpacing = 0.15.sp
                ),
                bodyLarge = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = palette.onSurface,
                ),
                bodyMedium = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = palette.onSurfaceLight,
                ),
                labelLarge = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.onPrimary,
                    letterSpacing = 1.sp
                ),
            ),
            shapes = Shapes(
                small = RoundedCornerShape(8.dp),
                medium = RoundedCornerShape(14.dp),
                large = RoundedCornerShape(20.dp),
            ),
            content = content
        )
    }
}
