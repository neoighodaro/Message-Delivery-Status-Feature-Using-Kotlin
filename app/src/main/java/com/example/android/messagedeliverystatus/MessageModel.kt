package com.example.android.messagedeliverystatus


data class MessageModel(var sender:String,
                        var messageId:String,
                        var message:String,
                        var status:String)