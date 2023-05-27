package com.hansung.petlifetimecare.searchPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R

class MessageAdapter(private var messageList: MutableList<Message>)
    : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftChatView: LinearLayout = view.findViewById(R.id.left_chat_view)
        val rightChatView: LinearLayout = view.findViewById(R.id.right_chat_view)
        val leftTextView: TextView = view.findViewById(R.id.left_chat_text_view)
        val rightTextView: TextView = view.findViewById(R.id.right_chat_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val chatView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, null)
        return MyViewHolder(chatView)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message : Message = messageList[position]
        if(message.sentBy.equals(Message.SENT_BY_ME)){
            holder.leftChatView.visibility = View.GONE
            holder.rightChatView.visibility = View.VISIBLE
            holder.rightTextView.text = message.message
        }else{
            holder.rightChatView.visibility = View.GONE
            holder.leftChatView.visibility = View.VISIBLE
            holder.leftTextView.text = message.message
        }
    }
}
