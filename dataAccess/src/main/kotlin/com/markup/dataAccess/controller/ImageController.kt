package com.markup.dataAccess.controller

import com.markup.dataAccess.model.dto.ImageDto
import com.markup.dataAccess.service.ImageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/image")
class ImageController(private val imageService: ImageService) {

    @GetMapping("/get-greeting")
    fun getGreeting(): String {
        return "Hello Word!"
    }

    @PostMapping("/save")
    fun saveImage(@RequestBody imageDto: ImageDto) {
        imageService.saveImage(imageDto)
    }

    @GetMapping("/get-by-folder-and-number")
    fun getImageByFolderAndNumber(@RequestParam folder: String, @RequestParam number: Int): ImageDto {
        return imageService.getImageByFolderAndNumber(folder, number)
    }

    @PostMapping("/save-list")
    fun saveImageList(@RequestBody imageDtoList: List<ImageDto>) {
        imageService.saveImageList(imageDtoList)
    }
}