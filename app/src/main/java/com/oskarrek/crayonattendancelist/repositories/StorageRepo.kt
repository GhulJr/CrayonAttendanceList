package com.oskarrek.crayonattendancelist.repositories

import android.app.Application
import android.content.Context
import android.os.Environment
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.utils.SingletonHolder
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class StorageRepo private constructor(context: Context) {

    private val folderName = "Crayon_App"
    private val fileName = "Participants.csv"

    // Singleton of repository
    companion object : SingletonHolder<StorageRepo, Context>(::StorageRepo)


    fun loadParticipantsFromExcel() : ArrayList<Participant>{

        val participantList = ArrayList<Participant>()

        val filesInputStream = FileInputStream(getPath())
        val inputStreamReader = InputStreamReader(filesInputStream, "UTF-8")
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

    private fun getPath() : String {
        return Environment.getExternalStorageDirectory().path + File.separator + folderName + File.separator + fileName
    }
}