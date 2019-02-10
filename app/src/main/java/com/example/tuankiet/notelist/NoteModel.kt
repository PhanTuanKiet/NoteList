package com.example.tuankiet.notelist

class NoteModel{

    var content : String? = null
    var title : String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(content: String?, title: String?) {
        this.content = content
        this.title = title
    }

}