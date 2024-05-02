package com.markup.dataAccess.service

import com.markup.dataAccess.model.dto.ImageDto
import com.markup.dataAccess.model.entity.Image
import com.markup.dataAccess.repository.ImageRepository
import org.springframework.stereotype.Service

@Service
class ImageService(private val imageRepository: ImageRepository) {

    fun saveImage(imageDto: ImageDto) {
        imageRepository.save(Image(imageDto))
    }

    fun getImageByFolderAndNumber(folder: String, number: Int): ImageDto {
        val image = imageRepository.getImageByFolderAndImageId(folder, number)
        return if (image != null) ImageDto(image.folder, image.imageId, image.imageEncoded) else ImageDto()
    }

    fun saveImageList(imageDtoList: List<ImageDto>) {
        val storedImages = imageRepository.getImageIdsByFolder(imageDtoList[0].folder)
        val imagesToSave = imageDtoList.filter { it.number !in storedImages }
        imageRepository.saveAll(imagesToSave.map { Image(null, it.folder, it.number, it.file) })
    }
}