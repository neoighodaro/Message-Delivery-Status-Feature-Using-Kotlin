package com.example.android.messagedeliverystatus


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            RetrofitClient.getRetrofitClient().login().enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    Log.d("TAG",t.toString())
                }
                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    val jsonObject = JSONObject(response!!.body().toString())
                    Log.d("TAG",jsonObject["id"].toString())
                    val currentUserId = jsonObject["id"].toString()
                    App.currentUser = currentUserId
                    startActivity(Intent(this@LoginActivity,ChatActivity::class.java))
                }
            })
        }


    }
}