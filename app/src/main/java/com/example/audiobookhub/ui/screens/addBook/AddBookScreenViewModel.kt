package com.example.audiobookhub.ui.screens.addBook

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audiobookhub.data.UriPathFinder
import com.example.audiobookhub.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddBookScreenViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    var fileType = mutableStateOf<FileTypes>(FileTypes.Mp3)

    var fileUris = mutableStateListOf<Uri>()

    var filePaths = mutableStateListOf<String>()

    var newBook = mutableStateOf(NewBook("", "", "", ""))

    private var uriPathFinder = UriPathFinder()

    private fun onFilePathChanged(uris: List<Uri>){
        fileUris.clear()
        fileUris.addAll(uris)
        viewModelScope.launch {
            changeUriToPath(uris)
        }

        Log.d("AddBookScreenViewModel", "onFilePathChanged: $filePaths")
    }

    private fun changeUriToPath(uris: List<Uri>){
        filePaths.clear()
        for (uri in uris){
            val path = uriPathFinder.getPath(context, uri)
            if (path != null) filePaths.add(path)
        }
    }

    fun onUiEvents(event: AddBookUiEvents) {
        when(event) {
            is AddBookUiEvents.ChangeFilePath -> {
                onFilePathChanged(event.uris)
            }
            is AddBookUiEvents.ChangeTitle -> {
                newBook.value = newBook.value.copy(title = event.title)
            }
            is AddBookUiEvents.ChangeAuthor -> {
                newBook.value = newBook.value.copy(author = event.author)
            }
            is AddBookUiEvents.ChangeNarrator -> {
                newBook.value = newBook.value.copy(narrator = event.narrator)
            }
            is AddBookUiEvents.ChangeDescription -> {
                newBook.value = newBook.value.copy(description = event.description)
            }
            is AddBookUiEvents.ChangeFileType -> {
                fileType.value = event.fileType
            }
            is AddBookUiEvents.SaveBook -> {
                saveBook()
            }
        }

    }

    private fun saveBook() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (fileType.value == FileTypes.Zip){
                    val newBookZip = newBook.value.getBookZip(filePaths[0])
                    bookRepository.addBook(newBookZip)
                } else {
                    val newBookMp3 = newBook.value.getBookMp3(filePaths)
                    bookRepository.addBook(newBookMp3)
                }
            }
        }
    }

}

sealed class AddBookUiEvents {
    data object SaveBook: AddBookUiEvents()
    data class ChangeFilePath(val uris: List<Uri>): AddBookUiEvents()
    data class ChangeTitle(val title: String): AddBookUiEvents()
    data class ChangeAuthor(val author: String): AddBookUiEvents()
    data class ChangeNarrator(val narrator: String): AddBookUiEvents()
    data class ChangeDescription(val description: String): AddBookUiEvents()
    data class ChangeFileType(val fileType: FileTypes): AddBookUiEvents()
}

sealed class FileTypes {
    data object Zip: FileTypes()
    data object Mp3: FileTypes()
}

data class NewBook(
    val title: String,
    val author: String,
    val narrator: String,
    val description: String,
) {
    fun getBookZip(filePath: String): NewBookZip {
        return NewBookZip(
            zipFilePath = filePath,
            title = title,
            author = author,
            narrator = narrator,
            description = description,
        )
    }

    fun getBookMp3(filePaths: List<String>): NewBookMp3 {
        return NewBookMp3(
            mp3FilePaths = filePaths,
            title = title,
            author = author,
            narrator = narrator,
            description = description,
        )
    }
}

data class NewBookZip(
    val zipFilePath: String,
    val title: String,
    val author: String,
    val narrator: String,
    val description: String,
)

data class NewBookMp3(
    val mp3FilePaths: List<String>,
    val title: String,
    val author: String,
    val narrator: String,
    val description: String,
)