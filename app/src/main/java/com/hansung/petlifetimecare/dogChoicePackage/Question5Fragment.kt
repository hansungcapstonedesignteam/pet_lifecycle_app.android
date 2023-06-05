package com.hansung.petlifetimecare.dogChoicePackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hansung.petlifetimecare.R

class Question5Fragment : Fragment() {

    private lateinit var dogPreference: DogPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogPreference = arguments?.getParcelable<DogPreference>("dogPreference") ?: DogPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question5, container, false)

        val button1 = view.findViewById<Button>(R.id.button)
        val button2= view.findViewById<Button>(R.id.button2)
        val button3= view.findViewById<Button>(R.id.button3)
        val button4= view.findViewById<Button>(R.id.button4)


        button1.setOnClickListener {
            dogPreference.walkFrequency= "매우 자주"
            navigateToNextFragment()
        }

        button2.setOnClickListener {
            dogPreference.walkFrequency = "자주"
            navigateToNextFragment()
        }
        button3.setOnClickListener {
            dogPreference.walkFrequency = "덜 자주"
            navigateToNextFragment()
        }
        button4.setOnClickListener {
            dogPreference.walkFrequency = "적게"
            navigateToNextFragment()
        }

        return view
    }

    private fun navigateToNextFragment() {
        val bundle = Bundle().apply {
            putParcelable("dogPreference", dogPreference)
        }

        // Update the below line to navigate to your next Fragment
        val question6Fragment = Question6Fragment() // replace with your next Fragment
        question6Fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .hide(this)
            .replace(R.id.mainFrame, question6Fragment)  // replace 'fragment_container' with your Fragment's container id
            .commit()
    }
}
