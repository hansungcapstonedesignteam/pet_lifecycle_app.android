package com.hansung.petlifetimecare.dogChoicePackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hansung.petlifetimecare.R

class Question3Fragment : Fragment() {

    private lateinit var dogPreference: DogPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogPreference = arguments?.getParcelable<DogPreference>("dogPreference") ?: DogPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question3, container, false)

        val buttonYes = view.findViewById<Button>(R.id.button)
        val buttonNo = view.findViewById<Button>(R.id.button2)

        buttonYes.setOnClickListener {
            dogPreference.hairLoss = "네"
            navigateToNextFragment()
        }

        buttonNo.setOnClickListener {
            dogPreference.hairLoss = "아니오"
            navigateToNextFragment()
        }

        return view
    }

    private fun navigateToNextFragment() {
        val bundle = Bundle().apply {
            putParcelable("dogPreference", dogPreference)
        }

        // Update the below line to navigate to your next Fragment
        val question4Fragment = Question4Fragment() // replace with your next Fragment
        question4Fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .hide(this)
            .replace(R.id.mainFrame, question4Fragment)  // replace 'fragment_container' with your Fragment's container id
            .commit()
    }
}
