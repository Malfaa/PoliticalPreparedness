package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.election.ElectionDao
import com.example.android.politicalpreparedness.repository.VoterInfoRepository


class VoterInfoViewModelFactory(val repo: VoterInfoRepository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel unknown")
    }

}