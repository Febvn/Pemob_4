package com.example.myfirstkmpapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myfirstkmpapp.ui.components.InfoItem
import com.example.myfirstkmpapp.ui.components.ProfileCard
import com.example.myfirstkmpapp.ui.components.ProfileHeader
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette
import com.example.myfirstkmpapp.ui.theme.SkeuPalette
import com.example.myfirstkmpapp.ui.theme.SkeuPalettes
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.ImageBitmap
import com.example.myfirstkmpapp.data.ProfileData
import com.example.myfirstkmpapp.ui.components.ColorWheel
import com.example.myfirstkmpapp.util.pickImageFile
import com.example.myfirstkmpapp.util.toImageBitmap

/**
 * ProfileScreen — Halaman utama profil
 *
 * @param profileData Data profil saat ini
 * @param onProfileDataChange Callback saat data profil disimpan
 * @param onPaletteChange Callback untuk mengubah tema warna
 */
@Composable
fun ProfileScreen(
    viewModel: com.example.myfirstkmpapp.viewmodel.ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel { com.example.myfirstkmpapp.viewmodel.ProfileViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val palette = LocalSkeuPalette.current

    // State untuk mode edit (local UI state)
    var isEditMode by remember { mutableStateOf(false) }
    var tempData by remember { mutableStateOf(uiState.profileData) }

    // State untuk bitmap yang di-decode
    val profileBitmap = remember(tempData.profileImage) {
        tempData.profileImage?.toImageBitmap()
    }

    // Update tempData if profileData changes externally (unlikely but good practice)
    LaunchedEffect(uiState.profileData) {
        if (!isEditMode) {
            tempData = uiState.profileData
        }
    }

    // State untuk animasi masuk bertahap (hanya saat pertama kali tampil)
    var showHeader by remember { mutableStateOf(false) }
    var showBio by remember { mutableStateOf(false) }
    var showContact by remember { mutableStateOf(false) }
    var showSkills by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showHeader = true
        delay(150)
        showBio = true
        delay(150)
        showContact = true
        delay(150)
        showSkills = true
        delay(150)
        showButtons = true
    }

    // === Main Layout (Scrollable Column) ===
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.background,
                        palette.surfaceVariant.copy(alpha = 0.5f),
                        palette.background,
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ==========================================
        // SECTION 1: Profile Header
        // ==========================================

        AnimatedVisibility(
            visible = showHeader,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(initialOffsetY = { -80 }),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileHeader(
                    name = tempData.name,
                    title = tempData.title,
                    image = profileBitmap,
                    onImageClick = if (isEditMode) {
                        {
                            pickImageFile { bytes ->
                                if (bytes != null) {
                                    tempData = tempData.copy(profileImage = bytes)
                                }
                            }
                        }
                    } else null,
                    onRemoveImageClick = if (isEditMode && tempData.profileImage != null) {
                        {
                            tempData = tempData.copy(profileImage = null)
                        }
                    } else null
                )
                if (isEditMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SkeuTextField(
                        value = tempData.name,
                        onValueChange = { tempData = tempData.copy(name = it) },
                        label = "Full Name"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SkeuTextField(
                        value = tempData.title,
                        onValueChange = { tempData = tempData.copy(title = it) },
                        label = "Job Title"
                    )
                // Dark Mode Toggle Switch (Eksklusif di Edit Mode)
                AnimatedVisibility(
                    visible = isEditMode,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "DARK MODE",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 11.sp,
                                    color = palette.onSurfaceLight,
                                    letterSpacing = 1.sp
                                )
                            )
                            androidx.compose.material3.Switch(
                                checked = uiState.isDarkTheme,
                                onCheckedChange = { viewModel.toggleTheme(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    checkedThumbColor = palette.primary,
                                    checkedTrackColor = palette.primary.copy(alpha = 0.4f),
                                    uncheckedThumbColor = palette.onSurfaceLight.copy(alpha = 0.5f),
                                    uncheckedTrackColor = palette.surfaceVariant
                                )
                            )
                        }
                        
                        // Color Wheel — Muncul kembali untuk ganti warna dasar
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "THEME COLOR",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 11.sp,
                                color = palette.onSurfaceLight,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            com.example.myfirstkmpapp.ui.components.ColorWheel(
                                modifier = Modifier.size(160.dp),
                                onColorChanged = { baseColor ->
                                    viewModel.updatePalette(com.example.myfirstkmpapp.ui.theme.generateSkeuPalette(baseColor, uiState.isDarkTheme))
                                }
                            )
                        }
                    }
                }

            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))




        // ==========================================
        // SECTION 2: Bio Card
        // ==========================================
        AnimatedVisibility(
            visible = showBio,
            enter = fadeIn(animationSpec = tween(500)) +
                    expandVertically(animationSpec = tween(500)),
        ) {
            ProfileCard(title = "About Me") {
                if (isEditMode) {
                    SkeuTextField(
                        value = tempData.bio,
                        onValueChange = { tempData = tempData.copy(bio = it) },
                        label = "Biography",
                        singleLine = false,
                        modifier = Modifier.heightIn(min = 100.dp)
                    )
                } else {
                    Text(
                        text = tempData.bio,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 24.sp,
                            color = palette.onSurface,
                        ),
                        textAlign = TextAlign.Justify,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // SECTION 3: Contact Info Card
        // ==========================================
        AnimatedVisibility(
            visible = showContact,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(initialOffsetY = { 60 }),
        ) {
            ProfileCard(title = "Contact Information") {
                if (isEditMode) {
                    SkeuTextField(
                        value = tempData.email,
                        onValueChange = { tempData = tempData.copy(email = it) },
                        label = "Email",
                        icon = Icons.Outlined.Email
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SkeuTextField(
                        value = tempData.phone,
                        onValueChange = { tempData = tempData.copy(phone = it) },
                        label = "Phone",
                        icon = Icons.Outlined.Phone
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SkeuTextField(
                        value = tempData.location,
                        onValueChange = { tempData = tempData.copy(location = it) },
                        label = "Location",
                        icon = Icons.Outlined.LocationOn
                    )
                } else {
                    InfoItem(
                        icon = Icons.Outlined.Email,
                        label = "Email",
                        value = tempData.email,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoItem(
                        icon = Icons.Outlined.Phone,
                        label = "Phone",
                        value = tempData.phone,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoItem(
                        icon = Icons.Outlined.LocationOn,
                        label = "Location",
                        value = tempData.location,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // SECTION 4: Skills Card
        // ==========================================
        AnimatedVisibility(
            visible = showSkills,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(initialOffsetY = { 60 }),
        ) {
            ProfileCard(title = "Education & Skills") {
                if (isEditMode) {
                    SkeuTextField(
                        value = tempData.university,
                        onValueChange = { tempData = tempData.copy(university = it) },
                        label = "University",
                        icon = Icons.Outlined.School
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SkeuTextField(
                        value = tempData.major,
                        onValueChange = { tempData = tempData.copy(major = it) },
                        label = "Major",
                        icon = Icons.Outlined.Work
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SkeuTextField(
                        value = tempData.skills,
                        onValueChange = { tempData = tempData.copy(skills = it) },
                        label = "Skills",
                        icon = Icons.Outlined.Star
                    )
                } else {
                    InfoItem(
                        icon = Icons.Outlined.School,
                        label = "University",
                        value = tempData.university,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoItem(
                        icon = Icons.Outlined.Work,
                        label = "Major",
                        value = tempData.major,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoItem(
                        icon = Icons.Outlined.Star,
                        label = "Skills",
                        value = tempData.skills,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // SECTION 5: Action Buttons
        // ==========================================
        AnimatedVisibility(
            visible = showButtons,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(initialOffsetY = { 40 }),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                // === Toggle Edit / Save Button ===
                Button(
                    onClick = {
                        if (isEditMode) {
                            viewModel.onSaveProfile(tempData)
                        }
                        isEditMode = !isEditMode
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(55.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = palette.shadowDark,
                        ),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = palette.primary,
                        contentColor = palette.onPrimary,
                    ),
                ) {
                    Icon(
                        imageVector = if (isEditMode) Icons.Default.Person else Icons.Outlined.Edit,
                        contentDescription = if (isEditMode) "Save" else "Edit",
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isEditMode) "SAVE PROFILE" else "EDIT PROFILE",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // === Footer ===
        AnimatedVisibility(
            visible = showButtons,
            enter = fadeIn(animationSpec = tween(800)),
        ) {
            Text(
                text = "— Created by Febvn —",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = palette.onSurfaceLight.copy(alpha = 0.6f),
                    letterSpacing = 1.sp,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * SkeuTextField — Custom TextField dengan gaya skeuomorphic (inset)
 */
@Composable
fun SkeuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    val palette = LocalSkeuPalette.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 10.sp,
                color = palette.onSurfaceLight,
                letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = palette.onSurface),
            leadingIcon = icon?.let {
                { Icon(imageVector = it, contentDescription = null, tint = palette.primary) }
            },
            singleLine = singleLine,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = palette.surface,
                unfocusedContainerColor = palette.surfaceVariant.copy(alpha = 0.3f),
                focusedBorderColor = palette.primary,
                unfocusedBorderColor = palette.stitching.copy(alpha = 0.3f),
                cursorColor = palette.primary
            )
        )
    }
}

@Composable
fun ThemeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val palette = LocalSkeuPalette.current
    Surface(
        onClick = onClick,
        modifier = Modifier
            .shadow(
                elevation = if (isSelected) 0.dp else 4.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = palette.shadowDark
            )
            .border(
                width = 1.dp,
                color = if (isSelected) palette.primary else palette.stitching.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) palette.primary else palette.surface,
        contentColor = if (isSelected) palette.onPrimary else palette.onSurface
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
