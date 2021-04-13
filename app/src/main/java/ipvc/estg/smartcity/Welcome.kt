package ipvc.estg.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val sessaoAuto: SharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        Handler(Looper.getMainLooper()).postDelayed({
            if (sessaoAuto.getBoolean("loged", false)) {
                val intent = Intent(this@Welcome, MenuLogado::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@Welcome, Login::class.java)
                startActivity(intent)
                finish()
            }
        },1500)




    }
}