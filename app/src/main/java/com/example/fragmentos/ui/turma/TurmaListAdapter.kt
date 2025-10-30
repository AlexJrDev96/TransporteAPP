package com.example.fragmentos.ui.turma

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Turma

class TurmaListAdapter : ListAdapter<Turma, TurmaListAdapter.TurmaViewHolder>(TurmasComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurmaViewHolder {
        return TurmaViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TurmaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome)
    }

    class TurmaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeTurma)

        fun bind(nome: String?) {
            nomeItemView.text = nome
        }

        companion object {
            fun create(parent: ViewGroup): TurmaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_turma, parent, false)
                return TurmaViewHolder(view)
            }
        }
    }

    class TurmasComparator : DiffUtil.ItemCallback<Turma>() {
        override fun areItemsTheSame(oldItem: Turma, newItem: Turma): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Turma, newItem: Turma): Boolean {
            return oldItem.id == newItem.id
        }
    }
}