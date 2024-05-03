package com.markup.dataAccess.model.dto


data class ImageDto(
    val folder: String,
    val number: Int,
    val file: String){

    constructor() : this("", -1, "")
}