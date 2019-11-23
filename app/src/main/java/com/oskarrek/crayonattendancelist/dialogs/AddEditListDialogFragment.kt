package com.oskarrek.crayonattendancelist.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.interfaces.IOnCreateListListener
import com.oskarrek.crayonattendancelist.models.AttendanceList
import kotlinx.android.synthetic.main.dialog_add_list.view.*
import java.lang.Exception

class AddEditListDialogFragment : DialogFragment() {

    lateinit var onEditListener : IOnCreateListListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onEditListener = context as IOnCreateListListener
        } catch (e : Exception) {

            throw ClassCastException((context.toString() +
                    " must implement IONCreateListListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Explicitly create view.
        val addLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_list, null, false)
        addLayout.attendanceList_editDate.setOnClickListener {
            buildDatePicker(it as TextView)
        }

        return activity?.let {
            val builder: AlertDialog.Builder? = AlertDialog.Builder(it)

            builder
                ?.setTitle(R.string.add_attendance_list)
                ?.setView(addLayout)
                ?.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                ?.setPositiveButton(getString(R.string.add)) { dialog, _ ->
                    val title : String = addLayout.attendanceList_editTitle.text.toString()
                    val date : Long = addLayout.attendanceList_editDate.tag as Long //TODO: (O) provide solution for this

                    onEditListener.onCreate(AttendanceList(title, date))
                    dialog.dismiss()
                }?.create()



         } ?: throw IllegalStateException("Activity cannot be null")
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
