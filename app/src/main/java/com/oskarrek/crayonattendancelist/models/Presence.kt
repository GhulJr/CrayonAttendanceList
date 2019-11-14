package com.oskarrek.crayonattendancelist.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "presence", foreignKeys = [
    ForeignKey(
            entity = AttendanceList::class,
            parentColumns = ["id"],
            childColumns = ["attendanceListId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE),

    ForeignKey(
                entity = Participant::class,
                parentColumns = ["id"],
                childColumns = ["participantId"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
        ]
)

data class Presence(
    @PrimaryKey val id: Int,
    val attendanceListId: Int,
    val participantId: Int)