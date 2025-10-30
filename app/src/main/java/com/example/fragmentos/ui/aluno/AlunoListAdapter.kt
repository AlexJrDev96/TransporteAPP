package com.example.fragmentos.ui.aluno

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Aluno

class AlunoListAdapter : ListAdapter<Aluno, AlunoListAdapter.AlunoViewHolder>(AlunosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        return AlunoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome, current.nomeResponsavel)
    }

    class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeAluno)
        private val responsavelItemView: TextView = itemView.findViewById(R.id.textViewResponsavelAluno)

        fun bind(nome: String?, responsavel: String?) {
            nomeItemView.text = nome
            responsavelItemView.text = responsavel
        }

        companion object {
            fun create(parent: ViewGroup): AlunoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_aluno, parent, false)
                return AlunoViewHolder(view)
            }
        }
    }

    class AlunosComparator : DiffUtil.ItemCallback<Aluno>() {
        override fun areItemsTheSame(oldItem: Aluno, newItem: Aluno): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Aluno, newItem: Aluno): Boolean {
            return oldItem.id == newItem.id
        }
    }
}