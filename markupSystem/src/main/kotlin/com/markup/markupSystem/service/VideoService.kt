package com.markup.markupSystem.service

import com.markup.markupSystem.client.VideoClient
import com.markup.markupSystem.model.dto.DescriptionDto
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

    fun streamVideoFromPC(folder: String): ByteArray {
        return photoToVideoConverter.convertToVideo(folder)
    }

    fun streamVideo(folder: String): ByteArray {
        val video = videoClient.getVideoByFolder(folder)
        return photoToVideoConverter.convertToVideo(video)
    }

    fun addDescription(description: String, folder: String) {
        videoClient.setDescription(DescriptionDto(folder, description))
        val path = Paths.get("$sourceFolder/$folder/description.txt")
        if (Files.exists(path)) {
            Files.writeString(path, "$description\n", StandardOpenOption.APPEND)
        }
        else {
            Files.writeString(path, "$description\n")
        }
    }

    fun getVideoFromPC(folder: String): VideoFrontDto {
        return VideoFrontDto(folder, if (Files.exists(Path.of("$sourceFolder/$folder/description.txt"))) Path.of("$sourceFolder/$folder/description.txt").readLines() else arrayListOf())
    }

    fun getVideo(folder: String): VideoFrontDto {
        val video = videoClient.getVideoByFolder(folder)
        return VideoFrontDto(video.folder, video.description)
    }

    fun getVideoList(): List<VideoFrontDto> {
        return arrayListOf()
    }

    fun getVideoPage(pageable: Pageable): Page<VideoFrontDto> {
        val videoPage = videoClient.getVideoPage(pageable)
        val videoDtoPage = videoPage.map { VideoFrontDto(it.folder, it.description)  }
        return videoDtoPage
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

    fun getNextVideoByOrder(folder: String): VideoFrontDto {
        val video = videoClient.getNextVideoByOrder(folder)
        return VideoFrontDto(video.folder, video.description)
    }

    fun getPreviousVideoByOrder(folder: String): VideoFrontDto {
        val video = videoClient.getPreviousVideoByOrder(folder)
        return VideoFrontDto(video.folder, video.description)
    }


}