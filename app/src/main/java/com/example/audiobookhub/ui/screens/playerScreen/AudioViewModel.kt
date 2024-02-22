package com.example.audiobookhub.ui.screens.playerScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.data.model.Chapter
import com.example.audiobookhub.data.repository.BookRepository
import com.example.audiobookhub.player.service.AudioServiceHandler
import com.example.audiobookhub.player.service.AudioState
import com.example.audiobookhub.player.service.PlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val bookDummy = AudioBook(
    "",
    "",
    "",
    "",
    3,
    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    0,
    "",
    emptyList(),
)

private val chapterDummy = Chapter(
    "".toUri(),
    "",
    1,
    0
)

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AudioViewModel @Inject constructor(
    private val audioServiceHandler: AudioServiceHandler,
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var chapterDuration by mutableLongStateOf(0L)
    var bookProgress by mutableFloatStateOf(0f)
    var timeElapsedString by mutableStateOf("00:00")
    var timeRemainingString by mutableStateOf("00:00")
    var chapterRemainingTimeString by mutableStateOf("00:00")
    var speed by mutableFloatStateOf(1f)
    var isPlaying by mutableStateOf(false)
    var currentSelectedAudio by mutableStateOf(chapterDummy)
    var audioBook by mutableStateOf(bookDummy)

    private val bookDuration: Long
    private val chapterTimeStamps = mutableListOf<Long>()

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadAudioData()

        bookDuration = audioBook.chapters.fold(0L) { acc, chapter -> acc + chapter.duration }
        audioBook.chapters.fold(0L) { acc, chapter ->
            chapterTimeStamps.add(acc)
            acc + chapter.duration
        }
    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    AudioState.Initial -> _uiState.value = UIState.Initial
                    is AudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is AudioState.Playing -> isPlaying = mediaState.isPlaying
                    is AudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is AudioState.CurrentPlaying -> {
                        currentSelectedAudio = audioBook.chapters[mediaState.mediaItemIndex]
                    }

                    is AudioState.Ready -> {
                        chapterDuration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }


        }
    }

    private fun loadAudioData() {
        viewModelScope.launch {
            val audio = repository.getBookByName("Ogień Przebudzenia")
            audioBook = audio
            currentSelectedAudio = audio.chapters.first()
            setMediaItems()
        }
    }

    private fun setMediaItems() {
        audioBook.chapters.map { chapter ->
            MediaItem.Builder()
                .setUri(chapter.uri)
                .setMediaId(chapter.number.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setMediaType(MediaMetadata.MEDIA_TYPE_AUDIO_BOOK_CHAPTER)
                        .setWriter(audioBook.author)
                        .setAlbumArtist(audioBook.narrator)
                        .setDisplayTitle(audioBook.title)
                        .setSubtitle("Read by ${audioBook.narrator}")
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        bookProgress =
            if (currentProgress > 0) {
                val bookProgress = chapterTimeStamps[currentSelectedAudio.number - 1] + currentProgress
                ((bookProgress.toFloat() / bookDuration.toFloat()) * 100f)
            }
            else 0f
        timeElapsedString = formatTimeElapsed(currentProgress)
        timeRemainingString = formatTimeRemaining(currentProgress)
        chapterRemainingTimeString = formatChapterTimeLeft(currentProgress)
    }

    private fun formatTimeElapsed(duration: Long): String {
        val timeElapsed = chapterTimeStamps[currentSelectedAudio.number - 1] + duration

        val hours = TimeUnit.HOURS.convert(timeElapsed, TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(timeElapsed, TimeUnit.MILLISECONDS) % 60
        val seconds = TimeUnit.SECONDS.convert(timeElapsed, TimeUnit.MILLISECONDS) % 60
        return String.format("%d:%02d:%02d", hours, minutes, seconds)
    }

    private fun formatTimeRemaining(duration: Long): String {
        val timeRemaining = bookDuration - (chapterTimeStamps[currentSelectedAudio.number - 1] + duration)

        val hours = TimeUnit.HOURS.convert(timeRemaining, TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(timeRemaining, TimeUnit.MILLISECONDS) % 60
        val seconds = TimeUnit.SECONDS.convert(timeRemaining, TimeUnit.MILLISECONDS) % 60
        return String.format("%d:%02d:%02d", hours, minutes, seconds)
    }

    private fun formatChapterTimeLeft(duration: Long): String {
        val timeRemaining = chapterDuration - duration

        val minutes = TimeUnit.MINUTES.convert(timeRemaining, TimeUnit.MILLISECONDS)
        val seconds = TimeUnit.SECONDS.convert(timeRemaining, TimeUnit.MILLISECONDS) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun onUiEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            is UIEvents.PlayPause -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.PlayPause
                )
            }

            is UIEvents.SeekTo -> {
//                audioServiceHandler.onPlayerEvents(
//                    PlayerEvent.SeekTo,
//                    seekPosition = ((chapterDuration * uiEvents.position) / 100f).toLong()
//                )
                if (uiEvents.position > 0f) {
                    Log.d("AudioViewModel", "chapterTimeStamps: $chapterTimeStamps")
                    Log.d("AudioViewModel", "bookDuration: ${(bookDuration * uiEvents.position)/100f}")
                    val chapterIndex = chapterTimeStamps.indexOfLast { it < (bookDuration * uiEvents.position)/100f }
                    val chapterSeekPosition = (bookDuration * uiEvents.position)/100f - chapterTimeStamps[chapterIndex]

                    Log.d("AudioViewModel", "chapterIndex: $chapterIndex")
                    Log.d("AudioViewModel", "chapterSeekPosition: $chapterSeekPosition")

                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SeekToAndChange,
                        selectedAudioIndex = chapterIndex,
                        seekPosition = chapterSeekPosition.toLong()
                    )
                }
                else {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SeekToAndChange,
                        selectedAudioIndex = 0,
                        seekPosition = 0
                    )
                }
            }

            is UIEvents.ChangeChapter -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }

            is UIEvents.UpdateProgress -> {
//                audioServiceHandler.onPlayerEvents(
//                    PlayerEvent.UpdateProgress(
//                        uiEvents.newProgress
//                    )
//                )
//                bookProgress = uiEvents.newProgress
            }

            is UIEvents.SpeedChange -> {
                speed = uiEvents.speed
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.ChangeSpeed(uiEvents.speed)
                )
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            audioServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }

}

sealed class UIEvents {
    data object PlayPause : UIEvents()
    data class ChangeChapter(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    data object Backward : UIEvents()
    data object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
    data class SpeedChange(val speed: Float) : UIEvents()
}


sealed class UIState {
    data object Initial : UIState()
    data object Ready : UIState()
}