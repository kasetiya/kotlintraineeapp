package com.softices.kotlintraineeapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.extra.L
import kotlinx.android.synthetic.main.activity_google_map.*

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private val TAG = "GoogleMapActivity"
    lateinit var mapFragment: SupportMapFragment
    lateinit var mGoogleMap: GoogleMap

    private val REQUEST_CHECK_SETTINGS = 1;
    private var mGoogleApiClient: GoogleApiClient? = null
    private var locationManager: LocationManager? = null
    private var mLocationManager: LocationManager? = null
    private val UPDATE_INTERVAL = (5 * 1000).toLong()
    private val FASTEST_INTERVAL: Long = 5000
    private lateinit var latLng: LatLng
    private var mLocationRequest: LocationRequest? = null

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //initialize map fragment
        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)!!
        mapFragment.getMapAsync(this)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fab.setOnClickListener {
            checkPermission()
        }
        checkPermission()
    }

    /**
     * executes when google api client connected.
     * @param p0
     */
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        checkPermission()
    }

    /**
     * executes when google api client connection suspended
     * @param i
     */
    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode())
    }

    override fun onResume() {
        super.onResume()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        } else {
            checkPermission()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient!!.disconnect()
        }
    }


    // change map
    private fun assignToMap() {
        if (latLng != null) {
            mGoogleMap.clear()
            val options = MarkerOptions()
                    .position(latLng)
                    .title("My Location")
            mGoogleMap.apply {
                addMarker(options)
                moveCamera(CameraUpdateFactory.newLatLng(latLng))
                animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        latLng = LatLng(location.latitude, location.longitude)
        assignToMap()
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        if (mGoogleApiClient!!.isConnected) {
            // Create the location request
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL)
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest!!)
            builder.setAlwaysShow(true);

            val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
            result.setResultCallback { locationSettingsResult ->
                val status = locationSettingsResult.status
                val state = locationSettingsResult.locationSettingsStates
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        L.t(this, "Fetching location...")
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                                mLocationRequest, this)
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        L.t(this, "No GPS available.")
                    }
                }
            }
            Log.d("reque", "--->>>>")
        } else {
            if (mGoogleApiClient != null) {
                mGoogleApiClient!!.connect()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
        // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.e("Settings", "Result OK")
                    if (mGoogleApiClient!!.isConnected) {
                        L.t(this, "Fetching location...")
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                                mLocationRequest, this)
                    } else {
                        mGoogleApiClient!!.connect()
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("Settings", "Result Cancel")
                    L.t(this, "GPS is Disabled in your device")
                }
            }
        }
    }

    /**
     * when map is ready to use
     * @param googleMap
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mGoogleMap = googleMap
            googleMap.uiSettings.setAllGesturesEnabled(true)
            googleMap.uiSettings.isZoomControlsEnabled()
        }
    }

    /**
     * check pemission given for location
     * and version above 23.
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            startLocationUpdates()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest
                        .permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 152)
        }
    }

    /**
     * @param permissions
     * @param requestCode
     * @param grantResults
     *
     * when runs when permission dialog close
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            when (requestCode) {
                152 -> goWithCameraPermission(grantResults)
            }
        }
    }

    private fun goWithCameraPermission(grantResults: IntArray) {
        if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED
                && grantResults.get(1) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest
                    .permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 152)
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}