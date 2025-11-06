package com.example.fragmentos.ui.responsavel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Responsavel

class ResponsavelListAdapter(
    private val onDeleteClicked: (Responsavel) -> Unit,
    private val onEditClicked: (Responsavel) -> Unit
) : ListAdapter<Responsavel, ResponsavelListAdapter.ResponsavelViewHolder>(ResponsaveisComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponsavelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_responsavel, parent, false)
        return ResponsavelViewHolder(view, onDeleteClicked, onEditClicked)
    }

    override fun onBindViewHolder(holder: ResponsavelViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ResponsavelViewHolder(
        itemView: View,
        private val onDeleteClicked: (Responsavel) -> Unit,
        private val onEditClicked: (Responsavel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeResponsavel)
        private val cpfItemView: TextView = itemView.findViewById(R.id.textViewCpfResponsavel)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)

        fun bind(responsavel: Responsavel) {
            nomeItemView.text = responsavel.nome
            cpfItemView.text = responsavel.cpf

            deleteButton.setOnClickListener {
                onDeleteClicked(responsavel)
            }
            editButton.setOnClickListener {
                onEditClicked(responsavel)
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