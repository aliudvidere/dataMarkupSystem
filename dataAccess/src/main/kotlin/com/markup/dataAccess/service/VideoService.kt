package com.markup.dataAccess.service

import com.markup.dataAccess.model.dto.SizeDto
import com.markup.dataAccess.model.dto.VideoDto
import com.markup.dataAccess.model.entity.Video
import com.markup.dataAccess.repository.VideoRepository
import org.springframework.stereotype.Service
import com.markup.dataAccess.util.EmptinessChecker.Companion.isNotEmpty
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class VideoService(private val videoRepository: VideoRepository) {

    @RabbitListener(queues = ["\${spring.rabbitmq.video_queue}"])
    fun saveVideo(videoDto: VideoDto) {
        val video = videoRepository.getVideoByFolder(videoDto.folder)
        if (isNotEmpty(video))
        {
            video!!.videoEncoded = videoDto.file
            video.size = videoDto.size
            video.description = videoDto.description
            videoRepository.save(video)
        }
        else {
            videoRepository.save(Video(videoDto))
        }
    }

    fun getVideoByFolder(folder: String): VideoDto {
        val video = videoRepository.getVideoByFolder(folder)
        return if (isNotEmpty(video)) VideoDto(video!!.folder, video.videoEncoded, video.description, video.size) else VideoDto()
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

    fun getVideoPage(pageable: Pageable): Page<VideoDto> {
        return videoRepository.findAll(pageable).map { VideoDto(it!!.folder, it.videoEncoded, "", it.size) }
    }
}