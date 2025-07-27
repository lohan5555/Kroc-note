package com.example.kroc_note.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kroc_note.ui.theme.KrocNoteTheme

import com.example.kroc_note.ui.screen.HelloScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrocNoteApp()
        }
    } 
}

@Composable
fun KrocNoteApp(){
    val navContoller = rememberNavController()
    NavHost(navContoller, startDestination = "home"){
        composable("home") { PageAccueilScreen(navContoller) }
        composable("hello") { HelloScreen(navContoller) }
    }
}

@OptIn(ExperimentalMaterial3Api::class) //pour TopAppBar
@Composable
fun PageAccueilScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kroc-Note") },
                actions = {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                }
            )
        }
    ){
        padding ->
        ListNoteCard(padding, navController)
    }
}

@Composable
fun ListNoteCard(paddingValues: PaddingValues,navController: NavHostController){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(17) {index ->
            NoteCard("Ma note","blablabla",navController)
        }
    }
}

@Preview
@Composable
fun PreviewListNoteCard(){
    val navController = rememberNavController()
    val paddingValues = PaddingValues()
    ListNoteCard(paddingValues, navController)
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

