package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter

class ElectionsFragment: Fragment() {

    lateinit var viewModel: ElectionsViewModel //by viewModels()

    lateinit var binding: FragmentElectionBinding

    private lateinit var factory: ElectionsViewModelFactory

    private lateinit var adapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentElectionBinding.inflate(inflater, container, false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        //TODO: Link elections to voter info


        //TODO: Initiate recycler adapters
        binding.upcomingElectionRv.adapter = adapter
        binding.savedElectionRv.adapter = adapter

        //TODO: Populate recycler adapters

        viewModel.savedElections.observe(viewLifecycleOwner, { elections ->
            savedElectionAdapter.submitList(elections)
        })
        //TODO: Refresh adapters when fragment loads

        return binding.root
    }
}