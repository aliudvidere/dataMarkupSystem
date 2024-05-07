package com.markup.dataAccess.model.dto

data class VideoDto(
    val folder: String,
    val file: String,
    val description: String,
    val size: Long) {

    constructor() : this("", "", "", 0)
}