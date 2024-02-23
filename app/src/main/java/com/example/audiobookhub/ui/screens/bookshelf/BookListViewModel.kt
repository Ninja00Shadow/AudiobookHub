package com.example.audiobookhub.ui.screens.bookshelf

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    val books = repository.getBooks()


    fun onUiEvent(event: UiEvents, book: AudioBook) {
        when(event) {
            is UiEvents.SelectAudiobook -> {
                changeCurrentBook(book)
            }
        }
    }

    private fun changeCurrentBook(book: AudioBook) {
        val editor = sharedPreferences.edit()
        editor.putString("currentBook", book.chapterFolderName)
        editor.apply()
    }
}


sealed class UiEvents {
    data object SelectAudiobook : UiEvents()
}