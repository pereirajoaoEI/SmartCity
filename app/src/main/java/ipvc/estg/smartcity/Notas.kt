package ipvc.estg.smartcity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Notas : AppCompatActivity(), WordListAdapter.noteInterface {

    private val newWordActivityRequestCode = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })


        //botao que muda de pagina
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@Notas, NewWordActivity::class.java)
            resultInsertNote.launch(intent)
        }


    }

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    private var resultInsertNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->


        if (result.resultCode == Activity.RESULT_OK) {
            val data:Intent?=result.data
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY_title)?.let { title ->
                data?.getStringExtra(NewWordActivity.EXTRA_REPLY_descricao)?.let { descricao ->
                    val word = Word(titulo = title, descricao = descricao)
                    wordViewModel.insert(word)
                }
            }
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }


    override fun noteDelete(position: Int) {
        wordViewModel.allWords.value?.get(position)?.id?.let {
            wordViewModel.deleteNota(it)
        }

    }

    override fun noteEdit(position: Int) {

        val intent = Intent(this, EditNota::class.java).apply { putExtra("position",position) }

        resultEditNote.launch(intent)
    }

    private var resultEditNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->


        if (result.resultCode == Activity.RESULT_OK) {
            val data:Intent?=result.data
            data?.getStringExtra(EditNota.EXTRA_REPLY_title_edit)?.let { title ->
                data.getStringExtra(EditNota.EXTRA_REPLY_descricao_edit)?.let { descricao ->
                    data.getIntExtra(EditNota.EXTRA_REPLY_position, 0).let { id ->
                        val word = Word(id = wordViewModel.allWords.value?.get(id)?.id, titulo = title, descricao = descricao)
                        wordViewModel.editNota(word)
                    }
                }
            }
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }

}