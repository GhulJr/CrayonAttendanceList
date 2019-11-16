package com.oskarrek.crayonattendancelist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_lists")
data class AttendanceList(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date_in_millis") val dateInMillis: Long) {

    @Ignore
    constructor(
        title   : String,
        dateInMillis    : Long) : this(0, title, dateInMillis)
}