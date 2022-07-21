package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativeRepository(//SHOWS EXCLUSiVeLY FROM THE API
    private val api: CivicsApi
) {

    private val _representatives = MutableLiveData<List<Representative>?>()
    val representatives: LiveData<List<Representative>?>
        get() = _representatives

    suspend fun refreshListRep(address: String){
        withContext(Dispatchers.IO) {
            _representatives.postValue(null)

            val repResponse = api.getRepresentatives(address)
            val repList = repResponse.offices.flatMap { office ->
                office.getRepresentatives(repResponse.officials)
            }

            _representatives.postValue(repList) //as MutableList<Representative>?
        }
    }

}