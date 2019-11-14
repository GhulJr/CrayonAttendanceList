package com.oskarrek.crayonattendancelist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oskarrek.crayonattendancelist.models.Participant

class ParticipantsViewModel : ViewModel() {
    val participants: LiveData<List<Participant>> = MutableLiveData()
}