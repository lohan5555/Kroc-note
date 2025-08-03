package com.example.kroc_note.ui.screen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
    private val viewModel by viewModels<NoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T: ViewModel> create(modelClass: Class<T>): T{
                    return NoteViewModel(db.noteDao()) as T
                }
            }
        }
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrocNoteTheme {
                val navController = rememberNavController()
                val state by viewModel.state.collectAsState()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        NoteScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            navController = navController
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
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}