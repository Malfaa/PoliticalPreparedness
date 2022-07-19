package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding

class ElectionsFragment: Fragment() {

    lateinit var viewModel: ElectionsViewModel //by viewModels()

    lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //TODO: Add binding values
        binding = FragmentElectionBinding.inflate(inflater, container, false)

        //TODO: Link elections to voter info


        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters


    }


    override fun onStart() {
        super.onStart()


    }
//TODO: Refresh adapters when fragment loads

}