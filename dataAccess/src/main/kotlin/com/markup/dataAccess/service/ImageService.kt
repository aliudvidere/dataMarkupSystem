package com.markup.dataAccess.service

import com.markup.dataAccess.model.dto.ImageDto
import com.markup.dataAccess.model.entity.Image
import com.markup.dataAccess.repository.ImageRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class ImageService(private val imageRepository: ImageRepository) {

    @RabbitListener(queues = ["\${spring.rabbitmq.image_queue}"])
    fun saveImage(imageDto: ImageDto) {
        imageRepository.save(Image(imageDto))
    }

    fun getImageByFolderAndNumber(folder: String, number: Int): ImageDto {
        val image = imageRepository.getImageByFolderAndImageId(folder, number)
        return if (image != null) ImageDto(image.folder, image.imageId, image.imageEncoded) else ImageDto()
    }
}