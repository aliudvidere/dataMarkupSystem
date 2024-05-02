package com.markup.markupSystem.service

import com.markup.markupSystem.client.ImageClient
import com.markup.markupSystem.model.dto.ImageDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64

@Service
class ImageService(private val imageClient: ImageClient) {

    @Value("\${markup_system.source_folder}")
    private lateinit var sourceFolder: String

    fun saveImage(folder: String, number: Int) {
        val image = Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("$sourceFolder/$folder/$number.jpg")))
        imageClient.saveImage(ImageDto(folder, number, image))
    }

    fun getImageByFolderAndNumber(folder: String, number: Int): String {
        return imageClient.getImageByFolderAndNumber(folder, number).file
    }
}