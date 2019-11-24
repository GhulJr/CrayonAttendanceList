package com.oskarrek.crayonattendancelist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.repositories.DatabaseRepo

class ParticipantsViewModel(application : Application) : AndroidViewModel(application) {
    private val repoDB : DatabaseRepo = DatabaseRepo.getInstance(application)

    val participants: LiveData<List<Participant>>

    init {
        participants = repoDB.getParticipants()
    }

}