package com.oskarrek.crayonattendancelist.repositories

import android.content.Context
import android.os.Environment
import android.util.Log
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.models.Presence
import com.oskarrek.crayonattendancelist.utils.SingletonHolder
import java.io.*
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class StorageRepo private constructor(context: Context) {

    private val TAG = StorageRepo.javaClass.simpleName
    private val folderName = "Crayon_App"
    private val fileName = "participants.csv"
    private val saveFileName = "presence.csv"
    private val encoding = "iso-8859-2"

    private val executor : Executor = Executors.newSingleThreadExecutor()


    // Singleton of repository
    companion object : SingletonHolder<StorageRepo, Context>(::StorageRepo)

    @Throws(Exception::class)
    fun loadParticipantsFromCsv() : ArrayList<Participant> {

        val participantList = ArrayList<Participant>()
            val filesInputStream = FileInputStream(getFilePath())
            val inputStreamReader = InputStreamReader(filesInputStream, encoding)
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

    fun saveParticipantsToCsv(getAttendanceList: () -> List<AttendanceList>, getParticipants: () -> List<Participant>, getPresenceByParticipantId: (Int) -> List<Presence>) {
        //TODO: refactor this to provide callback when finished (wrap into rx or something).
        // I can create list of string's arrays outside this function and then pass it as argument.
        executor.execute {

            // Get data.
            val participants : List<Participant> = getParticipants()
            val attendanceLists : List<AttendanceList> = getAttendanceList()

            // Stop process if there is no data to work with.
            if(attendanceLists.isEmpty() || participants.isEmpty()) return@execute

            // Create file.
            val file = File(getFolderPath(), saveFileName)
            if(!file.exists()) {
                file.createNewFile()
            }
            // create FileWriter object with file as parameter.
            val os = FileOutputStream(file)
            //Enable utf-8 encoding
            os.write(0xef)
            os.write(0xbb)
            os.write(0xbf)
            // create CSVWriter object filewriter object as parameter.
            val writer = CSVWriter(OutputStreamWriter(os))

            //Create and write header.
            val headerList = ArrayList<String>()
            val dates = ArrayList<String>().apply {
                for(al in attendanceLists) {
                    add(al.getDateAsString())
                }
            }


            headerList.apply {
                add("Nazwisko")
                add("Imię")
                add("Identyfikator")
                addAll(dates)
            }

            val array = arrayOfNulls<String>(headerList.size)
            headerList.toArray(array)
            writer.writeNext(array)

            for(p in participants) {
                val participantRecord = Array(attendanceLists.size + 3) {"0"}
                participantRecord.apply {
                    set(0, p.lastName)
                    set(1, p.firstName)
                    set(2, p.stringId)

                    var presences : List<Presence> = getPresenceByParticipantId(p.id)

                    for(i in 0 until attendanceLists.size) {
                        for(pr in presences) {
                            if(attendanceLists[i].id == pr.attendanceListId) {
                                set(i + 3, "1")
                            }
                        }
                    }
                }
                writer.writeNext(participantRecord)
            }

            writer.close()
        }
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