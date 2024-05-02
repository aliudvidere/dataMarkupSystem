package com.markup.dataAccess.repository

import com.markup.dataAccess.model.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository: JpaRepository<Image, Long> {

    fun getImageByFolderAndImageId(folder: String, imageId: Int): Image?

    @Query("select i.imageId from Image i where i.folder = :folder")
    fun getImageIdsByFolder(@Param("folder") folder: String): List<Int>

}