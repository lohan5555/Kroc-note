package com.example.kroc_note.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.input.pointer.pointerInput
import com.example.kroc_note.ui.data.type.SortType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    navController: NavController,
    onToggleTheme: () -> Unit
){
    var noteSelect by remember { mutableStateOf(setOf<Int>()) }
    var recherche by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false) } //indique si le dropDownMenu est ouvert
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
                                    if(recherche){
                                        recherche = false
                                    }else{
                                        recherche = true
                                    }
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
                onEvent(NoteEvent.SetTitre("nouvelle note"))
                onEvent(NoteEvent.SetBody(""))
                onEvent(NoteEvent.SetDateCreation(System.currentTimeMillis()))
                onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
                onEvent(NoteEvent.SaveNote)


                var newId = 0
                //println("newId: $newId")  //je n'arrive pas à récuperer le nouvel id
                //navController.navigate("Detail/$newId")
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (noteSelect.isNotEmpty()) {
                                noteSelect = emptySet()
                            }
                        }
                    )
                }
        ) {
            Column(){
                if(recherche){
                    LazyColumn(
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
                                        Text(text = matchText(sortType.name))
                                    }
                                }
                            }
                        }
                    }
                }
                ListNoteCard(
                    notes = state.notes,
                    navController = navController,
                    noteSelect = noteSelect,
                    onToggleSelection = { id ->
                        noteSelect = if (noteSelect.contains(id)){
                            noteSelect - id
                        } else {
                            noteSelect + id
                        }
                    }
                )
            }
        }
    }
}

fun matchText(sort: String):String{
    return when (sort) {
        "RECENTE" -> "Récente"
        "ANCIENNE" -> "Ancienne"
        "A_Z" -> "A à Z"
        "Z_A" -> "Z à A"
        "MODIF_RECENTE" -> "Modification recente"
        else -> sort
    }
}

@Composable
fun ListNoteCard(
    notes: List<Note>,
    navController: NavController,
    noteSelect: Set<Int>,
    onToggleSelection: (Int) -> Unit
) {
    if (notes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Aucune note")
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
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
}

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
                    if (selectedNote.isNotEmpty()) {
                        onToggleSelection(note.idNote)
                    } else {
                        navController.navigate("Detail/${note.idNote}")
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