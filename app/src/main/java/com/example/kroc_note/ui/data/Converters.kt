package com.example.kroc_note.ui.data.bddClass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.kroc_note.ui.theme.CouleurNote

import java.time.LocalDateTime
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
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString, formatter)
    }
}
