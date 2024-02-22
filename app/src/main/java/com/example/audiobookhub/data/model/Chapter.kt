package com.example.audiobookhub.data.model

import android.net.Uri

data class Chapter (
    val uri: Uri,
    val name: String,
    val number: Int,
    val duration: Int
) {
    fun getDurationLeft(progress: Float): String {
        val durationLeft = (duration * (1 - progress/100)).toLong()
        val minute = durationLeft / 60000
        val seconds = (durationLeft - minute * 60000) / 1000
        return String.format("%2d:%02d", minute, seconds)
    }
}
