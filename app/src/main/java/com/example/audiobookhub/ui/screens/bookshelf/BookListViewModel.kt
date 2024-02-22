package com.example.audiobookhub.ui.screens.bookshelf

import androidx.lifecycle.ViewModel
import com.example.audiobookhub.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {
    val books = repository.getBooks()
}


sealed class UiEvents {
    data object NavigateToPlayer : UiEvents()
}