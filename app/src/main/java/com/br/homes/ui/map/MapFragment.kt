package com.example.homes.ui.home

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.br.homes.R
import com.br.homes.Util.*
import com.br.homes.model.Property
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_map.*
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.CEP
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.RetrofitInitializer
import java.util.jar.Manifest


class MapFragment : Fragment(), OnMapReadyCallback {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()
    private lateinit var mMap: GoogleMap
    lateinit var currentLocation: Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }

        mMap = googleMap
        markers()
        mMap.setMyLocationEnabled(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
       // fetchLastLocation()


       /*mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                currentMarker = marker
                for(i  in 0..10) {
                    print(propertyTitle.get(0).latitude.toString())
                   /* if(property.equals(marker.position.latitude.toString())) {
                        AlertDialog.Builder(context!!)
                            .setTitle("${marker.position.latitude}")
                            .setMessage("")
                            .setPositiveButton("Ok", null)
                            .show()
                    } else {
                        Log.d("cassia", "nops")
                    }

                    */
                }

                return true
            }

        })

        */

    }

    fun fetchLastLocation() {

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            currentLocation = task.result!!
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            latLongUtil.add(latLng)
            mMap.addMarker(
                MarkerOptions().position(latLongUtil.get(0))
                    .title("Sua localização")
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE
                        )
                    )
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongUtil.get(0), 16.9f));
        }
    }

    fun markers() {
        val ref = firebaseDatabase.getReference("PropertyAll/")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertyUtilAll.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val property = postSnapshot.getValue(Property::class.java) as Property
                    propertyUtilAll.add(property)
                    val location =
                        "${property.address}, ${property.complement}, ${property.burgh}, Amazonas, AM, ${property.cep}"
                    val addressList: List<Address>
                    if (!location.equals("")) {
                        val geocoder = Geocoder(context)
                        addressList = geocoder.getFromLocationName(location, 1)
                        val address = addressList.get(0)
                        val latLng = LatLng(address.latitude, address.longitude)
                        propertyTitle.add(latLng)

                        if (property.option.equals("Alugar")) {
                            alugarUtil.add(property)
                            mMap.addMarker(
                                MarkerOptions().position(latLng)
                                    .title("${property.address} - R$${property.price}")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                    )
                            )
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.9f))
                        } else {
                            venderUtil.add(property)
                            mMap.addMarker(
                                MarkerOptions().position(latLng)
                                    .title("${property.address} - R$${property.price}")
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_CYAN
                                        )
                                    )
                            )
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.9f))


                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar as casas", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(postListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vender.setOnClickListener {
            mMap.clear()
            markersVender()
            fetchLastLocation()
        }
        alugar.setOnClickListener {
            mMap.clear()
            markersAlugar()
            fetchLastLocation()
        }
        map_all.setOnClickListener {
            mMap.clear()
            markersAlugar()
            fetchLastLocation()
            markersVender()
        }
    }

    fun markersVender() {
        var i = 0

        while (i < venderUtil.size) {
            val location =
                "${venderUtil.get(i).address}, ${venderUtil.get(i).complement}, ${venderUtil.get(i).burgh}, Amazonas, AM, ${venderUtil.get(
                    i
                ).cep}"
            val addressList: List<Address>
            if (!location.equals("")) {
                val geocoder = Geocoder(context)
                addressList = geocoder.getFromLocationName(location, 1)
                val address = addressList.get(0)
                addressList.get(0).locality
                val latLng = LatLng(address.latitude, address.longitude)

                mMap.addMarker(
                    MarkerOptions().position(latLng)
                        .title("${venderUtil.get(i).option} - R$ ${venderUtil.get(i).price}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                )
            }
            i++

        }
    }
    fun markersAlugar() {
        var i = 0

        while (i < alugarUtil.size) {
            val location =
                "${alugarUtil.get(i).address}, ${alugarUtil.get(i).complement}, ${alugarUtil.get(i).burgh}, Amazonas, AM, ${alugarUtil.get(
                    i
                ).cep}"
            val addressList: List<Address>
            if (!location.equals("")) {
                val geocoder = Geocoder(context)
                addressList = geocoder.getFromLocationName(location, 1)
                val address = addressList.get(0)
                var cep = address.countryCode

                val latLng = LatLng(address.latitude, address.longitude)

                mMap.addMarker(
                    MarkerOptions().position(latLng)
                        .title("${alugarUtil.get(i).option} - R$ ${alugarUtil.get(i).price}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
            }
            i++

        }
    }

    override fun onResume() {
        super.onResume()

        alugarUtil.clear()
        venderUtil.clear()
        markers()
    }

}