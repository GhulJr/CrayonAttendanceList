package com.oskarrek.crayonattendancelist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oskarrek.crayonattendancelist.models.Participant

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants ORDER BY participants.last_name ASC")
    fun getParticipantsLiveData() : LiveData<List<Participant>>

    @Query("SELECT * FROM participants ORDER BY participants.last_name ASC")
    fun getParticipants() : List<Participant>

    @Insert
    fun insertParticipant(participants : Participant)

    @Query("SELECT * FROM participants WHERE participants.string_id = :stringId")
    fun getParticipantsBuStringId(stringId: String): Participant?
}