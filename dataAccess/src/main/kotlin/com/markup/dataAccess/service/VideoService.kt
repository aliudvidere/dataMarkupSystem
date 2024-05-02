package com.markup.dataAccess.service

import com.markup.dataAccess.model.dto.SizeDto
import com.markup.dataAccess.model.dto.VideoDto
import com.markup.dataAccess.model.entity.Video
import com.markup.dataAccess.repository.VideoRepository
import org.springframework.stereotype.Service
import com.markup.dataAccess.util.EmptinessChecker.Companion.isNotEmpty

@Service
class VideoService(private val videoRepository: VideoRepository) {

    fun saveVideo(videoDto: VideoDto) {
        val video = videoRepository.getVideoByFolder(videoDto.folder)
        if (isNotEmpty(video))
        {
            video!!.videoEncoded = videoDto.file
            video.size = videoDto.size
            videoRepository.save(video)
        }
        else {
            videoRepository.save(Video(videoDto))
        }
    }

    fun getVideoByFolder(folder: String): VideoDto {
        val video = videoRepository.getVideoByFolder(folder)
        return if (isNotEmpty(video)) VideoDto(video!!.folder, video.videoEncoded, video.size) else VideoDto()
    }

    fun getVideoSize(folder: String): Long {
        val size = videoRepository.getVideoSize(folder)
        return if (isNotEmpty(size)) size!! else 0L
    }

    fun getFoldersList(): List<String> {
        return videoRepository.getFoldersList()
    }

    fun getSizeList(): List<SizeDto> {
        return videoRepository.getAllSize()
    }
}