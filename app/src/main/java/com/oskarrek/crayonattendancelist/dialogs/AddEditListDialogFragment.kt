package com.oskarrek.crayonattendancelist.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.oskarrek.crayonattendancelist.MainActivity.Companion.ATTENDANCELIST_ID
import com.oskarrek.crayonattendancelist.MainActivity.Companion.ATTENDANCELIST_TIMESPAN
import com.oskarrek.crayonattendancelist.MainActivity.Companion.ATTENDANCELIST_TITLE
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.interfaces.IOnListDialogListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import kotlinx.android.synthetic.main.dialog_add_list.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddEditListDialogFragment : DialogFragment() {

    private lateinit var onEditListener : IOnListDialogListener
    private var isAdding = true //TODO: provide better solution (for instance pass argument to the bundle)
    private var listId : Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onEditListener = context as IOnListDialogListener
            isAdding = arguments?.isEmpty ?: true
        } catch (e : Exception) {

            throw ClassCastException((context.toString() +
                    " must implement IONCreateListListener"))
        }
    }

    //TODO: refactor this method.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Explicitly create view.
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_add_list, null, false)
        layout.attendanceList_editDate.setOnClickListener {
            buildDatePicker(it as TextView)
        }

        // TODO: provide util class for string format.
        if(!isAdding) {
            listId = arguments?.getInt(ATTENDANCELIST_ID) ?: 0
            layout.apply {
                attendanceList_editTitle.text = SpannableStringBuilder(arguments?.getString(ATTENDANCELIST_TITLE))
                attendanceList_editDate.tag = arguments?.getLong(ATTENDANCELIST_TIMESPAN)
                attendanceList_editDate.text =  SpannableStringBuilder(dateAsString(attendanceList_editDate.tag as Long))
            }

        }


        return activity?.let {
            val builder: AlertDialog.Builder? = AlertDialog.Builder(it)

            builder
                ?.setTitle(R.string.add_attendance_list)
                ?.setView(layout)
                ?.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                ?.setPositiveButton(getString(R.string.add)) { dialog, _ ->
                    val title : String = layout.attendanceList_editTitle.text.toString()
                    val date : Long = (layout.attendanceList_editDate.tag ?: 0L) as Long

                    if(title.isBlank() || date == 0L) {
                        Toast
                            .makeText(
                            context,
                            R.string.data_empty,
                            Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if(isAdding) {
                            onEditListener.onCreate(AttendanceList(title, date))
                        } else {
                            onEditListener.onUpdate(AttendanceList(listId, title, date))
                        }
                    }

                }?.create()



         } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun dateAsString(dateInMillis : Long) : String{
        val date = Date(dateInMillis)
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        return simpleDateFormat.format(date)
    }

    private fun buildDatePicker(dateView: TextView) {


        val calendar = Calendar.getInstance()
        val timeSpan =  (dateView.tag ?: 0L) as Long

        if(timeSpan > 0) { calendar.timeInMillis = timeSpan }

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener{ view, year, month, day ->
                dateView.text = "${day}.${month + 1}.${year}"
                val cld = Calendar.getInstance()
                cld.set(year, month, day)
                dateView.tag = cld.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()

    }
}
