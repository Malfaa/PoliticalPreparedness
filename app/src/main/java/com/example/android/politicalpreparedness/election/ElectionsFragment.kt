package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.database.election.UpcomingElectionDatabase
import com.example.android.politicalpreparedness.database.voterinfo.VoterInfoDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionsRepository

class ElectionsFragment: Fragment() {

    private val viewModel : ElectionsViewModel by viewModels {
        ElectionsViewModelFactory(
            ElectionsRepository(
                ElectionDatabase.getInstance(requireContext()),
                VoterInfoDatabase.getInstance(requireContext()),
                UpcomingElectionDatabase.getInstance(requireContext()),
                CivicsApi
            )
        )
    }
    lateinit var binding: FragmentElectionBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentElectionBinding.inflate(inflater, container, false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //Upcoming Elections
        val upcomingElections = ElectionListAdapter(ElectionListener { election ->
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
            )
        })
        binding.upcomingElectionRv.adapter = upcomingElections
        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            upcomingElections.submitList(elections)
        }




        //Saved on Database Elections
        val savedElectionAdapter = ElectionListAdapter(ElectionListener { election ->
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election))
        })
        binding.savedElectionRv.adapter = savedElectionAdapter
        viewModel.savedElections.observe(viewLifecycleOwner) { elections ->
            savedElectionAdapter.submitList(elections)
        }


        return binding.root
    }
}