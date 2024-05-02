package com.markup.markupSystem.service

import com.markup.markupSystem.model.dto.VideoFrontDto
import com.markup.markupSystem.utils.PhotoToVideoConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readLines

@Service
class VideoService(private val photoToVideoConverter: PhotoToVideoConverter) {

    @Value("\${markup_system.source_folder}")
    private lateinit var sourceFolder: String

    @Value("\${markup_system.is_prod}")
    private lateinit var isProd: String

    fun streamVideo(folder: String): ByteArray {
        return photoToVideoConverter.convertToVideo("$sourceFolder/$folder")
    }

    fun addDescription(description: String, folder: String) {
        val path = Paths.get("$sourceFolder/$folder/description.txt")
        if (Files.exists(path)) {
            Files.writeString(path, "$description\n", StandardOpenOption.APPEND)
        }
        else {
            Files.writeString(path, "$description\n")
        }
    }

    fun getVideo(folder: String): VideoFrontDto {
        return VideoFrontDto(folder, if (Files.exists(Path.of("$sourceFolder/$folder/description.txt"))) Path.of("$sourceFolder/$folder/description.txt").readLines() else arrayListOf())
    }

    fun getVideoList(): List<VideoFrontDto> {
        val videos = arrayListOf<VideoFrontDto>()
        Files.newDirectoryStream(Path.of(sourceFolder))
            .filter { it.isDirectory() }
            .map { videos.add(VideoFrontDto(it.name, if (Files.exists(Path.of("$it/description.txt"))) Path.of("$it/description.txt").readLines() else arrayListOf())) }
            .toList()
        return if (isProd.toBoolean()) videos.sortedBy { it.name.split("_")[1].toInt() } else videos
    }
}