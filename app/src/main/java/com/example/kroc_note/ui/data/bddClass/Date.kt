package com.example.kroc_note.ui.data.bddClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = ["idNote"],
        childColumns = ["noteInt"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Date(
    @PrimaryKey(autoGenerate = true) val idDate: Int = 0,
    val date: LocalDateTime,
    val nom: String,
    val notificationActif: Boolean,
    val noteId: Int
)
