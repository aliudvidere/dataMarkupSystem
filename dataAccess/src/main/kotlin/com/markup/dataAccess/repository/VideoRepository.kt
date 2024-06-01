package com.markup.dataAccess.repository

import com.markup.dataAccess.model.dto.SizeDto
import com.markup.dataAccess.model.entity.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface VideoRepository: JpaRepository<Video, Long> {

    fun getVideoByFolder(folder: String): Video?

    @Query("select v.size from Video v where v.folder = :folder")
    fun getVideoSize(@Param("folder") folder: String): Long?

    @Query("select distinct v.folder from Video v")
    fun getFoldersList(): List<String>

    @Query("select v from Video v")
    fun getAllSize(): List<SizeDto>

    @Modifying
    @Transactional
    @Query("update Video v set v.description = concat(v.description, :description) where v.folder = :folder")
    fun setDescription(@Param("folder") folder: String, @Param("description") description: String)

    @Query("SELECT v FROM Video v ORDER BY CAST(SPLIT_PART(v.folder, '_', 2) AS integer)")
    fun findAllOrderByFolderNumber(pageable: Pageable): Page<Video>

    @Query("SELECT v.folder FROM Video v ORDER BY CAST(SPLIT_PART(v.folder, '_', 2) AS integer)")
    fun findAllFoldersOrderByFolderNumber(): List<String>

    fun countVideoByDescriptionNotLike(description: String): Long
}