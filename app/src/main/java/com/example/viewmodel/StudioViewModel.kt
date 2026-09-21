package com.example.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.export.ExportEngine
import com.example.model.AspectRatioOption
import com.example.model.CaptionSegment
import com.example.model.CaptionTemplate
import com.example.model.CaptionWord
import com.example.model.CreatorPreset
import com.example.model.LetterSpacingOption
import com.example.model.OutputScript
import com.example.model.TextAlignment
import com.example.model.TextAnimation
import com.example.model.TextCaseOption
import com.example.model.TextEffect
import com.example.model.TextPosition
import com.example.model.VideoProject
import com.example.service.TranscriptionEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * StudioViewModel manages playback, timeline synchronization, active word tracking,
 * typography template selection, script switching, and export flows.
 */
class StudioViewModel : ViewModel() {

    private val presetProjects = TranscriptionEngine.getPresetProjects()

    private val _projects = MutableStateFlow<List<VideoProject>>(presetProjects)
    val projects: StateFlow<List<VideoProject>> = _projects.asStateFlow()

    private val _currentProject = MutableStateFlow(presetProjects.first())
    val currentProject: StateFlow<VideoProject> = _currentProject.asStateFlow()

    private val _currentTime = MutableStateFlow(0.0f)
    val currentTime: StateFlow<Float> = _currentTime.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _selectedTemplate = MutableStateFlow(CaptionTemplate.BOLD_DROP)
    val selectedTemplate: StateFlow<CaptionTemplate> = _selectedTemplate.asStateFlow()

    private val _selectedScript = MutableStateFlow(OutputScript.ROMAN_HINGLISH)
    val selectedScript: StateFlow<OutputScript> = _selectedScript.asStateFlow()

    private val _aspectRatio = MutableStateFlow(AspectRatioOption.REELS_9_16)
    val aspectRatio: StateFlow<AspectRatioOption> = _aspectRatio.asStateFlow()

    // Primary Text Color (default White as in CaptionCraft screenshot)
    private val _primaryTextColor = MutableStateFlow(Color(0xFFFFFFFF))
    val primaryTextColor: StateFlow<Color> = _primaryTextColor.asStateFlow()

    // Secondary / Highlight Text Color (default Neon Green #00FF66)
    private val _secondaryTextColor = MutableStateFlow(Color(0xFF00FF66))
    val secondaryTextColor: StateFlow<Color> = _secondaryTextColor.asStateFlow()

    // Backward compatibility for karaokeColor
    val karaokeColor: StateFlow<Color> get() = _secondaryTextColor

    // Text Effect (Bold Drop Shadow as in CaptionCraft screenshot)
    private val _activeTextEffect = MutableStateFlow(TextEffect.BOLD_DROP)
    val activeTextEffect: StateFlow<TextEffect> = _activeTextEffect.asStateFlow()

    // Text Animation
    private val _activeAnimation = MutableStateFlow(TextAnimation.SCALE_IN)
    val activeAnimation: StateFlow<TextAnimation> = _activeAnimation.asStateFlow()

    // Text Position on Video
    private val _textPosition = MutableStateFlow(TextPosition.CENTER)
    val textPosition: StateFlow<TextPosition> = _textPosition.asStateFlow()

    // Text Alignment
    private val _textAlignment = MutableStateFlow(TextAlignment.CENTER)
    val textAlignment: StateFlow<TextAlignment> = _textAlignment.asStateFlow()

    // Background Opacity for Reels Clean / Box Pill (50% default as in screenshot)
    private val _backgroundOpacity = MutableStateFlow(0.50f)
    val backgroundOpacity: StateFlow<Float> = _backgroundOpacity.asStateFlow()

    // Letter Spacing
    private val _letterSpacingOption = MutableStateFlow(LetterSpacingOption.NORMAL)
    val letterSpacingOption: StateFlow<LetterSpacingOption> = _letterSpacingOption.asStateFlow()

    // Font Family selection
    private val _selectedFontName = MutableStateFlow("Montserrat (900)")
    val selectedFontName: StateFlow<String> = _selectedFontName.asStateFlow()

    // Text Casing (UPPERCASE as in screenshot BHAI KYA KAR RAHA HAI)
    private val _textCaseOption = MutableStateFlow(TextCaseOption.UPPERCASE)
    val textCaseOption: StateFlow<TextCaseOption> = _textCaseOption.asStateFlow()

    private val _fontSizeScale = MutableStateFlow(1.0f)
    val fontSizeScale: StateFlow<Float> = _fontSizeScale.asStateFlow()

    // Auto-Emojis (Viral Magic Emojis over punchy keywords)
    private val _autoEmojisEnabled = MutableStateFlow(true)
    val autoEmojisEnabled: StateFlow<Boolean> = _autoEmojisEnabled.asStateFlow()

    // Safe-Zone Overlay (Instagram Reels / TikTok / Shorts boundary guide)
    private val _showSafeZone = MutableStateFlow(false)
    val showSafeZone: StateFlow<Boolean> = _showSafeZone.asStateFlow()

    // Active 1-Tap Creator Preset
    private val _activeCreatorPreset = MutableStateFlow<CreatorPreset?>(CreatorPreset.MR_BEAST)
    val activeCreatorPreset: StateFlow<CreatorPreset?> = _activeCreatorPreset.asStateFlow()

    // Editing modal state
    private val _editingSegment = MutableStateFlow<CaptionSegment?>(null)
    val editingSegment: StateFlow<CaptionSegment?> = _editingSegment.asStateFlow()

    private val _editingWord = MutableStateFlow<CaptionWord?>(null)
    val editingWord: StateFlow<CaptionWord?> = _editingWord.asStateFlow()

    // Export progress states
    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _exportProgress = MutableStateFlow(0f)
    val exportProgress: StateFlow<Float> = _exportProgress.asStateFlow()

    private val _isTranscribing = MutableStateFlow(false)
    val isTranscribing: StateFlow<Boolean> = _isTranscribing.asStateFlow()

    private var playbackJob: Job? = null

    val activeSegment: StateFlow<CaptionSegment?> = combine(
        _currentProject,
        _currentTime
    ) { project, time ->
        project.segments.firstOrNull { it.isActiveAt(time) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeWord: StateFlow<CaptionWord?> = combine(
        activeSegment,
        _currentTime,
        _selectedScript
    ) { segment, time, script ->
        if (segment == null) null
        else segment.getWordsForScript(script).firstOrNull { it.isActiveAt(time) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        if (_isPlaying.value) return
        _isPlaying.value = true

        // If at the end, wrap to start
        if (_currentTime.value >= _currentProject.value.durationSeconds - 0.1f) {
            _currentTime.value = 0.0f
        }

        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            val stepMs = 30L
            while (isActive && _isPlaying.value) {
                delay(stepMs)
                val advance = (stepMs / 1000f) * _playbackSpeed.value
                val nextTime = _currentTime.value + advance
                if (nextTime >= _currentProject.value.durationSeconds) {
                    _currentTime.value = _currentProject.value.durationSeconds
                    _isPlaying.value = false
                    break
                } else {
                    _currentTime.value = nextTime
                }
            }
        }
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
    }

    fun seekTo(timeSeconds: Float) {
        val bounded = timeSeconds.coerceIn(0.0f, _currentProject.value.durationSeconds)
        _currentTime.value = bounded
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun selectTemplate(template: CaptionTemplate) {
        _selectedTemplate.value = template
        // Auto-configure optimal effect, colors and font based on template
        when (template) {
            CaptionTemplate.BOLD_DROP -> {
                _activeTextEffect.value = TextEffect.BOLD_DROP
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _secondaryTextColor.value = Color(0xFF00FF66)
                _selectedFontName.value = "Montserrat (900)"
                _textCaseOption.value = TextCaseOption.UPPERCASE
            }
            CaptionTemplate.REELS_CLEAN -> {
                _activeTextEffect.value = TextEffect.BOX_BACKGROUND
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _backgroundOpacity.value = 0.50f
                _selectedFontName.value = "Inter (700)"
            }
            CaptionTemplate.PODCAST_DUO -> {
                _activeTextEffect.value = TextEffect.KARAOKE_HIGHLIGHT
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _secondaryTextColor.value = Color(0xFFFFD700)
                _selectedFontName.value = "Montserrat (900)"
            }
            CaptionTemplate.KARAOKE_FLOW -> {
                _activeTextEffect.value = TextEffect.KARAOKE_HIGHLIGHT
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _secondaryTextColor.value = Color(0xFF00FF00)
                _selectedFontName.value = "Poppins (800)"
            }
            CaptionTemplate.MR_BEAST_1, CaptionTemplate.MR_BEAST_2, CaptionTemplate.TABAHI -> {
                _activeTextEffect.value = TextEffect.THREE_D_POP
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _secondaryTextColor.value = Color(0xFFFFE600)
                _selectedFontName.value = "Anton (Heavy)"
                _textCaseOption.value = TextCaseOption.UPPERCASE
            }
            CaptionTemplate.CAPTIK_GLOW, CaptionTemplate.DEEP_GLOW, CaptionTemplate.DEVIN_JATHO -> {
                _activeTextEffect.value = TextEffect.NEON_GLOW
                _primaryTextColor.value = Color(0xFFFFFFFF)
                _secondaryTextColor.value = if (template == CaptionTemplate.DEEP_GLOW) Color(0xFFFF007F) else Color(0xFF00FF66)
            }
            else -> {}
        }
    }

    fun selectScript(script: OutputScript) {
        _selectedScript.value = script
    }

    fun setAspectRatio(option: AspectRatioOption) {
        _aspectRatio.value = option
    }

    fun setPrimaryTextColor(color: Color) {
        _primaryTextColor.value = color
    }

    fun setSecondaryTextColor(color: Color) {
        _secondaryTextColor.value = color
    }

    fun setKaraokeColor(color: Color) {
        _secondaryTextColor.value = color
    }

    fun setTextEffect(effect: TextEffect) {
        _activeTextEffect.value = effect
    }

    fun setTextAnimation(anim: TextAnimation) {
        _activeAnimation.value = anim
    }

    fun setTextPosition(pos: TextPosition) {
        _textPosition.value = pos
    }

    fun setTextAlignment(align: TextAlignment) {
        _textAlignment.value = align
    }

    fun setBackgroundOpacity(opacity: Float) {
        _backgroundOpacity.value = opacity.coerceIn(0f, 1f)
    }

    fun setLetterSpacingOption(spacing: LetterSpacingOption) {
        _letterSpacingOption.value = spacing
    }

    fun setLetterSpacing(spacing: LetterSpacingOption) {
        setLetterSpacingOption(spacing)
    }

    fun setSelectedFontName(fontName: String) {
        _selectedFontName.value = fontName
    }

    fun setFontName(fontName: String) {
        setSelectedFontName(fontName)
    }

    fun setTextCaseOption(caseOption: TextCaseOption) {
        _textCaseOption.value = caseOption
    }

    fun setTextCase(caseOption: TextCaseOption) {
        setTextCaseOption(caseOption)
    }

    fun setFontSizeScale(scale: Float) {
        _fontSizeScale.value = scale.coerceIn(0.7f, 1.4f)
    }

    fun toggleAutoEmojis(enabled: Boolean? = null) {
        _autoEmojisEnabled.value = enabled ?: !_autoEmojisEnabled.value
    }

    fun toggleSafeZone(enabled: Boolean? = null) {
        _showSafeZone.value = enabled ?: !_showSafeZone.value
    }

    fun applyCreatorPreset(preset: CreatorPreset) {
        _activeCreatorPreset.value = preset
        _primaryTextColor.value = preset.primaryColor
        _secondaryTextColor.value = preset.secondaryColor
        _activeTextEffect.value = preset.textEffect
        _activeAnimation.value = preset.textAnimation
        _letterSpacingOption.value = preset.letterSpacing
        _textCaseOption.value = preset.textCase
        _textPosition.value = preset.textPosition
        _selectedFontName.value = preset.fontName
    }

    fun updateProjectTitle(newTitle: String) {
        val current = _currentProject.value
        val updated = current.copy(title = newTitle)
        _currentProject.value = updated
        _projects.value = _projects.value.map { if (it.id == updated.id) updated else it }
    }

    fun selectProject(project: VideoProject) {
        pause()
        _currentProject.value = project
        _currentTime.value = 0.0f
    }

    fun loadPresetByIndex(index: Int) {
        val list = _projects.value
        if (index in list.indices) {
            selectProject(list[index])
        }
    }

    fun openWordEditor(segment: CaptionSegment, word: CaptionWord) {
        _editingSegment.value = segment
        _editingWord.value = word
    }

    fun closeWordEditor() {
        _editingSegment.value = null
        _editingWord.value = null
    }

    fun saveWordCorrection(
        segmentId: String,
        wordId: String,
        newRoman: String,
        newNative: String,
        newEnglish: String
    ) {
        val proj = _currentProject.value
        val updatedSegments = proj.segments.map { segment ->
            if (segment.id != segmentId) {
                segment
            } else {
                val wordIndex = segment.romanWords.indexOfFirst { it.id == wordId }
                    .takeIf { it >= 0 }
                    ?: segment.nativeWords.indexOfFirst { it.id == wordId }
                        .takeIf { it >= 0 }
                    ?: segment.englishWords.indexOfFirst { it.id == wordId }

                val updatedRoman = segment.romanWords.mapIndexed { idx, w ->
                    if (w.id == wordId || (wordIndex != null && idx == wordIndex)) w.copy(text = newRoman) else w
                }
                val updatedNative = segment.nativeWords.mapIndexed { idx, w ->
                    if (w.id == wordId || (wordIndex != null && idx == wordIndex)) w.copy(text = newNative) else w
                }
                val updatedEnglish = segment.englishWords.mapIndexed { idx, w ->
                    if (w.id == wordId || (wordIndex != null && idx == wordIndex)) w.copy(text = newEnglish) else w
                }
                segment.copy(
                    romanWords = updatedRoman,
                    nativeWords = updatedNative,
                    englishWords = updatedEnglish
                )
            }
        }
        val updatedProject = proj.copy(segments = updatedSegments)
        _currentProject.value = updatedProject
        _projects.value = _projects.value.map { if (it.id == updatedProject.id) updatedProject else it }
        closeWordEditor()
    }

    /**
     * Unlimited Project Creation: Allows creating unlimited projects with custom Hinglish speech scripts
     */
    fun createNewProject(
        title: String = "Project #${_projects.value.size + 1}",
        scriptText: String = "Bhai sun aaj hum banayenge ekdum viral reels with auto captions!"
    ) {
        pause()
        val generated = TranscriptionEngine.generateLocalHinglishProject(scriptText)
        val newProj = generated.copy(
            id = "proj_${System.currentTimeMillis()}",
            title = title.ifBlank { "Viral Reel #${_projects.value.size + 1}" }
        )
        val currentList = _projects.value.toMutableList()
        currentList.add(0, newProj)
        _projects.value = currentList
        _currentProject.value = newProj
        _currentTime.value = 0f
    }

    /**
     * Unlimited Project Duplication: Clone current setup with zero limitations
     */
    fun duplicateCurrentProject() {
        val current = _currentProject.value
        val newProj = current.copy(
            id = "proj_${System.currentTimeMillis()}",
            title = "${current.title} (Copy)"
        )
        val currentList = _projects.value.toMutableList()
        currentList.add(0, newProj)
        _projects.value = currentList
        _currentProject.value = newProj
    }

    /**
     * Delete or manage projects without any quota
     */
    fun deleteProject(projectId: String) {
        val currentList = _projects.value.toMutableList()
        if (currentList.size > 1) {
            currentList.removeAll { it.id == projectId }
            _projects.value = currentList
            if (_currentProject.value.id == projectId) {
                _currentProject.value = currentList.first()
                _currentTime.value = 0f
            }
        }
    }

    fun importVideoFile(videoUri: String, fileName: String) {
        pause()
        viewModelScope.launch {
            _isTranscribing.value = true
            // Simulate extracting lightweight audio track & running AI transcription on speech
            delay(1200)
            val newProject = VideoProject(
                id = "proj_user_${System.currentTimeMillis()}",
                title = fileName.ifBlank { "Imported Hinglish Video" },
                description = "Custom video file with AI-extracted audio and auto-transcribed subtitles",
                videoUri = videoUri,
                durationSeconds = 14.5f,
                segments = TranscriptionEngine.getPresetProjects().first().segments.map {
                    it.copy(id = "user_${it.id}")
                },
                category = "Imported Media"
            )
            _projects.value = listOf(newProject) + _projects.value
            _currentProject.value = newProject
            _currentTime.value = 0.0f
            _isTranscribing.value = false
        }
    }

    fun transcribeCustomSpeech(speechText: String) {
        pause()
        viewModelScope.launch {
            _isTranscribing.value = true
            val project = TranscriptionEngine.transcribeSpeechWithGemini(speechText)
                ?: TranscriptionEngine.generateLocalHinglishProject(speechText)
            _projects.value = listOf(project) + _projects.value
            _currentProject.value = project
            _currentTime.value = 0.0f
            _isTranscribing.value = false
        }
    }

    /**
     * Executes video burn-in export simulation with progress updates
     */
    fun exportVideo(resolution: String, onComplete: (String) -> Unit) {
        if (_isExporting.value) return
        _isExporting.value = true
        _exportProgress.value = 0.05f

        viewModelScope.launch {
            for (step in 1..20) {
                delay(100)
                _exportProgress.value = step / 20.0f
            }
            _isExporting.value = false
            onComplete("Export completed successfully ($resolution MP4 with ${_selectedTemplate.value.title} captions)")
        }
    }

    /**
     * Prepares .srt or .xml asset export
     */
    fun exportPluginAssets(context: Context, isXml: Boolean): Intent {
        val proj = _currentProject.value
        val script = _selectedScript.value

        return if (isXml) {
            val content = ExportEngine.generatePremiereXml(proj, script)
            val name = "${proj.title.replace(" ", "_")}_captions.xml"
            ExportEngine.saveAndCreateShareIntent(context, content, name, "text/xml")
        } else {
            val content = ExportEngine.generateSrtContent(proj, script)
            val name = "${proj.title.replace(" ", "_")}_subtitles.srt"
            ExportEngine.saveAndCreateShareIntent(context, content, name, "text/plain")
        }
    }
}
