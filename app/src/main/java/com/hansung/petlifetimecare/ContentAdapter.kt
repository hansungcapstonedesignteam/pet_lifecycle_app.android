package com.hansung.petlifetimecare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContentAdapter(val items: MutableList<BoardModel>) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: BoardModel) {
            val title = itemView.findViewById<TextView>(R.id.itemTitle)
            val contents = itemView.findViewById<TextView>(R.id.itemContent)

            title.text = items.title
            contents.text = items.contents
            TODO("RecyclerView 테스트 후 구현 예정")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_community_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }
}