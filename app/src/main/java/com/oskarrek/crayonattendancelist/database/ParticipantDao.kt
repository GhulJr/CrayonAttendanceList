package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oskarrek.crayonattendancelist.models.Participant

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants ORDER BY participants.last_name ASC")
    fun getParticipants() : LiveData<List<Participant>>
    @Insert
    fun insertParticipants(vararg participants : Participant)
}