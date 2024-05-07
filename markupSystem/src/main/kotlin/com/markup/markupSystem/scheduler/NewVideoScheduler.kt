package com.markup.markupSystem.scheduler

import com.markup.markupSystem.client.ImageClient
import com.markup.markupSystem.client.VideoClient
import com.markup.markupSystem.model.dto.ImageDto
import com.markup.markupSystem.model.dto.SizeDto
import com.markup.markupSystem.model.dto.VideoDto
import com.markup.markupSystem.utils.PhotoToVideoConverter
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64
import kotlin.io.path.*

@Component
class NewVideoScheduler(
    private val imageClient: ImageClient,
    private val videoClient: VideoClient,
    private val photoToVideoConverter: PhotoToVideoConverter,
    private val rabbitTemplate: RabbitTemplate
) {

    @Value("\${markup_system.source_folder}")
    private lateinit var sourceFolder: String

    @Value("\${spring.rabbitmq.exchange}")
    private lateinit var exchange: String

    @Value("\${spring.rabbitmq.image_routing_key}")
    private lateinit var imageRoutingKey: String

    @Value("\${spring.rabbitmq.video_routing_key}")
    private lateinit var videoRoutingKey: String

    @Scheduled(fixedRateString = "\${markup_system.scanNewVideoFixedRate}", initialDelay = 0L)
    fun scanForNewVideo() {
        val currentFolders = Files.newDirectoryStream(Path.of(sourceFolder))
            .filter { it.isDirectory() }
            .filter { (it.name.matches("^\\d{4}-\\d{2}-\\d{2}_\\d+$".toRegex())) }
        val storedFolders = videoClient.getFoldersList()
        val newFolders = arrayListOf<String>()
        currentFolders.forEach{ if (it.name !in storedFolders) newFolders.add(it.name)}
        newFolders.forEach {
            saveImages(it)
            saveVideo(it)
        }
    }

    @Scheduled(fixedRateString = "\${markup_system.scanNewVideoFixedRate}", initialDelay = 1_800_000L)
    fun checkNewImages() {
        val sizes = videoClient.getSizeList()
        sizes.forEach{ if (isChanged(it)) {
            saveImages(it.folder)
            saveVideo(it.folder)
            }
        }
    }

    private fun saveVideo(folder: String) {
        val video = photoToVideoConverter.convertToVideo(folder)
        val videoDto = VideoDto(folder, Base64.getEncoder().encodeToString(video), getDescription(folder), getFolderSize(folder))
        rabbitTemplate.convertAndSend(exchange, videoRoutingKey, videoDto)
    }

    private fun saveImages(folder: String) {
        val storedImages = imageClient.getImageNumbersByFolder(folder)
        val images = Files.newDirectoryStream(Path.of("$sourceFolder/$folder"))
            .asSequence()
            .filter { it.name.endsWith(".jpg") }
            .filter { it.name.split(".")[0].toInt() !in storedImages }
            .filter { it.isRegularFile() }
            .sortedBy { it.name.split(".")[0].toInt() }
            .map { ImageDto(folder, it.name.split(".")[0].toInt(), Base64.getEncoder().encodeToString(Files.readAllBytes(it))) }
            .toList()

        images.forEach { rabbitTemplate.convertAndSend(exchange, imageRoutingKey, it) }
    }

    private fun getDescription(folder: String): String {
        val description = Path.of("$sourceFolder/$folder/description.txt")
        return if (description.exists()) Files.readString(description) else ""
    }

    private fun isChanged(sizeDto: SizeDto): Boolean {
        val currentSize = getFolderSize(sizeDto.folder)
        val storedSize = sizeDto.size
        return currentSize != storedSize
    }

    private fun getFolderSize(folder: String): Long {
        return try {
            Files.walk(Path.of("$sourceFolder/$folder"))
                .mapToLong { it.fileSize() }
                .sum()
        } catch (exception: IOException) {
            0L
        }
    }
}