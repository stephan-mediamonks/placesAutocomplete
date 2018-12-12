package com.mediamonks.testplacesautocomplete.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import com.mediamonks.testplacesautocomplete.R
import com.mediamonks.testplacesautocomplete.ui.viewmodel.PlacePredictionViewModel
import com.mediamonks.testplacesautocomplete.util.permissions.PermissionHelper
import com.mediamonks.testplacesautocomplete.util.permissions.PermissionListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var permissionHelper: PermissionHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var placePredictionViewModel: PlacePredictionViewModel

    private val permissionListener: PermissionListener = { permission, isGranted ->
        if (permission == PermissionHelper.Permission.ACCESS_FINE_LOCATION && isGranted) {
            updateUserLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container, PlacesFragment.newInstance()).commit()

        placePredictionViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlacePredictionViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        if (permissionHelper.hasPermission(this, PermissionHelper.Permission.ACCESS_FINE_LOCATION)) {
            updateUserLocation()
        } else {
            permissionHelper.addPermissionListener(permissionListener)

            permissionHelper.requestPermission(this, PermissionHelper.Permission.ACCESS_FINE_LOCATION, "")
        }
    }

    override fun onPause() {
        super.onPause()

        permissionHelper.removePermissionListener(permissionListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    private fun updateUserLocation() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { placePredictionViewModel.updateUserLocation(it) }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = dispatchingAndroidInjector
}
