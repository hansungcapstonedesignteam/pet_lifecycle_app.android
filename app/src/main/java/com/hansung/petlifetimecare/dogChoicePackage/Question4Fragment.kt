package com.hansung.petlifetimecare.dogChoicePackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hansung.petlifetimecare.R

class Question4Fragment : Fragment() {

    private lateinit var dogPreference: DogPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogPreference = arguments?.getParcelable<DogPreference>("dogPreference") ?: DogPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question4, container, false)

        val button1 = view.findViewById<Button>(R.id.button)
        val button2= view.findViewById<Button>(R.id.button2)
        val button3= view.findViewById<Button>(R.id.button3)


        button1.setOnClickListener {
            dogPreference.training= "충성스러움"
            navigateToNextFragment()
        }

        button2.setOnClickListener {
            dogPreference.training = "온순"
            navigateToNextFragment()
        }
        button3.setOnClickListener {
            dogPreference.training = "영리함"
            navigateToNextFragment()
        }


        return view
    }

    private fun navigateToNextFragment() {
        val bundle = Bundle().apply {
            putParcelable("dogPreference", dogPreference)
        }

        // Update the below line to navigate to your next Fragment
        val question5Fragment = Question5Fragment() // replace with your next Fragment
        question5Fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .hide(this)
            .replace(R.id.mainFrame, question5Fragment)  // replace 'fragment_container' with your Fragment's container id
            .commit()
    }
}
