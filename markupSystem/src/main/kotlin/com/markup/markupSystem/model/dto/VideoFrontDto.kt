package com.markup.markupSystem.model.dto

data class VideoFrontDto(val name: String,
                         val description: List<String>) {

    fun getTextDescription(): String {
        return description.joinToString("\n◦", prefix = "◦")
    }
}
