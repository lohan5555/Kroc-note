package com.example.kroc_note.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kroc-Note détail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Note note =
            Note(id)
        }
    }
}

@Preview
@Composable
fun PreviewDetailScreen(){
    val navController = rememberNavController()
    DetailScreen(navController, 6)
}


@Composable
fun Note(id: Int){
    Text(text = "Hello 👋, note n°$id", style = MaterialTheme.typography.headlineMedium)
}