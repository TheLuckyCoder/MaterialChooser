package net.theluckycoder.filechooser;

import android.support.annotation.NonNull;

class Option implements Comparable<Option> {

    private final String mName;
    private final String mPath;
    private final boolean mIsFolder;

    Option(String name, String path, boolean isFolder) {
        mName = name;
        mPath = path;
        mIsFolder = isFolder;
    }

    String getName() {
        return mName;
    }

    String getPath() {
        return mPath;
    }

    boolean isFolder() {
        return mIsFolder;
    }

    @Override
    public int compareTo(@NonNull Option option) {
        return mName.toLowerCase().compareTo(option.getName().toLowerCase());
    }
}
