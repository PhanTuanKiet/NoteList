package com.example.tuankiet.notelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_note_layout.*


class AddNoteActivity : AppCompatActivity() {

    val mFirebaseHelper = FirebaseHelper()
    var itemID : String = ""
    lateinit var Mode : String

    companion object {

        fun getIntentByID(itemID: String?, itemData : String?,mode: String, context: Context): Intent {
            var intent = Intent(context, AddNoteActivity::class.java)
            intent.putExtra("itemID", itemID)
            intent.putExtra("itemData", itemData)
            intent.putExtra("Mode", mode)
            return intent
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_layout)

        mFirebaseHelper.initFirebase()

        val extras = intent.extras

        if (extras != null) {
            Mode = extras!!.getString("Mode")
        }
        if (Mode.equals("Edit")) {
            addButton.text = "Edit"
            var itemData = extras.getString("itemData").split(",")
            titleEditText.setText(itemData[0])
            contentEditText.setText(itemData[1])
        } else {
            addButton.text = "Add"
        }

        addButton.setOnClickListener { view ->
            var title  = titleEditText.text.toString()
            var content = contentEditText.text.toString()
            itemID = extras!!.getString("itemID")
            if (!content.equals("")){
                mFirebaseHelper.createNote(itemID,content,title)
                finish()
            }
        }
    }
}