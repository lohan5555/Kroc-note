package com.example.kroc_note.ui.screen


import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.example.kroc_note.R
import com.example.kroc_note.ui.data.EditFolderDialog
import com.example.kroc_note.ui.data.FolderEvent
import com.example.kroc_note.ui.data.FolderState
import com.example.kroc_note.ui.data.bddClass.Folder
import com.example.kroc_note.ui.data.type.SortType
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    state: NoteState,
    stateFolder: FolderState,
    path: String,
    onEventNote: (NoteEvent) -> Unit,
    onEventFolder: (FolderEvent) -> Unit,
    navController: NavController,
    onToggleTheme: () -> Unit,
    isDark: Boolean
){

    //on ne garde que les notes et les dossiers qui ont le bon chemin
    val notes = state.notes.filter { it.path == path }
    val folders = stateFolder.folders.filter { it.path == path }

    var noteSelect by remember { mutableStateOf(setOf<Int>()) }
    var folderSelect by remember { mutableStateOf(setOf<Int>()) }

    var recherche by remember { mutableStateOf(false) }
    var filtre by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            var expanded by remember { mutableStateOf(false) } //indique si le dropDownMenu est ouvert
            val titreAppBar: String
            if(path == "home") {
                titreAppBar = "Kroc-Note"
            }else if(path == "corbeille"){
                titreAppBar = "Corbeille"
            }else{
                titreAppBar = path.substringAfterLast('/')
            }

            TopAppBar(
                title = { Text(titreAppBar) },
                navigationIcon = {
                    if(path != "home"){
                        IconButton(onClick = {
                            val parentPath = path.substringBeforeLast('/', "home")
                            val encodedParentPath = URLEncoder.encode(parentPath, StandardCharsets.UTF_8.toString())
                            navController.navigate("NoteScreen/$encodedParentPath") {
                                popUpTo("NoteScreen/$encodedParentPath") { inclusive = true }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "retour arrière")
                        }

                    }
                },
                actions = {
                    if(noteSelect.isEmpty() && folderSelect.isEmpty()){
                        if(path != "home" && path != "corbeille"){
                            IconButton(onClick = {
                                onEventFolder(FolderEvent.ShowDialog)
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "edit folder")
                            }
                        }

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
                            if(path != "corbeille"){
                                DropdownMenuItem(
                                    text = { Text("Corbeille") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("NoteScreen/corbeille")
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Rechercher") },
                                onClick = {
                                    recherche = !recherche
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
                    }else if(folderSelect.isNotEmpty() && path != "corbeille"){
                        IconButton(onClick = {
                            folderSelect = emptySet()
                            println("supp folder") //TODO
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "supprimer un dossier")
                        }
                    }else{   //if noteSelect.isNotEmpty
                        if(path == "corbeille"){
                            IconButton(onClick = {
                                onEventNote(NoteEvent.SetManyPath(noteSelect.toList(), "home"))
                                noteSelect = emptySet()
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "restaurer les notes")
                            }
                            IconButton(onClick = {
                                onEventNote(NoteEvent.DeleteManyNoteById(noteSelect.toList()))
                                noteSelect = emptySet()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "supprimer définitivement")
                            }
                        }else{
                            Icon(Icons.Default.Share, contentDescription = "partager")
                            IconButton(onClick = {
                                onEventNote(NoteEvent.SetManyPath(noteSelect.toList(), "corbeille"))
                                noteSelect = emptySet()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "mettre à la corbeille")
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            var expandedAdd by remember { mutableStateOf(false) }
            Box{
                if (path != "corbeille"){
                    FloatingActionButton(
                        onClick = { expandedAdd = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add note")
                    }
                }


                DropdownMenu(
                    expanded = expandedAdd,
                    onDismissRequest = { expandedAdd = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Créer une note") },
                        onClick = {
                            expandedAdd = false
                            onEventNote(
                                NoteEvent.CreateNote(path = path)
                            )

                            //var newId = 0
                            //println("newId: $newId")  //je n'arrive pas à récuperer le nouvel id
                            //navController.navigate("Detail/$newId")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Créer un dossier") },
                        onClick = {
                            expandedAdd = false
                            val folderName = folderNewName(folders, "Dossier")
                            onEventFolder(FolderEvent.SetName(folderName))
                            onEventFolder(FolderEvent.SetPath(path))
                            onEventFolder(FolderEvent.SetDateCreation(System.currentTimeMillis()))
                            onEventFolder(FolderEvent.SetDateModification(System.currentTimeMillis()))
                            onEventFolder(FolderEvent.SaveFolder)
                        }
                    )

                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){ padding ->
        if(stateFolder.isEditFolder){
            EditFolderDialog(path = path, state = stateFolder, onEvent = onEventFolder)
        }
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            noteSelect = emptySet()
                            folderSelect = emptySet()
                        }
                    )
                }
        ) {
            Column{
                if(recherche){
                    Column {

                        Row(modifier = Modifier.padding(start = 10.dp)) {
                            TextField(
                                value = filtre,
                                onValueChange = {
                                    filtre = it
                                },
                                placeholder = { Text("\uD83D\uDD0D Rechercher") },
                            )
                            IconButton(onClick = { filtre = "" }){
                                Icon(Icons.Default.Close, contentDescription = "reset la zone de saisie")
                            }
                            IconButton(
                                onClick = { recherche = false }
                            ){
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "ferme le menu des filtres")
                            }
                        }

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
                                                    onEventNote(NoteEvent.SortNote(sortType))
                                                },
                                            verticalAlignment = CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = state.sortType == sortType,
                                                onClick = {
                                                    onEventNote(NoteEvent.SortNote(sortType))
                                                }
                                            )
                                            Text(text = matchText(sortType.name))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ListItemCard(
                    notes = notes,
                    folders = folders,
                    navController = navController,
                    noteSelect = noteSelect,
                    folderSelect = folderSelect,
                    filtre = filtre,
                    onToggleSelectionNote = { id ->
                        noteSelect = if (noteSelect.contains(id)){
                            noteSelect - id
                        } else {
                            noteSelect + id
                        }
                    },
                    onToggleSelectionFolder = { id ->
                        folderSelect = if (folderSelect.contains(id)){
                            folderSelect - id
                        } else {
                            folderSelect + id
                        }
                    },
                    isDark = isDark,
                    path = path,
                    snackbarHostState = snackbarHostState
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

fun folderNewName(folders: List<Folder>, name: String):String{
    var newName = name
    var cpt = 1
    val existingNames = folders.map { it.name }.toSet()

    while (existingNames.contains(newName)) {
        newName = "$name($cpt)"
        cpt++
    }
    return newName
}

//type scellé pour pouvoir creer une liste de Folder et de Note
sealed class ItemUI {
    data class NoteItem(val note: Note) : ItemUI()
    data class FolderItem(val folder: Folder) : ItemUI()
}

@Composable
fun ListItemCard(
    notes: List<Note>,
    folders: List<Folder>,
    navController: NavController,
    noteSelect: Set<Int>,
    folderSelect: Set<Int>,
    filtre: String,
    onToggleSelectionNote: (Int) -> Unit,
    onToggleSelectionFolder: (Int) -> Unit,
    isDark: Boolean,
    path: String,
    snackbarHostState: SnackbarHostState
) {

    val notesFilter = filtreNotes(notes, filtre)

    val combinedList: List<ItemUI> =
        (folders.map { ItemUI.FolderItem(it) } + notesFilter.map { ItemUI.NoteItem(it) })

    if (combinedList.isEmpty()) {
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
            items(combinedList) { item ->
                when (item){
                    is ItemUI.NoteItem -> NoteCard(
                        note = item.note,
                        path = path,
                        navController = navController,
                        isSelected = noteSelect.contains(item.note.idNote),
                        onToggleSelectionNote = onToggleSelectionNote,
                        selectedNote = noteSelect,
                        selectedFolder = folderSelect,
                        snackbarHostState = snackbarHostState
                    )
                    is ItemUI.FolderItem -> FolderCard(
                        folder = item.folder,
                        path = path,
                        navController = navController,
                        isSelected = folderSelect.contains(item.folder.idFolder),
                        selectedNote = noteSelect,
                        selectedFolder = folderSelect,
                        isDark = isDark,
                        onToggleSelectionFolder = onToggleSelectionFolder
                    )
                }

            }
        }
    }
}

//pour la barre de recherche
fun filtreNotes(notes: List<Note>, filtre: String): List<Note> {
    return if (filtre.isBlank()) {
        notes
    } else {
        notes.filter { note ->
            note.titre.contains(filtre, ignoreCase = true)
        }
    }
}



@Composable
fun NoteCard(
    note: Note,
    navController: NavController,
    path: String,
    isSelected: Boolean,
    onToggleSelectionNote: (Int) -> Unit,
    selectedNote: Set<Int>,
    selectedFolder: Set<Int>,
    snackbarHostState: SnackbarHostState
){
    val scope = rememberCoroutineScope()
    val couleurAffichage: Color = note.couleur.color
    val borderColor = if (isSelected) MaterialTheme.colorScheme.surfaceBright else Color.Transparent

    Box(modifier = Modifier
        .padding(8.dp)
        .size(200.dp)
        //.clip(RoundedCornerShape(16.dp))
        .background(couleurAffichage)
        .border(width = 3.dp, color = borderColor)//, shape = RoundedCornerShape(16.dp))
        .pointerInput(selectedNote) {
            detectTapGestures(
                onTap = {
                    if(selectedNote.isEmpty() && selectedFolder.isEmpty()){
                        if(path != "corbeille"){
                            navController.navigate("Detail/${note.idNote}")
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Impossible d’ouvrir une note depuis la corbeille",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }else if(selectedNote.isNotEmpty() && selectedFolder.isEmpty()){
                        onToggleSelectionNote(note.idNote)
                    }
                },
                onLongPress = {
                    if (selectedFolder.isEmpty()){
                        onToggleSelectionNote(note.idNote)
                    }
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


@Composable
fun FolderCard(
    folder: Folder,
    path: String,
    navController: NavController,
    isSelected: Boolean,
    selectedNote: Set<Int>,
    selectedFolder: Set<Int>,
    isDark: Boolean,
    onToggleSelectionFolder: (Int) -> Unit
){
    val couleurAffichage: Color = folder.color.color
    val borderColor = if (isSelected) MaterialTheme.colorScheme.surfaceBright else Color.Transparent

    Box(modifier = Modifier
        .padding(8.dp)
        .size(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(couleurAffichage)
        .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
        .pointerInput(selectedFolder) {
            detectTapGestures(
                onTap = {
                    if(selectedFolder.isEmpty() && selectedNote.isEmpty()){
                        navController.navigate("NoteScreen/${Uri.encode(path + "/" + folder.name)}")
                    }else if(selectedFolder.isNotEmpty() && selectedNote.isEmpty()){
                        onToggleSelectionFolder(folder.idFolder)
                    }
                },
                onLongPress = {
                    if(selectedNote.isEmpty()) {
                        onToggleSelectionFolder(folder.idFolder)
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if(isDark){
                Image(
                    painter = painterResource(id = R.drawable.folder_fonce),
                    contentDescription = ("img folder foncé")
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.folder_claire),
                    contentDescription = ("img folder clair")
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = folder.name, color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp, maxLines = 1)
        }
    }
}