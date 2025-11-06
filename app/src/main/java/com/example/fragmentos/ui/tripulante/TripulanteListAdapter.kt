package com.example.fragmentos.ui.tripulante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Tripulante

class TripulanteListAdapter(
    private val onDeleteClicked: (Tripulante) -> Unit,
    private val onEditClicked: (Tripulante) -> Unit
) : ListAdapter<Tripulante, TripulanteListAdapter.TripulanteViewHolder>(TripulantesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripulanteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tripulante, parent, false)
        return TripulanteViewHolder(view, onDeleteClicked, onEditClicked)
    }

    override fun onBindViewHolder(holder: TripulanteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class TripulanteViewHolder(
        itemView: View,
        private val onDeleteClicked: (Tripulante) -> Unit,
        private val onEditClicked: (Tripulante) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeTripulante)
        private val funcaoItemView: TextView = itemView.findViewById(R.id.textViewFuncaoTripulante)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)

        fun bind(tripulante: Tripulante) {
            nomeItemView.text = tripulante.nome
            funcaoItemView.text = tripulante.funcao

            deleteButton.setOnClickListener {
                onDeleteClicked(tripulante)
            }
            editButton.setOnClickListener {
                onEditClicked(tripulante)
            }
        }
    }

    class TripulantesComparator : DiffUtil.ItemCallback<Tripulante>() {
        override fun areItemsTheSame(oldItem: Tripulante, newItem: Tripulante): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tripulante, newItem: Tripulante): Boolean {
            return oldItem.id == newItem.id
        }
    }
}