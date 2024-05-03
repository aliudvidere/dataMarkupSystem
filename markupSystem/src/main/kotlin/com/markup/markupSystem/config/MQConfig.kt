package com.markup.markupSystem.config

import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MQConfig {

    @Value("\${spring.rabbitmq.image_queue}")
    private lateinit var imageQueue: String

    @Value("\${spring.rabbitmq.video_queue}")
    private lateinit var videoQueue: String

    @Value("\${spring.rabbitmq.exchange}")
    private lateinit var exchange: String

    @Value("\${spring.rabbitmq.image_routing_key}")
    private lateinit var imageRoutingKey: String

    @Value("\${spring.rabbitmq.video_routing_key}")
    private lateinit var videoRoutingKey: String

    @Bean(name = ["imageQueue"])
    fun imageQueue(): Queue {
        return Queue(imageQueue)
    }

    @Bean(name = ["videoQueue"])
    fun videoQueue(): Queue {
        return Queue(videoQueue)
    }

    @Bean
    fun exchange(): Exchange {
        return TopicExchange(exchange)
    }

    @Bean
    fun bindingImage(@Qualifier("imageQueue") queue: Queue, exchange: Exchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(imageRoutingKey).noargs()
    }

    @Bean
    fun bindingVideo(@Qualifier("videoQueue") queue: Queue, exchange: Exchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(videoRoutingKey).noargs()
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}