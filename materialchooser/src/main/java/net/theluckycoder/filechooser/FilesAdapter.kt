package net.theluckycoder.filechooser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


internal class FilesAdapter(context: Context, private val items: List<FileItem>) : ArrayAdapter<FileItem>(context, R.layout.item_file, items) {

    override fun getItem(i: Int): FileItem {
        return items[i]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView != null) {
            convertView
        } else {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.item_file, null)
        }

        val option = items[position]

        val nameTxt: TextView = view.findViewById(R.id.text_name)
        val iconImg: ImageView = view.findViewById(R.id.image_icon)

        nameTxt.text = option.name

        if (option.isFolder && option.name == context.getString(R.string.parent_directory))
            iconImg.setImageResource(R.drawable.ic_up)
        else if (option.isFolder)
            iconImg.setImageResource(R.drawable.ic_folder)
        else
            iconImg.setImageResource(R.drawable.ic_file)

        return view
    }

}
