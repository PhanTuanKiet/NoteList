package com.example.tuankiet.notelist

import java.io.Serializable

data class NoteModel(@SerializedName("content") val content: String?, @SerializedName("title") val title: String? )