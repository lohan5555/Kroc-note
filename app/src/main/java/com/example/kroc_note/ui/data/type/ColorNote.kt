package com.example.kroc_note.ui.data.type

import androidx.compose.ui.graphics.Color
import com.example.kroc_note.ui.theme.Pink40
import com.example.kroc_note.ui.theme.Pink80
import com.example.kroc_note.ui.theme.Purple40
import com.example.kroc_note.ui.theme.Purple80
import com.example.kroc_note.ui.theme.PurpleGrey40
import com.example.kroc_note.ui.theme.PurpleGrey80


//liste des couleurs possible pour une note
enum class CouleurNote(val color: Color) {
    Violet(Purple80),
    Gris(PurpleGrey80),
    Rose(Pink80),
    VioletFoncé(Purple40),
    GrisFoncé(PurpleGrey40),
    RoseFoncé(Pink40)
}
