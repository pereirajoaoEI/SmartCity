package ipvc.estg.smartcity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ipvc.estg.smartcity.api.EndPoints
import ipvc.estg.smartcity.api.Markers
import ipvc.estg.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InsertAnomalia : AppCompatActivity() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var editTextDesc : EditText
    private lateinit var editTextTitle : EditText
    private lateinit var spinnerAnomalia : Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_anomalia)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val inserir = findViewById<Button>(R.id.button3)
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
                this,
                R.array.type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        //inserção
        inserir.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return@setOnClickListener
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    if (location != null) {
                        lastLocation = location

                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        var id: Int? = 0

                        val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences),
                            Context.MODE_PRIVATE
                        )

                        id = sessaoAuto.all[getString(R.string.id)] as Int?


                        editTextTitle = findViewById(R.id.tituloAnomalia)
                        editTextDesc = findViewById(R.id.descricaoAnomalia)
                        spinnerAnomalia = findViewById(R.id.spinner)





                            val titulo = editTextTitle.text.toString()
                            val descricao = editTextDesc.text.toString()
                            val foto = ""
                            val login_id = id
                            val lat = lastLocation.latitude
                            val longi = lastLocation.longitude
                            val spinnerVal = spinnerAnomalia.selectedItem.toString()



                            Log.i("latitude", lat.toString())
                            Log.i("longitude", longi.toString())
                            Log.i("login_id", login_id.toString())
                            Log.i("titulo", titulo)
                            Log.i("descricao", descricao)
                            Log.i("spinner", spinnerVal)

                            val request = ServiceBuilder.buildService(EndPoints::class.java)
                            val call = request.criarAnomalias(titulo, descricao, lat.toString(), longi.toString(), foto, login_id.toString().toInt(), spinnerVal)

                             if (TextUtils.isEmpty(editTextTitle.text) || TextUtils.isEmpty(editTextDesc.text)) {
                            Toast.makeText(this, R.string.preencacampos, Toast.LENGTH_SHORT).show()
                            }else {

                            call.enqueue(object : Callback<Markers> {
                                override fun onResponse(call: Call<Markers>, response: Response<Markers>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@InsertAnomalia, R.string.adicionado, Toast.LENGTH_SHORT).show()

                                    }

                                    val intent = Intent(this@InsertAnomalia, Mapa::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                                override fun onFailure(call: Call<Markers>?, t: Throwable?) {
                                    Toast.makeText(applicationContext, t!!.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }



}}


