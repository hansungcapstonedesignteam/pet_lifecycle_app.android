package com.hansung.petlifetimecare.adoptPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R

data class GiveItem(
    val imageId: Int,
    val text1: String,
    val iconId: Int,
    val text2: String,
    val text3: String
)

class GiveItemAdapter(private val giveItemList: List<GiveItem>,private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<GiveItemAdapter.GiveItemViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class GiveItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiveItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_get2_item, parent, false)
        return GiveItemViewHolder(view)
    }

    override fun getItemCount() = giveItemList.size

    override fun onBindViewHolder(holder: GiveItemViewHolder, position: Int) {
        val currentItem = giveItemList[position]

        holder.imageView.setImageResource(currentItem.imageId)
        holder.textView.text = currentItem.text1
        holder.imageView2.setImageResource(currentItem.iconId)
        holder.textView2.text = currentItem.text2
        holder.textView3.text = currentItem.text3

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
}
