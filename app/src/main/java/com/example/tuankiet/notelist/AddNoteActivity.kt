package com.example.tuankiet.notelist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.add_note_layout.*
import android.support.v4.app.NotificationCompat.getExtras



class AddNoteActivity : AppCompatActivity() {

    val mFirebaseHelper = FirebaseHelper()
    lateinit var itemID : String
    lateinit var editMode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_layout)

        mFirebaseHelper.initFirebase()

        val extras = intent.extras
        if (extras != null) {
            itemID = extras!!.getString("itemID")
            editMode = extras!!.getString("EditMode")
        }
        if (editMode.equals("Edit"))
            addButton.setText(editMode)

        addButton.setOnClickListener { view ->
            var title  = titleEditText.text.toString()
            var content = contentEditText.text.toString()
            if (!content.equals("")){
                mFirebaseHelper.createNote(itemID,content,title)
                finish()
            }
        }
    }
}