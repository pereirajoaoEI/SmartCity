package ipvc.estg.smartcity

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.squareup.picasso.Picasso
import ipvc.estg.smartcity.api.EndPoints
import ipvc.estg.smartcity.api.Markers
import ipvc.estg.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mapa : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var lastLocation: Location



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        //boas
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        //Filtro por users..
        val sessaoAuto: SharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllAnomalias()


        call.enqueue(object : Callback<List<Markers>> {
            override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {
                Log.i("response", response.toString())


                if (response.isSuccessful) {
                    val anomalias = response.body()!!

                    for(i in anomalias){
                        val latlong = LatLng(i.lat,i.longi)

                        if(i.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                            map.addMarker(MarkerOptions()
                                    .position(latlong)
                                    .title(i.titulo)
                                    .snippet(i.descricao)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                        }else{
                            map.addMarker(MarkerOptions().position(latlong).title(i.titulo).snippet(i.descricao))


                        }
                    }

                }
            }
            override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                Toast.makeText(this@Mapa, "Markers Errado!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        setUpMap()


        // 1
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
        map.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                Log.i("local",currentLatLng.toString())
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }

        map.setOnMarkerClickListener {
            map.setInfoWindowAdapter(MarkerInfo(this))
            false
        }


    }


   override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)



            return
        }
    }





}


