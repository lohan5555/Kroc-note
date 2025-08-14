package com.example.kroc_note.ui.screen

import FileViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import com.example.kroc_note.ui.data.NoteViewModel
import com.example.kroc_note.ui.theme.KrocNoteTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.kroc_note.ui.data.AppDatabase
import com.example.kroc_note.ui.theme.ThemePreferences
import com.example.kroc_note.ui.theme.ThemeViewModel
import com.example.kroc_note.ui.theme.ThemeViewModelFactory

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes.db"
        )
            .fallbackToDestructiveMigration(true)  //pour détruire et recreer la bdd quand elle est modifier (bien prod, pas en déploiment)
            .build()
    }
    private val noteViewModel by viewModels<NoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T: ViewModel> create(modelClass: Class<T>): T{
                    return NoteViewModel(db.noteDao()) as T
                }
            }
        }
    )

    private val fileViewModel: FileViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T: ViewModel> create(modelClass: Class<T>): T{
                    return FileViewModel(db.fileDao()) as T
                }
            }
        }
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = ThemePreferences(applicationContext)
        val themeViewModel = ViewModelProvider(
            this,
            ThemeViewModelFactory(preferences)
        )[ThemeViewModel::class.java]

        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            KrocNoteTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false //pour pouvoir utiliser mes propre couleur
            ) {
                val navController = rememberNavController()
                val stateNote by noteViewModel.state.collectAsState()
                val stateFile by fileViewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    fileViewModel.chargerDossiers()
                }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        NoteScreen(
                            state = stateNote,
                            stateFile = stateFile,
                            onEvent = noteViewModel::onEvent,
                            navController = navController,
                            onToggleTheme = { themeViewModel.toggleTheme() },
                            path = "home/"
                        )
                    }
                    composable(
                        "Detail/{id}",
                        arguments = listOf(navArgument("id"){type = NavType.IntType})
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: -1
                        DetailScreen(
                            navController = navController,
                            id = id,
                            state = stateNote,
                            onEvent = noteViewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}