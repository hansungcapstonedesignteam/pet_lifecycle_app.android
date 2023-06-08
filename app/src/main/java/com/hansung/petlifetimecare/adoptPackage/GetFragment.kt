package com.hansung.petlifetimecare.adoptPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.hansung.petlifetimecare.R

class GetFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var giveItemAdapter: GiveItemAdapter
    private val giveItemList = mutableListOf<GiveItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("giveItem")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get, container, false)

        giveItemAdapter = GiveItemAdapter(giveItemList, object : GiveItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGive)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = giveItemAdapter

        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val newGiveItem = dataSnapshot.getValue(GiveItem::class.java)
                newGiveItem?.let {
                    giveItemList.add(0, it) // 항목을 리스트의 시작 부분에 추가합니다.
                    giveItemAdapter.notifyDataSetChanged()
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors
            }
        })

        return view
    }
}