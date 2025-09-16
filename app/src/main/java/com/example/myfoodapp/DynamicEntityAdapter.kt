package com.example.myfoodapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DynamicEntityAdapter(
    private val data: List<Map<String, Any>>,
    private val onClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<DynamicEntityAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCountry: TextView = itemView.findViewById(R.id.tvCountry)
        val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_dynamic_entity, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = data[position]

        // Choose a title from common candidates, or first non-empty string value
        val title = pickFirst(
            entity,
            listOf("destination","name","title","username","id","code")
        ) ?: firstStringValue(entity) ?: "Untitled"

        // Choose a secondary label (like country / owner / type)
        val secondary = pickFirst(
            entity,
            listOf("country","owner","type","category","email","status")
        )

        // Compose a meta line from a couple of extras (without description)
        val meta = listOfNotNull(
            pickFirst(entity, listOf("bestSeason","role","city","state")),
            pickFirst(entity, listOf("popularAttraction","department","phone"))
        ).filter { it.isNotBlank() }.joinToString(" • ")

        holder.tvTitle.text = title

        if (secondary.isNullOrBlank()) {
            holder.tvCountry.visibility = View.GONE
        } else {
            holder.tvCountry.visibility = View.VISIBLE
            holder.tvCountry.text = secondary
        }

        if (meta.isBlank()) {
            holder.tvMeta.visibility = View.GONE
        } else {
            holder.tvMeta.visibility = View.VISIBLE
            holder.tvMeta.text = meta
        }

        holder.itemView.setOnClickListener { onClick(entity) }
    }

    override fun getItemCount() = data.size

    private fun pickFirst(map: Map<String, Any>, keys: List<String>): String? {
        for (k in keys) {
            val v = map[k]
            if (v != null) {
                val s = v.toString().trim()
                if (s.isNotEmpty() && s != "null") return s
            }
        }
        return null
    }

    private fun firstStringValue(map: Map<String, Any>): String? {
        return map.values
            .map { it?.toString()?.trim().orEmpty() }
            .firstOrNull { it.isNotEmpty() && it != "null" }
    }
}
