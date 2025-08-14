package com.example.kroc_note.ui.data.bddClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kroc_note.ui.data.type.CouleurNote
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
data class File(
    @PrimaryKey(autoGenerate = true) val idFile: Int = 0,
    val name: String,
    val path: String,
    val color: CouleurNote
)



