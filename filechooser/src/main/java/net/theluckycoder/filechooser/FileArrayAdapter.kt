package net.theluckycoder.filechooser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

internal class FileArrayAdapter(context: Context, private val id: Int, private val items: List<Option>) : ArrayAdapter<Option>(context, id, items) {

    override fun getItem(i: Int): Option? {
        return items[i]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(id, null)
        }

        val option = items[position]

        val nameTxt = view!!.findViewById<TextView>(R.id.name)
        val iconImg = view.findViewById<ImageView>(R.id.icon)

        nameTxt.text = option.name
        if (option.isFolder)
            iconImg.setImageResource(R.drawable.ic_folder)
        else
            iconImg.setImageResource(R.drawable.ic_file)

        return view
    }

}
