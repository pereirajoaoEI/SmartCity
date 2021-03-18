package ipvc.estg.smartcity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewWordActivity : AppCompatActivity() {

    private lateinit var titleNovo: EditText
    private lateinit var descricaoNova: EditText


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        titleNovo = findViewById(R.id.titleNovo)
        descricaoNova = findViewById(R.id.descricaoNova)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleNovo.text)&&TextUtils.isEmpty(descricaoNova.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = titleNovo.text.toString()
                val descricao = descricaoNova.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_title, title)
                replyIntent.putExtra(EXTRA_REPLY_descricao, descricao)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_title = "com.example.android.wordlistsql.REPLY_title"
        const val EXTRA_REPLY_descricao = "com.example.android.wordlistsql.REPLY_descricao"
    }
}