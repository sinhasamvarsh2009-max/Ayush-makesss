package com.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

/**
 * Authentic typography font families installed locally via font-util CLI
 * used across all 33+ caption templates and video voice overlays.
 */
object AppFonts {
    val Montserrat = FontFamily(
        Font(R.font.montserrat, FontWeight.Normal)
    )
    val Inter = FontFamily(
        Font(R.font.inter, FontWeight.Normal)
    )
    val Playfair = FontFamily(
        Font(R.font.playfair_display, FontWeight.Normal)
    )
    val Caveat = FontFamily(
        Font(R.font.caveat, FontWeight.Normal)
    )
    val Anton = FontFamily(
        Font(R.font.anton, FontWeight.Normal)
    )
    val Poppins = FontFamily(
        Font(R.font.poppins, FontWeight.Normal)
    )
    val Cinzel = FontFamily(
        Font(R.font.cinzel, FontWeight.Normal)
    )
    val SpaceMono = FontFamily(
        Font(R.font.space_mono, FontWeight.Normal)
    )
}
