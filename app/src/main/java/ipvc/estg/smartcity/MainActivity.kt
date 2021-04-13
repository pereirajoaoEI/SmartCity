package ipvc.estg.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<ImageButton>(R.id.notas)
        button.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<ImageButton>(R.id.login)
        button2.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val sessao_auto: SharedPreferences =getSharedPreferences(getString(R.string.shared_preferences),
            Context.MODE_PRIVATE)
    }
}