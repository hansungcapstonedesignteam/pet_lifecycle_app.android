package com.hansung.petlifetimecare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ContentAdapter(private val contentList: MutableList<BoardModel>) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    inner class ContentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.itemTitle)
        private val contents: TextView = itemView.findViewById(R.id.itemContent)
        private val imageView: ImageView = itemView.findViewById(R.id.itemImage)

        fun bindItems(content: BoardModel) {
            title.text = content.title
            contents.text = content.content

            // 이미지 URL이 null이 아니면 이미지를 로드
            if (content.imageUrl != null) {
                Glide.with(itemView)
                    .load(content.imageUrl)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_community_item, parent, false)
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bindItems(contentList[position])
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}
