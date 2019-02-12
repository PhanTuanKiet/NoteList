package com.example.tuankiet.notelist

import android.util.Log
import com.google.firebase.database.*

class FirebaseHelper {

    lateinit var mDatabase : FirebaseDatabase
    lateinit var myRef : DatabaseReference

    constructor()

    fun initFirebase() {
        mDatabase = FirebaseDatabase.getInstance()
        myRef = mDatabase.getReference("Notes")
    }

    fun createNote(id : String,content: String?, title: String?) {
        val note = NoteModel(content, title)
        myRef.child(id).setValue(note)
    }
}