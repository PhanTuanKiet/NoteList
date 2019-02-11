package com.example.tuankiet.notelist

import android.content.Intent
import android.graphics.Canvas
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
import android.widget.Toast
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import android.support.v7.widget.RecyclerView




class MainActivity : AppCompatActivity() {


    lateinit var myRef : DatabaseReference
    val firebaseHelper = FirebaseHelper()
    var itemCount : Int = 0
    lateinit var swipeController : SwipeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        firebaseHelper.initFirebase()
        myRef = firebaseHelper.myRef
        readData()

        val intent = Intent(this, AddNoteActivity::class.java)

        swipeController = SwipeController( object : SwipeControllerActions(){
            override fun onLeftClicked() {
                super.onLeftClicked()
                intent.putExtra("EditMode","Edit")
                startActivity(intent)
                Toast.makeText(this@MainActivity,"Left Click",Toast.LENGTH_SHORT).show()
            }
        }

        )
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recycleview_list)

        fab.setOnClickListener { view ->
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
                }
                itemCount = dataSnapshot.childrenCount.toInt()
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
        recycleview_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

}
