package com.example.rgblibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class PaletteAdapter(val list: MutableList<Color>, val onItemClick: OnItemClickRView):  RecyclerView.Adapter<PaletteAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaletteAdapter.MyHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item, parent, false
        )
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: PaletteAdapter.MyHolder, position: Int) {
        val color = this.list[position]
        holder.tvItem?.text = color.toString()
    }

    override fun getItemCount(): Int = this.list.size

    fun delete(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.list.size)
    }

    fun move(from: Int, to: Int) {
        Collections.swap(this.list, from, to)
        notifyItemMoved(from, to)
    }

    inner class MyHolder(item: View): RecyclerView.ViewHolder(item) {
        var tvItem: TextView?

        init {
            this.tvItem = item.findViewById(R.id.tvItem)

            itemView.setOnClickListener{
                this@PaletteAdapter.onItemClick.onItemClick(this.adapterPosition)
            }
        }
    }

}