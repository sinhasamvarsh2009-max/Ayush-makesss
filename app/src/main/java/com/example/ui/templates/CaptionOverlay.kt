package com.example.ui.templates

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionSegment
import com.example.model.CaptionTemplate
import com.example.model.CaptionWord
import com.example.model.LetterSpacingOption
import com.example.model.OutputScript
import com.example.model.TextAlignment
import com.example.model.TextAnimation
import com.example.model.TextCaseOption
import com.example.model.TextEffect
import com.example.model.TextPosition
import com.example.ui.theme.AppFonts

/**
 * Main Composable that burns-in and renders animated subtitles synchronized with the video voice
 * across all 34 authentic Captik templates.
 */
@Composable
fun CaptionOverlay(
    activeSegment: CaptionSegment?,
    currentTime: Float,
    template: CaptionTemplate,
    script: OutputScript,
    karaokeColor: Color = Color(0xFF00FF00),
    fontSizeScale: Float = 1.0f,
    primaryColor: Color = Color.White,
    secondaryColor: Color = karaokeColor,
    textEffect: TextEffect = TextEffect.BOLD_DROP,
    textAnimation: TextAnimation = TextAnimation.SCALE_IN,
    textPosition: TextPosition = TextPosition.CENTER,
    textAlignment: TextAlignment = TextAlignment.CENTER,
    backgroundOpacity: Float = 0.5f,
    letterSpacing: LetterSpacingOption = LetterSpacingOption.NORMAL,
    selectedFontName: String = "Montserrat (900)",
    textCase: TextCaseOption = TextCaseOption.UPPERCASE,
    autoEmojisEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (activeSegment == null) return

    val words = activeSegment.getWordsForScript(script)
    if (words.isEmpty()) return

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (template) {
            // Featured from CaptionCraft screenshot
            CaptionTemplate.BOLD_DROP -> BoldDropTemplate(
                words = words,
                currentTime = currentTime,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                fontSizeScale = fontSizeScale,
                textEffect = textEffect,
                textAnimation = textAnimation,
                textPosition = textPosition,
                textAlignment = textAlignment,
                backgroundOpacity = backgroundOpacity,
                letterSpacing = letterSpacing,
                fontName = selectedFontName,
                textCase = textCase,
                autoEmojisEnabled = autoEmojisEnabled
            )
            CaptionTemplate.REELS_CLEAN -> ReelsCleanTemplate(
                words = words,
                currentTime = currentTime,
                primaryColor = primaryColor,
                backgroundOpacity = backgroundOpacity,
                fontSizeScale = fontSizeScale,
                textPosition = textPosition,
                textAlignment = textAlignment,
                fontName = selectedFontName
            )
            CaptionTemplate.PODCAST_DUO -> PodcastDuoTemplate(
                activeSegment = activeSegment,
                words = words,
                currentTime = currentTime,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                fontSizeScale = fontSizeScale,
                textPosition = textPosition
            )

            // Core & Glow
            CaptionTemplate.CAPTIK_GLOW -> CaptikGlowTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CAPTIK_SHADOW -> CaptikShadowTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CAPTIK -> CaptikClassicTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CLEAN_GLOW_STYLE -> CleanGlowStyleTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.TABAHI -> TabahiTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.DEEP_GLOW -> DeepGlowTemplate(words, currentTime, fontSizeScale)

            // Kinetic & Editorial
            CaptionTemplate.ILLUSION -> IllusionTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.EDITOR_MASALA -> EditorMasalaTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.AURA -> AuraTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.SWISS -> SwissTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.THE_BIG_RED -> TheBigRedTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.BLOCKBUSTER -> BlockbusterTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.INTERLOCK -> InterlockTemplate(words, currentTime, fontSizeScale)

            // Handwritten & Organic
            CaptionTemplate.DELHI -> DelhiTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.SCRIBBLE -> ScribbleTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.ARCHIVES -> ArchivesTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.JOURNAL -> JournalTemplate(words, currentTime, fontSizeScale)

            // Static Captions
            CaptionTemplate.ALI_ABDAAL -> AliAbdaalTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CLEAN_MOTION -> CleanMotionTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.BUBBLE_STYLE -> BubbleStyleTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.HIGHLIGHTED_WORD -> HighlightedWordTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CAPTIK_CLEAN -> CaptikCleanTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.LIQUID_GLASS -> LiquidGlassTemplate(words, currentTime, fontSizeScale)

            // Creator & Influencer
            CaptionTemplate.EDITING_SKOOL -> EditingSkoolTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.MR_BEAST_1 -> MrBeast1Template(words, currentTime, fontSizeScale)
            CaptionTemplate.MR_BEAST_2 -> MrBeast2Template(words, currentTime, fontSizeScale)
            CaptionTemplate.IMAN_GADZHI -> ImanGadzhiTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.DEVIN_JATHO -> DevinJathoTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.SEEDHA_SAADHA -> SeedhaSaadhaTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.THORA_CINEMATIC -> ThoraCinematicTemplate(words, currentTime, fontSizeScale)

            // Clean & Highlight
            CaptionTemplate.BLACK_PUNCH -> BlackPunchTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.CAPTIK_WORD -> CaptikWordTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.PIXELATED_WORD -> PixelatedWordTemplate(words, currentTime, fontSizeScale)

            // AI Captions
            CaptionTemplate.BIG_REVEAL -> BigRevealTemplate(words, currentTime, fontSizeScale)
            CaptionTemplate.KARAOKE_FLOW -> KaraokeFlowTemplate(words, currentTime, karaokeColor, fontSizeScale)
        }
    }
}

/**
 * Utility helper that finds the currently spoken active word and surrounding context
 * according to the video voice timestamps.
 */
private class VoiceWindow(
    val activeIndex: Int,
    val activeWord: CaptionWord,
    val precedingWords: List<CaptionWord>,
    val upcomingWords: List<CaptionWord>,
    val isSpokenNow: Boolean
)

private fun getVoiceWindow(words: List<CaptionWord>, currentTime: Float): VoiceWindow {
    val exactIndex = words.indexOfFirst { it.isActiveAt(currentTime) }
    val activeIdx = if (exactIndex >= 0) {
        exactIndex
    } else {
        words.indexOfLast { it.startTime <= currentTime }.coerceIn(0, words.size - 1)
    }
    val activeWord = words[activeIdx]
    val preceding = words.subList(0, activeIdx)
    val upcoming = words.subList((activeIdx + 1).coerceAtMost(words.size), words.size)
    return VoiceWindow(
        activeIndex = activeIdx,
        activeWord = activeWord,
        precedingWords = preceding,
        upcomingWords = upcoming,
        isSpokenNow = exactIndex >= 0
    )
}

private fun resolveFontFamily(fontName: String): FontFamily {
    return when {
        fontName.contains("Montserrat", ignoreCase = true) -> AppFonts.Montserrat
        fontName.contains("Anton", ignoreCase = true) -> AppFonts.Anton
        fontName.contains("Poppins", ignoreCase = true) -> AppFonts.Poppins
        fontName.contains("Inter", ignoreCase = true) -> AppFonts.Inter
        fontName.contains("Caveat", ignoreCase = true) -> AppFonts.Caveat
        fontName.contains("Playfair", ignoreCase = true) -> AppFonts.Playfair
        fontName.contains("Cinzel", ignoreCase = true) -> AppFonts.Cinzel
        fontName.contains("Space", ignoreCase = true) -> AppFonts.SpaceMono
        else -> AppFonts.Montserrat
    }
}

/**
 * Intelligent viral keyword detector mapping spoken Hinglish & English keywords to animated emojis
 */
fun getKeywordEmoji(wordText: String): String? {
    val clean = wordText.lowercase().replace(Regex("[^a-zA-Z0-9]"), "")
    return when (clean) {
        "bhai", "yaar", "bro", "dost" -> "🤝"
        "viral", "aag", "fire", "trend", "trending" -> "🔥"
        "phone", "camera", "reel", "video" -> "📱"
        "crore", "paisa", "rupee", "money", "lakh", "rich", "cash" -> "💸"
        "sun", "dekho", "listen", "watch" -> "👀"
        "hustle", "power", "energy", "shock", "bijli" -> "⚡"
        "mast", "bawaal", "ekdum", "gazab", "boom" -> "💥"
        "delhi", "paratha", "swaad", "food", "tasty" -> "😋"
        "startup", "founder", "rocket", "growth", "scale" -> "🚀"
        "mindset", "brain", "soch", "idea", "smart" -> "💡"
        "king", "queen", "win", "boss", "champion" -> "👑"
        "love", "dil", "pyaar", "mohabbat" -> "❤️"
        "star", "super", "hero", "shandaar" -> "⭐"
        "best", "sahi", "perfect", "done" -> "💯"
        "target", "focus", "goal" -> "🎯"
        "warning", "danger", "khatra" -> "⚠️"
        else -> null
    }
}

// ==========================================
// FEATURED 1: BOLD DROP & MASTER DYNAMIC RENDERER
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.BoldDropTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    primaryColor: Color,
    secondaryColor: Color,
    fontSizeScale: Float,
    textEffect: TextEffect,
    textAnimation: TextAnimation,
    textPosition: TextPosition,
    textAlignment: TextAlignment,
    backgroundOpacity: Float,
    letterSpacing: LetterSpacingOption,
    fontName: String,
    textCase: TextCaseOption,
    autoEmojisEnabled: Boolean = true
) {
    val window = getVoiceWindow(words, currentTime)
    val fontFamily = resolveFontFamily(fontName)

    val verticalAlignment = when (textPosition) {
        TextPosition.TOP -> Alignment.TopCenter
        TextPosition.CENTER -> Alignment.Center
        TextPosition.BOTTOM -> Alignment.BottomCenter
    }

    val horizontalArrangement = when (textAlignment) {
        TextAlignment.LEFT -> Arrangement.Start
        TextAlignment.CENTER -> Arrangement.Center
        TextAlignment.RIGHT -> Arrangement.End
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 28.dp),
        contentAlignment = verticalAlignment
    ) {
        // Multi-line word wrapping with high impact effects & transitions
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = Arrangement.Center
        ) {
            words.forEachIndexed { index, word ->
                val isActive = index == window.activeIndex && window.isSpokenNow
                val isPreceding = index < window.activeIndex

                val formattedText = when (textCase) {
                    TextCaseOption.UPPERCASE -> word.text.uppercase()
                    TextCaseOption.TITLE_CASE -> word.text.replaceFirstChar { it.uppercase() }
                    TextCaseOption.AS_SPOKEN -> word.text
                }

                val wordColor = when {
                    isActive -> secondaryColor
                    isPreceding -> primaryColor
                    else -> primaryColor.copy(alpha = 0.85f)
                }

                // Smooth animated transition specs based on textAnimation
                val wordScale by animateFloatAsState(
                    targetValue = when (textAnimation) {
                        TextAnimation.SPRING_BOUNCE -> if (isActive) 1.16f else 1.0f
                        TextAnimation.KINETIC_ZOOM -> if (isActive) 1.25f else 1.0f
                        TextAnimation.ELASTIC_SNAP -> if (isActive) 1.18f else 1.0f
                        TextAnimation.POP_IN -> if (isActive) 1.15f else 1.0f
                        TextAnimation.SCALE_IN -> if (isActive) 1.08f else 1.0f
                        TextAnimation.GLOW_WAVE -> if (isActive) 1.12f else 1.0f
                        TextAnimation.SHIMMER_PULSE -> if (isActive) 1.14f else 1.0f
                        TextAnimation.BLUR_FOCUS -> if (isActive) 1.05f else 0.98f
                        else -> 1.0f
                    },
                    animationSpec = when (textAnimation) {
                        TextAnimation.SPRING_BOUNCE -> spring(dampingRatio = 0.52f, stiffness = Spring.StiffnessMediumLow)
                        TextAnimation.KINETIC_ZOOM -> spring(dampingRatio = 0.58f, stiffness = Spring.StiffnessMedium)
                        TextAnimation.ELASTIC_SNAP -> spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow)
                        TextAnimation.POP_IN -> spring(dampingRatio = 0.50f, stiffness = Spring.StiffnessMedium)
                        else -> spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow)
                    },
                    label = "trans_scale"
                )

                val wordOffsetY by animateFloatAsState(
                    targetValue = when (textAnimation) {
                        TextAnimation.SPRING_BOUNCE -> if (isActive) -5f else 0f
                        TextAnimation.SLIDE_UP -> if (isActive) 0f else if (isPreceding) 0f else 12f
                        else -> 0f
                    },
                    animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
                    label = "trans_offset_y"
                )

                val wordRotationX by animateFloatAsState(
                    targetValue = when (textAnimation) {
                        TextAnimation.FLIP_3D -> if (isActive) 0f else if (isPreceding) 0f else 32f
                        else -> 0f
                    },
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow),
                    label = "trans_rot_x"
                )

                val wordAlpha by animateFloatAsState(
                    targetValue = when {
                        textAnimation == TextAnimation.SLIDE_UP -> if (isActive) 1.0f else if (isPreceding) 0.85f else 0.45f
                        textAnimation == TextAnimation.TYPEWRITER -> if (isActive || isPreceding) 1.0f else 0.35f
                        isActive -> 1.0f
                        isPreceding -> 0.92f
                        else -> 0.80f
                    },
                    animationSpec = tween(150),
                    label = "trans_alpha"
                )

                val waveOffset = if (textAnimation == TextAnimation.WAVE_RIPPLE && (isActive || isPreceding)) {
                    kotlin.math.sin(currentTime * 8f + index * 0.75f).toFloat() * 3.5f
                } else 0f

                val fontSize = (28 * fontSizeScale).sp
                val emoji = if (autoEmojisEnabled) getKeywordEmoji(word.text) else null

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = wordScale
                            scaleY = wordScale
                            translationY = wordOffsetY + waveOffset
                            rotationX = wordRotationX
                            alpha = wordAlpha
                            cameraDistance = 16f * density
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    if (emoji != null) {
                        val emojiScale by animateFloatAsState(
                            targetValue = if (isActive) 1.30f else 0.95f,
                            animationSpec = spring(dampingRatio = 0.50f, stiffness = Spring.StiffnessMedium),
                            label = "trans_emoji_scale"
                        )
                        Text(
                            text = emoji,
                            fontSize = (16 * fontSizeScale).sp,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = emojiScale
                                    scaleY = emojiScale
                                }
                                .padding(bottom = 1.dp)
                        )
                    }

                    Box {
                        when (textEffect) {
                        TextEffect.BOLD_DROP -> {
                            // Deep 3D Drop Shadow layer
                            Text(
                                text = formattedText,
                                color = Color.Black,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 3.5.dp, y = 3.5.dp)
                            )
                            // Mid shadow for solid punch
                            Text(
                                text = formattedText,
                                color = Color.Black,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 1.8.dp, y = 1.8.dp)
                            )
                            // Forefront text
                            Text(
                                text = formattedText,
                                color = wordColor,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp
                            )
                        }

                        TextEffect.THREE_D_POP -> {
                            // Layered isometric extrusion
                            listOf(
                                Pair(1.0.dp, 1.0.dp),
                                Pair(2.0.dp, 2.0.dp),
                                Pair(3.0.dp, 3.0.dp),
                                Pair(4.5.dp, 4.5.dp)
                            ).forEach { (dx, dy) ->
                                Text(
                                    text = formattedText,
                                    color = Color.Black,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    modifier = Modifier.offset(x = dx, y = dy)
                                )
                            }
                            Text(
                                text = formattedText,
                                color = wordColor,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp
                            )
                        }

                        TextEffect.NEON_GLOW -> {
                            val glowColor = if (isActive) secondaryColor else primaryColor
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    color = glowColor,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    shadow = Shadow(
                                        color = glowColor.copy(alpha = if (isActive) 0.95f else 0.40f),
                                        offset = Offset(0f, 0f),
                                        blurRadius = if (isActive) 26f else 10f
                                    )
                                )
                            )
                        }

                        TextEffect.CHROME_METALLIC -> {
                            Text(
                                text = formattedText,
                                color = Color.Black,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                            )
                            val chromeBrush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFFFFFFF),
                                    Color(0xFFE2E8F0),
                                    Color(0xFF94A3B8),
                                    Color(0xFFFFFFFF)
                                )
                            )
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    brush = chromeBrush,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp
                                )
                            )
                        }

                        TextEffect.CYBER_GLITCH -> {
                            // Cyan split
                            Text(
                                text = formattedText,
                                color = Color(0xFF00FFFF).copy(alpha = 0.85f),
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = (-2.5).dp, y = 0.dp)
                            )
                            // Magenta split
                            Text(
                                text = formattedText,
                                color = Color(0xFFFF0055).copy(alpha = 0.85f),
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 2.5.dp, y = 0.dp)
                            )
                            // Front text
                            Text(
                                text = formattedText,
                                color = wordColor,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp
                            )
                        }

                        TextEffect.HOLO_SHIMMER -> {
                            Text(
                                text = formattedText,
                                color = Color.Black,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                            )
                            val holoBrush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFFF6080),
                                    Color(0xFFFFDC60),
                                    Color(0xFF60FF9F),
                                    Color(0xFF60EFFF),
                                    Color(0xFF9E60FF)
                                )
                            )
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    brush = holoBrush,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp
                                )
                            )
                        }

                        TextEffect.FIRE_BLAZE -> {
                            Text(
                                text = formattedText,
                                color = Color(0xFF7F1D1D),
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp,
                                modifier = Modifier.offset(x = 1.5.dp, y = 2.5.dp)
                            )
                            val fireBrush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFEF08A),
                                    Color(0xFFF97316),
                                    Color(0xFFDC2626)
                                )
                            )
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    brush = fireBrush,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    shadow = Shadow(
                                        color = Color(0xFFDC2626).copy(alpha = 0.85f),
                                        offset = Offset(0f, 0f),
                                        blurRadius = 16f
                                    )
                                )
                            )
                        }

                        TextEffect.FROSTED_GLASS -> {
                            Surface(
                                color = Color(0xFF0F172A).copy(alpha = backgroundOpacity.coerceAtLeast(0.35f)),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = formattedText,
                                    style = TextStyle(
                                        color = wordColor,
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = fontFamily,
                                        letterSpacing = letterSpacing.trackingSp.sp,
                                        shadow = Shadow(Color.Black.copy(alpha = 0.6f), Offset(1f, 1f), 3f)
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        TextEffect.TEXT_OUTLINE -> {
                            // 8-directional contour outline
                            val strokeOffsets = listOf(
                                Pair(-1.5.dp, 0.dp), Pair(1.5.dp, 0.dp),
                                Pair(0.dp, -1.5.dp), Pair(0.dp, 1.5.dp),
                                Pair(-1.2.dp, -1.2.dp), Pair(1.2.dp, 1.2.dp),
                                Pair(-1.2.dp, 1.2.dp), Pair(1.2.dp, -1.2.dp)
                            )
                            strokeOffsets.forEach { (dx, dy) ->
                                Text(
                                    text = formattedText,
                                    color = Color.Black,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    modifier = Modifier.offset(x = dx, y = dy)
                                )
                            }
                            Text(
                                text = formattedText,
                                color = wordColor,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Black,
                                fontFamily = fontFamily,
                                letterSpacing = letterSpacing.trackingSp.sp
                            )
                        }

                        TextEffect.BOX_BACKGROUND -> {
                            Surface(
                                color = Color.Black.copy(alpha = backgroundOpacity),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = formattedText,
                                    color = wordColor,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        TextEffect.KARAOKE_HIGHLIGHT -> {
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    color = wordColor,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    shadow = if (isActive) {
                                        Shadow(secondaryColor.copy(alpha = 0.85f), Offset(0f, 0f), 14f)
                                    } else {
                                        Shadow(Color.Black.copy(alpha = 0.7f), Offset(2f, 2f), 3f)
                                    }
                                )
                            )
                        }

                        TextEffect.MINIMAL_CLEAN -> {
                            Text(
                                text = formattedText,
                                style = TextStyle(
                                    color = wordColor,
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = fontFamily,
                                    letterSpacing = letterSpacing.trackingSp.sp,
                                    shadow = Shadow(Color.Black.copy(alpha = 0.75f), Offset(1f, 1f), 3f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
}

// ==========================================
// FEATURED 2: REELS CLEAN (From CaptionCraft Screenshot)
// ==========================================
@Composable
private fun BoxScope.ReelsCleanTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    primaryColor: Color,
    backgroundOpacity: Float,
    fontSizeScale: Float,
    textPosition: TextPosition,
    textAlignment: TextAlignment,
    fontName: String
) {
    val window = getVoiceWindow(words, currentTime)
    val fontFamily = resolveFontFamily(fontName)

    val verticalAlignment = when (textPosition) {
        TextPosition.TOP -> Alignment.TopCenter
        TextPosition.CENTER -> Alignment.Center
        TextPosition.BOTTOM -> Alignment.BottomCenter
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 36.dp),
        contentAlignment = verticalAlignment
    ) {
        Surface(
            color = Color.Black.copy(alpha = backgroundOpacity),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = words.joinToString(" ") { it.text },
                color = primaryColor,
                fontSize = (18 * fontSizeScale).sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                textAlign = when (textAlignment) {
                    TextAlignment.LEFT -> androidx.compose.ui.text.style.TextAlign.Start
                    TextAlignment.CENTER -> androidx.compose.ui.text.style.TextAlign.Center
                    TextAlignment.RIGHT -> androidx.compose.ui.text.style.TextAlign.End
                },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

// ==========================================
// FEATURED 3: PODCAST DUO (From CaptionCraft Screenshot)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.PodcastDuoTemplate(
    activeSegment: CaptionSegment,
    words: List<CaptionWord>,
    currentTime: Float,
    primaryColor: Color,
    secondaryColor: Color,
    fontSizeScale: Float,
    textPosition: TextPosition
) {
    val window = getVoiceWindow(words, currentTime)
    val isSpeakerB = activeSegment.speaker.contains("B", ignoreCase = true)
    val activeColor = if (isSpeakerB) secondaryColor else primaryColor

    val verticalAlignment = when (textPosition) {
        TextPosition.TOP -> Alignment.TopCenter
        TextPosition.CENTER -> Alignment.Center
        TextPosition.BOTTOM -> Alignment.BottomCenter
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 32.dp),
        contentAlignment = verticalAlignment
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Speaker badge
            Surface(
                color = if (isSpeakerB) Color(0xFFF59E0B).copy(alpha = 0.25f) else Color(0xFF3B82F6).copy(alpha = 0.25f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSpeakerB) Color(0xFFF59E0B) else Color(0xFF3B82F6)
                )
            ) {
                Text(
                    text = activeSegment.speaker.uppercase(),
                    color = if (isSpeakerB) Color(0xFFFDE047) else Color(0xFF93C5FD),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            // Word sequence with active highlight
            androidx.compose.foundation.layout.FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                words.forEachIndexed { index, word ->
                    val isActive = index == window.activeIndex && window.isSpokenNow
                    Text(
                        text = word.text,
                        color = if (isActive) activeColor else Color.White.copy(alpha = 0.7f),
                        fontSize = (22 * fontSizeScale).sp,
                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                        fontFamily = AppFonts.Montserrat,
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 1. CAPTIK GLOW (3-Line Kinetic Voice Sync)
// ==========================================
@Composable
private fun BoxScope.CaptikGlowTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text.uppercase()
    val bottomText = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "captik_glow_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = centerText,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (38f * fontSizeScale).sp,
                color = Color(0xFF00FF66),
                shadow = Shadow(
                    color = Color(0xFF00FF66).copy(alpha = 0.85f),
                    offset = Offset(0f, 0f),
                    blurRadius = 24f
                ),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (bottomText.isNotEmpty()) {
            Text(
                text = bottomText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 2. CAPTIK SHADOW (3-Line Solid Black Drop Shadow)
// ==========================================
@Composable
private fun BoxScope.CaptikShadowTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text.uppercase()
    val bottomText = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.12f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "captik_shadow_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = centerText,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (38f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(8f, 8f),
                    blurRadius = 0f
                ),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (bottomText.isNotEmpty()) {
            Text(
                text = bottomText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 3. CAPTIK CLASSIC (Electric Lime-Yellow #CCFF00)
// ==========================================
@Composable
private fun BoxScope.CaptikClassicTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text.uppercase()
    val bottomText = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "captik_classic_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = centerText,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (38f * fontSizeScale).sp,
                color = Color(0xFFCCFF00),
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(6f, 6f),
                    blurRadius = 0f
                ),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (bottomText.isNotEmpty()) {
            Text(
                text = bottomText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.70f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 4. DELHI (Playfair Display Cursive Italic Script)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.DelhiTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)
                val scaleAnim by animateFloatAsState(
                    targetValue = if (isActive) 1.22f else 1.0f,
                    animationSpec = tween(120),
                    label = "delhi_${word.id}"
                )

                Text(
                    text = word.text.lowercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Playfair,
                        fontStyle = if (isActive) FontStyle.Italic else FontStyle.Normal,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        fontSize = ((if (isActive) 34f else 26f) * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFFDE047) else Color.White,
                        shadow = if (isActive) {
                            Shadow(
                                color = Color(0xFFFDE047).copy(alpha = 0.9f),
                                offset = Offset(0f, 0f),
                                blurRadius = 18f
                            )
                        } else {
                            Shadow(
                                color = Color.Black.copy(alpha = 0.7f),
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        },
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.scale(scaleAnim)
                )
            }
        }
    }
}

// ==========================================
// 5. ILLUSION (Ultra Bold Kinetic Scaling)
// ==========================================
@Composable
private fun BoxScope.IllusionTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text
    val bottomText = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "illusion_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = (18f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = centerText,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (44f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(8f, 8f),
                    blurRadius = 0f
                ),
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (bottomText.isNotEmpty()) {
            Text(
                text = bottomText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = (18f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 6. EDITOR MASALA (Anton · Vivid Chrome Yellow #FFDE00)
// ==========================================
@Composable
private fun BoxScope.EditorMasalaTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text.uppercase()

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.20f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "editor_masala_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = (22f * fontSizeScale).sp,
                    color = Color.White,
                    shadow = Shadow(Color.Black, Offset(3f, 3f), 0f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = centerText,
            style = TextStyle(
                fontFamily = AppFonts.Anton,
                fontSize = (46f * fontSizeScale).sp,
                color = Color(0xFFFFDE00),
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(8f, 8f),
                    blurRadius = 0f
                ),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )
    }
}

// ==========================================
// 7. AURA (Caveat Cursive + Cyan Uppercase)
// ==========================================
@Composable
private fun BoxScope.AuraTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val scriptPart = window.precedingWords.takeLast(1).joinToString(" ") { it.text.lowercase() }
        .ifEmpty { "forget" }
    val statusPart = window.activeWord.text.uppercase()

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = scriptPart,
            style = TextStyle(
                fontFamily = AppFonts.Caveat,
                fontSize = (32f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(2f, 2f), 4f),
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = statusPart,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (42f * fontSizeScale).sp,
                color = Color(0xFF7DD3FC),
                shadow = Shadow(
                    color = Color(0xFF38BDF8).copy(alpha = 0.8f),
                    offset = Offset(0f, 0f),
                    blurRadius = 20f
                ),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 8. SWISS (Inter Minimalist · Bold White + Yellow)
// ==========================================
@Composable
private fun BoxScope.SwissTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topWord = window.precedingWords.lastOrNull()?.text?.lowercase() ?: "focus"
    val bottomWord = window.activeWord.text.uppercase()

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = topWord,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Bold,
                fontSize = (26f * fontSizeScale).sp,
                color = Color.White,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = bottomWord,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Black,
                fontSize = (42f * fontSizeScale).sp,
                color = Color(0xFFFACC15),
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 9. THE BIG RED (Cinzel Crimson Hero + Spoken Phrase)
// ==========================================
@Composable
private fun BoxScope.TheBigRedTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val heroWord = window.activeWord.text.uppercase()
    val fullPhrase = words.joinToString(" ") { it.text.lowercase() }

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Giant Crimson Background Typography
        Text(
            text = heroWord,
            style = TextStyle(
                fontFamily = AppFonts.Cinzel,
                fontWeight = FontWeight.Black,
                fontSize = (68f * fontSizeScale).sp,
                color = Color(0xFFEF4444).copy(alpha = 0.40f),
                shadow = Shadow(
                    color = Color(0xFFDC2626),
                    offset = Offset(0f, 0f),
                    blurRadius = 32f
                ),
                textAlign = TextAlign.Center
            )
        )

        // Foreground Speech
        Text(
            text = fullPhrase,
            style = TextStyle(
                fontFamily = AppFonts.Playfair,
                fontWeight = FontWeight.Bold,
                fontSize = (24f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(2f, 2f), 8f),
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 10. SCRIBBLE (Caveat + Yellow Highlighter Oval)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.ScribbleTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .background(
                                color = Color(0xFFFEF08A),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                color = Color(0xFFEAB308),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = word.text,
                            style = TextStyle(
                                fontFamily = AppFonts.Caveat,
                                fontWeight = FontWeight.Bold,
                                fontSize = (30f * fontSizeScale).sp,
                                color = Color.Black
                            )
                        )
                    }
                } else {
                    Text(
                        text = word.text + " ",
                        style = TextStyle(
                            fontFamily = AppFonts.Caveat,
                            fontSize = (28f * fontSizeScale).sp,
                            color = Color.White,
                            shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 11. ARCHIVES (Vintage Journal Script + Wavy Underline)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.ArchivesTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                Text(
                    text = word.text + " ",
                    style = TextStyle(
                        fontFamily = if (isActive) AppFonts.Caveat else AppFonts.Playfair,
                        fontStyle = if (isActive) FontStyle.Italic else FontStyle.Normal,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        fontSize = ((if (isActive) 34f else 24f) * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFFDE047) else Color.White,
                        textDecoration = if (isActive) TextDecoration.Underline else TextDecoration.None,
                        shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                    )
                )
            }
        }
    }
}

// ==========================================
// 12. BLOCKBUSTER (Neon Red Sans + White Cursive)
// ==========================================
@Composable
private fun BoxScope.BlockbusterTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val upperPart = window.precedingWords.takeLast(3).joinToString(" ") { it.text.uppercase() }
        .ifEmpty { "THIS IS THE NEXT" }
    val cursiveHero = window.activeWord.text.lowercase()

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = upperPart,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (26f * fontSizeScale).sp,
                color = Color(0xFFEF4444),
                shadow = Shadow(
                    color = Color(0xFFDC2626),
                    offset = Offset(0f, 0f),
                    blurRadius = 18f
                ),
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = cursiveHero,
            style = TextStyle(
                fontFamily = AppFonts.Caveat,
                fontSize = (40f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(2f, 2f), 6f),
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 13. JOURNAL (Caveat Motion Chalk Red)
// ==========================================
@Composable
private fun BoxScope.JournalTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val prevPart = window.precedingWords.takeLast(2).joinToString(" ") { it.text }
    val hero = window.activeWord.text

    Row(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prevPart.isNotEmpty()) {
            Text(
                text = "$prevPart ",
                style = TextStyle(
                    fontFamily = AppFonts.Caveat,
                    fontSize = (26f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            )
        }

        Text(
            text = hero,
            style = TextStyle(
                fontFamily = AppFonts.Caveat,
                fontWeight = FontWeight.Bold,
                fontSize = (38f * fontSizeScale).sp,
                color = Color(0xFFF87171),
                shadow = Shadow(
                    color = Color(0xFFDC2626).copy(alpha = 0.7f),
                    offset = Offset(0f, 0f),
                    blurRadius = 14f
                )
            )
        )
    }
}

// ==========================================
// 14. INTERLOCK (Space Mono + Playfair Italic)
// ==========================================
@Composable
private fun BoxScope.InterlockTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val header = window.precedingWords.takeLast(2).joinToString(" · ") { it.text.uppercase() }
        .ifEmpty { "EDITORIAL · INTERLOCK" }
    val italicHero = window.activeWord.text

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = header,
            style = TextStyle(
                fontFamily = AppFonts.SpaceMono,
                fontSize = (13f * fontSizeScale).sp,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = italicHero,
            style = TextStyle(
                fontFamily = AppFonts.Playfair,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                fontSize = (44f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(4f, 4f), 0f),
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 15. ALI ABDAAL (Crisp White Rounded Pill)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.AliAbdaalTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 64.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = word.text.lowercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Inter,
                                fontWeight = FontWeight.Black,
                                fontSize = (22f * fontSizeScale).sp,
                                color = Color.Black
                            )
                        )
                    }
                } else {
                    Text(
                        text = word.text.lowercase() + " ",
                        style = TextStyle(
                            fontFamily = AppFonts.Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = (20f * fontSizeScale).sp,
                            color = Color.White,
                            shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 16. CLEAN MOTION (Single Word Center Punch)
// ==========================================
@Composable
private fun BoxScope.CleanMotionTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.20f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "clean_motion_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = window.activeWord.text.lowercase(),
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Black,
                fontSize = (44f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(4f, 4f), 0f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "one word at a time",
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Medium,
                fontSize = (12f * fontSizeScale).sp,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
        )
    }
}

// ==========================================
// 17. BUBBLE STYLE (Mint/Teal Rounded Pill #2DD4BF)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.BubbleStyleTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .background(Color(0xFF2DD4BF), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = word.text.lowercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = (22f * fontSizeScale).sp,
                                color = Color.White
                            )
                        )
                    }
                } else {
                    Text(
                        text = word.text.lowercase() + " ",
                        style = TextStyle(
                            fontFamily = AppFonts.Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = (20f * fontSizeScale).sp,
                            color = Color.White,
                            shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 18. EDITING SKOOL (Bright Orange Rounded Pill #FF6600)
// ==========================================
@Composable
private fun BoxScope.EditingSkoolTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val topText = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val centerText = window.activeWord.text.uppercase()
    val bottomText = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (topText.isNotEmpty()) {
            Text(
                text = topText,
                style = TextStyle(
                    fontFamily = AppFonts.Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .background(Color(0xFFFF6600), RoundedCornerShape(10.dp))
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
            Text(
                text = centerText,
                style = TextStyle(
                    fontFamily = AppFonts.Poppins,
                    fontWeight = FontWeight.Black,
                    fontSize = (32f * fontSizeScale).sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (bottomText.isNotEmpty()) {
            Text(
                text = bottomText,
                style = TextStyle(
                    fontFamily = AppFonts.Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 19. MR BEAST STYLE 1 (Slanted Comic White + Heavy 3D Shadow)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.MrBeast1Template(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp)
            .rotate(-5f),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)
                val scaleAnim by animateFloatAsState(
                    targetValue = if (isActive) 1.25f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "beast1_${word.id}"
                )

                Text(
                    text = word.text.uppercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Anton,
                        fontSize = (36f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFF38BDF8) else Color.White,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(8f, 8f),
                            blurRadius = 0f
                        ),
                        letterSpacing = 1.5.sp
                    ),
                    modifier = Modifier.scale(scaleAnim)
                )
            }
        }
    }
}

// ==========================================
// 20. MR BEAST STYLE 2 (Slanted Yellow Active Word #FFE600)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.MrBeast2Template(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp)
            .rotate(-5f),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)
                val scaleAnim by animateFloatAsState(
                    targetValue = if (isActive) 1.25f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "beast2_${word.id}"
                )

                Text(
                    text = word.text.uppercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Anton,
                        fontSize = (36f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFFFE600) else Color.White,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(8f, 8f),
                            blurRadius = 0f
                        ),
                        letterSpacing = 1.5.sp
                    ),
                    modifier = Modifier.scale(scaleAnim)
                )
            }
        }
    }
}

// ==========================================
// 21. IMAN GADZHI (Clean Minimalist 2-Line All-Caps)
// ==========================================
@Composable
private fun BoxScope.ImanGadzhiTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val mid = (words.size / 2).coerceAtLeast(1)
    val line1 = words.take(mid).joinToString(" ") { it.text.uppercase() }
    val line2 = words.drop(mid).joinToString(" ") { it.text.uppercase() }

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = line1,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Bold,
                fontSize = (26f * fontSizeScale).sp,
                color = Color.White,
                letterSpacing = 2.5.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = line2,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Bold,
                fontSize = (26f * fontSizeScale).sp,
                color = Color.White,
                letterSpacing = 2.5.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 22. DEVIN JATHO (Electric Purple Neon #A855F7)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.DevinJathoTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                Text(
                    text = word.text.uppercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Montserrat,
                        fontWeight = FontWeight.Black,
                        fontSize = (30f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFA855F7) else Color.White,
                        shadow = if (isActive) {
                            Shadow(
                                color = Color(0xFFA855F7),
                                offset = Offset(0f, 0f),
                                blurRadius = 22f
                            )
                        } else {
                            Shadow(Color.Black, Offset(4f, 4f), 0f)
                        },
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

// ==========================================
// 23. HIGHLIGHTED WORD (Warm Amber #F59E0B)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.HighlightedWordTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                Text(
                    text = word.text + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Inter,
                        fontWeight = if (isActive) FontWeight.Black else FontWeight.Medium,
                        fontSize = (22f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFF59E0B) else Color.White,
                        shadow = if (isActive) {
                            Shadow(Color(0xFFF59E0B).copy(alpha = 0.8f), Offset(0f, 0f), 12f)
                        } else {
                            Shadow(Color.Black, Offset(2f, 2f), 4f)
                        }
                    )
                )
            }
        }
    }
}

// ==========================================
// 24. CLEAN GLOW STYLE (Ethereal White Glow)
// ==========================================
@Composable
private fun BoxScope.CleanGlowStyleTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val fullSentence = words.joinToString(" ") { it.text.lowercase() }

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = fullSentence,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Medium,
                fontSize = (20f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.95f),
                    offset = Offset(0f, 0f),
                    blurRadius = 18f
                ),
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 25. CAPTIK CLEAN (Minimalist Subtitles)
// ==========================================
@Composable
private fun BoxScope.CaptikCleanTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val fullSentence = words.joinToString(" ") { it.text }

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = fullSentence,
            style = TextStyle(
                fontFamily = AppFonts.Inter,
                fontWeight = FontWeight.Medium,
                fontSize = (19f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color.Black, Offset(2f, 2f), 4f),
                letterSpacing = (-0.3).sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 26. BLACK PUNCH (Silver Banner with Bold Black Text)
// ==========================================
@Composable
private fun BoxScope.BlackPunchTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val prevPart = window.precedingWords.takeLast(2).joinToString(" ") { it.text.lowercase() }
    val hero = window.activeWord.text.uppercase()
    val nextPart = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFFE2E8F0),
                        Color(0xFFFFFFFF),
                        Color(0xFFE2E8F0),
                        Color.Transparent
                    )
                )
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (prevPart.isNotEmpty()) {
                Text(
                    text = "$prevPart ",
                    style = TextStyle(
                        fontFamily = AppFonts.Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = (18f * fontSizeScale).sp,
                        color = Color(0xFF64748B)
                    )
                )
            }

            Text(
                text = hero,
                style = TextStyle(
                    fontFamily = AppFonts.Montserrat,
                    fontWeight = FontWeight.Black,
                    fontSize = (32f * fontSizeScale).sp,
                    color = Color.Black,
                    letterSpacing = 1.sp
                )
            )

            if (nextPart.isNotEmpty()) {
                Text(
                    text = " $nextPart",
                    style = TextStyle(
                        fontFamily = AppFonts.Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = (18f * fontSizeScale).sp,
                        color = Color(0xFF64748B)
                    )
                )
            }
        }
    }
}

// ==========================================
// 27. CAPTIK WORD (Progressive Voice Reveal)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.CaptikWordTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val hasBeenSpoken = word.startTime <= currentTime
                val isActive = word.isActiveAt(currentTime)

                Text(
                    text = word.text.lowercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = (22f * fontSizeScale).sp,
                        color = if (hasBeenSpoken) Color.White else Color.White.copy(alpha = 0.25f),
                        shadow = if (isActive) {
                            Shadow(Color.White.copy(alpha = 0.9f), Offset(0f, 0f), 12f)
                        } else {
                            Shadow(Color.Black, Offset(2f, 2f), 2f)
                        }
                    )
                )
            }
        }
    }
}

// ==========================================
// 28. PIXELATED WORD (Space Mono Retro Terminal)
// ==========================================
@Composable
private fun BoxScope.PixelatedWordTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val header = window.precedingWords.takeLast(2).joinToString("_") { it.text.uppercase() }
        .ifEmpty { "CODE_FLOW" }
    val hero = window.activeWord.text.uppercase()

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = header,
            style = TextStyle(
                fontFamily = AppFonts.SpaceMono,
                fontSize = (18f * fontSizeScale).sp,
                color = Color(0xFF22C55E),
                letterSpacing = 2.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = hero,
            style = TextStyle(
                fontFamily = AppFonts.SpaceMono,
                fontWeight = FontWeight.Bold,
                fontSize = (38f * fontSizeScale).sp,
                color = Color(0xFF22D3EE),
                shadow = Shadow(
                    color = Color(0xFF06B6D4),
                    offset = Offset(0f, 0f),
                    blurRadius = 14f
                ),
                letterSpacing = 3.sp
            )
        )
    }
}

// ==========================================
// 29. LIQUID GLASS (Frosted Glass Capsule)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.LiquidGlassTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF1E293B).copy(alpha = 0.65f),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.05f))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                words.forEach { word ->
                    val isActive = word.isActiveAt(currentTime)

                    Text(
                        text = word.text.lowercase() + " ",
                        style = TextStyle(
                            fontFamily = AppFonts.Inter,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            fontSize = (19f * fontSizeScale).sp,
                            color = if (isActive) Color(0xFF38BDF8) else Color.White,
                            shadow = if (isActive) {
                                Shadow(Color(0xFF38BDF8), Offset(0f, 0f), 10f)
                            } else null
                        )
                    )
                }
            }
        }
    }
}

// ==========================================
// 30. TABAHI (Slanted Extreme Indian Punch)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.TabahiTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp)
            .rotate(-6f),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)
                val scaleAnim by animateFloatAsState(
                    targetValue = if (isActive) 1.25f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "tabahi_${word.id}"
                )

                Text(
                    text = word.text.uppercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Anton,
                        fontSize = (40f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFFF0055) else Color.White,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(8f, 8f),
                            blurRadius = 0f
                        ),
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.scale(scaleAnim)
                )
            }
        }
    }
}

// ==========================================
// 31. DEEP GLOW (Magenta Neon #FF007F)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.DeepGlowTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)

                Text(
                    text = word.text.uppercase() + " ",
                    style = TextStyle(
                        fontFamily = AppFonts.Poppins,
                        fontWeight = FontWeight.Black,
                        fontSize = (32f * fontSizeScale).sp,
                        color = if (isActive) Color(0xFFFF007F) else Color.White,
                        shadow = if (isActive) {
                            Shadow(
                                color = Color(0xFFFF007F),
                                offset = Offset(0f, 0f),
                                blurRadius = 26f
                            )
                        } else {
                            Shadow(Color.Black, Offset(4f, 4f), 0f)
                        },
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

// ==========================================
// 32. SEEDHA SAADHA (Minimal Ultra-Heavy Center Punch)
// ==========================================
@Composable
private fun BoxScope.SeedhaSaadhaTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.20f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "seedha_scale"
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = window.activeWord.text.uppercase(),
            style = TextStyle(
                fontFamily = AppFonts.Anton,
                fontSize = (54f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(8f, 8f),
                    blurRadius = 0f
                ),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )
    }
}

// ==========================================
// 33. THORA CINEMATIC (Cinzel Luxury Serif Wide Tracking)
// ==========================================
@Composable
private fun BoxScope.ThoraCinematicTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val mid = (words.size / 2).coerceAtLeast(1)
    val line1 = words.take(mid).joinToString(" ") { it.text.uppercase() }
    val line2 = words.drop(mid).joinToString(" ") { it.text.uppercase() }

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = line1,
            style = TextStyle(
                fontFamily = AppFonts.Cinzel,
                fontWeight = FontWeight.Bold,
                fontSize = (22f * fontSizeScale).sp,
                color = Color.White.copy(alpha = 0.85f),
                shadow = Shadow(Color(0xFF38BDF8).copy(alpha = 0.4f), Offset(0f, 0f), 12f),
                letterSpacing = 3.5.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = line2,
            style = TextStyle(
                fontFamily = AppFonts.Cinzel,
                fontWeight = FontWeight.Black,
                fontSize = (30f * fontSizeScale).sp,
                color = Color.White,
                shadow = Shadow(Color(0xFF38BDF8).copy(alpha = 0.6f), Offset(0f, 0f), 16f),
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

// ==========================================
// 34. BIG REVEAL (Hero Bold Yellow + White Subtitle)
// ==========================================
@Composable
private fun BoxScope.BigRevealTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    fontSizeScale: Float
) {
    val window = getVoiceWindow(words, currentTime)
    val hero = window.activeWord.text.uppercase()
    val sub = window.upcomingWords.take(2).joinToString(" ") { it.text.lowercase() }

    val scaleAnim by animateFloatAsState(
        targetValue = if (window.isSpokenNow) 1.22f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "big_reveal_scale"
    )

    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.92f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hero,
            style = TextStyle(
                fontFamily = AppFonts.Montserrat,
                fontWeight = FontWeight.Black,
                fontSize = (48f * fontSizeScale).sp,
                color = Color(0xFFFEE500),
                shadow = Shadow(Color.Black, Offset(6f, 6f), 0f),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.scale(scaleAnim)
        )

        if (sub.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = sub,
                style = TextStyle(
                    fontFamily = AppFonts.Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (20f * fontSizeScale).sp,
                    color = Color.White,
                    shadow = Shadow(Color.Black, Offset(2f, 2f), 4f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ==========================================
// 35. KARAOKE FLOW (Neon Voice Highlight Sync)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BoxScope.KaraokeFlowTemplate(
    words: List<CaptionWord>,
    currentTime: Float,
    highlightColor: Color,
    fontSizeScale: Float
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .padding(bottom = 54.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            words.forEach { word ->
                val isActive = word.isActiveAt(currentTime)
                val wordScale by animateFloatAsState(
                    targetValue = if (isActive) 1.16f else 1.0f,
                    animationSpec = tween(120),
                    label = "word_scale_${word.id}"
                )

                Box(
                    modifier = Modifier
                        .scale(wordScale)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    // Stroke layer behind
                    Text(
                        text = word.text,
                        style = TextStyle(
                            fontFamily = AppFonts.Poppins,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (24f * fontSizeScale).sp,
                            color = Color.Black,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(4f, 4f),
                                blurRadius = 0f
                            ),
                            letterSpacing = 0.5.sp
                        )
                    )

                    // Foreground text with active word Neon Green/Gold glowing color
                    Text(
                        text = word.text,
                        style = TextStyle(
                            fontFamily = AppFonts.Poppins,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (24f * fontSizeScale).sp,
                            color = if (isActive) highlightColor else Color.White,
                            shadow = if (isActive) {
                                Shadow(
                                    color = highlightColor.copy(alpha = 0.9f),
                                    offset = Offset(0f, 0f),
                                    blurRadius = 16f
                                )
                            } else {
                                Shadow(
                                    color = Color.Black.copy(alpha = 0.8f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 2f
                                )
                            },
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}
