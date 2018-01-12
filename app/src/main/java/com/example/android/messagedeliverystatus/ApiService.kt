package com.example.android.messagedeliverystatus


import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/message")
    fun sendMessage(@Query("sender") sender:String, @Query("msg") message:String): Call<String>

    @POST("/delivered")
    fun delivered(@Query("sender") sender:String, @Query("messageId") messageId:String): Call<String>

    @POST("/auth")
    fun login(): Call<String>

}