package com.example.audiobookhub.ui.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val bookDummy = AudioBook(
    "",
    "",
    "",
    "",
    3,
    "".toUri(),
    0,
    0f,
    1f,
    "",
    emptyList(),
)

@HiltViewModel
class AudiobookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    var audioBook by mutableStateOf(bookDummy)

    private fun loadAudioBook(chapterFolderName: String) {
        audioBook = bookRepository.getBookByFolderName(chapterFolderName)
    }

    private fun updateScore(score: Int) {
        audioBook = audioBook.copy(score = score)
        bookRepository.updateScore(audioBook)
    }

    fun onUiEvents(event: DetailsUiEvents) {
        when(event) {
            is DetailsUiEvents.LoadAudioBook -> {
                loadAudioBook(event.chapterFolderName)
            }
            is DetailsUiEvents.UpdateScore -> {
                updateScore(event.score)
            }
        }
    }

    fun formatDuration(): String {
        val hours = TimeUnit.HOURS.convert(audioBook.duration.toLong(), TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(audioBook.duration.toLong(), TimeUnit.MILLISECONDS) % 60

        return if (hours > 0) {
            String.format("%dh %dmin", hours, minutes)
        } else {
            String.format("%dmin", minutes)
        }
    }
}

sealed class DetailsUiEvents {
    data class LoadAudioBook(val chapterFolderName: String) : DetailsUiEvents()
    data class UpdateScore(val score: Int) : DetailsUiEvents()
}