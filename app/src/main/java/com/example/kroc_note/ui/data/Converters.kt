package com.example.kroc_note.ui.data.bddClass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.kroc_note.ui.data.type.TypeMedia
import com.example.kroc_note.ui.data.type.CouleurNote
import java.time.Instant

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


//ce fichier est nécessaire car Room ne gère que des types simples : String, Int, Long, Double, etc.
class Converters {
    //-----------------pour les couleurs----------------------------
    @TypeConverter
    fun fromCouleur(couleur: CouleurNote): String {
        return couleur.name
    }

    @TypeConverter
    fun toCouleur(nom: String): CouleurNote {
        return CouleurNote.valueOf(nom)
    }

    //-----------------pour les dates----------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli().toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return Instant.ofEpochMilli(dateString.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    //-----------------pour le type TypeMedia----------------------------
    @TypeConverter
    fun fromTypeMedia(type: TypeMedia): String = type.name

    @TypeConverter
    fun toTypeMedia(value: String): TypeMedia = TypeMedia.valueOf(value)

}
