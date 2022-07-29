package com.example.android.politicalpreparedness.election

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.database.election.UpcomingElectionDatabase
import com.example.android.politicalpreparedness.database.voterinfo.VoterInfoDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var factory: VoterInfoViewModelFactory

    private lateinit var arguments: VoterInfoFragmentArgs

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        val dataSourceElection = ElectionDatabase.getInstance(requireContext())
        val dataSourceUpcomingElection = UpcomingElectionDatabase.getInstance(requireContext())
        val dataSourceVoterInfo = VoterInfoDatabase.getInstance(requireContext())
        factory = VoterInfoViewModelFactory(ElectionsRepository(dataSourceElection, dataSourceVoterInfo, dataSourceUpcomingElection, CivicsApi))
        viewModel = ViewModelProvider(this, factory)[VoterInfoViewModel::class.java]

        //ViewModel values and create ViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.data(arguments.election)

//        viewModel.location.observe(viewLifecycleOwner) { condition ->
//            if(condition) {
//                val urlStr = viewModel.voterInfo.value?.locationUrl
//                urlStr?.run {
//                    toUrlIntent(this)
//                }
//                viewModel.stateLocationReturn()
//            }
//        }
//
//        viewModel.ballot.observe(viewLifecycleOwner) { condition ->
//            if (condition){
//                val urlStr = viewModel.voterInfo.value?.ballotInformationUrl
//                urlStr?.run {
//                    toUrlIntent(this)
//                }
//                viewModel.stateBallotReturn()
//            }
//        }
        binding.stateLocations.setOnClickListener {
            val url = viewModel.voterInfo.value?.locationUrl
            url?.run {
                toUrlIntent(this)
            }
            //viewModel.stateLocationReturn()
        }
        binding.stateBallot.setOnClickListener {
            val url = viewModel.voterInfo.value?.ballotInformationUrl
            url?.run {
                toUrlIntent(this)
            }
            //viewModel.stateBallotReturn()
        }

        return binding.root
    }


    private fun toUrlIntent(urlStr: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlStr))

        try {
            //Info for package_name is from https://acervolima.com/guias-personalizadas-do-chrome-no-android-com-kotlin/
            intent.setPackage("com.android.chrome")
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {

            try {
                intent.setPackage(null)
                startActivity(intent)

            } catch (e: ActivityNotFoundException) {
                val snack = Snackbar.make(
                    requireView(),
                    getString(R.string.no_web_browser_found_msg),
                    Snackbar.LENGTH_LONG
                )
                snack.show()
            }
        }
    }
}
