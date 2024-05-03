package com.markup.markupSystem.model.dto

import java.io.Serializable

data class ImageDto(
    val folder: String,
    val number: Int,
    val file: String): Serializable