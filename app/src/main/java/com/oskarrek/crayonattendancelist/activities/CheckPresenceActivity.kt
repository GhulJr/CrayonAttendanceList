package com.oskarrek.crayonattendancelist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.MainActivity
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.adapters.ParticipantsAdapter
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.utils.CheckPresenceViewModelFactory
import com.oskarrek.crayonattendancelist.viewmodels.CheckPresenceViewModel
import kotlinx.android.synthetic.main.content_check_presence.*

class CheckPresenceActivity : AppCompatActivity() {

    private lateinit var participantsAdapter : ParticipantsAdapter
    private lateinit var viewModel: CheckPresenceViewModel

    //TODO: check if its possible to have two different object sources mapped into one (for example hashmap).

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_presence)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        handleIntent()
    }

    override fun onBackPressed() {
        viewModel.savePresences(participantsAdapter.presenceList)
        super.onBackPressed()
    }

    private fun handleIntent() {
        if(intent.hasExtra(MainActivity.ATTENDANCE_CHECK)) {
            setupRecyclerView()
            setupViewModel(intent.getIntExtra(MainActivity.ATTENDANCE_CHECK, -1))
        } else {
            Toast.makeText(application, "Brak listy obecności bądź błędne dane", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        participantsAdapter = ParticipantsAdapter()

        participant_recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@CheckPresenceActivity)
            adapter = participantsAdapter
        }
    }

    private fun setupViewModel(listId : Int) {

        val factory = CheckPresenceViewModelFactory(application, listId)

        viewModel = ViewModelProviders.of(this, factory).get(CheckPresenceViewModel::class.java)

        viewModel.participants.observe(this, Observer {list ->
            participantsAdapter.list = ArrayList(list) // Might cause a problem.
            participantsAdapter.notifyDataSetChanged()
        })

        viewModel.presencesLiveData.observe(this, Observer {
            viewModel.presencesList = it.toMutableList()
            participantsAdapter.presenceList = viewModel.getPresenceBooleanArray(it)
        } )
    }

    private fun getDummyData() : ArrayList<Participant> {
        val list = ArrayList<Participant>()

        for(i in 0..19) {
            list.add(Participant("Name", "Surname", "namesurname"))
        }

        return list
    }
}
