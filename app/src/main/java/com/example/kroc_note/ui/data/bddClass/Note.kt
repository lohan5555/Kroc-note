package com.example.kroc_note.ui.data.bddClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kroc_note.ui.data.type.CouleurNote
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val idNote: Int = 0,
    val titre: String,
    val body: String,
    val couleur: CouleurNote,
    val dateDerniereModification: Long,
    val dateCreation: Long,
)



