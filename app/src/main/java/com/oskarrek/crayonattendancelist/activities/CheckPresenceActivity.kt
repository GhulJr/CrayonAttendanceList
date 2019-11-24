package com.oskarrek.crayonattendancelist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.adapters.ParticipantsAdapter
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.viewmodels.ParticipantsViewModel
import kotlinx.android.synthetic.main.content_check_presence.*

class CheckPresenceActivity : AppCompatActivity() {

    private lateinit var participantsAdapter : ParticipantsAdapter
    private lateinit var viewModel: ParticipantsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_presence)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        participantsAdapter = ParticipantsAdapter()

        participant_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@CheckPresenceActivity)
            adapter = participantsAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ParticipantsViewModel::class.java)
        viewModel.participants.observe(this, Observer {list ->
            participantsAdapter.list = ArrayList(list) // Might cause a problem.
            participantsAdapter.notifyDataSetChanged()
        })
    }

    private fun getDummyData() : ArrayList<Participant> {
        val list = ArrayList<Participant>()

        for(i in 0..19) {
            list.add(Participant("Name", "Surname", "namesurname"))
        }

        return list
    }
}
