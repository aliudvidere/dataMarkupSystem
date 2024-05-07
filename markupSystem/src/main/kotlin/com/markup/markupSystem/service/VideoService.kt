package com.markup.markupSystem.service

import com.markup.markupSystem.client.VideoClient
import com.markup.markupSystem.model.dto.VideoFrontDto
import com.markup.markupSystem.utils.PhotoToVideoConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readLines

@Service
class VideoService(private val photoToVideoConverter: PhotoToVideoConverter,
    private val videoClient: VideoClient) {

    @Value("\${markup_system.source_folder}")
    private lateinit var sourceFolder: String

    fun streamVideo(folder: String): ByteArray {
        return photoToVideoConverter.convertToVideo(folder)
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
        return arrayListOf()
    }

    fun getVideoPage(pageable: Pageable): Page<VideoFrontDto> {
        return videoClient.getVideoPage(pageable).map { VideoFrontDto(it.folder, it.description) }
    }
    fun getVideoListFromPC(): List<VideoFrontDto> {
        val videos = arrayListOf<VideoFrontDto>()
        Files.newDirectoryStream(Path.of(sourceFolder))
            .filter { it.isDirectory() }
            .filter { (it.name.matches("^\\d{4}-\\d{2}-\\d{2}_\\d+$".toRegex())) }
            .map { videos.add(VideoFrontDto(it.name, if (Files.exists(Path.of("$it/description.txt"))) Path.of("$it/description.txt").readLines() else arrayListOf())) }
            .toList()
        return videos.sortedBy { it.name.split("_")[1].toInt() }
    }
}