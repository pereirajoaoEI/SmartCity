package ipvc.estg.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import ipvc.estg.smartcity.api.EndPoints
import ipvc.estg.smartcity.api.ServiceBuilder
import ipvc.estg.smartcity.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


            val button = findViewById<ImageButton>(R.id.notas)
            button.setOnClickListener {
                val intent = Intent(this, Notas::class.java)
                startActivity(intent)
            }


            val botao = findViewById<Button>(R.id.botao_login)

            botao.setOnClickListener {


                val nomeEditText = findViewById<EditText>(R.id.nome)
                val passEditText = findViewById<EditText>(R.id.password)

                val nome = nomeEditText.text.toString().trim()
                val pass = passEditText.text.toString().trim()

                Log.i("login", nome)
                Log.i("login", pass)

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.verifyUsers(username = nome, password = pass)

                Log.i("login", call.toString())


                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {

                            val user = response.body()!!

                            val intent = Intent(this@Login, MenuLogado::class.java)
                            startActivity(intent)
                            finish()
                            val sessaoAuto: SharedPreferences = getSharedPreferences(
                                getString(R.string.shared_preferences),
                                Context.MODE_PRIVATE
                            )
                            with(sessaoAuto.edit()) {
                                putBoolean(getString(R.string.loged), true)
                                putString(getString(R.string.username), nome)
                                putInt(getString(R.string.id), user.id)
                                Log.i("call", user.id.toString())

                                apply()
                            }
                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@Login, "Login Errado!", Toast.LENGTH_SHORT).show()
                    }
                    })
            }
        }
    }



