package com.hansung.petlifetimecare.dogChoicePackage

import Question1Fragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.hansung.petlifetimecare.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StartDogFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val startButton: Button = view.findViewById(R.id.startButton)
        startButton.setOnClickListener {
                navigateToQuestion1Fragment()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_start_dog, container, false)
    }

    private fun navigateToQuestion1Fragment() {
        val question1Fragment = Question1Fragment()

        parentFragmentManager.beginTransaction()
            .hide(this)
            .replace(R.id.mainFrame, question1Fragment) // "mainFrame" is your Fragment's container id
            .commit()
    }

}