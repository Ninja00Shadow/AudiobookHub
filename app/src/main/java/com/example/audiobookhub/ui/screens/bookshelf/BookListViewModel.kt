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
    var books = repository.getBooks()


    fun onUiEvents(event: UiEvents) {
        when(event) {
            UiEvents.LoadBooks -> {
                loadBooks()
            }
            is UiEvents.SelectAudiobook -> {
                changeCurrentBook(event.book)
            }
        }
    }

    private fun loadBooks() {
        books = repository.getBooks()
    }

    private fun changeCurrentBook(book: AudioBook) {
        val editor = sharedPreferences.edit()
        editor.putString("currentBook", book.chapterFolderName)
        editor.apply()
    }
}


sealed class UiEvents {
    data object LoadBooks : UiEvents()
    data class SelectAudiobook(val book: AudioBook) : UiEvents()
}