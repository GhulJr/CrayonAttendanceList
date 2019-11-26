package com.oskarrek.crayonattendancelist.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "presence", foreignKeys = [
    ForeignKey(
            entity = AttendanceList::class,
            parentColumns = ["id"],
            childColumns = ["attendanceListId"]),

    ForeignKey(
                entity = Participant::class,
                parentColumns = ["id"],
                childColumns = ["participantId"])
        ]
)

data class Presence(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val attendanceListId: Int,
    val participantId: Int) {

    @Ignore
    constructor(attendanceListId: Int, participantId: Int) : this(0, attendanceListId, participantId)
}