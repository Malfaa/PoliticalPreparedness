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
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository

class ElectionsFragment: Fragment() {

    lateinit var viewModel : ElectionsViewModel

    lateinit var binding: FragmentElectionBinding

    private lateinit var factory: ElectionsViewModelFactory


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentElectionBinding.inflate(inflater, container, false)

        val dataSource = ElectionDatabase.getInstance(requireContext())
        factory = ElectionsViewModelFactory(ElectionsRepository(dataSource, CivicsApi))
        viewModel = ViewModelProvider(this, factory)[ElectionsViewModel::class.java]


        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        //link elections to voter info
        val adapter = ElectionListAdapter(
            ElectionListAdapter.ElectionListener{
                viewModel.navigateTo(it)
            }
        )

        val adapterSaved = ElectionListAdapter(
            ElectionListAdapter.ElectionListener {
                viewModel.navigateTo(it)
            }
        )


        //initiate recycler adapters
        binding.upcomingElectionRv.adapter = adapter
        binding.savedElectionRv.adapter = adapterSaved

        viewModel.navigate.observe(viewLifecycleOwner){
            election ->
            election?.let {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    it.id,
                    it.division
                ))
                viewModel.navigated()
            }
        }

        //Populate recycler adapters
        viewModel.getSavedElections.observe(viewLifecycleOwner){
            viewModel.savedElections(it)
        }
        viewModel.getUpcomingElections()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //refresh adapters when fragment loads
        viewModel.getUpcomingElections()
    }
}