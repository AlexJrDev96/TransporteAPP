package com.example.fragmentos.ui.escola

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Escola

class EscolaListAdapter(
    private val onDeleteClicked: (Escola) -> Unit,
    private val onEditClicked: (Escola) -> Unit
) : ListAdapter<Escola, EscolaListAdapter.EscolaViewHolder>(EscolasComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscolaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_escola, parent, false)
        return EscolaViewHolder(view, onDeleteClicked, onEditClicked)
    }

    override fun onBindViewHolder(holder: EscolaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class EscolaViewHolder(
        itemView: View,
        private val onDeleteClicked: (Escola) -> Unit,
        private val onEditClicked: (Escola) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeEscola)
        private val enderecoItemView: TextView = itemView.findViewById(R.id.textViewEnderecoEscola)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)

        fun bind(escola: Escola) {
            nomeItemView.text = escola.nome
            enderecoItemView.text = escola.endereco

            deleteButton.setOnClickListener {
                onDeleteClicked(escola)
            }
            editButton.setOnClickListener {
                onEditClicked(escola)
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