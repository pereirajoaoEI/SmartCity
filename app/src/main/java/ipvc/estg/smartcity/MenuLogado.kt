package ipvc.estg.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MenuLogado : AppCompatActivity(), SensorEventListener {

    private lateinit var  sensorManager: SensorManager
    private var brightness: Sensor?= null
    private lateinit var text: TextView
    private lateinit var pb: CircularProgressBar
    private lateinit var square: TextView

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_logado)

        val bemVindo = findViewById<TextView>(R.id.bemVindo)

        val sessaoAuto: SharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
       bemVindo.setText(getString(R.string.bemvindouser)+" "+sessaoAuto.getString("username", null)+" !")

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        text = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)
        square = findViewById(R.id.tv_square)

        setUpSensorStuff()

        val button2 = findViewById<ImageButton>(R.id.notas)
        button2.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.mapa)
        button3.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
        }

        val logout = findViewById<Button>(R.id.logout)

        logout.setOnClickListener {
            val sessaoAuto: SharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )
            with(sessaoAuto.edit()) {
                clear()
                apply()
            }
            val intent = Intent(this@MenuLogado, Login::class.java)
            startActivity(intent)
            finish()
        }


    }


    //sensores

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setUpSensorStuff(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{

            sensorManager.registerListener(this,
                    it,
                    SensorManager.SENSOR_DELAY_FASTEST,
                    SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    private fun brightness(brightness: Float):String {
        return when (brightness.toInt()) {
            0 -> "Totalmente Escuro"
            in 1..10 -> "Muito Escuro"
            in 11..50 -> "Escuro"
            in 51..5000 -> "Normal"
            in 5001..25000-> "Luminosidade"
            else -> "Muita Luminosidade"

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            val light = event.values[0]

            text.text = "Sensor: $light\n${brightness(light)}"
            pb.setProgressWithAnimation(light)
        }

        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val upDown = event.values[1]

            square.apply{
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * -10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.TRANSPARENT else Color.GREEN
            square.setBackgroundColor(color)

            square.text=getString(R.string.cimabaixo)+" ${upDown.toInt()}\n"+getString(R.string.esquerdadireita)+" ${sides.toInt()}"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

}