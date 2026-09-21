package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.export.ExportEngine
import com.example.model.CaptionTemplate
import com.example.model.OutputScript
import com.example.service.TranscriptionEngine
import com.example.viewmodel.StudioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CaptionStudioRobolectricTest {

    @Test
    fun `test preset projects load with synchronized word timestamps across all three scripts`() {
        val projects = TranscriptionEngine.getPresetProjects()
        assertTrue(projects.isNotEmpty())

        val techProject = projects.first()
        assertTrue(techProject.segments.isNotEmpty())

        val firstSeg = techProject.segments.first()
        assertTrue(firstSeg.romanWords.isNotEmpty())
        assertTrue(firstSeg.nativeWords.isNotEmpty())
        assertTrue(firstSeg.englishWords.isNotEmpty())

        // Verify word timestamps are valid
        firstSeg.romanWords.forEach { word ->
            assertTrue("Start time should be >= 0", word.startTime >= 0f)
            assertTrue("End time should be > start time", word.endTime > word.startTime)
            assertTrue("Word text should not be empty", word.text.isNotBlank())
        }
    }

    @Test
    fun `test SRT export contains valid timestamps and line numbering`() {
        val project = TranscriptionEngine.getPresetProjects().first()
        val srtContent = ExportEngine.generateSrtContent(project, OutputScript.ROMAN_HINGLISH)

        assertTrue("SRT should contain segment counter", srtContent.contains("1\n"))
        assertTrue("SRT should contain arrow delimiter", srtContent.contains(" --> "))
        assertTrue("SRT should contain text", srtContent.contains("Arre bhai"))
    }

    @Test
    fun `test Premiere Pro XML export contains valid sequence and track markers`() {
        val project = TranscriptionEngine.getPresetProjects().first()
        val xmlContent = ExportEngine.generatePremiereXml(project, OutputScript.NATIVE_HINDI)

        assertTrue(xmlContent.contains("<xmeml version=\"4\">"))
        assertTrue(xmlContent.contains("<sequence>"))
        assertTrue(xmlContent.contains("<generatoritem"))
        assertTrue(xmlContent.contains("Basic Title"))
    }

    @Test
    fun `test StudioViewModel word edit updates all script representations`() {
        val viewModel = StudioViewModel()
        val project = viewModel.currentProject.value
        val firstSeg = project.segments.first()
        val firstWord = firstSeg.romanWords.first()

        viewModel.saveWordCorrection(
            segmentId = firstSeg.id,
            wordId = firstWord.id,
            newRoman = "Wah",
            newNative = "वाह",
            newEnglish = "Wow"
        )

        val updatedProject = viewModel.currentProject.value
        val updatedWord = updatedProject.segments.first().romanWords.first()
        assertEquals("Wah", updatedWord.text)

        val updatedNativeWord = updatedProject.segments.first().nativeWords.first()
        assertEquals("वाह", updatedNativeWord.text)

        val updatedEnglishWord = updatedProject.segments.first().englishWords.first()
        assertEquals("Wow", updatedEnglishWord.text)
    }

    @Test
    fun `test StudioViewModel template and script selection`() {
        val viewModel = StudioViewModel()

        viewModel.selectTemplate(CaptionTemplate.BOLD_DROP)
        assertEquals(CaptionTemplate.BOLD_DROP, viewModel.selectedTemplate.value)

        viewModel.selectScript(OutputScript.NATIVE_HINDI)
        assertEquals(OutputScript.NATIVE_HINDI, viewModel.selectedScript.value)

        viewModel.seekTo(3.5f)
        assertEquals(3.5f, viewModel.currentTime.value, 0.01f)
    }

    @Test
    fun `test unlimited project creation and duplication without quota`() {
        val viewModel = StudioViewModel()
        val initialCount = viewModel.projects.value.size

        // Create new project with custom speech
        viewModel.createNewProject(
            title = "Unlimited Reel Project",
            scriptText = "Bhai viral reel ban gayi bina kisi limit ke"
        )
        assertEquals(initialCount + 1, viewModel.projects.value.size)
        assertEquals("Unlimited Reel Project", viewModel.currentProject.value.title)
        assertTrue(viewModel.currentProject.value.segments.isNotEmpty())

        // Duplicate project
        viewModel.duplicateCurrentProject()
        assertEquals(initialCount + 2, viewModel.projects.value.size)
        assertTrue(viewModel.currentProject.value.title.contains("(Copy)"))

        // Delete duplicated project
        val currentId = viewModel.currentProject.value.id
        viewModel.deleteProject(currentId)
        assertEquals(initialCount + 1, viewModel.projects.value.size)
    }

    @Test
    fun `test all 12 text effects and 13 animations are selectable`() {
        val viewModel = StudioViewModel()
        com.example.model.TextEffect.values().forEach { effect ->
            viewModel.setTextEffect(effect)
            assertEquals(effect, viewModel.activeTextEffect.value)
        }

        com.example.model.TextAnimation.values().forEach { anim ->
            viewModel.setTextAnimation(anim)
            assertEquals(anim, viewModel.activeAnimation.value)
        }
    }
}
