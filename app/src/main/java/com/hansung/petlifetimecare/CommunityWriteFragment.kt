package com.hansung.petlifetimecare

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import com.hansung.petlifetimecare.databinding.FragmentCommunityWriteBinding

class CommunityWriteFragment : Fragment() {

    private lateinit var storage: FirebaseStorage
    private val REQUEST_IMAGE_PICK = 100
    private lateinit var imageUri: Uri
    private lateinit var database: FirebaseDatabase
    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database
        storage = Firebase.storage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)

        // 사진 선택 버튼 클릭 리스너
        binding.imageButton4.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // 업로드 버튼 클릭 리스너
        binding.button8.setOnClickListener {
            val title = binding.titleText.text.toString()
            val content = binding.editTextTextPersonName9.text.toString()
            val key = database.getReference("board").push().key

            Log.d("CommunityWriteFragment", "title: $title, content: $content, key: $key")

            if (!title.isNullOrBlank() && !content.isNullOrBlank() && key != null) {
                // Firebase Realtime Database에 게시물 데이터 추가
                database.getReference("board").child(key)
                    .setValue(
                        BoardModel(
                            key.hashCode(),
                            title,
                            content,
                            UUID.randomUUID().toString(),
                            System.currentTimeMillis()
                        )
                    )
                    .addOnSuccessListener {
                        uploadImage(key)

                        // 게시글이 성공적으로 업로드되면 화면을 이전으로 돌아가게 합니다.
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        // 게시글 업로드 실패 시 처리
                        Log.e("CommunityWriteFragment", "Failed to upload post", it)
                    }
            }

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            // 선택한 사진의 URI를 저장
            imageUri = data?.data!!

            // 선택한 사진을 ImageView에 표시
            binding.imageButton4.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImage(key: String) {
        val storageRef = storage.getReference("images/$key")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // 이미지 URL을 가져오고 데이터베이스에 저장
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    database.getReference("board").child(key).child("imageUrl").setValue(imageUrl)
                }
            }
            .addOnFailureListener {
                Log.e("CommunityWriteFragment", "Failed to upload image", it)
            }
    }



}
