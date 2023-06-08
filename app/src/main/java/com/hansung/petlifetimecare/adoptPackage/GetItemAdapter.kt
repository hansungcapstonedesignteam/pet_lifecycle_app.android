package com.hansung.petlifetimecare.adoptPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hansung.petlifetimecare.R

data class GiveItem(
    var imageUrl: String? = null,
    var address: String? = null,
    var mapflag: Int? = null,
    var currentTime: String? = null,
    var extra: String? = null
)

class GiveItemAdapter(private val giveItemList: List<GiveItem>,private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<GiveItemAdapter.GiveItemViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class GiveItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewWhere: TextView = itemView.findViewById(R.id.textWhere)
        val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        val textViewCalandar: TextView = itemView.findViewById(R.id.textCalander)
        val textIn: TextView = itemView.findViewById(R.id.textIn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiveItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_get2_item, parent, false)
        return GiveItemViewHolder(view)
    }

    override fun getItemCount() = giveItemList.size

    override fun onBindViewHolder(holder: GiveItemViewHolder, position: Int) {
        val currentItem = giveItemList[position]

        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageView)

        holder.textViewWhere.text = currentItem.address
        currentItem.mapflag?.let { holder.imageView2.setImageResource(it) }
        holder.textViewCalandar.text = currentItem.currentTime
        holder.textIn.text = currentItem.extra

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

}
