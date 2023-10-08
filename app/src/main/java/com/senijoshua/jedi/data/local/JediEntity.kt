package com.senijoshua.jedi.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representation of a Jedi as columns of the Jedi table.
 */
@Entity(tableName = "jedi")
data class JediEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gender: String,
    val height: String,
    val mass: String,
    @ColumnInfo(name = "hair_color")
    val hairColor: String,
    @ColumnInfo(name = "skin_color")
    val skinColor: String,
    @ColumnInfo(name = "eye_color")
    val eyeColor: String
)

