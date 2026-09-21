package com.example

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MultiTrackTimeline
import com.example.ui.components.StylingPanel
import com.example.ui.components.TranscriptPanel
import com.example.ui.components.VideoPlayerCanvas
import com.example.ui.dialogs.EditWordDialog
import com.example.ui.dialogs.ExportDialog
import com.example.ui.dialogs.ImportVideoDialog
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AutoCaptionStudioScreen(viewModel = viewModel)
            }
        }
    }
}

/**
 * Top-level AutoCaption Studio Screen supporting phone and wide-screen desktop layouts
 */
@Composable
fun AutoCaptionStudioScreen(viewModel: StudioViewModel) {
    val context = LocalContext.current

    val currentProject by viewModel.currentProject.collectAsState()
    val allProjects by viewModel.projects.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val selectedTemplate by viewModel.selectedTemplate.collectAsState()
    val selectedScript by viewModel.selectedScript.collectAsState()
    val aspectRatio by viewModel.aspectRatio.collectAsState()
    val karaokeColor by viewModel.karaokeColor.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val activeSegment by viewModel.activeSegment.collectAsState()

    // Enhanced Granular Text Styling States from ViewModel
    val primaryTextColor by viewModel.primaryTextColor.collectAsState()
    val secondaryTextColor by viewModel.secondaryTextColor.collectAsState()
    val activeTextEffect by viewModel.activeTextEffect.collectAsState()
    val activeAnimation by viewModel.activeAnimation.collectAsState()
    val textPosition by viewModel.textPosition.collectAsState()
    val textAlignment by viewModel.textAlignment.collectAsState()
    val backgroundOpacity by viewModel.backgroundOpacity.collectAsState()
    val letterSpacingOption by viewModel.letterSpacingOption.collectAsState()
    val selectedFontName by viewModel.selectedFontName.collectAsState()
    val textCaseOption by viewModel.textCaseOption.collectAsState()
    val autoEmojisEnabled by viewModel.autoEmojisEnabled.collectAsState()
    val showSafeZone by viewModel.showSafeZone.collectAsState()
    val activeCreatorPreset by viewModel.activeCreatorPreset.collectAsState()

    val editingSegment by viewModel.editingSegment.collectAsState()
    val editingWord by viewModel.editingWord.collectAsState()

    val isExporting by viewModel.isExporting.collectAsState()
    val exportProgress by viewModel.exportProgress.collectAsState()
    val isTranscribing by viewModel.isTranscribing.collectAsState()

    var showExportDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importDialogTab by remember { mutableStateOf(0) }

    // Direct Google Play compliant photo/video picker for Gallery
    val galleryVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importVideoFile(uri.toString(), "Gallery Video")
            Toast.makeText(context, "Imported video from Gallery", Toast.LENGTH_SHORT).show()
        }
    }

    // Direct system File/Document picker
    val fileDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importVideoFile(uri.toString(), "Storage Video")
            Toast.makeText(context, "Imported video from Files", Toast.LENGTH_SHORT).show()
        }
    }

    // On compact screens (phones), tab selection: 0 = Video & Timeline, 1 = Transcript, 2 = Styling
    var compactTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B12))
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF070B12))
        ) {
            // STUDIO TOP NAVIGATION BAR
            StudioTopBar(
                projectTitle = currentProject.title,
                scriptBadge = selectedScript.badgeText,
                onOpenGallery = {
                    galleryVideoLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    )
                },
                onOpenFile = {
                    fileDocumentLauncher.launch("video/*")
                },
                onOpenProjects = {
                    importDialogTab = 0
                    showImportDialog = true
                },
                onOpenExport = { showExportDialog = true }
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                val isWideScreen = maxWidth >= 840.dp

                if (isWideScreen) {
                    // -------------------------------------------------------------
                    // WIDE SCREEN / TABLET / DESKTOP LAYOUT (3 COLUMNS)
                    // Left: Transcript | Center: Video Preview + Timeline | Right: Styling
                    // -------------------------------------------------------------
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // LEFT COLUMN: Interactive Transcript
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .fillMaxHeight()
                        ) {
                            TranscriptPanel(
                                segments = currentProject.segments,
                                script = selectedScript,
                                currentTime = currentTime,
                                onSelectScript = { viewModel.selectScript(it) },
                                onWordClick = { word -> viewModel.seekTo(word.startTime) },
                                onWordEdit = { seg, word -> viewModel.openWordEditor(seg, word) }
                            )
                        }

                        // CENTER COLUMN: Video Player Canvas + Multi-Track Timeline
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            VideoPlayerCanvas(
                                videoUri = currentProject.videoUri,
                                currentTime = currentTime,
                                durationSeconds = currentProject.durationSeconds,
                                isPlaying = isPlaying,
                                playbackSpeed = playbackSpeed,
                                aspectRatio = aspectRatio,
                                template = selectedTemplate,
                                script = selectedScript,
                                karaokeColor = karaokeColor,
                                fontSizeScale = fontSizeScale,
                                activeSegment = activeSegment,
                                onTogglePlay = { viewModel.togglePlayPause() },
                                onSeek = { viewModel.seekTo(it) },
                                onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor,
                                activeTextEffect = activeTextEffect,
                                activeAnimation = activeAnimation,
                                textPosition = textPosition,
                                textAlignment = textAlignment,
                                backgroundOpacity = backgroundOpacity,
                                letterSpacingOption = letterSpacingOption,
                                selectedFontName = selectedFontName,
                                textCaseOption = textCaseOption,
                                autoEmojisEnabled = autoEmojisEnabled,
                                showSafeZone = showSafeZone,
                                onToggleSafeZone = { viewModel.toggleSafeZone() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )

                            // BOTTOM: Multi-Track Timeline
                            MultiTrackTimeline(
                                currentTime = currentTime,
                                durationSeconds = currentProject.durationSeconds,
                                segments = currentProject.segments,
                                script = selectedScript,
                                isPlaying = isPlaying,
                                playbackSpeed = playbackSpeed,
                                onTogglePlay = { viewModel.togglePlayPause() },
                                onSeek = { viewModel.seekTo(it) },
                                onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // RIGHT COLUMN: Styling Panel
                        Box(
                            modifier = Modifier
                                .width(320.dp)
                                .fillMaxHeight()
                        ) {
                            StylingPanel(
                                selectedTemplate = selectedTemplate,
                                selectedScript = selectedScript,
                                aspectRatio = aspectRatio,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor,
                                activeTextEffect = activeTextEffect,
                                activeAnimation = activeAnimation,
                                textPosition = textPosition,
                                textAlignment = textAlignment,
                                backgroundOpacity = backgroundOpacity,
                                letterSpacingOption = letterSpacingOption,
                                selectedFontName = selectedFontName,
                                textCaseOption = textCaseOption,
                                fontSizeScale = fontSizeScale,
                                onSelectTemplate = { viewModel.selectTemplate(it) },
                                onSelectScript = { viewModel.selectScript(it) },
                                onSelectAspectRatio = { viewModel.setAspectRatio(it) },
                                onSelectPrimaryTextColor = { viewModel.setPrimaryTextColor(it) },
                                onSelectSecondaryTextColor = { viewModel.setSecondaryTextColor(it) },
                                onSelectTextEffect = { viewModel.setTextEffect(it) },
                                onSelectTextAnimation = { viewModel.setTextAnimation(it) },
                                onSelectTextPosition = { viewModel.setTextPosition(it) },
                                onSelectTextAlignment = { viewModel.setTextAlignment(it) },
                                onBackgroundOpacityChange = { viewModel.setBackgroundOpacity(it) },
                                onSelectLetterSpacing = { viewModel.setLetterSpacing(it) },
                                onSelectFontName = { viewModel.setFontName(it) },
                                onSelectTextCase = { viewModel.setTextCase(it) },
                                onFontSizeChange = { viewModel.setFontSizeScale(it) },
                                activeCreatorPreset = activeCreatorPreset,
                                onSelectCreatorPreset = { viewModel.applyCreatorPreset(it) },
                                autoEmojisEnabled = autoEmojisEnabled,
                                onToggleAutoEmojis = { viewModel.toggleAutoEmojis(it) }
                            )
                        }
                    }
                } else {
                    // -------------------------------------------------------------
                    // COMPACT / MOBILE PHONE LAYOUT
                    // Switchable Tabs with Sticky Multi-Track Timeline
                    // -------------------------------------------------------------
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Compact Studio Tab Selector
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            StudioTabButton(
                                title = "Video Studio",
                                icon = Icons.Default.Movie,
                                isSelected = compactTab == 0,
                                onClick = { compactTab = 0 },
                                modifier = Modifier.weight(1f)
                            )
                            StudioTabButton(
                                title = "Transcript",
                                icon = Icons.Default.Mic,
                                isSelected = compactTab == 1,
                                onClick = { compactTab = 1 },
                                modifier = Modifier.weight(1f)
                            )
                            StudioTabButton(
                                title = "Styling",
                                icon = Icons.Default.FormatPaint,
                                isSelected = compactTab == 2,
                                onClick = { compactTab = 2 },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Active Tab Body
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            when (compactTab) {
                                0 -> {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        // Quick Video Source Bar (Gallery, File, Presets)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                // Gallery Button
                                                Surface(
                                                    color = Color(0xFF06B6D4).copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(6.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF06B6D4).copy(alpha = 0.4f)),
                                                    modifier = Modifier
                                                        .clickable {
                                                            galleryVideoLauncher.launch(
                                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                                            )
                                                        }
                                                        .testTag("quick_gallery_btn")
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.PhotoLibrary,
                                                            contentDescription = "Gallery Video",
                                                            tint = Color(0xFF22D3EE),
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "+ Gallery",
                                                            color = Color(0xFF22D3EE),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }

                                                // File Button
                                                Surface(
                                                    color = Color(0xFF3B82F6).copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(6.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f)),
                                                    modifier = Modifier
                                                        .clickable {
                                                            fileDocumentLauncher.launch("video/*")
                                                        }
                                                        .testTag("quick_file_btn")
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.FolderOpen,
                                                            contentDescription = "Storage Video",
                                                            tint = Color(0xFF60A5FA),
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "+ File",
                                                            color = Color(0xFF60A5FA),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }

                                            // Presets library button
                                            Surface(
                                                color = Color(0xFF1E293B),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.clickable {
                                                    importDialogTab = 0
                                                    showImportDialog = true
                                                }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "Presets ▾",
                                                        color = Color.White.copy(alpha = 0.7f),
                                                        fontSize = 11.sp
                                                    )
                                                }
                                            }
                                        }

                                        VideoPlayerCanvas(
                                            videoUri = currentProject.videoUri,
                                            currentTime = currentTime,
                                            durationSeconds = currentProject.durationSeconds,
                                            isPlaying = isPlaying,
                                            playbackSpeed = playbackSpeed,
                                            aspectRatio = aspectRatio,
                                            template = selectedTemplate,
                                            script = selectedScript,
                                            karaokeColor = karaokeColor,
                                            fontSizeScale = fontSizeScale,
                                            activeSegment = activeSegment,
                                            onTogglePlay = { viewModel.togglePlayPause() },
                                            onSeek = { viewModel.seekTo(it) },
                                            onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                                            primaryTextColor = primaryTextColor,
                                            secondaryTextColor = secondaryTextColor,
                                            activeTextEffect = activeTextEffect,
                                            activeAnimation = activeAnimation,
                                            textPosition = textPosition,
                                            textAlignment = textAlignment,
                                            backgroundOpacity = backgroundOpacity,
                                            letterSpacingOption = letterSpacingOption,
                                            selectedFontName = selectedFontName,
                                            textCaseOption = textCaseOption,
                                            autoEmojisEnabled = autoEmojisEnabled,
                                            showSafeZone = showSafeZone,
                                            onToggleSafeZone = { viewModel.toggleSafeZone() },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                        )
                                    }
                                }
                                1 -> {
                                    TranscriptPanel(
                                        segments = currentProject.segments,
                                        script = selectedScript,
                                        currentTime = currentTime,
                                        onSelectScript = { viewModel.selectScript(it) },
                                        onWordClick = { word ->
                                            viewModel.seekTo(word.startTime)
                                            compactTab = 0 // Auto-switch to preview to see word animated
                                        },
                                        onWordEdit = { seg, word -> viewModel.openWordEditor(seg, word) },
                                        onClose = { compactTab = 0 }
                                    )
                                }
                                2 -> {
                                    StylingPanel(
                                        selectedTemplate = selectedTemplate,
                                        selectedScript = selectedScript,
                                        aspectRatio = aspectRatio,
                                        primaryTextColor = primaryTextColor,
                                        secondaryTextColor = secondaryTextColor,
                                        activeTextEffect = activeTextEffect,
                                        activeAnimation = activeAnimation,
                                        textPosition = textPosition,
                                        textAlignment = textAlignment,
                                        backgroundOpacity = backgroundOpacity,
                                        letterSpacingOption = letterSpacingOption,
                                        selectedFontName = selectedFontName,
                                        textCaseOption = textCaseOption,
                                        fontSizeScale = fontSizeScale,
                                        onSelectTemplate = { viewModel.selectTemplate(it) },
                                        onSelectScript = { viewModel.selectScript(it) },
                                        onSelectAspectRatio = { viewModel.setAspectRatio(it) },
                                        onSelectPrimaryTextColor = { viewModel.setPrimaryTextColor(it) },
                                        onSelectSecondaryTextColor = { viewModel.setSecondaryTextColor(it) },
                                        onSelectTextEffect = { viewModel.setTextEffect(it) },
                                        onSelectTextAnimation = { viewModel.setTextAnimation(it) },
                                        onSelectTextPosition = { viewModel.setTextPosition(it) },
                                        onSelectTextAlignment = { viewModel.setTextAlignment(it) },
                                        onBackgroundOpacityChange = { viewModel.setBackgroundOpacity(it) },
                                        onSelectLetterSpacing = { viewModel.setLetterSpacing(it) },
                                        onSelectFontName = { viewModel.setFontName(it) },
                                        onSelectTextCase = { viewModel.setTextCase(it) },
                                        onFontSizeChange = { viewModel.setFontSizeScale(it) },
                                        activeCreatorPreset = activeCreatorPreset,
                                        onSelectCreatorPreset = { viewModel.applyCreatorPreset(it) },
                                        autoEmojisEnabled = autoEmojisEnabled,
                                        onToggleAutoEmojis = { viewModel.toggleAutoEmojis(it) }
                                    )
                                }
                            }
                        }

                        // Multi-Track Timeline (Permanently anchored at bottom for immediate scrubbing)
                        MultiTrackTimeline(
                            currentTime = currentTime,
                            durationSeconds = currentProject.durationSeconds,
                            segments = currentProject.segments,
                            script = selectedScript,
                            isPlaying = isPlaying,
                            playbackSpeed = playbackSpeed,
                            onTogglePlay = { viewModel.togglePlayPause() },
                            onSeek = { viewModel.seekTo(it) },
                            onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // DIALOGS
        // 1. Edit Word Dialog
        if (editingSegment != null && editingWord != null) {
            EditWordDialog(
                segment = editingSegment!!,
                word = editingWord!!,
                onDismiss = { viewModel.closeWordEditor() },
                onSave = { segId, wordId, roman, native, eng ->
                    viewModel.saveWordCorrection(segId, wordId, roman, native, eng)
                    Toast.makeText(context, "Word updated in all scripts", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 2. Export Dialog (Download Video + Download Plugin Assets)
        if (showExportDialog) {
            ExportDialog(
                project = currentProject,
                template = selectedTemplate,
                script = selectedScript,
                isExporting = isExporting,
                exportProgress = exportProgress,
                onDismiss = { showExportDialog = false },
                onStartVideoExport = { res ->
                    viewModel.exportVideo(res) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                },
                onExportAssets = { isXml ->
                    try {
                        val shareIntent = viewModel.exportPluginAssets(context, isXml)
                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Subtitle Asset"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "Asset export ready: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // 3. Import & Preset Video Dialog
        if (showImportDialog) {
            ImportVideoDialog(
                projects = allProjects,
                currentProject = currentProject,
                isTranscribing = isTranscribing,
                initialTab = importDialogTab,
                onDismiss = { showImportDialog = false },
                onSelectProject = { viewModel.selectProject(it) },
                onImportLocalVideo = { uri, name -> viewModel.importVideoFile(uri, name) },
                onTranscribeCustomSpeech = { speech -> viewModel.transcribeCustomSpeech(speech) },
                onCreateNewProject = { title, script -> viewModel.createNewProject(title, script) },
                onDuplicateProject = { viewModel.duplicateCurrentProject() },
                onDeleteProject = { viewModel.deleteProject(it) }
            )
        }
    }
}

@Composable
private fun StudioTopBar(
    projectTitle: String,
    scriptBadge: String,
    onOpenGallery: () -> Unit,
    onOpenFile: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenExport: () -> Unit
) {
    Surface(
        color = Color(0xFF0B101B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand identity matching CaptionCraft in screenshot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onOpenProjects() }
            ) {
                // Purple polygon brand badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFA855F7), Color(0xFF6366F1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = "CaptionCraft Logo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CaptionCraft",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Auto-save badge + Unlimited badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✔ Auto Save",
                                color = Color(0xFF22C55E),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Surface(
                                color = Color(0xFF4C1D95),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "♾️ UNLIMITED",
                                    color = Color(0xFFDDD6FE),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Turn Speech Into Stories",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 9.sp
                    )
                }
            }

            // Right Actions: Media + Clips + Download Video ▾
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Add from Gallery button
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier
                        .clickable { onOpenGallery() }
                        .testTag("gallery_add_top_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Add Video from Gallery",
                            tint = Color(0xFF22D3EE),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Gallery",
                            color = Color(0xFF22D3EE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Project Switcher
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier
                        .clickable { onOpenProjects() }
                        .testTag("projects_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Projects",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Clips",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Download Video ▾ Button matching screenshot
                Button(
                    onClick = onOpenExport,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA855F7)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 5.dp),
                    modifier = Modifier.testTag("export_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = "Download Video",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Download ▾",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) Color(0xFFA855F7) else Color(0xFF161F30),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Color.White.copy(alpha = 0.3f) else Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 7.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}
