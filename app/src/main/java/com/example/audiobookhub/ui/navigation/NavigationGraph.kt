package com.example.audiobookhub.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.audiobookhub.ui.screens.addBook.AddBookScreen
import com.example.audiobookhub.ui.screens.addBook.AddBookScreenViewModel
import com.example.audiobookhub.ui.screens.bookshelf.BookListScreen
import com.example.audiobookhub.ui.screens.bookshelf.BookListViewModel
import com.example.audiobookhub.ui.screens.bookshelf.ShelfUiEvents
import com.example.audiobookhub.ui.screens.details.AudiobookDetailsScreen
import com.example.audiobookhub.ui.screens.details.AudiobookDetailsViewModel
import com.example.audiobookhub.ui.screens.details.DetailsUiEvents
import com.example.audiobookhub.ui.screens.playerScreen.AudioViewModel
import com.example.audiobookhub.ui.screens.playerScreen.PlayerScreen
import com.example.audiobookhub.ui.screens.playerScreen.UIEvents
import com.example.audiobookhub.ui.screens.settings.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    playerViewModel: AudioViewModel,
    bookListViewModel: BookListViewModel,
    bookDetailsViewModel: AudiobookDetailsViewModel,
    addBookViewModel: AddBookScreenViewModel,
    startService: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Routes.AUDIO_PLAYER_SCREEN
    ) {
        composable(
            Routes.AUDIO_PLAYER_SCREEN
        ) {
            PlayerScreen(
                progress = playerViewModel.bookProgress,
                onProgress = {
                    playerViewModel.onUiEvents(UIEvents.SeekTo(it))
                },
                speed = playerViewModel.speed,
                onSpeedChange = {
                    playerViewModel.onUiEvents(UIEvents.SpeedChange(it))
                },
                chapterRemaining = playerViewModel.chapterRemainingTimeString,
                timeElapsed = playerViewModel.timeElapsedString,
                timeRemaining = playerViewModel.timeRemainingString,
                isPlaying = playerViewModel.isPlaying,
                audioBook = playerViewModel.audioBook,
                currentChapter = playerViewModel.currentSelectedAudio,
                onStart = {
                    playerViewModel.onUiEvents(UIEvents.PlayPause)
                    startService()
                },
                onForward = {
                    playerViewModel.onUiEvents(UIEvents.Forward)
                    Log.d("TAG", "onForward: ")
                },
                onBackward = { playerViewModel.onUiEvents(UIEvents.Backward) },
                onChapterClick = {
                    playerViewModel.onUiEvents(UIEvents.ChangeChapter(it))
                    startService()
                },
                onMore = {
                    navController.navigate("${Routes.AUDIO_BOOK_DETAILS_SCREEN}/${playerViewModel.audioBook.chapterFolderName}")
                }
            )
        }

        composable(
            Routes.SETTINGS_SCREEN
        ) {
            SettingsScreen()

        }

        composable(
            Routes.BOOK_LIST_SCREEN
        ) {
            bookListViewModel.onUiEvents(ShelfUiEvents.LoadBooks)
            BookListScreen(
                books = bookListViewModel.books,
                onBookSelected = {
                    bookListViewModel.onUiEvents(ShelfUiEvents.SelectAudiobook(it))
                    playerViewModel.bookChanged()
                    navController.navigate(Routes.AUDIO_PLAYER_SCREEN)
                },
                onDetailsClick = {
                    navController.navigate("${Routes.AUDIO_BOOK_DETAILS_SCREEN}/${it.chapterFolderName}")
                },
                currentBook = playerViewModel.getAudioBookOrNull(),
                isPlaying = playerViewModel.isPlaying,
                onPlayPause = {
                    playerViewModel.onUiEvents(UIEvents.PlayPause)
                    startService()
                },
                timeRemaining = playerViewModel.timeRemainingString,
                onBottomPlayerClick = {
                    navController.navigate(Routes.AUDIO_PLAYER_SCREEN)
                },
                onAddBook = {
                    navController.navigate(Routes.ADD_BOOK_SCREEN)
                }
            )
        }

        composable(
            "${Routes.AUDIO_BOOK_DETAILS_SCREEN}/{${Routes.AUDIO_BOOK_FILE}}",
            arguments = listOf(
                navArgument(Routes.AUDIO_BOOK_FILE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString(Routes.AUDIO_BOOK_FILE)
            LaunchedEffect(key1 = fileName) {
                bookDetailsViewModel.onUiEvents(DetailsUiEvents.LoadAudioBook(fileName!!))
            }

            AudiobookDetailsScreen(
                audiobook = bookDetailsViewModel.audioBook,
                formattedDuration = bookDetailsViewModel.formatDuration(),
                score = bookDetailsViewModel.audioBook.score,
                onScoreChanged = {
                    bookDetailsViewModel.onUiEvents(DetailsUiEvents.UpdateScore(it))
                },
                goBack = {
                    navController.popBackStack()
                },
                currentAudioBook = playerViewModel.getAudioBookOrNull(),
                isPlaying = playerViewModel.isPlaying,
                progress = playerViewModel.bookProgress,
                onPlayPause = {
                    playerViewModel.onUiEvents(UIEvents.PlayPause)
                    startService()
                },
                timeRemaining = playerViewModel.timeRemainingString,
                onPlayerClick = {
                    navController.navigate(Routes.AUDIO_PLAYER_SCREEN)
                }
            )
        }

        composable(
            Routes.ADD_BOOK_SCREEN
        ) {
            AddBookScreen(
                newBook = addBookViewModel.newBook.value,
                fileType = addBookViewModel.fileType.value,
                filesPath = addBookViewModel.filePaths,
                goBack = {
                    navController.popBackStack()
                },
                onUiEvents = {
                    addBookViewModel.onUiEvents(it)
                },
            )
        }

    }
}