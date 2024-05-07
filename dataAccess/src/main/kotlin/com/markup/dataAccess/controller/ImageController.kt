package com.markup.dataAccess.controller

import com.markup.dataAccess.model.dto.ImageDto
import com.markup.dataAccess.service.ImageService
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/get-by-folder-and-number")
    fun getImageByFolderAndNumber(@RequestParam folder: String, @RequestParam number: Int): ImageDto {
        return imageService.getImageByFolderAndNumber(folder, number)
    }

    @GetMapping("/image-numbers-by-folder")
    fun getImageNumbersByFolder(@RequestParam folder: String): List<Int> {
        return imageService.getImageNumbersByFolder(folder)
    }
}