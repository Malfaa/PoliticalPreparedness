package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.RepresentativeRepository

class RepresentativeViewModelFactory(private val repo: RepresentativeRepository, private val app: Application, private val ssh: SavedStateHandle) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)){
            return RepresentativeViewModel(repo, app, ssh) as T
        }
        throw IllegalArgumentException("Viewmodel unknown")
    }
}