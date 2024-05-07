package com.markup.dataAccess.controller

import com.markup.dataAccess.model.dto.SizeDto
import com.markup.dataAccess.model.dto.VideoDto
import com.markup.dataAccess.service.VideoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/video")
class VideoController(private val videoService: VideoService) {

    @GetMapping("/get-by-folder")
    fun getImageByFolderAndNumber(@RequestParam folder: String): VideoDto {
        return videoService.getVideoByFolder(folder)
    }

    @GetMapping("/get-video-size")
    fun getVideoSize(@RequestParam folder: String): Long {
        return videoService.getVideoSize(folder)
    }

    @GetMapping("/get-folders-list")
    fun getFoldersList(): List<String> {
        return videoService.getFoldersList()
    }

    @GetMapping("/get-size-list")
    fun getSizeList(): List<SizeDto> {
        return videoService.getSizeList()
    }

    @GetMapping("/get-video-page")
    fun getVideoPage(pageable: Pageable): Page<VideoDto> {
        return videoService.getVideoPage(pageable)
    }
}