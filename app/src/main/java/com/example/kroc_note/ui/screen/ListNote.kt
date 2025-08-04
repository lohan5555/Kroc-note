package com.example.kroc_note.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.NoteState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.selects.select


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    navController: NavController,
    onToggleTheme: () -> Unit
){
    var noteSelect by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            TopAppBar(
                title = { Text("Kroc-Note") },
                actions = {
                    if(noteSelect.isEmpty()){
                        IconButton(onClick = {expanded = true}) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu déroulant")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Calendrier") },
                                onClick = {
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Rechercher") },
                                onClick = {
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("Thème") },
                                onClick = {
                                    onToggleTheme()
                                    expanded = false
                                }
                            )
                        }
                    }else{
                        Icon(Icons.Default.Share, contentDescription = "partager")
                        IconButton(onClick = {
                            onEvent(NoteEvent.DeleteManyNoteById(noteSelect.toList()))
                            noteSelect = emptySet()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "supprimer")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(NoteEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add note")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){ padding ->
        if(state.isAddingNote){
            AddNoteDialog(state = state, onEvent = onEvent)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (noteSelect.isNotEmpty()) {
                                noteSelect = emptySet()
                                println(noteSelect)
                            }
                        }
                    )
                }
        ) {
            ListNoteCard(
                notes = state.notes,
                paddingValues = padding,
                navController = navController,
                noteSelect = noteSelect,
                onToggleSelection = { id ->
                    noteSelect = if (noteSelect.contains(id)){
                        noteSelect - id
                    } else {
                        noteSelect + id
                    }
                    println(noteSelect)
                }
            )
        }
    }
}

@Composable
fun ListNoteCard(
    notes: List<Note>,
    paddingValues: PaddingValues,
    navController: NavController,
    noteSelect: Set<Int>,
    onToggleSelection: (Int) -> Unit
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                navController = navController,
                isSelected = noteSelect.contains(note.idNote),
                onToggleSelection = onToggleSelection,
                selectedNote = noteSelect
            )
        }
    }
}

/*
@Preview
@Composable
fun PreviewListNoteCard(){
    val navController = rememberNavController()
    val paddingValues = PaddingValues()
    val notes = listOf(
        Note(1, "Note 1", "Contenu 1", CouleurNote.Rose, LocalDateTime.now()),
        Note(2, "Note 2", "Contenu 2", CouleurNote.Rose),
        Note(3, "Note 3", "Contenu 3", CouleurNote.Rose),
        Note(4, "Note 4", "Contenu 4", CouleurNote.Rose),
        Note(5, "Note 5", "Contenu 5", CouleurNote.Rose),
    )
    ListNoteCard(notes, paddingValues, navController)
}*/


@Composable
fun NoteCard(
    note: Note,
    navController: NavController,
    isSelected: Boolean,
    onToggleSelection: (Int) -> Unit,
    selectedNote: Set<Int>
){
    val couleurAffichage: Color = note.couleur.color
    val borderColor = if (isSelected) MaterialTheme.colorScheme.surfaceBright else Color.Transparent

    Box(modifier = Modifier
        .padding(8.dp)
        .size(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(couleurAffichage)
        .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
        .pointerInput(selectedNote) {
            detectTapGestures(
                onTap = {
                    println("on a ça: $selectedNote")
                    if (selectedNote.isNotEmpty()) {
                        onToggleSelection(note.idNote)
                        println("test 1")
                    } else {
                        navController.navigate("Detail/${note.idNote}")
                        println("test 2")
                    }
                },
                onLongPress = {
                    onToggleSelection(note.idNote)
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = note.titre, color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp, maxLines = 1)
            Text(text = note.body, color = MaterialTheme.colorScheme.onPrimary, maxLines = 4)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "aller à Detail",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}