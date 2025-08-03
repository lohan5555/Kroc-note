package com.example.kroc_note.ui.data.bddClass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kroc_note.ui.data.TypeMedia

@Entity(
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = ["idNote"],
        childColumns = ["noteInt"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Media(
    @PrimaryKey(autoGenerate = true) val mediaId: Int = 0,
    val cheminAcces: String,
    val type: TypeMedia,
    val noteInt: Int
)
