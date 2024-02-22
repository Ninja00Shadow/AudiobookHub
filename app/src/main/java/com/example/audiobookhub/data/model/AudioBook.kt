package com.example.audiobookhub.data.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.util.concurrent.TimeUnit

data class AudioBook (
    private val chapterFolderName: String,
    val title: String,
    val author: String,
    val narrator: String,
    val score: Int,
    val cover: Bitmap?,
    val duration: Int,
    val description: String,
    val chapters: List<Chapter>
) {
    fun getCover(): ImageBitmap {
        return cover?.asImageBitmap() ?: ImageBitmap(1, 1)
    }

    fun getTimeSinceBeginning(progress: Float): String {
        val time = (progress/100 * duration).toLong()

        val hours = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getTimeLeft(progress: Float): String {
        val timeLeft = duration - (progress/100 * duration).toLong()

        val hoursLeft = TimeUnit.MILLISECONDS.toHours(timeLeft)
        val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeLeft) - TimeUnit.HOURS.toMinutes(hoursLeft)
        val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(timeLeft) - TimeUnit.MINUTES.toSeconds(minutesLeft) - TimeUnit.HOURS.toSeconds(hoursLeft)

        return String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)
    }
}