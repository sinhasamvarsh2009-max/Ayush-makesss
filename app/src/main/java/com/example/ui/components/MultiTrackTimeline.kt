package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionSegment
import com.example.model.OutputScript
import java.util.Locale

/**
 * Multi-Track Timeline matching the bottom timeline panel in the CaptionCraft screenshot:
 * Features track side-labels (Video Track, Caption Track, + Add Track),
 * precise time-ruler marks (00:00, 00:02, 00:04, 00:06...),
 * filmstrip thumbnail representations for video,
 * word-by-word block pills with active voice highlight ([Bhai] [kya] [kar] [raha] [hai]),
 * synchronized purple vertical playhead scrubber line,
 * and bottom controls (speed selector, fit screen, zoom controls).
 */
@Composable
fun MultiTrackTimeline(
    currentTime: Float,
    durationSeconds: Float,
    segments: List<CaptionSegment>,
    script: OutputScript,
    isPlaying: Boolean,
    playbackSpeed: Float = 1.0f,
    onTogglePlay: () -> Unit,
    onSeek: (Float) -> Unit,
    onSpeedChange: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var timelineWidthPx by remember { mutableStateOf(1f) }
    var zoomFactor by remember { mutableStateOf(1.0f) }
    var videoTrackVisible by remember { mutableStateOf(true) }
    var captionTrackVisible by remember { mutableStateOf(true) }

    Surface(
        color = Color(0xFF0F172A),
        border = BorderStroke(1.dp, Color(0xFF1E293B)),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Control Header: Transport controls + time indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Playback Transport Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onSeek((currentTime - 2.0f).coerceAtLeast(0f)) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind 2s",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFA855F7), Color(0xFF7C3AED))
                                ),
                                CircleShape
                            )
                            .clickable { onTogglePlay() }
                            .testTag("timeline_play_pause_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    IconButton(
                        onClick = { onSeek((currentTime + 2.0f).coerceAtMost(durationSeconds)) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 2s",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Time Code Display from Screenshot (00:02.74 / 00:20.48)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatMillis(currentTime),
                        color = Color(0xFFD8B4FE),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " / ${formatMillis(durationSeconds)}",
                        color = Color.White.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Time Ruler Ticks (00:00, 00:02, 00:04, 00:06, 00:08, 00:10...)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 74.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val step = (durationSeconds / 5).coerceAtLeast(1f)
                for (i in 0..5) {
                    val sec = i * step
                    Text(
                        text = formatRulerTime(sec),
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Multi-Track Viewport: Left Track Labels + Right Canvas Tracks
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
            ) {
                // Left Track Headers
                Column(
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Header 1: Video Track
                    Surface(
                        color = Color(0xFF161F30),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF1E293B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clickable { videoTrackVisible = !videoTrackVisible }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Video", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "Toggle",
                                    tint = if (videoTrackVisible) Color(0xFFA855F7) else Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Text("Track 1", color = Color.White.copy(alpha = 0.4f), fontSize = 8.sp)
                        }
                    }

                    // Header 2: Caption Track
                    Surface(
                        color = Color(0xFF161F30),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF1E293B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clickable { captionTrackVisible = !captionTrackVisible }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Captions", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "Toggle",
                                    tint = if (captionTrackVisible) Color(0xFF00FF66) else Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Text("Voice Sync", color = Color(0xFF00FF66).copy(alpha = 0.8f), fontSize = 8.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Right: Scrubber Canvas Tracks
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF080C14))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .onGloballyPositioned { coordinates ->
                            timelineWidthPx = coordinates.size.width.toFloat().coerceAtLeast(1f)
                        }
                        .pointerInput(durationSeconds) {
                            detectTapGestures { offset ->
                                val fraction = (offset.x / timelineWidthPx).coerceIn(0f, 1f)
                                onSeek(fraction * durationSeconds)
                            }
                        }
                        .pointerInput(durationSeconds) {
                            detectDragGestures { change, _ ->
                                change.consume()
                                val fraction = (change.position.x / timelineWidthPx).coerceIn(0f, 1f)
                                onSeek(fraction * durationSeconds)
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // TRACK 1: Video Filmstrip Sequence
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (videoTrackVisible) Color(0xFF162235) else Color(0xFF0B111E))
                                .border(0.5.dp, Color(0xFF1E293B), RoundedCornerShape(4.dp))
                        ) {
                            if (videoTrackVisible) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (frame in 0..7) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .border(0.5.dp, Color(0xFF1E293B))
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                                                    )
                                                )
                                                .padding(2.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            Text(
                                                text = "${frame * 2}s",
                                                color = Color.White.copy(alpha = 0.3f),
                                                fontSize = 7.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // TRACK 2: Caption Word Blocks
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (captionTrackVisible) Color(0xFF0B111E) else Color(0xFF070B12))
                                .border(0.5.dp, Color(0xFF1E293B), RoundedCornerShape(4.dp))
                        ) {
                            if (captionTrackVisible) {
                                val allWords = segments.flatMap { it.getWordsForScript(script) }

                                allWords.forEach { word ->
                                    val startFraction = (word.startTime / durationSeconds.coerceAtLeast(1f)).coerceIn(0f, 1f)
                                    val endFraction = (word.endTime / durationSeconds.coerceAtLeast(1f)).coerceIn(0f, 1f)
                                    val blockWidthFraction = (endFraction - startFraction).coerceAtLeast(0.025f)
                                    val isWordActive = word.isActiveAt(currentTime)

                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(blockWidthFraction)
                                            .offset { IntOffset((startFraction * timelineWidthPx).toInt(), 0) }
                                            .padding(vertical = 2.dp, horizontal = 1.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                when {
                                                    isWordActive -> Color(0xFF00FF66)
                                                    else -> Color(0xFFA855F7).copy(alpha = 0.65f)
                                                }
                                            )
                                            .border(
                                                width = if (isWordActive) 1.5.dp else 0.5.dp,
                                                color = if (isWordActive) Color.White else Color.Transparent,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .clickable { onSeek(word.startTime) }
                                            .padding(horizontal = 2.dp, vertical = 2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = word.text,
                                            color = if (isWordActive) Color.Black else Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = if (isWordActive) FontWeight.Black else FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Synchronized Purple Playhead needle with Pin handle
                    val playheadFraction = (currentTime / durationSeconds.coerceAtLeast(1f)).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.5.dp)
                            .offset { IntOffset((playheadFraction * timelineWidthPx).toInt(), 0) }
                            .background(Color(0xFFA855F7))
                    ) {
                        // Purple needle cap handle
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(10.dp)
                                .offset(y = (-2).dp)
                                .background(Color(0xFFA855F7), CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Timeline Toolbar: [1.0x ▾] [Fit] [🔍 Zoom Slider]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Playback speed chip
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier.clickable {
                        val next = when (playbackSpeed) {
                            1.0f -> 1.5f
                            1.5f -> 2.0f
                            2.0f -> 0.75f
                            else -> 1.0f
                        }
                        onSpeedChange(next)
                    }
                ) {
                    Text(
                        text = "${playbackSpeed}x ▾",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Zoom controls matching screenshot bottom right
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomOut,
                        contentDescription = "Zoom Out",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { zoomFactor = (zoomFactor - 0.2f).coerceAtLeast(0.6f) }
                    )

                    Slider(
                        value = zoomFactor,
                        onValueChange = { zoomFactor = it },
                        valueRange = 0.6f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFA855F7),
                            activeTrackColor = Color(0xFFA855F7),
                            inactiveTrackColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.width(80.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom In",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { zoomFactor = (zoomFactor + 0.2f).coerceAtMost(2.0f) }
                    )
                }
            }
        }
    }
}

private fun formatMillis(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    val ms = ((seconds - totalSeconds) * 100).toInt()
    return String.format(Locale.US, "%02d:%02d.%02d", mins, secs, ms)
}

private fun formatRulerTime(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}
