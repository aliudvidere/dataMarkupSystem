package com.markup.markupSystem.controller

import com.markup.markupSystem.service.ImageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ImageController(private val imageService: ImageService) {

    @GetMapping(value = ["/save-image"])
    fun saveImage(@RequestParam folder: String, @RequestParam number: Int) {
        imageService.saveImage(folder, number)
    }

    @GetMapping(value = ["/get-image"])
    fun getImageByFolderAndNumber(@RequestParam folder: String, @RequestParam number: Int, model: Model): String {
        val image = imageService.getImageByFolderAndNumber(folder, number)
        model.addAttribute("image", image)
        return "image"
    }
}