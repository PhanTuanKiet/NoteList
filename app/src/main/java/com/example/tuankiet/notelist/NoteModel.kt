package com.example.tuankiet.notelist

data class NoteModel(@SerializedName("content") val content: String?, @SerializedName("title") val title: String? )