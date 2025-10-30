package com.example.fragmentos.ui.tripulante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentos.R
import com.example.fragmentos.db.entity.Tripulante

class TripulanteListAdapter : ListAdapter<Tripulante, TripulanteListAdapter.TripulanteViewHolder>(TripulantesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripulanteViewHolder {
        return TripulanteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TripulanteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome, current.funcao)
    }

    class TripulanteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeItemView: TextView = itemView.findViewById(R.id.textViewNomeTripulante)
        private val funcaoItemView: TextView = itemView.findViewById(R.id.textViewFuncaoTripulante)

        fun bind(nome: String?, funcao: String?) {
            nomeItemView.text = nome
            funcaoItemView.text = funcao
        }

        companion object {
            fun create(parent: ViewGroup): TripulanteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tripulante, parent, false)
                return TripulanteViewHolder(view)
            }
        }
    }

    class TripulantesComparator : DiffUtil.ItemCallback<Tripulante>() {
        override fun areItemsTheSame(oldItem: Tripulante, newItem: Tripulante):
 Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tripulante, newItem: Tripulante):
 Boolean {
            return oldItem.id == newItem.id
        }
    }
}