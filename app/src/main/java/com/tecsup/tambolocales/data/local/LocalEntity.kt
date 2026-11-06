package com.tecsup.tambolocales.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locales")
data class LocalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val direccion: String,
    val gerente: String,
    val diasAtencion: String,
    val horario: String,
    val latitud: Double,
    val longitud: Double
)
