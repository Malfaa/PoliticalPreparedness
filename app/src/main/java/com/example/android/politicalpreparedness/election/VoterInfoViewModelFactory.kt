package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao


class VoterInfoViewModelFactory(val dao: ElectionDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(dao) as T
        }
        throw IllegalArgumentException("ViewModel unknown")
    }

}