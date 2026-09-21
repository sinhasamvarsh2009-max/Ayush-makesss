package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.AspectRatioOption
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
import com.example.ui.templates.CaptionOverlay
import java.util.Locale
import kotlin.math.sin

/**
 * Center Live Video Preview Canvas showing the video layer and the burned-in animated subtitles.
 */
@Composable
fun VideoPlayerCanvas(
    videoUri: String?,
    currentTime: Float,
    durationSeconds: Float,
    isPlaying: Boolean,
    playbackSpeed: Float,
    aspectRatio: AspectRatioOption,
    template: CaptionTemplate,
    script: OutputScript,
    karaokeColor: Color,
    fontSizeScale: Float,
    activeSegment: CaptionSegment?,
    onTogglePlay: () -> Unit,
    onSeek: (Float) -> Unit,
    onSpeedChange: (Float) -> Unit,
    primaryTextColor: Color = Color.White,
    secondaryTextColor: Color = karaokeColor,
    activeTextEffect: TextEffect = TextEffect.BOLD_DROP,
    activeAnimation: TextAnimation = TextAnimation.SCALE_IN,
    textPosition: TextPosition = TextPosition.CENTER,
    textAlignment: TextAlignment = TextAlignment.CENTER,
    backgroundOpacity: Float = 0.5f,
    letterSpacingOption: LetterSpacingOption = LetterSpacingOption.NORMAL,
    selectedFontName: String = "Montserrat (900)",
    textCaseOption: TextCaseOption = TextCaseOption.UPPERCASE,
    autoEmojisEnabled: Boolean = true,
    showSafeZone: Boolean = false,
    onToggleSafeZone: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B12)),
        contentAlignment = Alignment.Center
    ) {
        // Dynamic Aspect Ratio Box
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxHeight(0.95f)
                .aspectRatio(aspectRatio.ratio)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .background(Color(0xFF0D131F))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showControls = !showControls
                },
            contentAlignment = Alignment.Center
        ) {
            // Video Surface Layer
            if (videoUri != null) {
                RealVideoPlayer(
                    uriString = videoUri,
                    isPlaying = isPlaying,
                    currentTime = currentTime
                )
            } else {
                // Synthetic Cinematic Creator Canvas
                CinematicVideoSimulation(
                    currentTime = currentTime,
                    durationSeconds = durationSeconds,
                    isPlaying = isPlaying
                )
            }

            // Top Status Bar in Preview
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(if (isPlaying) Color(0xFF22C55E) else Color(0xFFEAB308), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isPlaying) "PREVIEW LIVE" else "PAUSED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    // Template & Aspect ratio badge
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFF06B6D4).copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF06B6D4).copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = template.title,
                                color = Color(0xFF22D3EE),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = aspectRatio.label,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Reels Safe Zone Guide Toggle
                        Surface(
                            color = if (showSafeZone) Color(0xFF10B981).copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (showSafeZone) Color(0xFF10B981) else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.clickable { onToggleSafeZone() }
                        ) {
                            Text(
                                text = if (showSafeZone) "🛡️ Safe Zone" else "🛡️ Safe Zone",
                                color = if (showSafeZone) Color(0xFF34D399) else Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = if (showSafeZone) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // BURNED-IN ANIMATED SUBTITLE OVERLAY (The Core Engine)
            CaptionOverlay(
                activeSegment = activeSegment,
                currentTime = currentTime,
                template = template,
                script = script,
                karaokeColor = secondaryTextColor,
                fontSizeScale = fontSizeScale,
                primaryColor = primaryTextColor,
                secondaryColor = secondaryTextColor,
                textEffect = activeTextEffect,
                textAnimation = activeAnimation,
                textPosition = textPosition,
                textAlignment = textAlignment,
                backgroundOpacity = backgroundOpacity,
                letterSpacing = letterSpacingOption,
                selectedFontName = selectedFontName,
                textCase = textCaseOption,
                autoEmojisEnabled = autoEmojisEnabled,
                modifier = Modifier.fillMaxSize()
            )

            // Instagram Reels / TikTok / Shorts Safe Zone Viewport Overlay
            if (showSafeZone) {
                ReelsSafeZoneOverlay()
            }

            // Play/Pause Overlay Button when tapped or paused
            AnimatedVisibility(
                visible = showControls || !isPlaying,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Bottom Player HUD overlay
            AnimatedVisibility(
                visible = showControls || !isPlaying,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Time display
                        Text(
                            text = "${formatTimer(currentTime)} / ${formatTimer(durationSeconds)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        // Quick Speed Switcher
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { spd ->
                                val isSelected = playbackSpeed == spd
                                Text(
                                    text = "${spd}x",
                                    color = if (isSelected) Color(0xFF00FF00) else Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier
                                        .clickable { onSpeedChange(spd) }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Native Android VideoView for actual device video playback
 */
@Composable
private fun RealVideoPlayer(
    uriString: String,
    isPlaying: Boolean,
    currentTime: Float
) {
    val context = LocalContext.current
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(Uri.parse(uriString))
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    if (isPlaying) start() else pause()
                }
                videoViewRef = this
            }
        },
        update = { vv ->
            if (isPlaying && !vv.isPlaying) {
                vv.start()
            } else if (!isPlaying && vv.isPlaying) {
                vv.pause()
            }
            val targetMs = (currentTime * 1000).toInt()
            if (kotlin.math.abs(vv.currentPosition - targetMs) > 350) {
                vv.seekTo(targetMs)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

/**
 * Realistic Cinematic Video Simulation for instant responsive creator editing
 */
@Composable
private fun CinematicVideoSimulation(
    currentTime: Float,
    durationSeconds: Float,
    isPlaying: Boolean
) {
    // Dynamic cinematic scene backgrounds based on time
    val progress = (currentTime / durationSeconds.coerceAtLeast(1f)).coerceIn(0f, 1f)
    val color1 = Color(0xFF091E3A)
    val color2 = Color(0xFF1E1035)
    val color3 = Color(0xFF2A1B0E)

    val currentBg = when {
        progress < 0.33f -> Brush.radialGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0A0F1D)))
        progress < 0.66f -> Brush.radialGradient(listOf(Color(0xFF581C87), Color(0xFF0F0A1C)))
        else -> Brush.radialGradient(listOf(Color(0xFF78350F), Color(0xFF160D08)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBg)
    ) {
        // Decorative video grid / creator aesthetics
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stylized Creator Waveform simulation
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                for (i in 0..19) {
                    val waveHeight = if (isPlaying) {
                        (14 + 26 * sin(currentTime * 4.0 + i * 0.45)).toFloat().coerceIn(6f, 40f)
                    } else {
                        10f
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.5.dp)
                            .width(3.5.dp)
                            .height(waveHeight.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
                                ),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

private fun formatTimer(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    val millis = ((seconds - totalSeconds) * 10).toInt()
    return String.format(Locale.US, "%02d:%02d.%d", mins, secs, millis)
}

/**
 * Authentic Instagram Reels / TikTok / YouTube Shorts Safe Zone Guide
 * Guides creators so burned-in captions never get cut off by platform UI overlays.
 */
@Composable
private fun ReelsSafeZoneOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top dangerous area (Stories bar, audio tag, back navigation)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.12f)
                .align(Alignment.TopCenter)
                .background(Color(0x28EF4444))
                .border(1.dp, Color(0x55EF4444))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "⚠️ Top UI Zone (Stories / Back / Audio)",
                color = Color(0xFFFCA5A5),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Right side dangerous area (Like, Comment, Bookmark, Share, Audio Disc)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.18f)
                .align(Alignment.CenterEnd)
                .background(Color(0x28EF4444))
                .border(1.dp, Color(0x55EF4444))
                .padding(4.dp)
        ) {
            Text(
                text = "⚠️ Right Icons\n(Likes, Shares)",
                color = Color(0xFFFCA5A5),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Bottom dangerous area (Username, Caption text, Audio title)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.18f)
                .align(Alignment.BottomCenter)
                .background(Color(0x28EF4444))
                .border(1.dp, Color(0x55EF4444))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "⚠️ Bottom UI Zone (Creator Handle & Sound Track)",
                color = Color(0xFFFCA5A5),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }

        // Optimal Center Safe Zone Box
        Box(
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .fillMaxHeight(0.68f)
                .align(Alignment.Center)
                .border(1.5.dp, Color(0x8810B981), RoundedCornerShape(8.dp))
                .padding(6.dp)
        ) {
            Text(
                text = "✓ 100% REELS SAFE ZONE",
                color = Color(0xFF34D399),
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
