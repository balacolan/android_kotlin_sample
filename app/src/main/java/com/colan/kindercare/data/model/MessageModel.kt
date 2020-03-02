package com.colan.kindercare.data.model

class MessageModel (
    var id:Int,
    var profilePic:Int,
    var name:String,
    var type:String,
    var time:String,
    var reportAttachment:String,
    var isSelected:Boolean,
    var isRead:Boolean = true
)