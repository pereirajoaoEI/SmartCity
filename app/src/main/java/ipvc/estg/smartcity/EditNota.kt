package ipvc.estg.smartcity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe

class EditNota  : AppCompatActivity() {

    private lateinit var titleEdit: EditText
    private lateinit var descricaoEdit: EditText

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editnota)
        titleEdit = findViewById(R.id.titleEdit)
        descricaoEdit = findViewById(R.id.descricaoEdit)

        val position = intent.getIntExtra("position", 0)

       wordViewModel.allWords.observe(this) { notas-> titleEdit.text=SpannableStringBuilder(notas[position].titulo)
           descricaoEdit.text=SpannableStringBuilder(notas[position].descricao)
       }

        val button = findViewById<Button>(R.id.buttonEdit)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleEdit.text)&& TextUtils.isEmpty(descricaoEdit.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = titleEdit.text.toString()
                val descricao = descricaoEdit.text.toString()

                    replyIntent.putExtra(EXTRA_REPLY_title_edit, title)
                    replyIntent.putExtra(EXTRA_REPLY_descricao_edit, descricao)
                    replyIntent.putExtra(EXTRA_REPLY_position, position)
                    setResult(Activity.RESULT_OK, replyIntent)

            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_title_edit = "com.example.android.wordlistsql.REPLY_title_edit"
        const val EXTRA_REPLY_descricao_edit = "com.example.android.wordlistsql.REPLY_descricao_edit"
        const val EXTRA_REPLY_position = "com.example.android.wordlistsql.REPLY_position"
    }




}