package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioOption
import com.example.model.CaptionTemplate
import com.example.model.CreatorPreset
import com.example.model.LetterSpacingOption
import com.example.model.OutputScript
import com.example.model.TemplateCategory
import com.example.model.TextAlignment
import com.example.model.TextAnimation
import com.example.model.TextCaseOption
import com.example.model.TextEffect
import com.example.model.TextPosition
import com.example.ui.theme.AppFonts

/**
 * Mobile-adapted Text Styling Panel matching the CaptionCraft desktop reference image:
 * Includes "Templates" and "Custom" tabs, 2x2 "Choose Template" grid,
 * Primary Text Colour & Secondary Text Colour editing with swatches and direct Hex input,
 * Text Effects selection (Bold Drop, 3D Pop, Neon Glow, etc.), Background opacity slider,
 * Text Script selector, Alignment buttons, and expandable properties (Animation, Font, Letter Spacing, Casing).
 */
@Composable
fun StylingPanel(
    selectedTemplate: CaptionTemplate,
    selectedScript: OutputScript,
    aspectRatio: AspectRatioOption,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    activeTextEffect: TextEffect,
    activeAnimation: TextAnimation,
    textPosition: TextPosition,
    textAlignment: TextAlignment,
    backgroundOpacity: Float,
    letterSpacingOption: LetterSpacingOption,
    selectedFontName: String,
    textCaseOption: TextCaseOption,
    fontSizeScale: Float,
    onSelectTemplate: (CaptionTemplate) -> Unit,
    onSelectScript: (OutputScript) -> Unit,
    onSelectAspectRatio: (AspectRatioOption) -> Unit,
    onSelectPrimaryTextColor: (Color) -> Unit,
    onSelectSecondaryTextColor: (Color) -> Unit,
    onSelectTextEffect: (TextEffect) -> Unit,
    onSelectTextAnimation: (TextAnimation) -> Unit,
    onSelectTextPosition: (TextPosition) -> Unit,
    onSelectTextAlignment: (TextAlignment) -> Unit,
    onBackgroundOpacityChange: (Float) -> Unit,
    onSelectLetterSpacing: (LetterSpacingOption) -> Unit,
    onSelectFontName: (String) -> Unit,
    onSelectTextCase: (TextCaseOption) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    activeCreatorPreset: CreatorPreset? = null,
    onSelectCreatorPreset: (CreatorPreset) -> Unit = {},
    autoEmojisEnabled: Boolean = true,
    onToggleAutoEmojis: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("Templates") } // "Templates" or "Custom"
    var colorEditingTarget by remember { mutableStateOf("Primary") } // "Primary" or "Secondary"
    var hexInputText by remember { mutableStateOf("#FFFFFF") }

    // Dialog state for expandable property pickers
    var showFontDialog by remember { mutableStateOf(false) }
    var showAnimationDialog by remember { mutableStateOf(false) }
    var showLetterSpacingDialog by remember { mutableStateOf(false) }
    var showTextCaseDialog by remember { mutableStateOf(false) }

    // Preset color swatches matching the screenshot
    val paletteColors = listOf(
        Color(0xFFFFFFFF), // White
        Color(0xFF000000), // Black
        Color(0xFFFFE600), // Yellow
        Color(0xFF00FF66), // Green (Karaoke)
        Color(0xFFA855F7), // Purple
        Color(0xFFEC4899), // Pink
        Color(0xFF06B6D4), // Cyan
        Color(0xFFFF6600), // Orange
        Color(0xFFEF4444)  // Red
    )

    Surface(
        color = Color(0xFF0D131F),
        border = BorderStroke(1.dp, Color(0xFF1E293B)),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Panel Header: "Text Styling"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Text Styling",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                )

                // Quick Canvas Aspect Ratio Selector
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AspectRatioOption.values().forEach { option ->
                        val isSelected = option == aspectRatio
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFFA855F7) else Color(0xFF1E293B))
                                .clickable { onSelectAspectRatio(option) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = option.label.split(" ").first(),
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-Navigation Tabs: [ Templates ] and [ Custom ]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF161F30))
                    .padding(3.dp)
            ) {
                listOf("Templates", "Custom").forEach { tab ->
                    val isSelected = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isSelected) Color(0xFFA855F7) else Color.Transparent
                            )
                            .clickable { activeTab = tab }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SECTION: CHOOSE TEMPLATE (From Screenshot)
            // ==========================================
            if (activeTab == "Templates") {
                Text(
                    text = "Choose Template",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 2x2 Grid of the 4 featured templates from the screenshot
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Row 1: Bold Drop & Reels Clean
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeaturedTemplateCard(
                            title = "Bold Drop",
                            subtitle = "YOUR TEXT",
                            isSelected = selectedTemplate == CaptionTemplate.BOLD_DROP,
                            isDropShadow = true,
                            onClick = { onSelectTemplate(CaptionTemplate.BOLD_DROP) },
                            modifier = Modifier.weight(1f)
                        )
                        FeaturedTemplateCard(
                            title = "Reels Clean",
                            subtitle = "Your text here",
                            isSelected = selectedTemplate == CaptionTemplate.REELS_CLEAN,
                            isPillBackground = true,
                            onClick = { onSelectTemplate(CaptionTemplate.REELS_CLEAN) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: Podcast Duo & Karaoke Flow
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeaturedTemplateCard(
                            title = "Podcast Duo",
                            subtitle = "Speaker A: Text\nSpeaker B: Text",
                            isSelected = selectedTemplate == CaptionTemplate.PODCAST_DUO,
                            isDuoSpeaker = true,
                            onClick = { onSelectTemplate(CaptionTemplate.PODCAST_DUO) },
                            modifier = Modifier.weight(1f)
                        )
                        FeaturedTemplateCard(
                            title = "Karaoke Flow",
                            subtitle = "Your text here",
                            isSelected = selectedTemplate == CaptionTemplate.KARAOKE_FLOW,
                            isKaraoke = true,
                            onClick = { onSelectTemplate(CaptionTemplate.KARAOKE_FLOW) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category selector to switch to all 34 authentic templates
                Text(
                    text = "Browse All 34 Styles:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        CaptionTemplate.BOLD_DROP,
                        CaptionTemplate.CAPTIK_GLOW,
                        CaptionTemplate.TABAHI,
                        CaptionTemplate.MR_BEAST_1,
                        CaptionTemplate.ALI_ABDAAL,
                        CaptionTemplate.SWISS,
                        CaptionTemplate.DELHI,
                        CaptionTemplate.IMAN_GADZHI
                    ).forEach { t ->
                        val isSelected = selectedTemplate == t
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.25f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFA855F7) else Color(0xFF334155)),
                            modifier = Modifier.clickable { onSelectTemplate(t) }
                        ) {
                            Text(
                                text = t.title,
                                color = if (isSelected) Color(0xFFD8B4FE) else Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION: 1-TAP CREATOR STYLES (Viral Influencer Magic)
            // =========================================================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ 1-Tap Creator Styles",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Instant Viral Look",
                        color = Color(0xFFA855F7),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CreatorPreset.values().forEach { preset ->
                        val isSelected = activeCreatorPreset == preset
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.22f) else Color(0xFF161F30),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFFA855F7) else Color(0xFF1E293B)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .clickable { onSelectCreatorPreset(preset) }
                                .width(130.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = preset.iconEmoji, fontSize = 14.sp)
                                    Text(
                                        text = preset.title,
                                        color = if (isSelected) Color(0xFFD8B4FE) else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = preset.subtitle,
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 9.sp,
                                    lineHeight = 11.sp,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(preset.primaryColor)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(preset.secondaryColor)
                                    )
                                    Text(
                                        text = preset.fontName.take(8),
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // SECTION: AUTO-EMOJIS (Viral Magic Emojis over Spoken Keywords)
            // =========================================================================
            Surface(
                color = if (autoEmojisEnabled) Color(0xFF1E1B4B).copy(alpha = 0.6f) else Color(0xFF161F30),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (autoEmojisEnabled) Color(0xFF818CF8).copy(alpha = 0.6f) else Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "✨ Auto-Emojis (Viral Magic)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = Color(0xFFFFCC00).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "🔥 🚀 💡 💸 👑",
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Floats dynamic 3D emojis above punchy keywords as they are spoken",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp
                        )
                    }

                    Switch(
                        checked = autoEmojisEnabled,
                        onCheckedChange = { onToggleAutoEmojis(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF6366F1),
                            uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                            uncheckedTrackColor = Color(0xFF334155)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // SECTION: TEXT COLOR EDIT OPTION (Primary Text Colour & Secondary Colour)
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Text Color",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Toggle between editing Primary Text Color vs Secondary (Karaoke / Spoken) Color
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1E293B))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (colorEditingTarget == "Primary") Color(0xFFA855F7) else Color.Transparent)
                            .clickable {
                                colorEditingTarget = "Primary"
                                hexInputText = colorToHex(primaryTextColor)
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Primary",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (colorEditingTarget == "Secondary") Color(0xFFA855F7) else Color.Transparent)
                            .clickable {
                                colorEditingTarget = "Secondary"
                                hexInputText = colorToHex(secondaryTextColor)
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Secondary (Voice)",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = if (colorEditingTarget == "Primary") "Standard text appearance" else "Active spoken word highlight color",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            // Swatch Row & Hex Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Color circles
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val activeTargetColor = if (colorEditingTarget == "Primary") primaryTextColor else secondaryTextColor

                    paletteColors.forEach { color ->
                        val isSelected = color == activeTargetColor
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFA855F7) else Color.White.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (colorEditingTarget == "Primary") {
                                        onSelectPrimaryTextColor(color)
                                    } else {
                                        onSelectSecondaryTextColor(color)
                                    }
                                    hexInputText = colorToHex(color)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = if (color == Color.Black) Color.White else Color.Black,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Direct Hex Code Input Box (from screenshot)
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        BasicTextField(
                            value = hexInputText,
                            onValueChange = { newHex ->
                                hexInputText = newHex
                                parseHexColor(newHex)?.let { c ->
                                    if (colorEditingTarget == "Primary") {
                                        onSelectPrimaryTextColor(c)
                                    } else {
                                        onSelectSecondaryTextColor(c)
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            singleLine = true,
                            modifier = Modifier.width(68.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SECTION: TEXT EFFECTS (Premium Selection)
            // ==========================================
            Text(
                text = "Text Effects",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                TextEffect.values().forEach { effect ->
                    val isSelected = activeTextEffect == effect
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) {
                                    Brush.linearGradient(
                                        listOf(Color(0xFFA855F7), Color(0xFF6366F1))
                                    )
                                } else {
                                    Brush.linearGradient(
                                        listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                                    )
                                }
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFFD8B4FE) else Color(0xFF334155),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectTextEffect(effect) }
                            .padding(horizontal = 11.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = effect.displayName,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = activeTextEffect.description,
                color = Color(0xFFD8B4FE).copy(alpha = 0.8f),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ========================================================
            // SECTION: BACKGROUND OPACITY (for Reels Clean / Box Pill)
            // ========================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Opacity,
                        contentDescription = "Opacity",
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Background Opacity",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${(backgroundOpacity * 100).toInt()}%",
                        color = Color(0xFF22D3EE),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Slider(
                value = backgroundOpacity,
                onValueChange = onBackgroundOpacityChange,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFA855F7),
                    activeTrackColor = Color(0xFFA855F7),
                    inactiveTrackColor = Color(0xFF334155)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ==========================================
            // SECTION: TEXT SCRIPT (From Screenshot)
            // ==========================================
            Text(
                text = "Text Script",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutputScript.values().forEach { script ->
                    val isSelected = selectedScript == script
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFA855F7) else Color(0xFF1E293B))
                            .border(
                                1.dp,
                                if (isSelected) Color.White.copy(alpha = 0.5f) else Color(0xFF334155),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectScript(script) }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = script.displayName.split(" ").first(),
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SECTION: POSITION & ALIGNMENT
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Position & Alignment",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Segmented Alignment Control (Left, Center, Right matching screenshot icons)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1E293B))
                        .padding(2.dp)
                ) {
                    val alignItems = listOf(
                        TextAlignment.LEFT to Icons.Default.FormatAlignLeft,
                        TextAlignment.CENTER to Icons.Default.FormatAlignCenter,
                        TextAlignment.RIGHT to Icons.Default.FormatAlignRight
                    )

                    alignItems.forEach { (align, icon) ->
                        val isSelected = textAlignment == align
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) Color(0xFFA855F7) else Color.Transparent)
                                .clickable { onSelectTextAlignment(align) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = align.displayName,
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vertical position placement chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TextPosition.values().forEach { pos ->
                    val isSelected = textPosition == pos
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFFA855F7).copy(alpha = 0.25f) else Color(0xFF1E293B))
                            .border(1.dp, if (isSelected) Color(0xFFA855F7) else Color(0xFF334155), RoundedCornerShape(6.dp))
                            .clickable { onSelectTextPosition(pos) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pos.displayName,
                            color = if (isSelected) Color(0xFFD8B4FE) else Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ========================================================
            // SECTION: PROPERTY LIST ITEMS (From Screenshot Right Panel)
            // ========================================================
            Surface(
                color = Color(0xFF161F30),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    PropertyRowItem(
                        label = "Animation",
                        value = activeAnimation.displayName,
                        onClick = { showAnimationDialog = true }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
                    PropertyRowItem(
                        label = "Font",
                        value = selectedFontName,
                        onClick = { showFontDialog = true }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
                    PropertyRowItem(
                        label = "Letter Spacing",
                        value = letterSpacingOption.displayName,
                        onClick = { showLetterSpacingDialog = true }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
                    PropertyRowItem(
                        label = "Text Case",
                        value = textCaseOption.displayName,
                        onClick = { showTextCaseDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Font Size Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Font Scale: ${String.format(java.util.Locale.US, "%.1f", fontSizeScale)}x",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(0.85f, 1.0f, 1.15f, 1.3f).forEach { scale ->
                        val isSelected = kotlin.math.abs(fontSizeScale - scale) < 0.05f
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.clickable { onFontSizeChange(scale) }
                        ) {
                            Text(
                                text = "${scale}x",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // --- PROPERTY PICKER DIALOGS ---

    // 1. Font Family Picker Dialog
    if (showFontDialog) {
        val fonts = listOf(
            "Montserrat (900)",
            "Anton (Heavy)",
            "Poppins (800)",
            "Inter (700)",
            "Playfair (Serif)",
            "Caveat (Handwritten)",
            "Cinzel (Luxury)",
            "Space Mono (Mono)"
        )
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("Select Font Family", color = Color.White, fontWeight = FontWeight.Bold) },
            containerColor = Color(0xFF0F172A),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    fonts.forEach { font ->
                        val isSelected = selectedFontName == font
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.3f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFA855F7) else Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectFontName(font)
                                    showFontDialog = false
                                }
                        ) {
                            Text(
                                text = font,
                                color = if (isSelected) Color(0xFFD8B4FE) else Color.White,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontDialog = false }) {
                    Text("Close", color = Color(0xFFA855F7))
                }
            }
        )
    }

    // 2. Animation Picker Dialog
    if (showAnimationDialog) {
        AlertDialog(
            onDismissRequest = { showAnimationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFA855F7).copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Text Transitions & Motion",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            containerColor = Color(0xFF0F172A),
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TextAnimation.values().forEach { anim ->
                        val isSelected = activeAnimation == anim
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.25f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFA855F7) else Color(0xFF334155)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectTextAnimation(anim)
                                    showAnimationDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = anim.displayName,
                                        color = if (isSelected) Color(0xFFD8B4FE) else Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = anim.description,
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 10.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color(0xFFA855F7),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAnimationDialog = false }) {
                    Text("Done", color = Color(0xFFA855F7), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 3. Letter Spacing Picker Dialog
    if (showLetterSpacingDialog) {
        AlertDialog(
            onDismissRequest = { showLetterSpacingDialog = false },
            title = { Text("Letter Spacing", color = Color.White, fontWeight = FontWeight.Bold) },
            containerColor = Color(0xFF0F172A),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LetterSpacingOption.values().forEach { spacing ->
                        val isSelected = letterSpacingOption == spacing
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.3f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFA855F7) else Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLetterSpacing(spacing)
                                    showLetterSpacingDialog = false
                                }
                        ) {
                            Text(
                                text = "${spacing.displayName} (${spacing.trackingSp}sp)",
                                color = if (isSelected) Color(0xFFD8B4FE) else Color.White,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLetterSpacingDialog = false }) {
                    Text("Close", color = Color(0xFFA855F7))
                }
            }
        )
    }

    // 4. Text Case Dialog
    if (showTextCaseDialog) {
        AlertDialog(
            onDismissRequest = { showTextCaseDialog = false },
            title = { Text("Text Casing", color = Color.White, fontWeight = FontWeight.Bold) },
            containerColor = Color(0xFF0F172A),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextCaseOption.values().forEach { c ->
                        val isSelected = textCaseOption == c
                        Surface(
                            color = if (isSelected) Color(0xFFA855F7).copy(alpha = 0.3f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFA855F7) else Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectTextCase(c)
                                    showTextCaseDialog = false
                                }
                        ) {
                            Text(
                                text = c.displayName,
                                color = if (isSelected) Color(0xFFD8B4FE) else Color.White,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTextCaseDialog = false }) {
                    Text("Close", color = Color(0xFFA855F7))
                }
            }
        )
    }
}

/**
 * Featured Template Card in 2x2 grid matching the screenshot exactly
 */
@Composable
private fun FeaturedTemplateCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    isDropShadow: Boolean = false,
    isPillBackground: Boolean = false,
    isDuoSpeaker: Boolean = false,
    isKaraoke: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF191F34) else Color(0xFF0F172A)
        ),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = if (isSelected) 1.8.dp else 1.dp,
            color = if (isSelected) Color(0xFFA855F7) else Color(0xFF1E293B)
        ),
        modifier = modifier
            .height(104.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Visual Preview Box in top/center
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isDropShadow -> {
                        // Bold 3D Drop Shadow preview
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = subtitle,
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = AppFonts.Montserrat,
                                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                            )
                            Text(
                                text = subtitle,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = AppFonts.Montserrat
                            )
                        }
                    }
                    isPillBackground -> {
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = subtitle,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    isDuoSpeaker -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Speaker A: Text",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Speaker B: Text",
                                color = Color(0xFFFFD700),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    isKaraoke -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("Your", color = Color.White, fontSize = 11.sp)
                            Text("text", color = Color(0xFF00FF66), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("here", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Title label at bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = title,
                    color = if (isSelected) Color(0xFFD8B4FE) else Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Purple Selected Checkmark badge on top right
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(16.dp)
                        .background(Color(0xFFA855F7), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

/**
 * Property item row with chevron right
 */
@Composable
private fun PropertyRowItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Open",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

private fun colorToHex(color: Color): String {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()
    return String.format(java.util.Locale.US, "#%02X%02X%02X", r, g, b)
}

private fun parseHexColor(hexString: String): Color? {
    val clean = hexString.trim().removePrefix("#")
    if (clean.length != 6 && clean.length != 8) return null
    return try {
        val colorLong = clean.toLong(16)
        if (clean.length == 6) {
            Color(0xFF000000 or colorLong)
        } else {
            Color(colorLong)
        }
    } catch (e: Exception) {
        null
    }
}
