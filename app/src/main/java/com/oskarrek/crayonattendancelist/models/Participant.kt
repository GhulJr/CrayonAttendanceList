package com.oskarrek.crayonattendancelist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name")    val firstName   : String,
    @ColumnInfo(name = "last_name")     val lastName    : String,
    @ColumnInfo(name = "string_id")     val stringId    : String) {

    @Ignore
    constructor(
     firstName   : String,
        lastName    : String,
        stringId    : String
    ) : this(0, firstName, lastName, stringId)
}