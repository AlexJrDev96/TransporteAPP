package com.example.fragmentos.ui.escola

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Escola

class EscolaListAdapter : ListAdapter<Escola, EscolaListAdapter.EscolaViewHolder>(EscolasComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscolaViewHolder {
        return EscolaViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EscolaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome, current.endereco)
    }

    class EscolaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeEscola)
        private val enderecoItemView: TextView = itemView.findViewById(R.id.textViewEnderecoEscola)

        fun bind(nome: String?, endereco: String?) {
            nomeItemView.text = nome
            enderecoItemView.text = endereco
        }

        companion object {
            fun create(parent: ViewGroup): EscolaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_escola, parent, false)
                return EscolaViewHolder(view)
            }
        }
    }

    class EscolasComparator : DiffUtil.ItemCallback<Escola>() {
        override fun areItemsTheSame(oldItem: Escola, newItem: Escola): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Escola, newItem: Escola): Boolean {
            return oldItem.id == newItem.id
        }
    }
}