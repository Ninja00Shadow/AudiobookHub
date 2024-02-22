package com.example.audiobookhub.data.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.util.concurrent.TimeUnit

data class AudioBook (
    val chapterFolderName: String,
    val title: String,
    val author: String,
    val narrator: String,
    val score: Int,
    val cover: Bitmap?,
    val duration: Int,
    var progress: Float,
    var playbackSpeed: Float,
    val description: String,
    val chapters: List<Chapter>
) {
    fun getCover(): ImageBitmap {
        return cover?.asImageBitmap() ?: ImageBitmap(1, 1)
    }
}