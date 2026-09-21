package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Supported script formats for Hinglish auto-caption output
 */
enum class OutputScript(val displayName: String, val badgeText: String, val sampleText: String) {
    ROMAN_HINGLISH("Roman Hinglish", "Hinglish", "Bhai kya kar raha hai"),
    NATIVE_HINDI("Native Script (Devanagari)", "हिन्दी", "भाई क्या कर रहा है"),
    ENGLISH_TRANSLATION("English Translation", "English", "Brother, what are you doing")
}

enum class TemplateCategory(val label: String) {
    CORE_GLOW("Core & Glow"),
    KINETIC_EDITORIAL("Kinetic & Editorial"),
    HANDWRITTEN("Handwritten"),
    STATIC_CAPTIONS("Static Captions"),
    INFLUENCER("Creator & Influencer"),
    CLEAN_HIGHLIGHT("Clean & Highlight"),
    AI_CAPTIONS("AI Captions")
}

/**
 * All 34 authentic typography templates matching Captik editor screenshots,
 * with exact badges, categories, font styles, and voice-synced sample phrases.
 */
enum class CaptionTemplate(
    val title: String,
    val category: TemplateCategory,
    val fontDescription: String,
    val styleNotes: String,
    val positionLabel: String,
    val badge: String = "",
    val tags: List<String> = emptyList(),
    val sampleWords: List<String> = listOf("THIS", "IS", "TRENDING", "VIRAL", "CONTENT"),
    val sampleActiveIndex: Int = 2
) {
    // --- Screenshot 1: Core & Glow ---
    BOLD_DROP(
        title = "Bold Drop",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "Heavy 3D crisp black drop shadow with high-contrast uppercase punch, as seen on CaptionCraft",
        positionLabel = "Center Frame Punch",
        badge = "Featured",
        tags = listOf("Bold", "Shadow"),
        sampleWords = listOf("BHAI", "KYA", "KAR", "RAHA", "HAI"),
        sampleActiveIndex = 2
    ),
    REELS_CLEAN(
        title = "Reels Clean",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 600 SemiBold",
        styleNotes = "Minimalist subtitles with customizable translucent rounded background pill",
        positionLabel = "Bottom Safe Zone",
        tags = listOf("Static"),
        sampleWords = listOf("Your", "text", "here"),
        sampleActiveIndex = 1
    ),
    PODCAST_DUO(
        title = "Podcast Duo",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Montserrat · 700 Bold",
        styleNotes = "Dual-speaker layout: Speaker A in white, Speaker B in gold yellow with distinct badge chips",
        positionLabel = "Center 2-Line",
        tags = listOf("Bold"),
        sampleWords = listOf("Speaker A: Text here", "Speaker B: Text here"),
        sampleActiveIndex = 1
    ),
    CAPTIK_GLOW(
        title = "Captik Glow",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "3-line kinetic voice sync: active spoken word is huge neon green (#00FF66) with luminous back-glow",
        positionLabel = "Center Frame (3-Line Stack)",
        badge = "Popular",
        tags = listOf("Bold", "Glow"),
        sampleWords = listOf("the", "quick", "BROWN", "fox", "jumps"),
        sampleActiveIndex = 2
    ),
    CAPTIK_SHADOW(
        title = "Captik Shadow",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "3-line voice sync: active spoken word is solid white with crisp 3D black drop shadow (4px 4px)",
        positionLabel = "Center Frame (3-Line Stack)",
        badge = "New",
        tags = listOf("Bold"),
        sampleWords = listOf("the", "quick", "BROWN", "fox", "jumps"),
        sampleActiveIndex = 2
    ),
    CAPTIK(
        title = "Captik",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "3-line voice sync: active spoken word is high-contrast electric lime-yellow (#CCFF00)",
        positionLabel = "Center Frame (3-Line Stack)",
        tags = listOf("Bold"),
        sampleWords = listOf("the", "quick", "BROWN", "fox", "jumps"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 2: Handwritten & Kinetic ---
    DELHI(
        title = "Delhi",
        category = TemplateCategory.HANDWRITTEN,
        fontDescription = "Playfair Display · Italic Serif",
        styleNotes = "Centered phrase: active spoken word illuminates in delicate glowing italic cursive script",
        positionLabel = "Center Frame",
        sampleWords = listOf("the", "quick", "fox"),
        sampleActiveIndex = 1
    ),
    ILLUSION(
        title = "Illusion",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Montserrat · Ultra Heavy",
        styleNotes = "3-line stack: active spoken word expands into giant bold white headline",
        positionLabel = "Center Kinetic Stack",
        badge = "New",
        tags = listOf("Bold", "Kinetic"),
        sampleWords = listOf("he had", "rented the", "BIGGEST", "apartment", "in town"),
        sampleActiveIndex = 2
    ),
    EDITOR_MASALA(
        title = "Editor Masala",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Anton · Ultra Heavy",
        styleNotes = "Punchy 2-line kinetic: active spoken word explodes in vivid chrome yellow (#FFDE00)",
        positionLabel = "Center Kinetic",
        badge = "Smart",
        tags = listOf("Bold", "Kinetic"),
        sampleWords = listOf("trust", "the", "PROCESS"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 3: Editorial & Luxury ---
    AURA(
        title = "Aura",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Caveat Cursive + Montserrat Sans",
        styleNotes = "Dual typography: cursive script paired with bold cyan/blue uppercase (#7DD3FC)",
        positionLabel = "Center Editorial",
        badge = "Editorial",
        tags = listOf("Bold", "Kinetic"),
        sampleWords = listOf("forget", "STATUS"),
        sampleActiveIndex = 1
    ),
    SWISS(
        title = "Swiss",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Inter · Ultra Bold",
        styleNotes = "Swiss minimalist 2-line layout: bold white sans paired with vibrant golden yellow (#FACC15)",
        positionLabel = "Center Editorial",
        badge = "Editorial",
        tags = listOf("Bold", "Kinetic"),
        sampleWords = listOf("focus", "DEEPLY"),
        sampleActiveIndex = 1
    ),
    THE_BIG_RED(
        title = "The Big Red",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Cinzel + Playfair Display",
        styleNotes = "Dramatic giant crimson red (#EF4444) serif typography glowing behind the spoken speech",
        positionLabel = "Center Layered",
        badge = "New",
        sampleWords = listOf("the", "quick", "fox", "SECOND"),
        sampleActiveIndex = 3
    ),

    // --- Screenshot 4: Handwritten & Organic ---
    SCRIBBLE(
        title = "Scribble",
        category = TemplateCategory.HANDWRITTEN,
        fontDescription = "Caveat · Handwritten",
        styleNotes = "Hand-drawn aesthetics: active spoken word gets yellow highlighter oval pill and sketched loop",
        positionLabel = "Center Handwritten",
        badge = "New",
        tags = listOf("Kinetic", "Handwritten"),
        sampleWords = listOf("the", "little", "things"),
        sampleActiveIndex = 1
    ),
    ARCHIVES(
        title = "Archives",
        category = TemplateCategory.HANDWRITTEN,
        fontDescription = "Caveat + Playfair Display",
        styleNotes = "Vintage journal typography: active spoken word in elegant script with double hand-drawn wavy underline",
        positionLabel = "Center Handwritten",
        badge = "New",
        tags = listOf("Kinetic", "Handwritten"),
        sampleWords = listOf("Your", "Style", "is", "it"),
        sampleActiveIndex = 1
    ),
    BLOCKBUSTER(
        title = "Blockbuster",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Montserrat + Caveat",
        styleNotes = "Bold cinematic neon red banner text paired with white cursive subtitle",
        positionLabel = "Center Cinematic",
        badge = "New",
        sampleWords = listOf("THIS", "IS", "THE", "NEXT", "big", "thing"),
        sampleActiveIndex = 3
    ),

    // --- Screenshot 5: Journal & Interlock ---
    JOURNAL(
        title = "Journal",
        category = TemplateCategory.HANDWRITTEN,
        fontDescription = "Caveat · Handwritten Script",
        styleNotes = "Chalky red handwritten script with motion-blurred preceding words syncing with the voice",
        positionLabel = "Center Handwritten",
        badge = "New",
        tags = listOf("Kinetic", "Handwritten"),
        sampleWords = listOf("Back", "to", "routine"),
        sampleActiveIndex = 2
    ),
    INTERLOCK(
        title = "Interlock",
        category = TemplateCategory.KINETIC_EDITORIAL,
        fontDescription = "Space Mono + Playfair Display",
        styleNotes = "Mixed editorial layout: monospaced header, giant serif italic hero word, and condensed footer",
        positionLabel = "Center Editorial",
        badge = "New",
        tags = listOf("Kinetic", "Editorial"),
        sampleWords = listOf("I CAN'T DRAW", "A", "straight", "LINE"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 5 & 6: Static Captions ---
    ALI_ABDAAL(
        title = "Ali Abdaal",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 600 SemiBold",
        styleNotes = "Signature creator style: active spoken word is bold dark text encapsulated in a crisp white pill",
        positionLabel = "Bottom 30% Safe Zone",
        tags = listOf("Static"),
        sampleWords = listOf("the", "quick", "fox"),
        sampleActiveIndex = 1
    ),
    CLEAN_MOTION(
        title = "Clean Motion",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 700 Bold",
        styleNotes = "Pure single-word center impact following exact voice timing, one word at a time",
        positionLabel = "Center Single Word",
        sampleWords = listOf("brown", "one", "word", "at", "a", "time"),
        sampleActiveIndex = 0
    ),
    BUBBLE_STYLE(
        title = "Bubble Style",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Poppins · 700 Bold",
        styleNotes = "Friendly bubble layout: active spoken word gets mint/teal green (#2DD4BF) rounded badge pill",
        positionLabel = "Center Safe Zone",
        sampleWords = listOf("the", "quick", "fox"),
        sampleActiveIndex = 1
    ),

    // --- Screenshot 7: Creator & Influencer ---
    EDITING_SKOOL(
        title = "Editing Skool",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Poppins · 800 Bold",
        styleNotes = "3-line kinetic: active spoken word is bold white inside a vibrant orange (#FF6600) pill",
        positionLabel = "Center-Bottom 3-Line",
        tags = listOf("Bold"),
        sampleWords = listOf("the", "quick", "BROWN", "fox", "jumps"),
        sampleActiveIndex = 2
    ),
    MR_BEAST_1(
        title = "Mr Beast Style 1",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Anton · Heavy Slanted",
        styleNotes = "Slanted high-energy comic block typography with thick black stroke and 3D drop shadow",
        positionLabel = "Center Frame Punch",
        tags = listOf("Bold", "Shadow"),
        sampleWords = listOf("THE", "BROWN", "FOX"),
        sampleActiveIndex = 1
    ),

    // --- Screenshot 8: Influencer & Pop-Culture ---
    MR_BEAST_2(
        title = "Mr Beast Style 2",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Anton · Heavy Slanted",
        styleNotes = "Slanted heavy text with active spoken word popping in brilliant yellow (#FFE600)",
        positionLabel = "Center Frame Punch",
        tags = listOf("Bold", "Shadow"),
        sampleWords = listOf("THE", "BROWN", "FOX"),
        sampleActiveIndex = 1
    ),
    IMAN_GADZHI(
        title = "Iman Gadzhi",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Inter · Wide Tracking",
        styleNotes = "2-line minimalist all-caps typography with spacious tracking (2.5sp) and clean white finish",
        positionLabel = "Center Minimal 2-Line",
        sampleWords = listOf("THE", "QUICK", "BROWN", "FOX"),
        sampleActiveIndex = 2
    ),
    DEVIN_JATHO(
        title = "Devin Jatho",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Montserrat · Heavy Bold",
        styleNotes = "Signature creator style: active spoken word glows with electric purple neon (#A855F7)",
        positionLabel = "Center Frame",
        tags = listOf("Shadow"),
        sampleWords = listOf("THE", "BROWN", "FOX"),
        sampleActiveIndex = 1
    ),

    // --- Screenshot 9: Highlighted & Clean Glow ---
    HIGHLIGHTED_WORD(
        title = "Highlighted Word",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 700 Bold",
        styleNotes = "Clean sentence-case subtitles where the spoken word highlights in warm amber (#F59E0B)",
        positionLabel = "Bottom Safe Zone",
        tags = listOf("Bold"),
        sampleWords = listOf("the", "quick", "fox"),
        sampleActiveIndex = 1
    ),
    CLEAN_GLOW_STYLE(
        title = "Clean Glow Style",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Inter · 500 Medium",
        styleNotes = "Ethereal white lowercase text with soft omnidirectional drop-glow shadow blur",
        positionLabel = "Bottom 30% Safe Zone",
        tags = listOf("Shadow"),
        sampleWords = listOf("the", "quick", "brown", "fox", "jumps", "over"),
        sampleActiveIndex = 2
    ),
    CAPTIK_CLEAN(
        title = "Captik Clean",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 500 Medium",
        styleNotes = "Crisp, clean 2-line sentence-case subtitles designed for high readability without distractions",
        positionLabel = "Bottom Safe Zone",
        sampleWords = listOf("the", "quick", "brown", "fox", "jumps", "over"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 10: Black Punch & Captik Word ---
    BLACK_PUNCH(
        title = "Black Punch",
        category = TemplateCategory.CLEAN_HIGHLIGHT,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "Horizontal silver-chrome metallic gradient banner with bold black active spoken word",
        positionLabel = "Center Full Banner",
        tags = listOf("Bold"),
        sampleWords = listOf("the", "quick", "BROWN", "fox", "jumps"),
        sampleActiveIndex = 2
    ),
    CAPTIK_WORD(
        title = "Captik Word",
        category = TemplateCategory.CLEAN_HIGHLIGHT,
        fontDescription = "Inter · 700 Bold",
        styleNotes = "Voice reveal sync: spoken words appear solid white, upcoming words dim translucent",
        positionLabel = "Bottom Center",
        sampleWords = listOf("the", "quick", "brown"),
        sampleActiveIndex = 1
    ),

    // --- Screenshot 11: Pixelated, Glass & Tabahi ---
    PIXELATED_WORD(
        title = "Pixelated Word",
        category = TemplateCategory.CLEAN_HIGHLIGHT,
        fontDescription = "Space Mono · Terminal Mono",
        styleNotes = "Retro developer terminal style: monospaced code font with active spoken word in terminal cyan",
        positionLabel = "Center Frame",
        sampleWords = listOf("THE_QUICK", "BROWN"),
        sampleActiveIndex = 1
    ),
    LIQUID_GLASS(
        title = "Liquid Glass",
        category = TemplateCategory.STATIC_CAPTIONS,
        fontDescription = "Inter · 600 SemiBold",
        styleNotes = "Modern frosted glass capsule pill with subtle ambient light gradient border and blur",
        positionLabel = "Bottom Safe Zone",
        sampleWords = listOf("the", "quick", "fox"),
        sampleActiveIndex = 1
    ),
    TABAHI(
        title = "Tabahi",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Anton · Heavy Slanted",
        styleNotes = "High-energy Indian YouTube creator style: extreme slanted italic with thick black stroke",
        positionLabel = "Center Frame Punch",
        tags = listOf("Bold", "Shadow"),
        sampleWords = listOf("THE", "QUICK", "BROWN", "FOX"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 12: Deep Glow & Seedha Saadha ---
    DEEP_GLOW(
        title = "Deep Glow",
        category = TemplateCategory.CORE_GLOW,
        fontDescription = "Poppins · 900 Black",
        styleNotes = "Active spoken word emits intense magenta-pink neon glow (#FF007F) with purple ambient light",
        positionLabel = "Center Frame",
        tags = listOf("Bold", "Shadow"),
        sampleWords = listOf("BROWN", "FOX", "JUMPS", "OVER"),
        sampleActiveIndex = 0
    ),
    SEEDHA_SAADHA(
        title = "Seedha Saadha",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Anton · Ultra Heavy",
        styleNotes = "Ultra-heavy minimalist single-word center punch with deep black drop shadow",
        positionLabel = "Center Single Word",
        tags = listOf("Shadow"),
        sampleWords = listOf("BROWN"),
        sampleActiveIndex = 0
    ),
    THORA_CINEMATIC(
        title = "Thora Cinematic",
        category = TemplateCategory.INFLUENCER,
        fontDescription = "Cinzel · Luxury Serif",
        styleNotes = "Spaced-out small-caps tracking (3.5sp) with luxury cinematic navy blue aura",
        positionLabel = "Center Frame Cinematic",
        sampleWords = listOf("THE", "QUICK", "BROWN", "FOX"),
        sampleActiveIndex = 2
    ),

    // --- Screenshot 13: Big Reveal & Karaoke ---
    BIG_REVEAL(
        title = "Big Reveal",
        category = TemplateCategory.AI_CAPTIONS,
        fontDescription = "Montserrat · 900 Black",
        styleNotes = "Huge yellow bold (#FEE500) hero word paired with clean lowercase white subtitle",
        positionLabel = "Center Kinetic Stack",
        badge = "New",
        tags = listOf("Bold", "Kinetic"),
        sampleWords = listOf("HELLO", "guys."),
        sampleActiveIndex = 0
    ),
    KARAOKE_FLOW(
        title = "Karaoke Flow",
        category = TemplateCategory.AI_CAPTIONS,
        fontDescription = "Poppins · 800 ExtraBold",
        styleNotes = "Dynamic karaoke word-by-word voice sync with customizable neon highlight and black outline",
        positionLabel = "Center-Bottom Dynamic",
        tags = listOf("Sync"),
        sampleWords = listOf("SING", "ALONG", "TO", "EVERY", "BEAT"),
        sampleActiveIndex = 3
    )
}

/**
 * Represents a single spoken word with exact millisecond start and end times
 */
data class CaptionWord(
    val id: String,
    val text: String,
    val startTime: Float, // In seconds (e.g. 1.250f)
    val endTime: Float,   // In seconds (e.g. 1.650f)
    val speaker: String = "Speaker A"
) {
    val duration: Float get() = (endTime - startTime).coerceAtLeast(0.05f)
    
    fun isActiveAt(time: Float): Boolean = time >= startTime && time <= endTime
}

/**
 * Represents a synchronized sentence segment containing word-level timestamps
 * across all three requested scripts: Roman Hinglish, Native Script, and English
 */
data class CaptionSegment(
    val id: String,
    val startTime: Float,
    val endTime: Float,
    val speaker: String = "Speaker A",
    val romanWords: List<CaptionWord>,
    val nativeWords: List<CaptionWord>,
    val englishWords: List<CaptionWord>
) {
    fun getWordsForScript(script: OutputScript): List<CaptionWord> = when (script) {
        OutputScript.ROMAN_HINGLISH -> romanWords
        OutputScript.NATIVE_HINDI -> nativeWords
        OutputScript.ENGLISH_TRANSLATION -> englishWords
    }

    fun getFullTextForScript(script: OutputScript): String {
        return getWordsForScript(script).joinToString(" ") { it.text }
    }

    fun isActiveAt(time: Float): Boolean = time >= startTime && time <= endTime
}

/**
 * Aspect ratio options for the live video canvas preview
 */
enum class AspectRatioOption(val label: String, val ratio: Float, val iconName: String) {
    REELS_9_16("9:16 Reels", 9f / 16f, "portrait"),
    LANDSCAPE_16_9("16:9 YouTube", 16f / 9f, "landscape"),
    SQUARE_1_1("1:1 Feed", 1f, "square")
}

/**
 * A complete Video Subtitle Studio project
 */
data class VideoProject(
    val id: String,
    val title: String,
    val description: String,
    val videoUri: String? = null,
    val durationSeconds: Float,
    val segments: List<CaptionSegment>,
    val category: String = "Hinglish Creators"
)

/**
 * Text Effect options for premium video styling
 */
enum class TextEffect(val displayName: String, val description: String) {
    BOLD_DROP("Bold Drop", "Heavy 3D crisp black drop shadow with depth"),
    THREE_D_POP("3D Pop", "Layered isometric extrusion pop shadow"),
    NEON_GLOW("Neon Glow", "Luminous radial bloom aura in secondary color"),
    CHROME_METALLIC("Chrome Specular", "Specular platinum & silver metallic gradient"),
    CYBER_GLITCH("Cyber Glitch", "Dual-chromatic RGB split offset 3D distortion"),
    HOLO_SHIMMER("Holo Shimmer", "Iridescent prismatic rainbow spectrum gradient"),
    FIRE_BLAZE("Fire Blaze", "Combustion fire glow with amber-to-ruby heat aura"),
    FROSTED_GLASS("Frosted Glass", "Translucent glass capsule with specular border"),
    TEXT_OUTLINE("Stroke Outline", "Crisp high-contrast contour around every character"),
    KARAOKE_HIGHLIGHT("Karaoke Flow", "Active word illuminates in secondary color with scale bounce"),
    BOX_BACKGROUND("Box Background", "Translucent rounded pill behind text with adjustable opacity"),
    MINIMAL_CLEAN("Clean Subtitle", "Minimalist cinematic subtitle with soft shadow")
}

/**
 * Animation and transition effects for spoken text appearance
 */
enum class TextAnimation(val displayName: String, val description: String) {
    SPRING_BOUNCE("Spring Bounce", "Dynamic physical bounce with lively overshoot"),
    KINETIC_ZOOM("Kinetic Punch", "High-impact rapid punch zoom & settle"),
    FLIP_3D("3D Flip In", "Perspective isometric flip rotation transition"),
    ELASTIC_SNAP("Elastic Snap", "Elastic squash & stretch snap on spoken word"),
    SLIDE_UP("Slide Up Fade", "Smooth upward glide with soft alpha entrance"),
    GLOW_WAVE("Glow Wave", "Luminous traveling pulse wave on spoken word"),
    WAVE_RIPPLE("Wave Ripple", "Fluid sinusoidal wave displacement across letters"),
    SHIMMER_PULSE("Shimmer Pulse", "Rhythmic breathing glow pulse on active word"),
    BLUR_FOCUS("Blur to Sharp", "Rapid optical blur snap into tack-sharp clarity"),
    TYPEWRITER("Typewriter", "Progressive letter-by-letter reveal on speech cadence"),
    POP_IN("Pop In", "Instant explosive pop scale transition"),
    SCALE_IN("Scale In", "Smooth cinematic zoom-in transition"),
    NONE("Static", "Clean static display without motion")
}

/**
 * Vertical text positioning on the video canvas
 */
enum class TextPosition(val displayName: String, val verticalBias: Float) {
    TOP("Top Safe Zone", 0.20f),
    CENTER("Center Frame", 0.50f),
    BOTTOM("Bottom Safe Zone", 0.80f)
}

/**
 * Horizontal text alignment
 */
enum class TextAlignment(val displayName: String) {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right")
}

/**
 * Letter spacing options
 */
enum class LetterSpacingOption(val displayName: String, val trackingSp: Float) {
    TIGHT("Tight", -0.5f),
    NORMAL("Normal", 0.0f),
    WIDE("Wide", 2.0f),
    ULTRA_WIDE("Ultra Wide", 4.0f)
}

/**
 * Text casing options
 */
enum class TextCaseOption(val displayName: String) {
    UPPERCASE("UPPERCASE"),
    TITLE_CASE("Title Case"),
    AS_SPOKEN("As Spoken")
}

/**
 * 1-Tap Creator Styles (Iconic viral video aesthetics)
 */
enum class CreatorPreset(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val fontName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val textEffect: TextEffect,
    val textAnimation: TextAnimation,
    val letterSpacing: LetterSpacingOption,
    val textCase: TextCaseOption,
    val textPosition: TextPosition
) {
    MR_BEAST(
        title = "MrBeast Hero",
        subtitle = "Bold yellow punch & black 3D shadow",
        iconEmoji = "🦁",
        fontName = "Montserrat (900)",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFFFEE500),
        textEffect = TextEffect.BOLD_DROP,
        textAnimation = TextAnimation.SPRING_BOUNCE,
        letterSpacing = LetterSpacingOption.NORMAL,
        textCase = TextCaseOption.UPPERCASE,
        textPosition = TextPosition.CENTER
    ),
    HORMOZI_PUNCH(
        title = "Alex Hormozi",
        subtitle = "High-energy lime neon with 3D pop extrusion",
        iconEmoji = "💪",
        fontName = "Anton",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFF00FF66),
        textEffect = TextEffect.THREE_D_POP,
        textAnimation = TextAnimation.KINETIC_ZOOM,
        letterSpacing = LetterSpacingOption.TIGHT,
        textCase = TextCaseOption.UPPERCASE,
        textPosition = TextPosition.CENTER
    ),
    VIRAL_FIRE(
        title = "Desi Viral Fire",
        subtitle = "Fiery orange embers with dynamic wave motion",
        iconEmoji = "🔥",
        fontName = "Montserrat (900)",
        primaryColor = Color(0xFFFFCC00),
        secondaryColor = Color(0xFFFF3B30),
        textEffect = TextEffect.FIRE_BLAZE,
        textAnimation = TextAnimation.WAVE_RIPPLE,
        letterSpacing = LetterSpacingOption.NORMAL,
        textCase = TextCaseOption.UPPERCASE,
        textPosition = TextPosition.CENTER
    ),
    CYBER_PUNK(
        title = "Cyber Neon",
        subtitle = "Electric cyan & magenta with dual chromatic glitch",
        iconEmoji = "⚡",
        fontName = "Montserrat (900)",
        primaryColor = Color(0xFF00F0FF),
        secondaryColor = Color(0xFFFF007F),
        textEffect = TextEffect.CYBER_GLITCH,
        textAnimation = TextAnimation.ELASTIC_SNAP,
        letterSpacing = LetterSpacingOption.WIDE,
        textCase = TextCaseOption.UPPERCASE,
        textPosition = TextPosition.CENTER
    ),
    ALI_ABDAAL(
        title = "Ali Abdaal",
        subtitle = "Clean aesthetic subtitle with translucent capsule",
        iconEmoji = "☕",
        fontName = "Inter (600)",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFF67E8F9),
        textEffect = TextEffect.BOX_BACKGROUND,
        textAnimation = TextAnimation.SCALE_IN,
        letterSpacing = LetterSpacingOption.NORMAL,
        textCase = TextCaseOption.TITLE_CASE,
        textPosition = TextPosition.BOTTOM
    ),
    LUXURY_AURA(
        title = "Cinematic Luxury",
        subtitle = "Champagne gold serif with subtle breathing aura",
        iconEmoji = "✨",
        fontName = "Playfair Display",
        primaryColor = Color(0xFFF6E05E),
        secondaryColor = Color(0xFFECC94B),
        textEffect = TextEffect.NEON_GLOW,
        textAnimation = TextAnimation.SHIMMER_PULSE,
        letterSpacing = LetterSpacingOption.ULTRA_WIDE,
        textCase = TextCaseOption.UPPERCASE,
        textPosition = TextPosition.CENTER
    )
}

