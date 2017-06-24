package net.theluckycoder.filechooser;

import android.app.Activity;
import android.content.Intent;

public class FileChooser {

    private final Activity mActivity;
    private final int mRequestCode;
    private String mRootPath;
    private String mStartPath;
    private String mFileExtension;
    private boolean mShowHiddenFiles = false;

    public FileChooser(Activity activity, int requestCode) {
        mActivity = activity;
        mRequestCode = requestCode;
    }

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     */
    public FileChooser setRootPath(String rootPath) {
        mRootPath = rootPath;
        return this;
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     */
    public FileChooser setStartPath(String startPath) {
        mStartPath = startPath;
        return this;
    }

    /**
     * Filter files trough extensions
     *
     * @param extension file extension in string format
     *                  Example : "txt"
     */
    public FileChooser setFileExtension(String extension) {
        mFileExtension = extension;
        return this;
    }

    /**
     * Show or hide hidden files
     *
     * @param show show files that begin with '.'
     */
    public FileChooser showHiddenFiles(boolean show) {
        mShowHiddenFiles = show;
        return this;
    }

    /**
     * Start the file chooser activity
     *
     */
    public void start() {
        Intent intent = new Intent(mActivity, ChooserActivity.class);
        if (mRootPath != null)
            intent.putExtra(Args.ROOT_DIR_PATH, mRootPath);
        if (mStartPath != null)
            intent.putExtra(Args.START_DIR_PATH, mStartPath);
        if (mFileExtension != null)
            intent.putExtra(Args.FILE_EXTENSION, mFileExtension);
        intent.putExtra(Args.SHOW_HIDDEN, mShowHiddenFiles);

        mActivity.startActivityForResult(intent, mRequestCode);
    }
}
