package com.example.audiobookhub.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.audiobookhub.ui.screens.bookshelf.BookListScreen
import com.example.audiobookhub.ui.screens.bookshelf.BookListViewModel
import com.example.audiobookhub.ui.screens.bookshelf.UiEvents
import com.example.audiobookhub.ui.screens.details.AudiobookDetailsScreen
import com.example.audiobookhub.ui.screens.playerScreen.AudioViewModel
import com.example.audiobookhub.ui.screens.playerScreen.PlayerScreen
import com.example.audiobookhub.ui.screens.playerScreen.UIEvents
import com.example.audiobookhub.ui.screens.settings.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    playerViewModel: AudioViewModel,
    bookListViewModel: BookListViewModel,
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
                },
                onForward = {
                    playerViewModel.onUiEvents(UIEvents.Forward)
                    Log.d("TAG", "onForward: ")
                },
                onBackward = { playerViewModel.onUiEvents(UIEvents.Backward) },
                onItemClick = {
                    playerViewModel.onUiEvents(UIEvents.ChangeChapter(it))
                    startService()
                },
                onMore = {
                    navController.navigate(Routes.AUDIO_BOOK_DETAILS_SCREEN)
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
            bookListViewModel.onUiEvents(UiEvents.LoadBooks)
            BookListScreen(
                books = bookListViewModel.books,
                onBookSelected = {
                    bookListViewModel.onUiEvents(UiEvents.SelectAudiobook(it))
//                    playerViewModel.loadAudiobook()
                    playerViewModel.bookChanged()
                    navController.navigate(Routes.AUDIO_PLAYER_SCREEN)
                },
                currentBook = playerViewModel.audioBook,
                isPlaying = playerViewModel.isPlaying,
                onPlayPause = {
                    playerViewModel.onUiEvents(UIEvents.PlayPause)
                },
                timeRemaining = playerViewModel.timeRemainingString,
                onBottomPlayerClick = {
                    navController.navigate(Routes.AUDIO_PLAYER_SCREEN)
                }
            )
        }

        composable(
            Routes.AUDIO_BOOK_DETAILS_SCREEN
        ) {
            AudiobookDetailsScreen(
                audiobook = playerViewModel.audioBook,
                formattedDuration = playerViewModel.formatDuration(),
                score = playerViewModel.audioBook.score,
                onScoreChanged = {

                },
                goBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}