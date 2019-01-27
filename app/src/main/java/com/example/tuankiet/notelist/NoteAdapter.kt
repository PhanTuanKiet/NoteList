package com.example.tuankiet.notelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_layout.view.*



class NoteAdapter(val noteList : ArrayList<NoteModel>) : RecyclerView.Adapter<NoteAdapter.NoteListViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteListViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_layout,p0,false)
        return NoteListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(p0: NoteListViewHolder, p1: Int) {
        return p0.bindItems(noteList[p1])
    }

    class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(note: NoteModel) {
            itemView.tv_item_title.text = note.title
        }

    }
}