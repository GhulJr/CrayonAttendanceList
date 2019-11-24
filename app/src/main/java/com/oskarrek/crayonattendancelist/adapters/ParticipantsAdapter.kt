package com.oskarrek.crayonattendancelist.adapters

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

class ParticipantsAdapter : RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    lateinit var list : ArrayList<Participant>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false))
    }

    override fun getItemCount(): Int = if(::list.isInitialized) list.size else 0

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = list[position]
        holder.bind(participant)
        //TODO: do something with checkbox
    }


    class ParticipantViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView =  view.findViewById(R.id.participant_name)
        val surname : TextView =  view.findViewById(R.id.participant_surname)
        val checkBox : CheckBox = view.findViewById(R.id.participant_checked)

        fun bind(participant: Participant) {
            view.participant_name.text = participant.firstName
            view.participant_surname.text = participant.lastName
            //todo:provide on click.
        }
    }
}