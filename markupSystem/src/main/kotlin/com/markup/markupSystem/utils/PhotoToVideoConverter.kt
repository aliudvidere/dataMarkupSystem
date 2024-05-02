package com.markup.markupSystem.utils

import org.jcodec.api.awt.AWTSequenceEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.*
import javax.imageio.ImageIO
import kotlin.io.path.createFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.name


@Component
class PhotoToVideoConverter {

    @Value("\${markup_system.source_folder}")
    private lateinit var sourceFolder: String

    fun convertToVideo(folder: String): ByteArray {
        val directory = "$sourceFolder/$folder"
        val path: Path = Path.of("$directory/video.mp4").createFile()
        val encoder: AWTSequenceEncoder = AWTSequenceEncoder.createSequenceEncoder(path.toFile(), 25)
        val images = Files.newDirectoryStream(Path.of(directory))
            .map { it.name }
            .filter { it.endsWith(".jpg") }
            .sortedBy { it.split(".")[0].toInt() }
            .toList()
        images.map { File("$directory/$it") }
            .map { ImageIO.read(it) }
            .forEach{ encoder.encodeImage(it) }
        encoder.finish()
        val bytes = Files.readAllBytes(path)
        path.deleteExisting()
        return bytes
    }
}

