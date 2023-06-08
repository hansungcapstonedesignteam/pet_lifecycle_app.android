package com.hansung.petlifetimecare

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hansung.petlifetimecare.adoptPackage.AdoptHomeFragment
import com.hansung.petlifetimecare.mapPackage.MapHospitalActivity
import com.hansung.petlifetimecare.petShopPackage.MapPetShopActivity
import com.hansung.petlifetimecare.mapPackage.MapWalkActivity


class HomeFragment : Fragment() {

//    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent())
//    { uri: Uri? ->
//        if (uri != null) {
//            loadImageIntoImageButton(uri)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        val petImageButton: ImageButton = view.findViewById(R.id.petImage)
//        petImageButton.setOnClickListener {
//            getContent.launch("image/*")
//            val belowText : TextView = view.findViewById(R.id.textImageBelow)
//            belowText.text = ""
//        }

        val walkingPetButton : ImageButton = view.findViewById(R.id.walkingButton)
        walkingPetButton.setOnClickListener{
            val intent:Intent = Intent(requireActivity(),MapWalkActivity::class.java)
            startActivity(intent)
        }

        val hospitalButton : ImageButton = view.findViewById(R.id.petHospitalButton)
        hospitalButton.setOnClickListener{
            val intent:Intent = Intent(requireActivity(),MapHospitalActivity::class.java)

            startActivity(intent)
        }

        val petShopButton: ImageButton = view.findViewById(R.id.petShopButton)
        petShopButton.setOnClickListener {
            val intent = Intent(requireActivity(), MapPetShopActivity::class.java)
            startActivity(intent)
        }

        val adoptButton : ImageButton = view.findViewById(R.id.adoptButton)
        adoptButton.setOnClickListener{
            val toAdoptHomeFragment = AdoptHomeFragment()
            val fragmentManager = parentFragmentManager

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.mainFrame,toAdoptHomeFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

/*
        val petShopButton: ImageButton = view.findViewById(R.id.petShopButton)
        petShopButton.setOnClickListener {
            val toPetShopFragment = ShopFragment()
            val fragmentManager = parentFragmentManager

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.mainFrame, toPetShopFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
*/
        return view
    }

//    private fun loadImageIntoImageButton(uri: Uri) {
//        val petImageButton: ImageButton = requireView().findViewById(R.id.petImage)
//        setImageToImageButton(uri, 700, 450, petImageButton)
//    }

    private fun setImageToImageButton(uri: Uri, targetWidth: Int, targetHeight: Int, imageButton: ImageButton) {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = resizeBitmap(originalBitmap, targetWidth, targetHeight)
        imageButton.setImageBitmap(resizedBitmap)
    }

    private fun resizeBitmap(originalBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase reference
        val database = Firebase.database
        val myRef = database.getReference("walkStartTime")

        val dateTextView: TextView = view.findViewById(R.id.textView15)

        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val date = dataSnapshot.getValue(String::class.java)
                date?.let {
                    dateTextView.text = it
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })

        //... rest of your code
    }


}