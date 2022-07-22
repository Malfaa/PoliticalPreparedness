package com.example.android.politicalpreparedness.election

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.election.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.google.android.material.snackbar.Snackbar

// TODO: ALTERAR CÓDIGO
class VoterInfoFragment : Fragment() { //Pegar as infos e atribuir ao layout

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var factory: VoterInfoViewModelFactory

    private val arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)

        val dataSource = ElectionDatabase.getInstance(requireContext())
        factory = VoterInfoViewModelFactory(ElectionsRepository(dataSource, CivicsApi))
        viewModel = ViewModelProvider(this, factory)[VoterInfoViewModel::class.java]

        //ViewModel values and create ViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.refresh(arguments.election)

        binding.stateLocations.setOnClickListener {
            val urlStr = viewModel.voterInfo.value?.locationUrl
            urlStr?.run {
                startActivityUrlIntent(this)
            }
        }

        binding.stateBallot.setOnClickListener {
            val urlStr = viewModel.voterInfo.value?.ballotInformationUrl
            urlStr?.run {
                startActivityUrlIntent(this)
            }
        }

        return binding.root
    }


    private fun startActivityUrlIntent(urlStr: String) {
        val uri: Uri = Uri.parse(urlStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
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
