package com.hansung.petlifetimecare.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R

class CommunityFragment : Fragment() {

    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize your data here
        val items = listOf(
            Item(R.drawable.poodle, "Title1", "Content1", "Breed1", "Date1"),
            Item(R.drawable.poodle, "Title2", "Content2", "Breed2", "Date2"),
            // Add more items
        )

        // Initialize the adapter with the data
        adapter = ItemAdapter(items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the RecyclerView from the layout and set the adapter
        recyclerView = view.findViewById(R.id.navigation_community)
        recyclerView.adapter = adapter
    }
}
