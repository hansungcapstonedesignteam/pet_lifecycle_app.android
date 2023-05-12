package com.hansung.petlifetimecare.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R


data class Item(
    val image: Int,
    val title: String,
    val content: String,
    val breed: String,
    val date: String
)

class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage)
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val contentView: TextView = view.findViewById(R.id.itemContent)
        val breedView: TextView = view.findViewById(R.id.itemBreed)
        val dateView: TextView = view.findViewById(R.id.itemDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_community_item, parent, false)
        return ItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.image)
        holder.titleView.text = item.title
        holder.contentView.text = item.content
        holder.breedView.text = item.breed
        holder.dateView.text = item.date
    }

    override fun getItemCount() = items.size
}
