package com.example.fragmentos.ui.responsavel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Responsavel

class ResponsavelListAdapter : ListAdapter<Responsavel, ResponsavelListAdapter.ResponsavelViewHolder>(ResponsaveisComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponsavelViewHolder {
        return ResponsavelViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ResponsavelViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome, current.cpf)
    }

    class ResponsavelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeResponsavel)
        private val cpfItemView: TextView = itemView.findViewById(R.id.textViewCpfResponsavel)

        fun bind(nome: String?, cpf: String?) {
            nomeItemView.text = nome
            cpfItemView.text = cpf
        }

        companion object {
            fun create(parent: ViewGroup): ResponsavelViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_responsavel, parent, false)
                return ResponsavelViewHolder(view)
            }
        }
    }

    class ResponsaveisComparator : DiffUtil.ItemCallback<Responsavel>() {
        override fun areItemsTheSame(oldItem: Responsavel, newItem: Responsavel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Responsavel, newItem: Responsavel): Boolean {
            return oldItem.id == newItem.id
        }
    }
}