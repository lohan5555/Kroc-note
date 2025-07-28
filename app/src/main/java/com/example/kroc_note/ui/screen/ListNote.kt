package com.example.kroc_note.ui.screen


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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kroc_note.ui.data.Note
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.NoteState
import com.example.kroc_note.ui.data.NoteViewModel
import com.example.kroc_note.ui.data.SortType


@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
){
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(NoteEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add note")
            }
        },
        modifier = Modifier.padding(16.dp)
    ){ padding ->
        if(state.isAddingNote){
            AddNoteDialog(state = state, onEvent = onEvent)
        }

        /*LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = CenterVertically
                ) {
                    SortType.entries.forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(NoteEvent.SortNote(sortType))
                                },
                            verticalAlignment = CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(NoteEvent.SortNote(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.notes){note ->
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${note.titre} ${note.body} ${note.id}",
                            fontSize = 20.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(NoteEvent.DeleteNote(note))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete note")
                    }
                }
            }
        }*/
        ListNoteCard(state.notes, padding, navController)

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {  }
            composable("hello/{titre}") { backStackEntry ->
                val titre = backStackEntry.arguments?.getString("titre") ?: "pas de titre"
                HelloScreen(navController,titre) }
        }
    }
}


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
        .clickable { navController.navigate("hello/${titre}") }
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
                IconButton(onClick = {navController.navigate("hello/$titre")},
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
    NoteCard("Ma note","blablabla", navController)
}

