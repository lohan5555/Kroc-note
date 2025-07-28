package com.example.kroc_note.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kroc_note.ui.data.Note
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.NoteState
import com.example.kroc_note.ui.data.NoteViewModel
import com.example.kroc_note.ui.data.SortType
import com.example.kroc_note.ui.theme.KrocNoteTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kroc_note.ui.data.AppDatabase
import com.example.kroc_note.ui.data.NoteDao

import com.example.kroc_note.ui.screen.HelloScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.selects.select
import java.util.SortedMap

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes.db"
        ).build()
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrocNoteTheme {
                val state by viewModel.state.collectAsState()
                NoteScreen(state = state , onEvent = viewModel::onEvent)
            }
        }
    }
}
/*
@Composable
fun KrocNoteApp(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home"){
        composable("home") { PageAccueilScreen(navController) }
        composable("hello") { HelloScreen(navController) }
        composable("add") { AddNoteScreen(navController, noteViewModel) }
    }
}*/

//il faut un composant PageAccueilScreen qui appel NoteScreen(qui s'occupe déjà d'appeler formAddNote)

//PAS TOUCHE

@Composable
fun ListNoteCard(notes: List<Note>, paddingValues: PaddingValues, navController: NavHostController){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes) { note ->
            NoteCard(note.titre, note.body, navController)
        }
    }
}

@Preview
@Composable
fun PreviewListNoteCard(){
    val navController = rememberNavController()
    val paddingValues = PaddingValues()
    val notes = listOf(
        Note(1, "Note 1", "Contenu 1"),
        Note(2, "Note 2", "Contenu 2"),
        Note(3, "Note 3", "Contenu 3"),
        Note(4, "Note 4", "Contenu 4"),
        Note(5, "Note 5", "Contenu 5"),
        )
    ListNoteCard(notes, paddingValues, navController)
}


@Composable
fun NoteCard(titre: String, body: String, navController: NavHostController){
    Box(modifier = Modifier
        .padding(8.dp)
        .size(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.primary)
        .clickable { navController.navigate("hello") }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = titre, color = MaterialTheme.colorScheme.onPrimary)
            Text(text = body, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {navController.navigate("hello")},
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "aller à hello",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewNoteCard(){
    val navController = rememberNavController()
    NoteCard("Ma note","blablabla",navController)
}

