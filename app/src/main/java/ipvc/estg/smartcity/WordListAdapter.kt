package ipvc.estg.smartcity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class WordListAdapter(private val listener:noteInterface): ListAdapter<Word, WordListAdapter.WordViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.titulo)
        holder.descricao(current.descricao)
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val title: TextView = itemView.findViewById(R.id.titulo)
        private val descricao: TextView = itemView.findViewById(R.id.descricao)

        init {
            itemView.findViewById<Button>(R.id.apagar).setOnClickListener(this)
            itemView.findViewById<Button>(R.id.editar).setOnClickListener(this)
        }

        fun bind(text: String?) {
            title.text = text
        }

        fun descricao(text: String?) {
            descricao.text = text
        }

        override fun onClick(v: View?) {
            if(v?.findViewById<Button>(R.id.apagar)?.isClickable==true) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.noteDelete(position)
                }
            }

            if(v?.findViewById<Button>(R.id.editar)?.isClickable==true) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.noteEdit(position)
                }
            }
        }
    }

    interface noteInterface{
        fun noteDelete(position: Int)
        fun noteEdit(position: Int)
    }

    class WordsComparator : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id== newItem.id
        }
    }
}
