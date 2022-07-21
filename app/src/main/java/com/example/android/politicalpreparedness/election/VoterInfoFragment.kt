package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionsRepository

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var factory: VoterInfoViewModelFactory

    private val arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        val dataSource = ElectionDatabase.getInstance(requireContext())
        factory = VoterInfoViewModelFactory(ElectionsRepository(dataSource, CivicsApi), arguments.argElectionId, arguments.argDivision)
        viewModel = ViewModelProvider(this, factory)[VoterInfoViewModel::class.java]

        //ViewModel values and create ViewModel
        binding.viewModel = viewModel



        //TODO: Add binding values


        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        binding.viewModel

        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    //TODO: Create method to load URL intents

}