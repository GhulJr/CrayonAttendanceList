package com.oskarrek.crayonattendancelist.repositories

import android.content.Context
import android.os.Environment
import android.util.Log
import com.opencsv.CSVReader
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.utils.SingletonHolder
import java.io.*
import java.lang.Exception

class StorageRepo private constructor(context: Context) {

    private val TAG = StorageRepo.javaClass.simpleName
    private val folderName = "Crayon_App"
    private val fileName = "participants.csv"

    // Singleton of repository
    companion object : SingletonHolder<StorageRepo, Context>(::StorageRepo)

    @Throws(Exception::class)
    fun loadParticipantsFromExcel() : ArrayList<Participant> {

        val participantList = ArrayList<Participant>()
            val filesInputStream = FileInputStream(getFilePath())
            val inputStreamReader = InputStreamReader(filesInputStream, "iso-8859-2")
            val csvReader = CSVReader(inputStreamReader)

            // Skip first title row.
            csvReader.readNext()

            // Get first data row.
            var next = csvReader.readNext()
            while(next != null) {

                participantList.add(Participant(next[1], next[2], next[3]))
                next = csvReader.readNext()
            }

        return participantList
    }

    private fun getFilePath() : String = getFolderPath() + fileName


    private fun getFolderPath() : String =
        Environment.getExternalStorageDirectory().path + File.separator + folderName+ File.separator

    fun createAppFolder() {
        val dir = File(getFolderPath())
        if(!dir.exists()) {
            dir.mkdirs()
        }
    }
}