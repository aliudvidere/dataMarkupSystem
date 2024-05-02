package com.markup.markupSystem.client

import com.markup.markupSystem.config.FeignClientConfiguration
import com.markup.markupSystem.model.dto.ImageDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "imagesClient",
    url = "http://127.0.0.1:555",
    path = "/data-access/image",
    configuration = [FeignClientConfiguration::class])
interface ImageClient {

    @GetMapping(value = ["/get-greeting"])
    fun getGreeting(): String

    @PostMapping(value = ["/save"])
    fun saveImage(@RequestBody imageDto: ImageDto)

    @GetMapping(value = ["/get-by-folder-and-number"])
    fun getImageByFolderAndNumber(@RequestParam folder: String, @RequestParam number: Int): ImageDto

    @PostMapping(value = ["/save-list"])
    fun saveImageList(@RequestBody imageDtoList: List<ImageDto>)
}