package com.example.android.politicalpreparedness.network

import android.telephony.ClosedSubscriberGroupInfo
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //Elections API Call
    @GET("elections")
    fun getElections(): ElectionResponse

    @GET("elections")
    suspend fun getElectionsJsonStr(): String

    //Voterinfo API Call
    @GET("voterinfo")
    fun getVoterInfo(
        @Query("address")
        address: String,
        @Query("electionId")
        electionId: Int
    ): VoterInfoResponse

    @GET("voterinfo")
    suspend fun getVoterInfoJsonStr(
        @Query("address")
        address: String,
        @Query("electionId")
        electionId: Int
    ): String

    //Representatives API Call
    @GET("representatives")
    fun getRepresentatives(
        @Query("address")
        address: String
    ):RepresentativeResponse

    @GET("representatives")
    suspend fun getRepresentativesJsonStr(
        @Query("address")
        address: String
    ): String
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }

    //getElections
    fun getElections() = retrofitService.getElections()
    suspend fun getElectionsJsonStr() = retrofitService.getElectionsJsonStr()

    //getVoterInfo
    fun getVoterInfo(address: String, electionId: Int) = retrofitService.getVoterInfo(address, electionId)
    suspend fun getVoterInfoJsonStr(address: String, electionId: Int) = retrofitService.getVoterInfoJsonStr(address, electionId)

    //getRepresentatives
    fun getRepresentatives(address: String) = retrofitService.getRepresentatives(address)
    suspend fun getRepresentativesJsonStr(address: String) = retrofitService.getRepresentativesJsonStr(address)
}