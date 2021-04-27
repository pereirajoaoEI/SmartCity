package ipvc.estg.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MenuLogado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_logado)

        val bemVindo = findViewById<TextView>(R.id.bemVindo)

        val sessaoAuto: SharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
       bemVindo.setText("Bem-vindo "+sessaoAuto.getString("username", null)+" !")



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

}