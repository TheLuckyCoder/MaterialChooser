package net.theluckycoder.filechooser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class FileArrayAdapter extends ArrayAdapter<Option> {

    private final Context mContext;
    private final int id;
    private final List<Option> items;

    FileArrayAdapter(Context context, int textViewResourceId, List<Option> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        id = textViewResourceId;
        items = objects;
    }

    public Option getItem(int i)
    {
        return items.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(id, null);
        }

        final Option option = items.get(position);

        TextView nameTxt = convertView.findViewById(R.id.name);
        ImageView iconImg = convertView.findViewById(R.id.icon);

        if (option != null) {
            nameTxt.setText(option.getName());
            if (option.isFolder())
                iconImg.setImageResource(R.drawable.ic_folder);
            else
                iconImg.setImageResource(R.drawable.ic_file);

        }

        return convertView;
    }

}
