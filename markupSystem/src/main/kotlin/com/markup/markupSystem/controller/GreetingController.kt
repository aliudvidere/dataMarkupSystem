package com.markup.markupSystem.controller

import com.markup.markupSystem.client.ImageClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/service")
class GreetingController(
    private val imageClient: ImageClient) {

    @GetMapping("/get-greeting")
    fun getGreeting(): String {
        return imageClient.getGreeting()
    }
}