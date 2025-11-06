package com.example.fragmentos.ui.aluno

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Aluno

class AlunoListAdapter(
    private val onDeleteClicked: (Aluno) -> Unit,
    private val onEditClicked: (Aluno) -> Unit
) : ListAdapter<Aluno, AlunoListAdapter.AlunoViewHolder>(AlunosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aluno, parent, false)
        return AlunoViewHolder(view, onDeleteClicked, onEditClicked)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class AlunoViewHolder(
        itemView: View,
        private val onDeleteClicked: (Aluno) -> Unit,
        private val onEditClicked: (Aluno) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeAluno)
        private val responsavelItemView: TextView = itemView.findViewById(R.id.textViewResponsavelAluno)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)

        fun bind(aluno: Aluno) {
            nomeItemView.text = aluno.nome
            responsavelItemView.text = aluno.nomeResponsavel // Apenas exibe o nome do responsável

            deleteButton.setOnClickListener {
                onDeleteClicked(aluno)
            }
            editButton.setOnClickListener {
                onEditClicked(aluno)
            }
        }
    }

    class AlunosComparator : DiffUtil.ItemCallback<Aluno>() {
        override fun areItemsTheSame(oldItem: Aluno, newItem: Aluno): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Aluno, newItem: Aluno): Boolean {
            return oldItem == newItem
        }
    }
}