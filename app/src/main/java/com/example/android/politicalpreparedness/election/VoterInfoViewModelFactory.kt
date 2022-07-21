package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionsRepository


class VoterInfoViewModelFactory(private val repository: ElectionsRepository, private val id: Int, private val division: Division): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(repository, id, division) as T
        }
        throw IllegalArgumentException("ViewModel unknown")
    }

}