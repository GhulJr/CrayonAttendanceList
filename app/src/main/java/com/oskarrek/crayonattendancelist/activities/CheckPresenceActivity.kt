package com.oskarrek.crayonattendancelist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarrek.crayonattendancelist.MainActivity
import com.oskarrek.crayonattendancelist.R
import com.oskarrek.crayonattendancelist.adapters.ParticipantsAdapter
import com.oskarrek.crayonattendancelist.models.Participant
import com.oskarrek.crayonattendancelist.utils.CheckPresenceViewModelFactory
import com.oskarrek.crayonattendancelist.viewmodels.CheckPresenceViewModel
import kotlinx.android.synthetic.main.activity_check_presence.*
import kotlinx.android.synthetic.main.content_check_presence.*
import kotlinx.android.synthetic.main.content_main.*

class CheckPresenceActivity : AppCompatActivity() {

    private lateinit var participantsAdapter : ParticipantsAdapter
    private lateinit var viewModel: CheckPresenceViewModel

    //TODO: check if its possible to have two different object sources mapped into one (for example hashmap).

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_presence)
        setSupportActionBar(check_presence_toolbar)
        handleIntent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_check_presence, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_list -> {
                saveList()
                finish()
                true
            }
            R.id.action_discard_list -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if(participantsAdapter.isChanged) {
            checkIfModified()
        } else {
            super.onBackPressed()
        }
    }

    private fun handleIntent() {
        // Check if attendance list has been passed as intent extra,
        // otherwise finish activity.
        if(intent.hasExtra(MainActivity.ATTENDANCELIST_ID)) {
            setupRecyclerView()
            setupViewModel(intent.getIntExtra(MainActivity.ATTENDANCELIST_ID, -1))
        }
        else {
            Toast.makeText(application, "Brak listy obecności bądź błędne dane", Toast.LENGTH_SHORT).show()
            finish()
        }

        // List name is not so critical, so we won't finish activity if it doesn't exist.
        if(intent.hasExtra(MainActivity.ATTENDANCELIST_TITLE)) {
            supportActionBar?.title = intent.getStringExtra(MainActivity.ATTENDANCELIST_TITLE)
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

    private fun checkIfModified() {
        AlertDialog.Builder(this)
            .setTitle(R.string.unsaved_changes)
            .setMessage(R.string.unsaved_changes_message)
            .setPositiveButton(R.string.yes) { _,_ ->
                saveList()
                finish()
            }
            .setNegativeButton(R.string.no) { _,_ ->
                finish()
            }
            .setNeutralButton(R.string.cancel) { _,_ ->}
            .show()
    }

    private fun saveList() {
        viewModel.savePresences(participantsAdapter.presenceList)
    }
    private fun getDummyData() : ArrayList<Participant> {
        val list = ArrayList<Participant>()

        for(i in 0..19) {
            list.add(Participant("Name", "Surname", "namesurname"))
        }

        return list
    }
}
