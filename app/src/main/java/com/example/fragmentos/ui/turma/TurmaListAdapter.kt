package com.example.fragmentos.ui.turma

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Turma

class TurmaListAdapter(
    private val onDeleteClicked: (Turma) -> Unit,
    private val onEditClicked: (Turma) -> Unit
) : ListAdapter<Turma, TurmaListAdapter.TurmaViewHolder>(TurmasComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurmaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_turma, parent, false)
        return TurmaViewHolder(view, onDeleteClicked, onEditClicked)
    }

    override fun onBindViewHolder(holder: TurmaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class TurmaViewHolder(
        itemView: View,
        private val onDeleteClicked: (Turma) -> Unit,
        private val onEditClicked: (Turma) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeTurma)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)

        fun bind(turma: Turma) {
            nomeItemView.text = turma.nome

            deleteButton.setOnClickListener {
                onDeleteClicked(turma)
            }
            editButton.setOnClickListener {
                onEditClicked(turma)
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