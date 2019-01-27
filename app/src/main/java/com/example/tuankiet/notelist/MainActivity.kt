package com.example.tuankiet.notelist

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {

    lateinit var mDatabase: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        val users = ArrayList<NoteModel>()
        users.add(NoteModel("new content","new title"))
        users.add(NoteModel("content 1","title 1"))
        users.add(NoteModel("content 2","title 2"))

        val list_adapter = NoteAdapter(users)

        recycleview_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycleview_list.adapter = list_adapter
        recycleview_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        initFirebase()
        createNote("ffee","ghh")
        readData()

        fab.setOnClickListener { view ->
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    fun initFirebase(){

        mDatabase = FirebaseDatabase.getInstance()
        myRef = mDatabase.getReference("Notes")

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

    fun createNote(content : String?,title : String?){
        val note = NoteModel(content,title)
        myRef.child("4").setValue(note)
    }

    fun readData() {
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
