package com.example.android.messagedeliverystatus


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var list = ArrayList<MessageModel>()
    fun addMessage(e: MessageModel){
        list.add(e)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.custom_chat_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val params = holder!!.message.layoutParams as RelativeLayout.LayoutParams
        val params2 = holder!!.deliveryStatus.layoutParams as RelativeLayout.LayoutParams

        if (list[position].sender==App.currentUser){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        }

        holder.message.text = list[position].message
        holder.deliveryStatus.text = list[position].status
    }

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView!!.findViewById(R.id.message)
        var deliveryStatus: TextView = itemView!!.findViewById(R.id.delivery_status)
    }

    fun updateData(s: String) {

        for(item in list){
            if (item.messageId==s){
                item.status="delivered"
                notifyDataSetChanged()
            }
        }

    }

}