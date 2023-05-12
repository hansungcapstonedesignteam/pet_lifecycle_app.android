package com.hansung.petlifetimecare.adoptPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.hansung.petlifetimecare.R


class GiveFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_apply_adopt, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spinner_animal)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.pet,
            android.R.layout.simple_spinner_item
        ).also { adapter->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        return view


    }


    }
