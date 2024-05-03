package com.markup.markupSystem.model.dto

import java.io.Serializable

data class VideoDto(
    val folder: String,
    val file: String,
    val size: Long): Serializable