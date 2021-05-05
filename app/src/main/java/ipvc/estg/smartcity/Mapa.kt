package ipvc.estg.smartcity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
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
import ipvc.estg.smartcity.api.EndPoints
import ipvc.estg.smartcity.api.Markers
import ipvc.estg.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class Mapa : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var results = FloatArray(1)




    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var lastLocation: Location

//

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        //boas
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




        val layout = findViewById<RelativeLayout>(R.id.layout)

        val geek1 = RadioButton(this)
        geek1.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek1.setText(R.string.transito) //setting text of first radio button
        geek1.id = 0


        val geek2 = RadioButton(this)
        geek2.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek2.setText(R.string.danificacoes) //setting text of first radio button
        geek2.id = 1

        val geek3 = RadioButton(this)
        geek3.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek3.setText(R.string.outros) //setting text of first radio button
        geek3.id = 2

        val geek4 = RadioButton(this)
        geek4.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek4.setText(R.string.todos) //setting text of first radio button
        geek4.id = 3
        geek4.setChecked(true)

        val radioGroup = RadioGroup(this)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(40, 0, 0, 0)
        radioGroup.layoutParams = params

        radioGroup.addView(geek1)
        radioGroup.addView(geek2)
        radioGroup.addView(geek3)
        radioGroup.addView(geek4)
        layout.addView(radioGroup)


        val switchDistancia = findViewById<Switch>(R.id.switch1)
        switchDistancia.setOnCheckedChangeListener { _, isCheked ->
            if (isCheked) {
                filtroDistancia()
                geek4.setChecked(true)
            } else {
                onMapReady(map)

            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                0 -> getTransito()
                1 -> getDanificacoes()
                2 -> getOutros()
                3 -> onMapReady(map)
            }
            switchDistancia.setChecked(false)
        }


    }




    fun getTransito() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Transito")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.lat, anomalia.longi)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.descricao)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@Mapa, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun getDanificacoes() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Danificacoes")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.lat, anomalia.longi)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.descricao)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@Mapa, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun getOutros() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Outros")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.lat, anomalia.longi)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.descricao)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@Mapa, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.clear()
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

                            val marker: Marker? =
                                    map.addMarker(MarkerOptions()
                                    .position(latlong)
                                    .title(i.titulo)
                                    .snippet(i.tipo)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                            marker?.tag=i
                        }else{
                            val marker: Marker? =
                                    map.addMarker(MarkerOptions().position(latlong).title(i.titulo).snippet(i.descricao))
                            marker?.tag=i



                        }
                    }

                }
            }
            override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                Toast.makeText(this@Mapa, "Markers Errado!", Toast.LENGTH_SHORT).show()
            }
        })




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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.inserir -> {
                val intent = Intent(this, InsertAnomalia::class.java)
                startActivity(intent)
                finish()
                true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }




    fun calcularDistancia(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    fun filtroDistancia() {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomalias()
                    var position: LatLng


                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {

                                    position = LatLng(anomalia.lat, anomalia.longi)

                                    if (calcularDistancia(location.latitude, location.longitude, anomalia.lat, anomalia.longi) < 1000) {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distancia: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

                                    } else if (calcularDistancia(location.latitude, location.longitude, anomalia.lat, anomalia.longi) < 2000) {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distancia: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
                                    } else {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distancia: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@Mapa, "morreu", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }




}


