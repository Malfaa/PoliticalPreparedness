package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.util.*

class RepresentativeFragment : Fragment() {

    companion object {
        //add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION_REQUEST_CODE = 1

        private const val REP_SEARCH_KEY = "REP_SEARCH_KEY"

        const val REP_LIST_KEY = "REP_LIST_KEY"

        private const val REP_RECYCLER_POS = "REP_RECYCLER_POS"

        private const val MOTION_STATE_KEY = "MOT_STATE_KEY"
    }

    private lateinit var binding: FragmentRepresentativeBinding

    //declare ViewModel
    private val viewModel by viewModels<RepresentativeViewModel> {
        RepresentativeViewModelFactory(RepresentativeRepository(
            CivicsApi
        ), requireActivity().application, SavedStateHandle()
        )
    }

    private val adapter = RepresentativeListAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //define and assign Representative adapter
        // populate Representative adapter
        binding.representativesRecyclerView.adapter = adapter

        if (savedInstanceState != null){
            binding.motionLayout.transitionToState(savedInstanceState.getInt(MOTION_STATE_KEY)) //certo

            viewModel.savingStance(savedInstanceState.getParcelable(REP_SEARCH_KEY)!!)

            binding.representativesRecyclerView.layoutManager!!.onRestoreInstanceState(savedInstanceState.getParcelable(REP_RECYCLER_POS))

            viewModel.representatives.value = savedInstanceState.getParcelableArrayList(REP_LIST_KEY)
        }


        viewModel.representatives.observe(viewLifecycleOwner){
                representative ->

            adapter.submitList(representative)
        }


        binding.searchButton.setOnClickListener {
            hideKeyboard()
            viewModel.onSearchButtonClick()
        }


        //establish button listeners for field and location search
        binding.locationButton.setOnClickListener { requestLocationPermissions() }

        return binding.root
    }

    private fun requestLocationPermissions() {
        if (checkLocationPermissions()) {
            checkSettingsReturnLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //request Location permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //check if permission is already granted and return (true = granted, false = denied/other)
        var granted = false

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            granted = true
        }

        return granted
    }

    private fun checkSettingsReturnLocation(resolve:Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                try {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION_REQUEST_CODE
                    )

                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            } else {
                Log.e("Error Location", "Location Required Error")
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                getLocation()
            }
        }
    }

    //get location from LocationServices
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.let {
                    val address = geoCodeLocation(it.lastLocation)
                    viewModel.refreshByCurrentLocation(address)

                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                it
            )
        }}

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val motionLayoutState = binding.motionLayout.currentState
        val repList = viewModel.address.value
        val recyclerPositionState: Parcelable = binding.representativesRecyclerView.layoutManager!!.onSaveInstanceState()!!

        super.onSaveInstanceState(outState)

        outState.putInt(MOTION_STATE_KEY, motionLayoutState) //motionLayout
        outState.putParcelable(REP_SEARCH_KEY, repList) //search
        outState.putParcelable(REP_RECYCLER_POS,recyclerPositionState) //scrollPosition
        outState.putParcelableArrayList(REP_LIST_KEY, viewModel.representatives.value as ArrayList<out Parcelable>) //list

    }
}