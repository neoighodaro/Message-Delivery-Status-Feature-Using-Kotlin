package com.example.android.messagedeliverystatus


import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var myUserId: String
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        myUserId = App.currentUser
        setupRecyclerView()
        setupFabListener()
        setupPusher()
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter()
        recyclerView.adapter = adapter
    }


    private fun setupFabListener() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener({
            createAndShowDialog()
        })
    }


    private fun createAndShowDialog() {
        val builder: AlertDialog = AlertDialog.Builder(this).create()
        // Get the layout inflater
        val view = this.layoutInflater.inflate(R.layout.dialog_message, null)
        builder.setMessage("Compose new message")
        builder.setView(view)

        val sendMessage: Button = view.findViewById(R.id.send)
        val editTextMessage: EditText = view.findViewById(R.id.edit_message)
        sendMessage.setOnClickListener({

            if (editTextMessage.text.isNotEmpty())
                RetrofitClient.getRetrofitClient().sendMessage(myUserId, editTextMessage.text.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        // message has sent
                        val jsonObject = JSONObject(response!!.body())
                        Log.d("TAG", response.body())
                        val newMessage = MessageModel(
                                jsonObject["sender"].toString(),
                                jsonObject["id"].toString(),
                                jsonObject["message"].toString(),
                                "sent"
                        )
                        adapter.addMessage(newMessage)
                        builder.dismiss()
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        // Message could not send
                    }

                })


        })

        builder.show()
    }


    private fun setupPusher() {

        val options = PusherOptions()
        options.setCluster("PUSHER_APP_CLUSTER")
        val pusher = Pusher("PUSHER_APP_KEY", options)
        val channel = pusher.subscribe("my-channel")

        channel.bind("new_message") { channelName, eventName, data ->
            val jsonObject = JSONObject(data)
            val sender = jsonObject["sender"].toString()

            if (sender != myUserId) {
                // this message is not from me, instead, it is from another user
                val newMessage = MessageModel(
                        sender,
                        jsonObject["id"].toString(),
                        jsonObject["message"].toString(),
                        ""
                )
                runOnUiThread {
                    adapter.addMessage(newMessage)
                }

                // tell the sender that his message has delivered
                RetrofitClient.getRetrofitClient().delivered(sender, jsonObject["id"].toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        // I have told the sender that his message delivered
                        Log.d("TAG", response!!.body())
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        // I could not tell the sender
                        Log.d("TAG", "Error")
                    }

                })


            }

        }

        channel.bind("delivery-status") { channelName, eventName, data ->

            val jsonObject = JSONObject(data)
            val sender = jsonObject["sender"]
            if (sender == myUserId) {
                runOnUiThread {
                    adapter.updateData(jsonObject["id"].toString())
                }
            }
        }

        pusher.connect()
    }


}
