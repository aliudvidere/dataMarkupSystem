package com.markup.dataAccess.model.entity

import com.markup.dataAccess.model.dto.VideoDto
import jakarta.persistence.*

@Entity
@Table(name = "VIDEOS")
data class Video(
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,
        generator="videos_seq")
    @SequenceGenerator(name="videos_seq",
        sequenceName="videos_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "folder", nullable = false)
    val folder: String,

    @Column(name = "video", nullable = false)
    var videoEncoded: String,

    @Column(name = "size", nullable = false)
    var size: Long,

    @Column(name = "description", nullable = false)
    var description: String

) {
    constructor(videoDto: VideoDto) : this(null, videoDto.folder, videoDto.file, videoDto.size, videoDto.description)
}
