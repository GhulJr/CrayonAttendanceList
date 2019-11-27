package com.oskarrek.crayonattendancelist.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.models.AttendanceList
import com.oskarrek.crayonattendancelist.models.Participant
import kotlinx.android.synthetic.main.item_participant.view.*

class ParticipantsAdapter
    : RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    lateinit var list : ArrayList<Participant>
    lateinit var presenceList : SparseBooleanArray

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false))
    }

    override fun getItemCount(): Int = if(::list.isInitialized) list.size else 0

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        if(!::list.isInitialized || !::presenceList.isInitialized) {return} //TODO: provide better solution.

        val participant = list[position]
        val isChecked = presenceList[participant.id, false]

        holder.bind(participant, isChecked) {
            presenceList.put(participant.id, it)
            notifyItemChanged(position)
        }
    }


    class ParticipantViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(participant: Participant, isChecked : Boolean, notifyAdapter: (Boolean) -> Unit ) {
            view.participant_name.text = participant.firstName
            view.participant_surname.text = participant.lastName
            view.participant_checked.isChecked = isChecked

            view.setOnClickListener {
                view.participant_checked.isChecked = !view.participant_checked.isChecked
                notifyAdapter(view.participant_checked.isChecked)
            }
        }
    }
}