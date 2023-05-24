package com.hansung.petlifetimecare.searchPackage

class Message {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }

    var message: String? = null
    var sentBy: String? = null

    constructor(message: String,sentBy : String){
        this.message = message
        this.sentBy = sentBy
    }

}