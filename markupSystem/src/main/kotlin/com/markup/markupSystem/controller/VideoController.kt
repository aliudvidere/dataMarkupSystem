package com.markup.markupSystem.controller

import com.markup.markupSystem.service.VideoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.io.IOException


@Controller
class VideoController(
    private val videoService: VideoService,

    @Value("\${markup_system.page_size}")
    private var pageSize: Int,

    @Value("\${markup_system.pagination_list}")
    private val paginationArray: List<Int>
    ) {

    private var currentPage = 0

    @GetMapping("/video-list")
    fun getVideoList(model: Model): String {
        model.addAttribute("videoList", videoService.getVideoList())
        return "video-list"
    }

    @GetMapping("/video-page")
    fun getVideoPage(@PageableDefault(size = 5, page = 0) pageable: Pageable, model: Model): String {
        pageSize = pageable.pageSize
        currentPage = pageable.pageNumber
        model.addAttribute("paginatedData", videoService.getVideoPage(pageable))
        model.addAttribute("pageSize", pageSize)
        model.addAttribute("paginationArray", paginationArray)
        return "video-page"
    }

    @GetMapping("/stream-video/{folder}")
    @ResponseBody
    @Throws(IOException::class)
    fun streamVideo(@PathVariable folder: String): ResponseEntity<ByteArray> {
        val videoBytes = videoService.streamVideo(folder)
        return ResponseEntity.ok()
            .contentType(MediaType.valueOf("video/mp4"))
            .body(videoBytes)
    }

    @GetMapping("/video")
    fun getVideo(@RequestParam folder: String, model: Model): String {
        model.addAttribute("video", videoService.getVideo(folder))
        model.addAttribute("pageSize", pageSize)
        model.addAttribute("pageNumber", currentPage)
        return "video"
    }

    @PostMapping("/add-description/{folder}")
    fun addDescription(@RequestParam description: String, @PathVariable folder: String): String {
        videoService.addDescription(description, folder)
        return "redirect:/video?folder=$folder"
    }

    @GetMapping("/next")
    fun getNextVideoByOrder(@RequestParam folder: String, model: Model): String {
        model.addAttribute("video", videoService.getNextVideoByOrder(folder))
        model.addAttribute("pageSize", pageSize)
        model.addAttribute("pageNumber", currentPage)
        return "video"
    }

    @GetMapping("/prev")
    fun getPreviousVideoByOrder(@RequestParam folder: String, model: Model): String {
        model.addAttribute("video", videoService.getPreviousVideoByOrder(folder))
        model.addAttribute("pageSize", pageSize)
        model.addAttribute("pageNumber", currentPage)
        return "video"
    }
}

