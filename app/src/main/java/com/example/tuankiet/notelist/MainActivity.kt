package com.example.tuankiet.notelist

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import com.google.firebase.database.GenericTypeIndicator


class MainActivity : AppCompatActivity() {


    lateinit var myRef : DatabaseReference
    val firebaseHelper = FirebaseHelper()
    var itemCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        firebaseHelper.initFirebase()
        myRef = firebaseHelper.myRef
        readData()

        val swipeController = SwipeController()
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recycleview_list)

        fab.setOnClickListener { view ->
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("itemID",(itemCount+1).toString())
            startActivity(intent)
        }
    }

    fun readData(){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notelist = ArrayList<NoteModel?>()
                for (dataList in dataSnapshot.children) {
                    var note = dataList.getValue(NoteModel::class.java)
                    notelist.add(NoteModel(note!!.content, note!!.title))
                    itemCount = dataSnapshot.childrenCount.toInt()
                }
                setupRecycleView(notelist)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun setupRecycleView(users: ArrayList<NoteModel?>) {
        val list_adapter = NoteAdapter(users)

        recycleview_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleview_list.adapter = list_adapter
        recycleview_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

}
