package com.example.cloudfirestore.model

class Note {

    var id: String? = null
    var noteId: String? = null
    var title: String? = null
    var content: String? = null

    constructor() {}

    constructor(id: String, noteId:String, title: String, content: String) {
        this.id = id
        this.noteId = noteId
        this.title = title
        this.content = content
    }

    constructor(title: String, noteId: String, content: String) {
        this.noteId = noteId
        this.title = title
        this.content = content
    }


    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("noteId", noteId!!)
        result.put("title", title!!)
        result.put("content", content!!)

        return result
    }
    fun toUpdateMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("noteId", noteId!!)
        result.put("title", title!!)
        result.put("content", content!!)

        return result
    }
}