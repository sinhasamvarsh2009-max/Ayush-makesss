package com.example.service

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.model.CaptionSegment
import com.example.model.CaptionWord
import com.example.model.VideoProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * TranscriptionEngine handles AI speech recognition optimized for Hinglish, Hindi,
 * and English code-mixing with exact millisecond word-level timestamps.
 */
object TranscriptionEngine {

    private const val TAG = "TranscriptionEngine"
    private const val GEMINI_MODEL = "gemini-3.5-flash"
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * The exact Hinglish & code-mixing AI prompt specified in requirements
     */
    val SYSTEM_PROMPT = """
        You are an elite AI speech transcription engine specialized in Hinglish (colloquial Hindi-English code-mixing) and Indian cultural slang.
        
        CRITICAL RULES:
        1. Recognize and preserve Hinglish slang and code-mixing exactly as spoken (e.g., words like 'bhai', 'yaar', 'jugaad', 'ekdum', 'bawaal', 'mast', 'scene', 'bro').
        2. DO NOT force a pure English translation for the Hinglish transcript unless generating the english_translation array.
        3. Output a valid JSON payload containing three synchronized word arrays:
           - "roman_hinglish": Spoken words in Latin/Roman characters (e.g., "Bhai kya kar raha hai")
           - "native_script": Spoken words in Devanagari Hindi script (e.g., "भाई क्या कर रहा है")
           - "english_translation": Meaningful English translation (e.g., "Brother, what are you doing")
        4. Every single word in each array MUST have:
           - "text": The word string
           - "start_time": Floating point timestamp in seconds (with millisecond precision, e.g. 1.25)
           - "end_time": Floating point timestamp in seconds
           - "speaker": "Speaker A" or "Speaker B"
    """.trimIndent()

    /**
     * Default curated projects showcasing Hinglish code-mixing and all 4 typography templates
     */
    fun getPresetProjects(): List<VideoProject> {
        return listOf(
            createTechReviewProject(),
            createPodcastDuoProject(),
            createFoodVlogProject(),
            createCreatorInspirationProject()
        )
    }

    private fun createTechReviewProject(): VideoProject {
        val segments = listOf(
            CaptionSegment(
                id = "seg_tech_1",
                startTime = 0.40f,
                endTime = 2.80f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("w1", "Arre", 0.40f, 0.70f, "Speaker A"),
                    CaptionWord("w2", "bhai", 0.72f, 1.05f, "Speaker A"),
                    CaptionWord("w3", "sun,", 1.08f, 1.45f, "Speaker A"),
                    CaptionWord("w4", "is", 1.50f, 1.70f, "Speaker A"),
                    CaptionWord("w5", "phone", 1.72f, 2.05f, "Speaker A"),
                    CaptionWord("w6", "ka", 2.08f, 2.25f, "Speaker A"),
                    CaptionWord("w7", "camera", 2.28f, 2.80f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("nw1", "अरे", 0.40f, 0.70f, "Speaker A"),
                    CaptionWord("nw2", "भाई", 0.72f, 1.05f, "Speaker A"),
                    CaptionWord("nw3", "सुन,", 1.08f, 1.45f, "Speaker A"),
                    CaptionWord("nw4", "इस", 1.50f, 1.70f, "Speaker A"),
                    CaptionWord("nw5", "फोन", 1.72f, 2.05f, "Speaker A"),
                    CaptionWord("nw6", "का", 2.08f, 2.25f, "Speaker A"),
                    CaptionWord("nw7", "कैमरा", 2.28f, 2.80f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("ew1", "Hey", 0.40f, 0.70f, "Speaker A"),
                    CaptionWord("ew2", "brother", 0.72f, 1.05f, "Speaker A"),
                    CaptionWord("ew3", "listen,", 1.08f, 1.45f, "Speaker A"),
                    CaptionWord("ew4", "this", 1.50f, 1.70f, "Speaker A"),
                    CaptionWord("ew5", "phone's", 1.72f, 2.05f, "Speaker A"),
                    CaptionWord("ew6", "camera", 2.08f, 2.80f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_tech_2",
                startTime = 2.95f,
                endTime = 5.60f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("w8", "ekdum", 2.95f, 3.40f, "Speaker A"),
                    CaptionWord("w9", "crazy", 3.42f, 3.85f, "Speaker A"),
                    CaptionWord("w10", "hai!", 3.88f, 4.25f, "Speaker A"),
                    CaptionWord("w11", "Look", 4.30f, 4.60f, "Speaker A"),
                    CaptionWord("w12", "at", 4.62f, 4.80f, "Speaker A"),
                    CaptionWord("w13", "this", 4.82f, 5.05f, "Speaker A"),
                    CaptionWord("w14", "display.", 5.08f, 5.60f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("nw8", "एकदम", 2.95f, 3.40f, "Speaker A"),
                    CaptionWord("nw9", "क्रेज़ी", 3.42f, 3.85f, "Speaker A"),
                    CaptionWord("nw10", "है!", 3.88f, 4.25f, "Speaker A"),
                    CaptionWord("nw11", "लुक", 4.30f, 4.60f, "Speaker A"),
                    CaptionWord("nw12", "एट", 4.62f, 4.80f, "Speaker A"),
                    CaptionWord("nw13", "दिस", 4.82f, 5.05f, "Speaker A"),
                    CaptionWord("nw14", "डिस्प्ले।", 5.08f, 5.60f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("ew7", "is", 2.95f, 3.20f, "Speaker A"),
                    CaptionWord("ew8", "totally", 3.22f, 3.65f, "Speaker A"),
                    CaptionWord("ew9", "insane!", 3.68f, 4.25f, "Speaker A"),
                    CaptionWord("ew10", "Check", 4.30f, 4.65f, "Speaker A"),
                    CaptionWord("ew11", "out", 4.68f, 4.95f, "Speaker A"),
                    CaptionWord("ew12", "this", 4.98f, 5.25f, "Speaker A"),
                    CaptionWord("ew13", "display.", 5.28f, 5.60f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_tech_3",
                startTime = 5.75f,
                endTime = 8.80f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("w15", "120Hz", 5.75f, 6.30f, "Speaker A"),
                    CaptionWord("w16", "butter", 6.35f, 6.80f, "Speaker A"),
                    CaptionWord("w17", "smooth", 6.82f, 7.30f, "Speaker A"),
                    CaptionWord("w18", "chal", 7.35f, 7.70f, "Speaker A"),
                    CaptionWord("w19", "raha", 7.72f, 8.10f, "Speaker A"),
                    CaptionWord("w20", "hai!", 8.12f, 8.80f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("nw15", "120Hz", 5.75f, 6.30f, "Speaker A"),
                    CaptionWord("nw16", "बटर", 6.35f, 6.80f, "Speaker A"),
                    CaptionWord("nw17", "स्मूथ", 6.82f, 7.30f, "Speaker A"),
                    CaptionWord("nw18", "चल", 7.35f, 7.70f, "Speaker A"),
                    CaptionWord("nw19", "रहा", 7.72f, 8.10f, "Speaker A"),
                    CaptionWord("nw20", "है!", 8.12f, 8.80f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("ew14", "120Hz", 5.75f, 6.30f, "Speaker A"),
                    CaptionWord("ew15", "running", 6.35f, 6.85f, "Speaker A"),
                    CaptionWord("ew16", "butter", 6.88f, 7.35f, "Speaker A"),
                    CaptionWord("ew17", "smooth", 7.38f, 8.00f, "Speaker A"),
                    CaptionWord("ew18", "everywhere!", 8.05f, 8.80f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_tech_4",
                startTime = 8.95f,
                endTime = 12.00f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("w21", "Battery", 8.95f, 9.40f, "Speaker A"),
                    CaptionWord("w22", "life", 9.42f, 9.75f, "Speaker A"),
                    CaptionWord("w23", "toh", 9.78f, 10.05f, "Speaker A"),
                    CaptionWord("w24", "next", 10.08f, 10.45f, "Speaker A"),
                    CaptionWord("w25", "level", 10.48f, 10.95f, "Speaker A"),
                    CaptionWord("w26", "bawaal", 11.00f, 11.55f, "Speaker A"),
                    CaptionWord("w27", "hai.", 11.58f, 12.00f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("nw21", "बैटरी", 8.95f, 9.40f, "Speaker A"),
                    CaptionWord("nw22", "लाइफ", 9.42f, 9.75f, "Speaker A"),
                    CaptionWord("nw23", "तो", 9.78f, 10.05f, "Speaker A"),
                    CaptionWord("nw24", "नेक्स्ट", 10.08f, 10.45f, "Speaker A"),
                    CaptionWord("nw25", "लेवल", 10.48f, 10.95f, "Speaker A"),
                    CaptionWord("nw26", "बवाल", 11.00f, 11.55f, "Speaker A"),
                    CaptionWord("nw27", "है।", 11.58f, 12.00f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("ew19", "The", 8.95f, 9.20f, "Speaker A"),
                    CaptionWord("ew20", "battery", 9.22f, 9.65f, "Speaker A"),
                    CaptionWord("ew21", "life", 9.68f, 10.05f, "Speaker A"),
                    CaptionWord("ew22", "is", 10.08f, 10.35f, "Speaker A"),
                    CaptionWord("ew23", "truly", 10.38f, 10.85f, "Speaker A"),
                    CaptionWord("ew24", "next", 10.88f, 11.35f, "Speaker A"),
                    CaptionWord("ew25", "level.", 11.38f, 12.00f, "Speaker A")
                )
            )
        )

        return VideoProject(
            id = "proj_tech_1",
            title = "Tech Review: Camera & 120Hz Butter Smooth",
            description = "High-energy Hinglish review with slang, slang code-mixing, and punchy transitions.",
            durationSeconds = 12.5f,
            segments = segments,
            category = "Gadgets & Tech"
        )
    }

    private fun createPodcastDuoProject(): VideoProject {
        val segments = listOf(
            CaptionSegment(
                id = "seg_pod_1",
                startTime = 0.50f,
                endTime = 3.60f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("pw1", "Maine", 0.50f, 0.90f, "Speaker A"),
                    CaptionWord("pw2", "bola", 0.92f, 1.30f, "Speaker A"),
                    CaptionWord("pw3", "bro,", 1.32f, 1.70f, "Speaker A"),
                    CaptionWord("pw4", "startup", 1.75f, 2.25f, "Speaker A"),
                    CaptionWord("pw5", "me", 2.28f, 2.55f, "Speaker A"),
                    CaptionWord("pw6", "hustle", 2.58f, 3.05f, "Speaker A"),
                    CaptionWord("pw7", "is", 3.08f, 3.25f, "Speaker A"),
                    CaptionWord("pw8", "real.", 3.28f, 3.60f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("pnw1", "मैंने", 0.50f, 0.90f, "Speaker A"),
                    CaptionWord("pnw2", "बोला", 0.92f, 1.30f, "Speaker A"),
                    CaptionWord("pnw3", "ब्रो,", 1.32f, 1.70f, "Speaker A"),
                    CaptionWord("pnw4", "स्टार्टअप", 1.75f, 2.25f, "Speaker A"),
                    CaptionWord("pnw5", "में", 2.28f, 2.55f, "Speaker A"),
                    CaptionWord("pnw6", "हसल", 2.58f, 3.05f, "Speaker A"),
                    CaptionWord("pnw7", "इज़", 3.08f, 3.25f, "Speaker A"),
                    CaptionWord("pnw8", "रियल।", 3.28f, 3.60f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("pew1", "I", 0.50f, 0.70f, "Speaker A"),
                    CaptionWord("pew2", "said", 0.72f, 1.10f, "Speaker A"),
                    CaptionWord("pew3", "bro,", 1.12f, 1.50f, "Speaker A"),
                    CaptionWord("pew4", "hustle", 1.55f, 2.10f, "Speaker A"),
                    CaptionWord("pew5", "in", 2.12f, 2.40f, "Speaker A"),
                    CaptionWord("pew6", "startups", 2.42f, 2.95f, "Speaker A"),
                    CaptionWord("pew7", "is", 2.98f, 3.20f, "Speaker A"),
                    CaptionWord("pew8", "real.", 3.22f, 3.60f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_pod_2",
                startTime = 3.80f,
                endTime = 7.10f,
                speaker = "Speaker B",
                romanWords = listOf(
                    CaptionWord("pw9", "Par", 3.80f, 4.10f, "Speaker B"),
                    CaptionWord("pw10", "burnout", 4.12f, 4.65f, "Speaker B"),
                    CaptionWord("pw11", "bhi", 4.68f, 4.95f, "Speaker B"),
                    CaptionWord("pw12", "toh", 4.98f, 5.25f, "Speaker B"),
                    CaptionWord("pw13", "hota", 5.28f, 5.65f, "Speaker B"),
                    CaptionWord("pw14", "hai", 5.68f, 5.95f, "Speaker B"),
                    CaptionWord("pw15", "na!", 5.98f, 6.30f, "Speaker B"),
                    CaptionWord("pw16", "Health", 6.35f, 6.70f, "Speaker B"),
                    CaptionWord("pw17", "mat", 6.72f, 6.90f, "Speaker B"),
                    CaptionWord("pw18", "bhulo.", 6.92f, 7.10f, "Speaker B")
                ),
                nativeWords = listOf(
                    CaptionWord("pnw9", "पर", 3.80f, 4.10f, "Speaker B"),
                    CaptionWord("pnw10", "बर्नआउट", 4.12f, 4.65f, "Speaker B"),
                    CaptionWord("pnw11", "भी", 4.68f, 4.95f, "Speaker B"),
                    CaptionWord("pnw12", "तो", 4.98f, 5.25f, "Speaker B"),
                    CaptionWord("pnw13", "होता", 5.28f, 5.65f, "Speaker B"),
                    CaptionWord("pnw14", "है", 5.68f, 5.95f, "Speaker B"),
                    CaptionWord("pnw15", "ना!", 5.98f, 6.30f, "Speaker B"),
                    CaptionWord("pnw16", "हेल्थ", 6.35f, 6.70f, "Speaker B"),
                    CaptionWord("pnw17", "मत", 6.72f, 6.90f, "Speaker B"),
                    CaptionWord("pnw18", "भूलो।", 6.92f, 7.10f, "Speaker B")
                ),
                englishWords = listOf(
                    CaptionWord("pew9", "But", 3.80f, 4.10f, "Speaker B"),
                    CaptionWord("pew10", "burnout", 4.12f, 4.70f, "Speaker B"),
                    CaptionWord("pew11", "also", 4.72f, 5.10f, "Speaker B"),
                    CaptionWord("pew12", "happens,", 5.12f, 5.70f, "Speaker B"),
                    CaptionWord("pew13", "right?", 5.72f, 6.20f, "Speaker B"),
                    CaptionWord("pew14", "Never", 6.25f, 6.65f, "Speaker B"),
                    CaptionWord("pew15", "forget", 6.68f, 7.00f, "Speaker B"),
                    CaptionWord("pew16", "health.", 7.02f, 7.10f, "Speaker B")
                )
            ),
            CaptionSegment(
                id = "seg_pod_3",
                startTime = 7.30f,
                endTime = 10.50f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("pw19", "Ekdum", 7.30f, 7.75f, "Speaker A"),
                    CaptionWord("pw20", "sahi", 7.78f, 8.10f, "Speaker A"),
                    CaptionWord("pw21", "baat,", 8.12f, 8.50f, "Speaker A"),
                    CaptionWord("pw22", "smart", 8.55f, 9.00f, "Speaker A"),
                    CaptionWord("pw23", "work", 9.02f, 9.45f, "Speaker A"),
                    CaptionWord("pw24", "beats", 9.48f, 9.90f, "Speaker A"),
                    CaptionWord("pw25", "blind", 9.92f, 10.20f, "Speaker A"),
                    CaptionWord("pw26", "hustle.", 10.22f, 10.50f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("pnw19", "एकदम", 7.30f, 7.75f, "Speaker A"),
                    CaptionWord("pnw20", "सही", 7.78f, 8.10f, "Speaker A"),
                    CaptionWord("pnw21", "बात,", 8.12f, 8.50f, "Speaker A"),
                    CaptionWord("pnw22", "स्मार्ट", 8.55f, 9.00f, "Speaker A"),
                    CaptionWord("pnw23", "वर्क", 9.02f, 9.45f, "Speaker A"),
                    CaptionWord("pnw24", "बीट्स", 9.48f, 9.90f, "Speaker A"),
                    CaptionWord("pnw25", "ब्लाइंड", 9.92f, 10.20f, "Speaker A"),
                    CaptionWord("pnw26", "हसल।", 10.22f, 10.50f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("pew17", "Spot", 7.30f, 7.60f, "Speaker A"),
                    CaptionWord("pew18", "on,", 7.62f, 8.00f, "Speaker A"),
                    CaptionWord("pew19", "smart", 8.02f, 8.55f, "Speaker A"),
                    CaptionWord("pew20", "work", 8.58f, 9.00f, "Speaker A"),
                    CaptionWord("pew21", "beats", 9.02f, 9.50f, "Speaker A"),
                    CaptionWord("pew22", "blind", 9.52f, 9.95f, "Speaker A"),
                    CaptionWord("pew23", "hustle", 9.98f, 10.35f, "Speaker A"),
                    CaptionWord("pew24", "daily.", 10.38f, 10.50f, "Speaker A")
                )
            )
        )

        return VideoProject(
            id = "proj_pod_2",
            title = "Podcast Duo: Hustle Culture vs Burnout",
            description = "Conversational dialogue between Speaker A (White) and Speaker B (Tailwind Yellow 400).",
            durationSeconds = 11.2f,
            segments = segments,
            category = "Podcast & Talk"
        )
    }

    private fun createFoodVlogProject(): VideoProject {
        val segments = listOf(
            CaptionSegment(
                id = "seg_food_1",
                startTime = 0.30f,
                endTime = 3.50f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("fw1", "Aaj", 0.30f, 0.65f, "Speaker A"),
                    CaptionWord("fw2", "hum", 0.68f, 0.95f, "Speaker A"),
                    CaptionWord("fw3", "explore", 0.98f, 1.45f, "Speaker A"),
                    CaptionWord("fw4", "karne", 1.48f, 1.85f, "Speaker A"),
                    CaptionWord("fw5", "wale", 1.88f, 2.25f, "Speaker A"),
                    CaptionWord("fw6", "hain", 2.28f, 2.55f, "Speaker A"),
                    CaptionWord("fw7", "Old", 2.58f, 2.95f, "Speaker A"),
                    CaptionWord("fw8", "Delhi", 2.98f, 3.50f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("fnw1", "आज", 0.30f, 0.65f, "Speaker A"),
                    CaptionWord("fnw2", "हम", 0.68f, 0.95f, "Speaker A"),
                    CaptionWord("fnw3", "एक्सप्लोर", 0.98f, 1.45f, "Speaker A"),
                    CaptionWord("fnw4", "करने", 1.48f, 1.85f, "Speaker A"),
                    CaptionWord("fnw5", "वाले", 1.88f, 2.25f, "Speaker A"),
                    CaptionWord("fnw6", "हैं", 2.28f, 2.55f, "Speaker A"),
                    CaptionWord("fnw7", "पुरानी", 2.58f, 2.95f, "Speaker A"),
                    CaptionWord("fnw8", "दिल्ली", 2.98f, 3.50f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("few1", "Today", 0.30f, 0.70f, "Speaker A"),
                    CaptionWord("few2", "we", 0.72f, 1.00f, "Speaker A"),
                    CaptionWord("few3", "are", 1.02f, 1.30f, "Speaker A"),
                    CaptionWord("few4", "going", 1.32f, 1.75f, "Speaker A"),
                    CaptionWord("few5", "to", 1.78f, 2.05f, "Speaker A"),
                    CaptionWord("few6", "explore", 2.08f, 2.70f, "Speaker A"),
                    CaptionWord("few7", "Old", 2.72f, 3.10f, "Speaker A"),
                    CaptionWord("few8", "Delhi", 3.12f, 3.50f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_food_2",
                startTime = 3.65f,
                endTime = 7.00f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("fw9", "ki", 3.65f, 3.90f, "Speaker A"),
                    CaptionWord("fw10", "famous", 3.92f, 4.45f, "Speaker A"),
                    CaptionWord("fw11", "secret", 4.48f, 5.00f, "Speaker A"),
                    CaptionWord("fw12", "Parathe", 5.05f, 5.65f, "Speaker A"),
                    CaptionWord("fw13", "Wali", 5.68f, 6.15f, "Speaker A"),
                    CaptionWord("fw14", "Galli!", 6.18f, 7.00f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("fnw9", "की", 3.65f, 3.90f, "Speaker A"),
                    CaptionWord("fnw10", "फेमस", 3.92f, 4.45f, "Speaker A"),
                    CaptionWord("fnw11", "सीक्रेट", 4.48f, 5.00f, "Speaker A"),
                    CaptionWord("fnw12", "पराठे", 5.05f, 5.65f, "Speaker A"),
                    CaptionWord("fnw13", "वाली", 5.68f, 6.15f, "Speaker A"),
                    CaptionWord("fnw14", "गली!", 6.18f, 7.00f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("few9", "its", 3.65f, 3.90f, "Speaker A"),
                    CaptionWord("few10", "famous", 3.92f, 4.45f, "Speaker A"),
                    CaptionWord("few11", "secret", 4.48f, 5.00f, "Speaker A"),
                    CaptionWord("few12", "Parathe", 5.05f, 5.65f, "Speaker A"),
                    CaptionWord("few13", "Wali", 5.68f, 6.15f, "Speaker A"),
                    CaptionWord("few14", "Lane!", 6.18f, 7.00f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_food_3",
                startTime = 7.15f,
                endTime = 10.20f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("fw15", "Crispy", 7.15f, 7.65f, "Speaker A"),
                    CaptionWord("fw16", "hot", 7.68f, 8.05f, "Speaker A"),
                    CaptionWord("fw17", "paratha,", 8.08f, 8.65f, "Speaker A"),
                    CaptionWord("fw18", "yaar", 8.68f, 9.05f, "Speaker A"),
                    CaptionWord("fw19", "swaad", 9.08f, 9.55f, "Speaker A"),
                    CaptionWord("fw20", "aa", 9.58f, 9.85f, "Speaker A"),
                    CaptionWord("fw21", "gaya!", 9.88f, 10.20f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("fnw15", "क्रिस्पी", 7.15f, 7.65f, "Speaker A"),
                    CaptionWord("fnw16", "हॉट", 7.68f, 8.05f, "Speaker A"),
                    CaptionWord("fnw17", "पराठा,", 8.08f, 8.65f, "Speaker A"),
                    CaptionWord("fnw18", "यार", 8.68f, 9.05f, "Speaker A"),
                    CaptionWord("fnw19", "स्वाद", 9.08f, 9.55f, "Speaker A"),
                    CaptionWord("fnw20", "आ", 9.58f, 9.85f, "Speaker A"),
                    CaptionWord("fnw21", "गया!", 9.88f, 10.20f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("few15", "Crispy", 7.15f, 7.60f, "Speaker A"),
                    CaptionWord("few16", "hot", 7.62f, 8.00f, "Speaker A"),
                    CaptionWord("few17", "parathas,", 8.02f, 8.60f, "Speaker A"),
                    CaptionWord("few18", "taste", 8.62f, 9.10f, "Speaker A"),
                    CaptionWord("few19", "is", 9.12f, 9.45f, "Speaker A"),
                    CaptionWord("few20", "pure", 9.48f, 9.85f, "Speaker A"),
                    CaptionWord("few21", "bliss!", 9.88f, 10.20f, "Speaker A")
                )
            )
        )

        return VideoProject(
            id = "proj_food_3",
            title = "Food Vlog: Old Delhi Parathe Wali Galli",
            description = "Fast-paced Instagram reel format with appetizing Hinglish expressions.",
            durationSeconds = 10.8f,
            segments = segments,
            category = "Food & Travel"
        )
    }

    private fun createCreatorInspirationProject(): VideoProject {
        val segments = listOf(
            CaptionSegment(
                id = "seg_insp_1",
                startTime = 0.40f,
                endTime = 3.60f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("cw1", "Agar", 0.40f, 0.75f, "Speaker A"),
                    CaptionWord("cw2", "aap", 0.78f, 1.05f, "Speaker A"),
                    CaptionWord("cw3", "content", 1.08f, 1.60f, "Speaker A"),
                    CaptionWord("cw4", "create", 1.62f, 2.15f, "Speaker A"),
                    CaptionWord("cw5", "karna", 2.18f, 2.65f, "Speaker A"),
                    CaptionWord("cw6", "chahte", 2.68f, 3.15f, "Speaker A"),
                    CaptionWord("cw7", "ho,", 3.18f, 3.60f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("cnw1", "अगर", 0.40f, 0.75f, "Speaker A"),
                    CaptionWord("cnw2", "आप", 0.78f, 1.05f, "Speaker A"),
                    CaptionWord("cnw3", "कंटेंट", 1.08f, 1.60f, "Speaker A"),
                    CaptionWord("cnw4", "क्रिएट", 1.62f, 2.15f, "Speaker A"),
                    CaptionWord("cnw5", "करना", 2.18f, 2.65f, "Speaker A"),
                    CaptionWord("cnw6", "चाहते", 2.68f, 3.15f, "Speaker A"),
                    CaptionWord("cnw7", "हो,", 3.18f, 3.60f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("cew1", "If", 0.40f, 0.70f, "Speaker A"),
                    CaptionWord("cew2", "you", 0.72f, 1.00f, "Speaker A"),
                    CaptionWord("cew3", "want", 1.02f, 1.45f, "Speaker A"),
                    CaptionWord("cew4", "to", 1.48f, 1.75f, "Speaker A"),
                    CaptionWord("cew5", "make", 1.78f, 2.25f, "Speaker A"),
                    CaptionWord("cew6", "content,", 2.28f, 3.60f, "Speaker A")
                )
            ),
            CaptionSegment(
                id = "seg_insp_2",
                startTime = 3.80f,
                endTime = 7.20f,
                speaker = "Speaker A",
                romanWords = listOf(
                    CaptionWord("cw8", "toh", 3.80f, 4.10f, "Speaker A"),
                    CaptionWord("cw9", "overthinking", 4.12f, 4.85f, "Speaker A"),
                    CaptionWord("cw10", "chhod", 4.88f, 5.35f, "Speaker A"),
                    CaptionWord("cw11", "do.", 5.38f, 5.75f, "Speaker A"),
                    CaptionWord("cw12", "Just", 5.80f, 6.20f, "Speaker A"),
                    CaptionWord("cw13", "hit", 6.22f, 6.60f, "Speaker A"),
                    CaptionWord("cw14", "record!", 6.62f, 7.20f, "Speaker A")
                ),
                nativeWords = listOf(
                    CaptionWord("cnw8", "तो", 3.80f, 4.10f, "Speaker A"),
                    CaptionWord("cnw9", "ओवरथिंकिंग", 4.12f, 4.85f, "Speaker A"),
                    CaptionWord("cnw10", "छोड़", 4.88f, 5.35f, "Speaker A"),
                    CaptionWord("cnw11", "दो।", 5.38f, 5.75f, "Speaker A"),
                    CaptionWord("cnw12", "जस्ट", 5.80f, 6.20f, "Speaker A"),
                    CaptionWord("cnw13", "हिट", 6.22f, 6.60f, "Speaker A"),
                    CaptionWord("cnw14", "रिकॉर्ड!", 6.62f, 7.20f, "Speaker A")
                ),
                englishWords = listOf(
                    CaptionWord("cew7", "then", 3.80f, 4.15f, "Speaker A"),
                    CaptionWord("cew8", "stop", 4.18f, 4.60f, "Speaker A"),
                    CaptionWord("cew9", "overthinking.", 4.62f, 5.50f, "Speaker A"),
                    CaptionWord("cew10", "Just", 5.55f, 6.00f, "Speaker A"),
                    CaptionWord("cew11", "press", 6.02f, 6.55f, "Speaker A"),
                    CaptionWord("cew12", "record!", 6.58f, 7.20f, "Speaker A")
                )
            )
        )

        return VideoProject(
            id = "proj_insp_4",
            title = "Creator Motivation: Overthinking Chhod Do",
            description = "Motivational speaking clip with rapid word emphasis.",
            durationSeconds = 7.8f,
            segments = segments,
            category = "Motivation"
        )
    }

    /**
     * Transcribe arbitrary Hinglish speech text or video audio using Gemini API
     */
    suspend fun transcribeSpeechWithGemini(userSpeech: String): VideoProject? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "No active Gemini API key found, generating localized transcription")
            return@withContext generateLocalHinglishProject(userSpeech)
        }

        try {
            val prompt = """
                $SYSTEM_PROMPT
                
                Transcribe the following audio/speech input:
                "$userSpeech"
                
                Return JSON only in this exact format:
                {
                  "segments": [
                    {
                      "speaker": "Speaker A",
                      "roman_hinglish": [
                        {"text": "word", "start_time": 0.0, "end_time": 0.5, "speaker": "Speaker A"}
                      ],
                      "native_script": [
                        {"text": "शब्द", "start_time": 0.0, "end_time": 0.5, "speaker": "Speaker A"}
                      ],
                      "english_translation": [
                        {"text": "word", "start_time": 0.0, "end_time": 0.5, "speaker": "Speaker A"}
                      ]
                    }
                  ]
                }
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.2)
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$GEMINI_BASE_URL$GEMINI_MODEL:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini API error ${response.code}, falling back to local engine")
                return@withContext generateLocalHinglishProject(userSpeech)
            }

            val responseBody = response.body?.string() ?: return@withContext generateLocalHinglishProject(userSpeech)
            val jsonRoot = JSONObject(responseBody)
            val candidates = jsonRoot.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textPart = parts?.optJSONObject(0)?.optString("text")

            if (textPart.isNullOrBlank()) {
                return@withContext generateLocalHinglishProject(userSpeech)
            }

            parseTranscriptionJson(textPart, userSpeech)
        } catch (e: Exception) {
            Log.e(TAG, "Error in transcribeSpeechWithGemini: ${e.message}", e)
            generateLocalHinglishProject(userSpeech)
        }
    }

    private fun parseTranscriptionJson(jsonString: String, originalTitle: String): VideoProject {
        val root = JSONObject(jsonString)
        val segmentsArray = root.optJSONArray("segments") ?: JSONArray()
        val segmentsList = mutableListOf<CaptionSegment>()
        var maxTime = 0.0f

        for (i in 0 until segmentsArray.length()) {
            val segObj = segmentsArray.getJSONObject(i)
            val speaker = segObj.optString("speaker", "Speaker A")

            val romanWords = parseWordsArray(segObj.optJSONArray("roman_hinglish"), speaker)
            val nativeWords = parseWordsArray(segObj.optJSONArray("native_script"), speaker)
            val englishWords = parseWordsArray(segObj.optJSONArray("english_translation"), speaker)

            val startTime = romanWords.firstOrNull()?.startTime ?: (i * 3.0f)
            val endTime = romanWords.lastOrNull()?.endTime ?: (startTime + 3.0f)
            if (endTime > maxTime) maxTime = endTime

            segmentsList.add(
                CaptionSegment(
                    id = "seg_${UUID.randomUUID().toString().take(8)}",
                    startTime = startTime,
                    endTime = endTime,
                    speaker = speaker,
                    romanWords = romanWords,
                    nativeWords = nativeWords,
                    englishWords = englishWords
                )
            )
        }

        return VideoProject(
            id = "proj_${UUID.randomUUID().toString().take(8)}",
            title = originalTitle.take(35).ifBlank { "Custom Hinglish Video" },
            description = "AI Hinglish transcription with word-level timestamps",
            durationSeconds = (maxTime + 1.0f).coerceAtLeast(5.0f),
            segments = segmentsList,
            category = "Imported Video"
        )
    }

    private fun parseWordsArray(arr: JSONArray?, defaultSpeaker: String): List<CaptionWord> {
        val list = mutableListOf<CaptionWord>()
        if (arr == null) return list

        for (j in 0 until arr.length()) {
            val w = arr.getJSONObject(j)
            list.add(
                CaptionWord(
                    id = "w_${UUID.randomUUID().toString().take(8)}",
                    text = w.optString("text", ""),
                    startTime = w.optDouble("start_time", 0.0).toFloat(),
                    endTime = w.optDouble("end_time", 0.5).toFloat(),
                    speaker = w.optString("speaker", defaultSpeaker)
                )
            )
        }
        return list
    }

    /**
     * Highly responsive fallback generator for when offline or custom imported video speech
     */
    fun generateLocalHinglishProject(speechText: String): VideoProject {
        val words = speechText.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val chunkSize = 6
        val segmentsList = mutableListOf<CaptionSegment>()
        var currentOffset = 0.5f

        val chunks = words.chunked(chunkSize)
        chunks.forEachIndexed { index, chunkWords ->
            val speaker = if (index % 2 == 0) "Speaker A" else "Speaker B"
            val segmentStart = currentOffset
            val romanWords = mutableListOf<CaptionWord>()
            val nativeWords = mutableListOf<CaptionWord>()
            val englishWords = mutableListOf<CaptionWord>()

            chunkWords.forEach { word ->
                val duration = (0.28f + (word.length * 0.04f)).coerceIn(0.25f, 0.70f)
                val wStart = currentOffset
                val wEnd = wStart + duration
                currentOffset = wEnd + 0.06f

                romanWords.add(CaptionWord("rw_${UUID.randomUUID().toString().take(6)}", word, wStart, wEnd, speaker))
                nativeWords.add(CaptionWord("nw_${UUID.randomUUID().toString().take(6)}", transliterateToHindi(word), wStart, wEnd, speaker))
                englishWords.add(CaptionWord("ew_${UUID.randomUUID().toString().take(6)}", translateHinglishWord(word), wStart, wEnd, speaker))
            }

            val segmentEnd = currentOffset
            currentOffset += 0.35f

            segmentsList.add(
                CaptionSegment(
                    id = "seg_loc_$index",
                    startTime = segmentStart,
                    endTime = segmentEnd,
                    speaker = speaker,
                    romanWords = romanWords,
                    nativeWords = nativeWords,
                    englishWords = englishWords
                )
            )
        }

        val totalDuration = (currentOffset + 1.0f).coerceAtLeast(6.0f)
        return VideoProject(
            id = "proj_loc_${System.currentTimeMillis()}",
            title = speechText.take(30).ifBlank { "Hinglish Video Session" },
            description = "Auto-generated Hinglish caption timeline with word synchronization",
            durationSeconds = totalDuration,
            segments = segmentsList,
            category = "Custom Speech"
        )
    }

    private fun transliterateToHindi(word: String): String {
        val lower = word.lowercase().replace(Regex("[^a-zA-Z]"), "")
        return when (lower) {
            "bhai" -> "भाई"
            "kya" -> "क्या"
            "kar" -> "कर"
            "raha" -> "रहा"
            "hai" -> "है"
            "sun" -> "सुन"
            "arre" -> "अरे"
            "ekdum" -> "एकदम"
            "bawaal" -> "बवाल"
            "mast" -> "मस्त"
            "scene" -> "सीन"
            "startup" -> "स्टार्टअप"
            "bro" -> "ब्रो"
            "hustle" -> "हसल"
            "health" -> "हेल्थ"
            "phone" -> "फोन"
            "camera" -> "कैमरा"
            "battery" -> "बैटरी"
            "par" -> "पर"
            "bhi" -> "भी"
            "toh" -> "तो"
            "hota" -> "होता"
            "na" -> "ना"
            "aaj" -> "आज"
            "hum" -> "हम"
            "explore" -> "एक्सप्लोर"
            "delhi" -> "दिल्ली"
            "paratha" -> "पराठा"
            "swaad" -> "स्वाद"
            else -> word // Keep original if unknown
        }
    }

    private fun translateHinglishWord(word: String): String {
        val lower = word.lowercase().replace(Regex("[^a-zA-Z]"), "")
        return when (lower) {
            "bhai" -> "brother"
            "kya" -> "what"
            "kar" -> "do"
            "raha" -> "doing"
            "hai" -> "is"
            "sun" -> "listen"
            "arre" -> "hey"
            "ekdum" -> "totally"
            "bawaal" -> "crazy"
            "mast" -> "awesome"
            "par" -> "but"
            "bhi" -> "also"
            "toh" -> "then"
            "hota" -> "happens"
            "na" -> "right"
            "aaj" -> "today"
            "hum" -> "we"
            "swaad" -> "taste"
            else -> word
        }
    }
}
