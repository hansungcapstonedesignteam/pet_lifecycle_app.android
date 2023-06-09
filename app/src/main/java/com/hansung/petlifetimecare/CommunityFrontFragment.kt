package com.hansung.petlifetimecare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hansung.petlifetimecare.databinding.FragmentCommunityFrontBinding

class CommunityFrontFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var contentAdapter: ContentAdapter
    private var _binding: FragmentCommunityFrontBinding? = null
    private val contentList = mutableListOf<BoardModel>()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database
        contentAdapter = ContentAdapter(contentList)

        database.getReference("board").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contentList.clear()
                snapshot.children.forEach {
                    val boardModel = it.getValue(BoardModel::class.java)
                    boardModel?.let { model -> contentList.add(0, model) }
                }
                contentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // DB 읽기 실패 시 처리
                Toast.makeText(context, "Failed to load data: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
//        val key = myRef.push().key.toString()
//        myRef.child(key).setValue(BoardModel(0, "Test 제목", "테스트 용 내용입니다", "ㅇㅇ", 1234))

//        for (i in 1..10) {
//            contentList.add(BoardModel(i, "Test$i", "Test 내용$i", i.toString(), 9122+i))
//        }

        contentAdapter = ContentAdapter(contentList)
//        binding.communityRecyclerview.adapter = contentAdapter
//        binding.communityRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun getContentData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contentList.clear()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityFrontBinding.inflate(inflater, container, false)
//        val view = inflater.inflate(R.layout.fragment_community_front, container, false)
        val spinner: Spinner = _binding!!.spinnerBoard

        binding.floatingActionButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, CommunityWriteFragment())  // container는 Fragment가 표시될 레이아웃의 ID입니다.
                .addToBackStack(null)
                .commit()
        }

        // 게시판 선택을 위한 Spinner Array 연결
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.community_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBoard.adapter = adapter
        }

        binding.communityRecyclerview.adapter = contentAdapter
        binding.communityRecyclerview.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}