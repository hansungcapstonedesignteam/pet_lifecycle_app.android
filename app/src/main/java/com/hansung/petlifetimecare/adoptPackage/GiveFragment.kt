package com.hansung.petlifetimecare.adoptPackage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hansung.petlifetimecare.R
import java.time.LocalDateTime
import java.util.*
import java.time.format.DateTimeFormatter


class GiveFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var imageUri: Uri? = null
    private lateinit var selectedImage: ImageView

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("giveItem")
        storage = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_apply_adopt, container, false)

        val btnSubmit = view.findViewById<Button>(R.id.btn_submit)
        val edtAddress = view.findViewById<EditText>(R.id.edt_address)
        val edtExtra = view.findViewById<EditText>(R.id.edt_extra)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = formatter.format(LocalDateTime.now())

        val btnSelectImage = view.findViewById<Button>(R.id.btn_select_image)
        selectedImage = view.findViewById(R.id.selected_image)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
        }

        btnSubmit.setOnClickListener {
            val address = edtAddress.text.toString().trim()
            val extra = edtExtra.text.toString().trim()

            if (address.isEmpty() || extra.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                val fileName = UUID.randomUUID().toString()
                val ref = storage.child("images/$fileName")

                ref.putFile(imageUri!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val giveItems = GiveItem(uri.toString(), address, R.drawable.mapflag, currentTime, extra)
                            database.push().setValue(giveItems).addOnCompleteListener {
                                Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                                // Navigate to GetFragment
                                fragmentManager?.beginTransaction()
                                    ?.replace(R.id.viewPager, GetFragment()) // assuming your ViewPager ID is viewPager
                                    ?.commit()
                            }
                        }
                    }
                    .addOnFailureListener {
                        val giveItems = GiveItem("", address, R.drawable.mapflag, currentTime, extra)
                        database.push().setValue(giveItems).addOnCompleteListener {
                            Toast.makeText(context, "Failed to upload image, but data is saved", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                val giveItems = GiveItem("", address, R.drawable.mapflag, currentTime, extra)
                database.push().setValue(giveItems).addOnCompleteListener {
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                }
            }
            if (parentFragment is FragmentChangeListener) {
                (parentFragment as FragmentChangeListener).switchFragment(0) // switch to GetFragment (assuming it is at index 0)
            }
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode ==
            Activity.RESULT_OK && data != null) {
            imageUri = data.data
            selectedImage.setImageURI(imageUri)
        }
    }
}