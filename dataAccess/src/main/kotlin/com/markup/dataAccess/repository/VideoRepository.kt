package com.markup.dataAccess.repository

import com.markup.dataAccess.model.dto.SizeDto
import com.markup.dataAccess.model.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository: JpaRepository<Video, Long> {

    fun getVideoByFolder(folder: String): Video?

    @Query("select v.size from Video v where v.folder = :folder")
    fun getVideoSize(@Param("folder") folder: String): Long?

    @Query("select distinct v.folder from Video v")
    fun getFoldersList(): List<String>

    @Query("select v from Video v")
    fun getAllSize(): List<SizeDto>
}