package com.example.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.model.CaptionSegment
import com.example.model.OutputScript
import com.example.model.VideoProject
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

/**
 * ExportEngine provides standard .srt subtitle export, Premiere Pro/DaVinci Resolve XML sequences,
 * and high-definition video export preparation.
 */
object ExportEngine {

    /**
     * Generates a standard SubRip (.srt) subtitle string
     */
    fun generateSrtContent(project: VideoProject, script: OutputScript): String {
        val sb = StringBuilder()
        var counter = 1

        for (segment in project.segments) {
            val text = segment.getFullTextForScript(script)
            if (text.isBlank()) continue

            sb.append(counter).append("\n")
            sb.append(formatSrtTimestamp(segment.startTime))
                .append(" --> ")
                .append(formatSrtTimestamp(segment.endTime))
                .append("\n")
            sb.append(text).append("\n\n")
            counter++
        }

        return sb.toString()
    }

    /**
     * Formats seconds into HH:MM:SS,mmm
     */
    private fun formatSrtTimestamp(seconds: Float): String {
        val totalMillis = (seconds * 1000).toLong()
        val hrs = totalMillis / 3600000
        val mins = (totalMillis % 3600000) / 60000
        val secs = (totalMillis % 60000) / 1000
        val millis = totalMillis % 1000

        return String.format(Locale.US, "%02d:%02d:%02d,%03d", hrs, mins, secs, millis)
    }

    /**
     * Generates a standard Final Cut Pro / Premiere Pro XML sequence (<xmeml>)
     * compatible with Adobe Premiere Pro and DaVinci Resolve
     */
    fun generatePremiereXml(project: VideoProject, script: OutputScript, fps: Int = 30): String {
        val totalFrames = (project.durationSeconds * fps).toLong()
        val sb = StringBuilder()

        sb.append("""<?xml version="1.0" encoding="UTF-8"?>""").append("\n")
        sb.append("""<!DOCTYPE xmeml>""").append("\n")
        sb.append("""<xmeml version="4">""").append("\n")
        sb.append("  <sequence>\n")
        sb.append("    <name>${escapeXml(project.title)} - Hinglish Captions</name>\n")
        sb.append("    <duration>$totalFrames</duration>\n")
        sb.append("    <rate>\n")
        sb.append("      <timebase>$fps</timebase>\n")
        sb.append("      <ntsc>FALSE</ntsc>\n")
        sb.append("    </rate>\n")
        sb.append("    <media>\n")
        sb.append("      <video>\n")
        sb.append("        <format>\n")
        sb.append("          <samplecharacteristics>\n")
        sb.append("            <width>1080</width>\n")
        sb.append("            <height>1920</height>\n")
        sb.append("            <pixelaspectratio>square</pixelaspectratio>\n")
        sb.append("          </samplecharacteristics>\n")
        sb.append("        </format>\n")
        sb.append("        <track>\n")

        for ((index, segment) in project.segments.withIndex()) {
            val inFrame = (segment.startTime * fps).toLong()
            val outFrame = (segment.endTime * fps).toLong()
            val text = escapeXml(segment.getFullTextForScript(script))

            sb.append("          <generatoritem id=\"caption_$index\">\n")
            sb.append("            <name>$text</name>\n")
            sb.append("            <duration>${outFrame - inFrame}</duration>\n")
            sb.append("            <rate><timebase>$fps</timebase></rate>\n")
            sb.append("            <in>0</in>\n")
            sb.append("            <out>${outFrame - inFrame}</out>\n")
            sb.append("            <start>$inFrame</start>\n")
            sb.append("            <end>$outFrame</end>\n")
            sb.append("            <effect>\n")
            sb.append("              <name>Basic Title</name>\n")
            sb.append("              <effectid>Basic Title</effectid>\n")
            sb.append("              <effecttype>generator</effecttype>\n")
            sb.append("              <mediatype>video</mediatype>\n")
            sb.append("              <parameter>\n")
            sb.append("                <parameterid>text</parameterid>\n")
            sb.append("                <name>Text</name>\n")
            sb.append("                <value>$text</value>\n")
            sb.append("              </parameter>\n")
            sb.append("              <parameter>\n")
            sb.append("                <parameterid>speaker</parameterid>\n")
            sb.append("                <name>Speaker</name>\n")
            sb.append("                <value>${segment.speaker}</value>\n")
            sb.append("              </parameter>\n")
            sb.append("            </effect>\n")
            sb.append("          </generatoritem>\n")
        }

        sb.append("        </track>\n")
        sb.append("      </video>\n")
        sb.append("    </media>\n")
        sb.append("  </sequence>\n")
        sb.append("</xmeml>\n")

        return sb.toString()
    }

    private fun escapeXml(str: String): String {
        return str.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    /**
     * Exports a file to cache and returns the shareable intent
     */
    fun saveAndCreateShareIntent(
        context: Context,
        content: String,
        fileName: String,
        mimeType: String
    ): Intent {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)
        FileOutputStream(file).use { it.write(content.toByteArray(Charsets.UTF_8)) }

        val uri = try {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: Exception) {
            Uri.fromFile(file)
        }

        return Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, fileName)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
