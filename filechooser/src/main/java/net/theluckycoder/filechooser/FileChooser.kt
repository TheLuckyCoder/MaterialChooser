package net.theluckycoder.filechooser

import android.app.Activity
import android.content.Intent

class FileChooser(private val activity: Activity, private val requestCode: Int) {

    private var mRootPath: String? = null
    private var mStartPath: String? = null
    private var mFileExtension: String? = null
    private var mShowHiddenFiles = false

    /**
     * Set the root directory of the picker

     * @param rootPath the user can't go any higher than this
     */
    fun setRootPath(rootPath: String): FileChooser {
        mRootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker

     * @param startPath where the user starts
     */
    fun setStartPath(startPath: String): FileChooser {
        mStartPath = startPath
        return this
    }

    /**
     * Filter files trough extensions

     * @param extension file extension in string format
     * *                  Example : "txt"
     */
    fun setFileExtension(extension: String): FileChooser {
        mFileExtension = extension
        return this
    }

    /**
     * Show or hide hidden files

     * @param show show files that begin with '.'
     */
    fun showHiddenFiles(show: Boolean): FileChooser {
        mShowHiddenFiles = show
        return this
    }

    /**
     * Start the file chooser activity

     */
    fun start() {
        val intent = Intent(activity, ChooserActivity::class.java)
        if (mRootPath != null)
            intent.putExtra(Chooser.rootDirPath, mRootPath)
        if (mStartPath != null)
            intent.putExtra(Chooser.startDirPath, mStartPath)
        if (mFileExtension != null)
            intent.putExtra(Chooser.fileExtension, mFileExtension)
        intent.putExtra(Chooser.showHiddenFiles, mShowHiddenFiles)

        activity.startActivityForResult(intent, requestCode)
    }
}
