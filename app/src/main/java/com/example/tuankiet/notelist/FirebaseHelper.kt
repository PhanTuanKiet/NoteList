package com.example.tuankiet.notelist

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseHelper {

    val mDatabase = FirebaseDatabase.getInstance()
    val myRef = mDatabase.getReference("note")

    var noteId = myRef.push().key


}