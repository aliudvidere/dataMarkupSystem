package com.markup.dataAccess.model.entity

import com.markup.dataAccess.model.dto.ImageDto
import jakarta.persistence.*

@Entity
@Table(name = "IMAGES")
data class Image(
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,
        generator="images_seq")
    @SequenceGenerator(name="images_seq",
        sequenceName="images_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "folder", nullable = false)
    val folder: String,

    @Column(name = "image_id", nullable = false)
    val imageId: Int,

    @Column(name = "image", nullable = false)
    val imageEncoded: String
) {
    constructor(imageDto: ImageDto) : this(null, imageDto.folder, imageDto.number, imageDto.file)
}
