package com.markup.markupSystem.controller

import com.markup.markupSystem.service.VideoService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.io.IOException


@Controller
class VideoController(private val videoService: VideoService) {

    @GetMapping("/video-list")
    fun getVideoList(model: Model): String {
        model.addAttribute("videoList", videoService.getVideoList())
        return "video-list"
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
        return "video"
    }

    @PostMapping("/add-description/{folder}")
    fun addDescription(@RequestParam description: String, @PathVariable folder: String): String {
        videoService.addDescription(description, folder)
        return "redirect:/video?folder=$folder"
    }
}

