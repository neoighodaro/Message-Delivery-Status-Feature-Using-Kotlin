package com.example.android.messagedeliverystatus


import android.app.Application

class App:Application() {
    companion object {
        lateinit var currentUser:String
    }
}