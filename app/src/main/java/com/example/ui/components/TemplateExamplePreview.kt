package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionTemplate
import com.example.ui.theme.AppFonts

/**
 * Renders an authentic 4-5 word typography sample for each template
 * matching its specific colors, stroke, drop shadow, and typography layout.
 */
@Composable
fun TemplateExamplePreview(
    template: CaptionTemplate,
    isSelected: Boolean
) {
    Surface(
        color = Color(0xFF070B14),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.4f) else Color(0xFF1A2333)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            when (template) {
                CaptionTemplate.CAPTIK_GLOW -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "SUCCESS" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color(0xFF00FF66),
                                shadow = Shadow(color = Color(0xFF00FF66), offset = Offset(0f, 0f), blurRadius = 14f),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = template.sampleWords.drop(template.sampleActiveIndex + 1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                    }
                }

                CaptionTemplate.CAPTIK_SHADOW -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "CONSISTENT" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White,
                                shadow = Shadow(color = Color.Black, offset = Offset(5f, 5f), blurRadius = 0f),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = template.sampleWords.drop(template.sampleActiveIndex + 1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                    }
                }

                CaptionTemplate.CAPTIK -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "MINDSET" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color(0xFFCCFF00),
                                shadow = Shadow(color = Color.Black, offset = Offset(4f, 4f), blurRadius = 0f),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = template.sampleWords.drop(template.sampleActiveIndex + 1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                    }
                }

                CaptionTemplate.DELHI -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word.lowercase() + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Playfair,
                                    fontStyle = if (isActive) FontStyle.Italic else FontStyle.Normal,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = if (isActive) 15.sp else 12.sp,
                                    color = if (isActive) Color(0xFFFDE047) else Color.White,
                                    shadow = if (isActive) Shadow(Color(0xFFFDE047), Offset(0f, 0f), 12f) else Shadow(Color.Black, Offset(2f, 2f), 2f)
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.ILLUSION -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Medium, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "DISCIPLINE" },
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color.White,
                                shadow = Shadow(color = Color.Black, offset = Offset(5f, 5f), blurRadius = 0f),
                                letterSpacing = (-0.5).sp
                            )
                        )
                        Text(
                            text = template.sampleWords.drop(template.sampleActiveIndex + 1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Medium, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                        )
                    }
                }

                CaptionTemplate.EDITOR_MASALA -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(2).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "PROCESS" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Anton,
                                fontSize = 20.sp,
                                color = Color(0xFFFFDE00),
                                shadow = Shadow(color = Color.Black, offset = Offset(5f, 5f), blurRadius = 0f),
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                CaptionTemplate.AURA -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(2).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Caveat, fontSize = 15.sp, color = Color.White)
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "STATUS" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color(0xFF7DD3FC),
                                shadow = Shadow(Color(0xFF38BDF8), Offset(0f, 0f), 12f),
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }

                CaptionTemplate.SWISS -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(2).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "DEEPLY" }.uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Inter,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color(0xFFFACC15),
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }

                CaptionTemplate.THE_BIG_RED -> {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "STORY",
                            style = TextStyle(
                                fontFamily = AppFonts.Cinzel,
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp,
                                color = Color(0xFFEF4444).copy(alpha = 0.35f),
                                shadow = Shadow(Color(0xFFDC2626), Offset(0f, 0f), 18f)
                            )
                        )
                        Text(
                            text = template.sampleWords.joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Playfair, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        )
                    }
                }

                CaptionTemplate.SCRIBBLE -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFEF08A), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFFEAB308), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = word,
                                        style = TextStyle(fontFamily = AppFonts.Caveat, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                                    )
                                }
                            } else {
                                Text(
                                    text = word + " ",
                                    style = TextStyle(fontFamily = AppFonts.Caveat, fontSize = 14.sp, color = Color.White)
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.ARCHIVES -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word + " ",
                                style = TextStyle(
                                    fontFamily = if (isActive) AppFonts.Caveat else AppFonts.Playfair,
                                    fontStyle = if (isActive) FontStyle.Italic else FontStyle.Normal,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = if (isActive) 15.sp else 12.sp,
                                    color = if (isActive) Color(0xFFFDE047) else Color.White,
                                    textDecoration = if (isActive) TextDecoration.Underline else TextDecoration.None
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.BLOCKBUSTER -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "THIS IS THE NEXT",
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color(0xFFEF4444), shadow = Shadow(Color(0xFFDC2626), Offset(0f, 0f), 10f))
                        )
                        Text(
                            text = template.sampleWords.lastOrNull()?.lowercase() ?: "big thing",
                            style = TextStyle(fontFamily = AppFonts.Caveat, fontSize = 17.sp, color = Color.White)
                        )
                    }
                }

                CaptionTemplate.JOURNAL -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = template.sampleWords.take(2).joinToString(" ") + " ",
                            style = TextStyle(fontFamily = AppFonts.Caveat, fontSize = 13.sp, color = Color.White.copy(alpha = 0.5f))
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "every detail" },
                            style = TextStyle(fontFamily = AppFonts.Caveat, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFF87171), shadow = Shadow(Color(0xFFDC2626), Offset(0f, 0f), 10f))
                        )
                    }
                }

                CaptionTemplate.INTERLOCK -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "EDITORIAL · ARCHIVE",
                            style = TextStyle(fontFamily = AppFonts.SpaceMono, fontSize = 8.sp, color = Color.White.copy(alpha = 0.7f), letterSpacing = 2.sp)
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "Aesthetic" },
                            style = TextStyle(fontFamily = AppFonts.Playfair, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                        )
                    }
                }

                CaptionTemplate.ALI_ABDAAL -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = word.lowercase(),
                                        style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Black, fontSize = 11.sp, color = Color.Black)
                                    )
                                }
                            } else {
                                Text(
                                    text = word.lowercase() + " ",
                                    style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color.White)
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.CLEAN_MOTION -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "focus" }.lowercase(),
                            style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                        )
                        Text(
                            text = "one word at a time",
                            style = TextStyle(fontFamily = AppFonts.Inter, fontSize = 8.sp, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                        )
                    }
                }

                CaptionTemplate.BUBBLE_STYLE -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2DD4BF), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = word.lowercase(),
                                        style = TextStyle(fontFamily = AppFonts.Poppins, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                    )
                                }
                            } else {
                                Text(
                                    text = word.lowercase() + " ",
                                    style = TextStyle(fontFamily = AppFonts.Poppins, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color.White)
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.EDITING_SKOOL -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.take(1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Poppins, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF6600), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "SHORT" }.uppercase(),
                                style = TextStyle(fontFamily = AppFonts.Poppins, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White, letterSpacing = 1.sp)
                            )
                        }
                    }
                }

                CaptionTemplate.MR_BEAST_1 -> {
                    Box(modifier = Modifier.rotate(-4f), contentAlignment = Alignment.Center) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            template.sampleWords.forEachIndexed { idx, word ->
                                val isActive = idx == template.sampleActiveIndex
                                Text(
                                    text = word.uppercase() + " ",
                                    style = TextStyle(
                                        fontFamily = AppFonts.Anton,
                                        fontSize = if (isActive) 16.sp else 13.sp,
                                        color = if (isActive) Color(0xFF38BDF8) else Color.White,
                                        shadow = Shadow(Color.Black, Offset(4f, 4f), 0f),
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.MR_BEAST_2 -> {
                    Box(modifier = Modifier.rotate(-4f), contentAlignment = Alignment.Center) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            template.sampleWords.forEachIndexed { idx, word ->
                                val isActive = idx == template.sampleActiveIndex
                                Text(
                                    text = word.uppercase() + " ",
                                    style = TextStyle(
                                        fontFamily = AppFonts.Anton,
                                        fontSize = if (isActive) 16.sp else 13.sp,
                                        color = if (isActive) Color(0xFFFFE600) else Color.White,
                                        shadow = Shadow(Color.Black, Offset(4f, 4f), 0f),
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.IMAN_GADZHI -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TIME TO TAKE",
                            style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, letterSpacing = 2.sp)
                        )
                        Text(
                            text = "ACTION NOW",
                            style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, letterSpacing = 2.sp)
                        )
                    }
                }

                CaptionTemplate.DEVIN_JATHO -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word.uppercase() + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Montserrat,
                                    fontWeight = FontWeight.Black,
                                    fontSize = if (isActive) 14.sp else 12.sp,
                                    color = if (isActive) Color(0xFFA855F7) else Color.White,
                                    shadow = if (isActive) Shadow(Color(0xFFA855F7), Offset(0f, 0f), 14f) else Shadow(Color.Black, Offset(2f, 2f), 0f)
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.HIGHLIGHTED_WORD -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Inter,
                                    fontWeight = if (isActive) FontWeight.Black else FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = if (isActive) Color(0xFFF59E0B) else Color.White,
                                    shadow = if (isActive) Shadow(Color(0xFFF59E0B), Offset(0f, 0f), 8f) else null
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.CLEAN_GLOW_STYLE -> {
                    Text(
                        text = template.sampleWords.joinToString(" ") { it.lowercase() },
                        style = TextStyle(
                            fontFamily = AppFonts.Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color.White,
                            shadow = Shadow(Color.White.copy(alpha = 0.9f), Offset(0f, 0f), 12f)
                        )
                    )
                }

                CaptionTemplate.CAPTIK_CLEAN -> {
                    Text(
                        text = template.sampleWords.joinToString(" "),
                        style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = Color.White)
                    )
                }

                CaptionTemplate.BLACK_PUNCH -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color(0xFFE2E8F0), Color(0xFFFFFFFF), Color(0xFFE2E8F0), Color.Transparent)
                                )
                            )
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "POWER" }.uppercase(),
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.Black, letterSpacing = 1.sp)
                        )
                    }
                }

                CaptionTemplate.CAPTIK_WORD -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word.lowercase() + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Inter,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (idx <= template.sampleActiveIndex) Color.White else Color.White.copy(alpha = 0.3f),
                                    shadow = if (isActive) Shadow(Color.White, Offset(0f, 0f), 8f) else null
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.PIXELATED_WORD -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SYS_INIT",
                            style = TextStyle(fontFamily = AppFonts.SpaceMono, fontSize = 10.sp, color = Color(0xFF22C55E), letterSpacing = 1.sp)
                        )
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "MATRIX" }.uppercase(),
                            style = TextStyle(fontFamily = AppFonts.SpaceMono, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF22D3EE), shadow = Shadow(Color(0xFF06B6D4), Offset(0f, 0f), 10f), letterSpacing = 2.sp)
                        )
                    }
                }

                CaptionTemplate.LIQUID_GLASS -> {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1E293B).copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                            .border(1.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.05f))), RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            template.sampleWords.forEachIndexed { idx, word ->
                                val isActive = idx == template.sampleActiveIndex
                                Text(
                                    text = word.lowercase() + " ",
                                    style = TextStyle(
                                        fontFamily = AppFonts.Inter,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (isActive) Color(0xFF38BDF8) else Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.TABAHI -> {
                    Box(modifier = Modifier.rotate(-5f), contentAlignment = Alignment.Center) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            template.sampleWords.forEachIndexed { idx, word ->
                                val isActive = idx == template.sampleActiveIndex
                                Text(
                                    text = word.uppercase() + " ",
                                    style = TextStyle(
                                        fontFamily = AppFonts.Anton,
                                        fontSize = if (isActive) 16.sp else 13.sp,
                                        color = if (isActive) Color(0xFFFF0055) else Color.White,
                                        shadow = Shadow(Color.Black, Offset(4f, 4f), 0f),
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }
                }

                CaptionTemplate.DEEP_GLOW -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word.uppercase() + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Poppins,
                                    fontWeight = FontWeight.Black,
                                    fontSize = if (isActive) 14.sp else 12.sp,
                                    color = if (isActive) Color(0xFFFF007F) else Color.White,
                                    shadow = if (isActive) Shadow(Color(0xFFFF007F), Offset(0f, 0f), 16f) else Shadow(Color.Black, Offset(2f, 2f), 0f)
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.SEEDHA_SAADHA -> {
                    Text(
                        text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "FOCUS" }.uppercase(),
                        style = TextStyle(fontFamily = AppFonts.Anton, fontSize = 22.sp, color = Color.White, shadow = Shadow(Color.Black, Offset(5f, 5f), 0f), letterSpacing = 1.sp)
                    )
                }

                CaptionTemplate.THORA_CINEMATIC -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CHASING THE",
                            style = TextStyle(fontFamily = AppFonts.Cinzel, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f), letterSpacing = 3.sp)
                        )
                        Text(
                            text = "HORIZON",
                            style = TextStyle(fontFamily = AppFonts.Cinzel, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White, shadow = Shadow(Color(0xFF38BDF8).copy(alpha = 0.6f), Offset(0f, 0f), 12f), letterSpacing = 3.sp)
                        )
                    }
                }

                CaptionTemplate.BIG_REVEAL -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = template.sampleWords.getOrElse(template.sampleActiveIndex) { "SECRET" }.uppercase(),
                            style = TextStyle(fontFamily = AppFonts.Montserrat, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFFFEE500), shadow = Shadow(Color.Black, Offset(4f, 4f), 0f), letterSpacing = 1.sp)
                        )
                        Text(
                            text = template.sampleWords.drop(template.sampleActiveIndex + 1).joinToString(" ") { it.lowercase() },
                            style = TextStyle(fontFamily = AppFonts.Inter, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color.White)
                        )
                    }
                }

                CaptionTemplate.KARAOKE_FLOW -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isActive = idx == template.sampleActiveIndex
                            Text(
                                text = word.uppercase() + " ",
                                style = TextStyle(
                                    fontFamily = AppFonts.Poppins,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = if (isActive) 13.sp else 11.sp,
                                    color = if (isActive) Color(0xFF00FF66) else Color.White,
                                    shadow = if (isActive) Shadow(Color(0xFF00FF66), Offset(0f, 0f), 12f) else Shadow(Color.Black, Offset(2f, 2f), 2f)
                                )
                            )
                        }
                    }
                }

                CaptionTemplate.BOLD_DROP -> {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = template.sampleWords.joinToString(" ").uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(start = 3.dp, top = 3.dp)
                        )
                        Text(
                            text = template.sampleWords.joinToString(" ").uppercase(),
                            style = TextStyle(
                                fontFamily = AppFonts.Montserrat,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = Color(0xFFFACC15)
                            )
                        )
                    }
                }

                CaptionTemplate.REELS_CLEAN -> {
                    Text(
                        text = template.sampleWords.joinToString(" "),
                        style = TextStyle(
                            fontFamily = AppFonts.Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color.White,
                            shadow = Shadow(Color.Black.copy(alpha = 0.8f), Offset(1f, 1f), 3f)
                        )
                    )
                }

                CaptionTemplate.PODCAST_DUO -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        template.sampleWords.forEachIndexed { idx, word ->
                            val isSpeakerA = idx % 2 == 0
                            Text(
                                text = word,
                                style = TextStyle(
                                    fontFamily = AppFonts.Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSpeakerA) Color(0xFF38BDF8) else Color(0xFFF472B6)
                                )
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        text = template.sampleWords.joinToString(" "),
                        style = TextStyle(
                            fontFamily = AppFonts.Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
