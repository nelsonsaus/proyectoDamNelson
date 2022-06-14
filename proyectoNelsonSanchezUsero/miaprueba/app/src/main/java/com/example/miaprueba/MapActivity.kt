package com.example.miaprueba

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.miaprueba.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    lateinit var binding: ActivityMapBinding
    lateinit var google: GoogleMap


    val LOCALIZACION_PERMISO_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ponerMap()
    }

    private fun ponerMap() {

        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        map.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap) {
        google = p0
        google.setOnMyLocationButtonClickListener(this)
        google.setOnMyLocationClickListener(this)
        google.uiSettings.isZoomControlsEnabled = true
        marcas()
        enableLocation()
    }



    private fun marcas() {

        var coordenadas = LatLng(36.84280635016291, -2.4557272576711306)
        var marca = MarkerOptions().position(coordenadas).title("SpartanGym")

        google.addMarker(marca)
        google.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 15f), 6000, null)


    }

    override fun onMyLocationButtonClick(): Boolean {

        return false

    }

    override fun onMyLocationClick(p0: Location) {

    }

    private fun enableLocation() {
        if (!::google.isInitialized) return //que no hace nada si no esta inicializado
        if (permisos()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            google.isMyLocationEnabled = true
        } else {
            pedir()
        }
    }

    private fun permisos(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun pedir() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            //pedimos permisos y se han rechazado o uno de ellos o los dos
            Toast.makeText(this, "Los permisos se han rechazado", Toast.LENGTH_SHORT).show()
        } else {
            //pedimos permisos por primera vez
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCALIZACION_PERMISO_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCALIZACION_PERMISO_CODE -> {
                if (grantResults.isNotEmpty() && (grantResults[0]) == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    google.isMyLocationEnabled = true
                } else {
                    Toast.makeText(this, "Se rechazo algun permiso", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }
}