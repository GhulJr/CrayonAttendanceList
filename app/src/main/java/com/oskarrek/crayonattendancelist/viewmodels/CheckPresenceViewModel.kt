package com.oskarrek.crayonattendancelist.viewmodels

import android.app.Application
import android.util.SparseBooleanArray
import androidx.lifecycle.*
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.models.Presence
import com.oskarrek.crayonattendancelist.repositories.DatabaseRepo

class CheckPresenceViewModel(application : Application,
                             val attendanceListId : Int) : AndroidViewModel(application) {

    lateinit var presencesList : MutableList<Presence>
    val participants: LiveData<List<Participant>>
    val presencesLiveData: LiveData<List<Presence>>
    private val repoDB : DatabaseRepo = DatabaseRepo.getInstance(application)


    init {
        participants = repoDB.getParticipantsLiveData()
        presencesLiveData = repoDB.getPresencesLiveData(attendanceListId)
        //TODO: provide transformation to SparseBooleanArray.
    }

    fun getPresenceBooleanArray(presencesList : List<Presence>): SparseBooleanArray {
        val array = SparseBooleanArray()
        for(p in presencesList) {
            array.put(p.participantId, true)
        }
        return array
    }

    //TODO: optimize this method.
    fun savePresences(presenceCheckedList: SparseBooleanArray) {

        System.out.println("-----------------------------------${presencesList.size} $")

        // Delete all presences
        for(p in this.presencesList) {
            repoDB.deletePresence(p)

        }

        for(i in 0 until presenceCheckedList.size()) {
            if(presenceCheckedList.valueAt(i)) {
                repoDB.insertPresence(Presence(attendanceListId, presenceCheckedList.keyAt(i)))
            }
        }
    }

}