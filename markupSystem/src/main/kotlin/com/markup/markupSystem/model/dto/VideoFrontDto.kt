package com.markup.markupSystem.model.dto

data class VideoFrontDto(val name: String,
                         val description: List<String>) {

    constructor(name: String, description: String) : this(name, description.split("\n"))

    fun getTextDescription(): String {
        return description.joinToString("\n◦", prefix = "◦")
    }

    fun getListDescription(): String {
        return description.joinToString("; ")
    }

}
