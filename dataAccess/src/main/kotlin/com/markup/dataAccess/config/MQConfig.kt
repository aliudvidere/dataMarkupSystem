package com.markup.dataAccess.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MQConfig {

    @Value("\${spring.rabbitmq.image_queue}")
    private lateinit var imageQueue: String

    @Value("\${spring.rabbitmq.video_queue}")
    private lateinit var videoQueue: String

    @Bean
    fun imageQueue(): Queue {
        return Queue(imageQueue)
    }

    @Bean
    fun videoQueue(): Queue {
        return Queue(videoQueue)
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}