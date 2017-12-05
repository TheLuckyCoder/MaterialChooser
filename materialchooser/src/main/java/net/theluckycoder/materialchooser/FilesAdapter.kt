package net.theluckycoder.materialchooser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


internal class FilesAdapter(private val list: List<FileItem>, private val listener: OnFileClickListener) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    internal interface OnFileClickListener {
        fun onFileClick(item: FileItem)
    }

    override fun getItemId(position: Int) = list[position].hashCode().toLong()

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder =
            FilesAdapter.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_file, viewGroup, false))

    override fun onBindViewHolder(viewHolder: FilesAdapter.ViewHolder, position: Int) {
        val item = list[position]

        val drawable = when {
            item.isParent -> R.drawable.ic_up
            item.isFolder -> R.drawable.ic_folder
            else -> R.drawable.ic_file
        }

        viewHolder.nameTxt.text = item.name
        viewHolder.iconImg.setImageResource(drawable)
        viewHolder.itemView.setOnClickListener { listener.onFileClick(item) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField val nameTxt: TextView = view.findViewById(R.id.text_name)
        @JvmField val iconImg: ImageView = view.findViewById(R.id.image_icon)
    }
}
