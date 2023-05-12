package com.hansung.petlifetimecare.adoptPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R

class GiveFragment : Fragment() {

    // Assuming you have some data
    private val giveItemList = listOf(
        GiveItem(R.drawable.poodle, "Text1", R.drawable.mapflag, "Text2", "Text3"),
        // Add more items...
    )

    private lateinit var giveItemAdapter: GiveItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_give, container, false)

        giveItemAdapter = GiveItemAdapter(giveItemList)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGive)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = giveItemAdapter

        return view
    }
}
