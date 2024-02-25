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


    fun onUiEvents(event: ShelfUiEvents) {
        when(event) {
            ShelfUiEvents.LoadBooks -> {
                loadBooks()
            }
            is ShelfUiEvents.SelectAudiobook -> {
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


sealed class ShelfUiEvents {
    data object LoadBooks : ShelfUiEvents()
    data class SelectAudiobook(val book: AudioBook) : ShelfUiEvents()
}