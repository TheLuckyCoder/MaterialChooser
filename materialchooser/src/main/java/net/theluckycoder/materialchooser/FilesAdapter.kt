package net.theluckycoder.materialchooser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

internal class FilesAdapter(
    private val list: List<FileItem>,
    private val onItemClick: (item: FileItem) -> Unit
) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    init { setHasStableIds(true) }

    override fun getItemId(position: Int) = list[position].hashCode().toLong()

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_file, viewGroup, false))

        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition

            if (pos != RecyclerView.NO_POSITION) onItemClick(list[pos])
        }

        return holder
    }

    override fun onBindViewHolder(viewHolder: FilesAdapter.ViewHolder, position: Int) {
        val item = list[position]

        val drawable = when {
            item.isParent -> R.drawable.ic_up
            item.isFolder -> R.drawable.ic_folder
            else -> R.drawable.ic_file
        }

        viewHolder.tvName.text = item.name
        viewHolder.ivIcon.setImageResource(drawable)
    }

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val ivIcon: ImageView = view.findViewById(R.id.iv_icon)
    }
}
