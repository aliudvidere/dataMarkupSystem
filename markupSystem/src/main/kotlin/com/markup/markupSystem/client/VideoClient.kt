package com.markup.markupSystem.client

import com.markup.markupSystem.config.FeignClientConfiguration
import com.markup.markupSystem.model.dto.DescriptionDto
import com.markup.markupSystem.model.dto.SizeDto
import com.markup.markupSystem.model.dto.VideoDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "videosClient",
    url = "http://127.0.0.1:555",
    path = "/data-access/video",
    configuration = [FeignClientConfiguration::class])
interface VideoClient {

    @PostMapping(value = ["/save"])
    fun saveVideo(@RequestBody videoDto: VideoDto)

    @GetMapping(value = ["/get-by-folder"])
    fun getVideoByFolder(@RequestParam folder: String): VideoDto

    @GetMapping(value = ["/get-video-size"])
    fun getVideoSize(@RequestParam folder: String): Long

    @GetMapping("/get-folders-list")
    fun getFoldersList(): List<String>

    @GetMapping("/get-size-list")
    fun getSizeList(): List<SizeDto>

    @GetMapping("/get-video-page")
    fun getVideoPage(@SpringQueryMap pageable: Pageable): Page<VideoDto>

    @PostMapping("/set-description")
    fun setDescription(@RequestBody descriptionDto: DescriptionDto)

    @GetMapping("/get-next-by-order")
    fun getNextVideoByOrder(@RequestParam folder: String): VideoDto

    @GetMapping("/get-prev-by-order")
    fun getPreviousVideoByOrder(@RequestParam folder: String): VideoDto
}